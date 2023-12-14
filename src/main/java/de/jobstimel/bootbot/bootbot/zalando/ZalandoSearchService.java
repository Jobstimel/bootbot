package de.jobstimel.bootbot.bootbot.zalando;

import de.jobstimel.bootbot.bootbot.SearchService;
import de.jobstimel.bootbot.bootbot.constants.BootbotConstants;
import de.jobstimel.bootbot.bootbot.constants.HTMLConstants;
import de.jobstimel.bootbot.bootbot.constants.JsoupConstants;
import de.jobstimel.bootbot.bootbot.data.Boot;
import de.jobstimel.bootbot.bootbot.discord.DiscordMessage;
import de.jobstimel.bootbot.bootbot.discord.DiscordService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class ZalandoSearchService implements SearchService {

    private final ZalandoCacheService zalandoCacheService;
    private final ZalandoMapper zalandoMapper;
    private final DiscordService discordService;

    @Autowired
    public ZalandoSearchService(ZalandoCacheService zalandoCacheService, ZalandoMapper zalandoMapper, DiscordService discordService) {
        this.zalandoCacheService = zalandoCacheService;
        this.zalandoMapper = zalandoMapper;
        this.discordService = discordService;
    }

    @Override
    public void search(Boot boot) {
        if (!this.zalandoCacheService.containsValue(boot.buildKey())) {
            try {
                Optional<Element> optSearchHit = executeSearch(boot);
                if (optSearchHit.isPresent()) {
                    Optional<ZalandoSearchHit> zalandoSearchHit = parseSearchHit(optSearchHit.get(), boot.size());
                    if (zalandoSearchHit.isPresent()) {
                        sendDiscordMessage(zalandoSearchHit.get());
                        this.zalandoCacheService.addValue(boot.buildKey());
                        log.info("[NEW HIT] - {}: {} | {} {} | {}", ZalandoConstants.SHOP_NAME, zalandoSearchHit.get().name(),
                                ZalandoConstants.SIZE_TYPE, zalandoSearchHit.get().size(), zalandoSearchHit.get().price());
                    } else {
                        log.warn("Failed to parse search hit for '{}' at '{}'", boot.name(), ZalandoConstants.SHOP_NAME);
                    }
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private Optional<Element> executeSearch(Boot boot) throws IOException {
        String searchUrl = buildUrl(boot);
        Document webPage = Jsoup.connect(searchUrl)
                .userAgent(JsoupConstants.USER_AGENT)
                .get();
        Elements searchHits = webPage.select(ZalandoConstants.PRODUCT_CLASSES);
        if (searchHits.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(searchHits.get(0));
    }

    private Optional<ZalandoSearchHit> parseSearchHit(Element searchHit, String size) {
        Optional<String> optGeneralInformation = retrieveInformation(searchHit);
        String name, altName, description;
        if (optGeneralInformation.isPresent()) {
            String[] textParts = optGeneralInformation.get().split(ZalandoConstants.INFORMATION_DELIMITER);
            if (textParts.length != 3) {
                return Optional.empty();
            }
            name = textParts[0].strip();
            altName = textParts[1].strip();
            description = textParts[2].strip();
        } else {
            return Optional.empty();
        }

        Optional<String> optPriceInformation = retrievePrice(searchHit);
        String price;
        if (optPriceInformation.isPresent()) {
            price = optPriceInformation.get();
        } else {
            return Optional.empty();
        }

        Optional<String> optProductLink = retrieveProductLink(searchHit);
        String productLink;
        if (optProductLink.isPresent()) {
            productLink = optProductLink.get();
        } else {
            return Optional.empty();
        }

        return Optional.of(new ZalandoSearchHit(
                name, altName, description, price, size, ZalandoConstants.SIZE_TYPE, productLink));
    }

    private void sendDiscordMessage(ZalandoSearchHit zalandoSearchHit) {
        DiscordMessage discordMessage = this.zalandoMapper.map(zalandoSearchHit);
        this.discordService.sendMessage(discordMessage);
    }

    private String buildUrl(Boot boot) {
        return ZalandoConstants.SEARCH_URL
                .replace(ZalandoConstants.QUERY_PLACEHOLDER, processQuery(boot.name()))
                .replace(ZalandoConstants.SIZE_PLACEHOLDER, processSize(boot.size()));
    }

    private Optional<String> retrieveProductLink(Element searchHit) {
        Element element = searchHit.select(HTMLConstants.A).first();
        if (element == null) {
            return Optional.empty();
        }
        return Optional.of(element.attr(HTMLConstants.HREF));
    }

    private Optional<String> retrieveInformation(Element searchHit) {
        if (searchHit.select(HTMLConstants.DIV + ZalandoConstants.INFORMATION_CLASS).size() != 1) {
            return Optional.empty();
        }
        return Optional.of(searchHit.select(HTMLConstants.DIV + ZalandoConstants.INFORMATION_CLASS).get(0).text());
    }

    private Optional<String> retrievePrice(Element searchHit) {
        Element priceElement = searchHit.select(HTMLConstants.SECTION).first();
        if (priceElement == null) {
            return Optional.empty();
        }
        return Optional.of(priceElement.text().strip());
    }

    private String processQuery(String query) {
        return query.replaceAll(BootbotConstants.SPACE, ZalandoConstants.QUERY_DELIMITER);
    }

    private String processSize(String size) {
        return size.replace(BootbotConstants.DOT, ZalandoConstants.SIZE_DELIMITER);
    }

}
