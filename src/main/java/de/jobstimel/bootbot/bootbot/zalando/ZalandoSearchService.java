package de.jobstimel.bootbot.bootbot.zalando;

import de.jobstimel.bootbot.bootbot.BootbotConstants;
import de.jobstimel.bootbot.bootbot.SearchService;
import de.jobstimel.bootbot.bootbot.data.Boot;
import de.jobstimel.bootbot.bootbot.discord.DiscordMessage;
import de.jobstimel.bootbot.bootbot.discord.DiscordService;
import de.jobstimel.bootbot.bootbot.footlocker.FootlockerConstants;
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
                        log.info("[NEW HIT]: {} | EU {} | {} | {}", zalandoSearchHit.get().name(), zalandoSearchHit.get().size(),
                                zalandoSearchHit.get().price(), FootlockerConstants.NAME);
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
                .userAgent(BootbotConstants.USER_AGENT)
                .get();
        Elements searchHits = webPage.select(ZalandoConstants.PRODUCT_CLASSES);
        if (searchHits.first() == null) {
            return Optional.empty();
        }

        return Optional.of(searchHits.get(0));
    }

    private void sendDiscordMessage(ZalandoSearchHit zalandoSearchHit) {
        DiscordMessage discordMessage = this.zalandoMapper.map(zalandoSearchHit);
        this.discordService.sendMessage(discordMessage);
    }

    private Optional<ZalandoSearchHit> parseSearchHit(Element searchHit, String size) {
        Optional<String> optGeneralInformation = retrieveInformation(searchHit);
        Optional<String> optPriceInformation = retrievePrice(searchHit);
        Optional<String> optUrl = retrieveProductLink(searchHit);

        String name, altName, description;
        if (optGeneralInformation.isPresent()) {
            String[] textParts = optGeneralInformation.get().split("-");
            if (textParts.length != 3) {
                return Optional.empty();
            }
            name = textParts[0].strip();
            altName = textParts[1].strip();
            description = textParts[2].strip();
        } else {
            return Optional.empty();
        }

        String price;
        if (optPriceInformation.isPresent()) {
            price = optPriceInformation.get();
        } else {
            return Optional.empty();
        }

        String url;
        if (optUrl.isPresent()) {
            url = optUrl.get();
        } else {
            return Optional.empty();
        }

        return Optional.of(new ZalandoSearchHit(
                name, altName, description, price, size, ZalandoConstants.SIZE_TYPE, url));
    }

    private Optional<String> retrieveProductLink(Element searchHit) {
        Element element = searchHit.select("a").first();
        if (element == null) {
            return Optional.empty();
        }
        return Optional.of(element.attr("href"));
    }

    private Optional<String> retrieveInformation(Element searchHit) {
        if (searchHit.select("div.Zhr-fS").size() != 1) {
            return Optional.empty();
        }
        return Optional.of(searchHit.select("div.Zhr-fS").get(0).text());
    }

    private Optional<String> retrievePrice(Element searchHit) {
        if (searchHit.select("section").size() != 1) {
            return Optional.empty();
        }
        return Optional.of(searchHit.select("section").text());
    }

    private String buildUrl(Boot boot) {
        String query = processQuery(boot.name());
        String size = processSize(boot.size());
        return ZalandoConstants.SEARCH_URL.replace(ZalandoConstants.QUERY_PLACEHOLDER, query).replace(ZalandoConstants.SIZE_PLACEHOLDER, size);
    }

    private String processQuery(String query) {
        return query.replaceAll(" ", ZalandoConstants.QUERY_DELIMITER);
    }

    private String processSize(String size) {
        return size.replace(".", ZalandoConstants.SIZE_DELIMITER);
    }

}
