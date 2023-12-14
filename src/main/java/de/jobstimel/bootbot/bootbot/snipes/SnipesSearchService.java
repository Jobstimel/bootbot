package de.jobstimel.bootbot.bootbot.snipes;

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
public class SnipesSearchService implements SearchService {

    private final SnipesCacheService snipesCacheService;
    private final SnipesMapper snipesMapper;
    private final DiscordService discordService;

    @Autowired
    public SnipesSearchService(SnipesCacheService snipesCacheService, SnipesMapper snipesMapper, DiscordService discordService) {
        this.snipesCacheService = snipesCacheService;
        this.snipesMapper = snipesMapper;
        this.discordService = discordService;
    }

    @Override
    public void search(Boot boot) {
        if (!this.snipesCacheService.containsValue(boot.buildKey())) {
            try {
                Optional<Element> optSearchHit = executeSearch(boot);
                if (optSearchHit.isPresent()) {
                    Optional<SnipesSearchHit> optSnipesSearchHit = parseSearchHit(optSearchHit.get(), boot.size());
                    if (optSnipesSearchHit.isPresent()) {
                        sendDiscordMessage(optSnipesSearchHit.get());
                        this.snipesCacheService.addValue(boot.buildKey());
                        log.info("[NEW HIT] - {}: {} | {} {} | {}", SnipesConstants.SHOP_NAME, optSnipesSearchHit.get().name(),
                                SnipesConstants.SIZE_TYPE, optSnipesSearchHit.get().size(), optSnipesSearchHit.get().price());
                    } else {
                        log.warn("Failed to parse search hit for '{}' at '{}'", boot.name(), SnipesConstants.SHOP_NAME);
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
        Elements searchHits = webPage.select(SnipesConstants.PRODUCT_CLASS);
        if (searchHits.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(searchHits.get(0));
    }

    private Optional<SnipesSearchHit> parseSearchHit(Element searchHit, String size) {
        Element brandElement = searchHit.select(SnipesConstants.PRODUCT_BRAND_CLASS).first();
        String brand;
        if (brandElement == null) {
            brand = BootbotConstants.FALLBACK_TEXT_VALUE;
        } else {
            brand = brandElement.text().strip();
        }

        Optional<String> productInformation = retrieveInformation(searchHit);
        if (productInformation.isEmpty()) {
            return Optional.empty();
        }

        Element priceElement = searchHit.select(SnipesConstants.PRODUCT_PRICE_CLASS).first();
        String price;
        if (priceElement == null) {
            price = BootbotConstants.FALLBACK_TEXT_VALUE;
        } else {
            price = priceElement.text().strip();
        }

        Optional<String> productLink = retrieveProductLink(searchHit);
        if (productLink.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new SnipesSearchHit(
                productInformation.get(), brand, BootbotConstants.FALLBACK_TEXT_VALUE, price, size,
                SnipesConstants.SIZE_TYPE, productLink.get()));
    }

    private String buildUrl(Boot boot) {
        return SnipesConstants.SEARCH_URL
                .replace(SnipesConstants.QUERY_PLACEHOLDER, boot.name())
                .replace(SnipesConstants.SIZE_PLACEHOLDER, boot.size())
                .replace(SnipesConstants.GENDER_PLACEHOLDER, boot.gender().getValue());
    }

    private void sendDiscordMessage(SnipesSearchHit snipesSearchHit) {
        DiscordMessage discordMessage = this.snipesMapper.map(snipesSearchHit);
        this.discordService.sendMessage(discordMessage);
    }

    private Optional<String> retrieveInformation(Element searchHit) {
        Element linkElement = searchHit.select(SnipesConstants.PRODUCT_LINK_CLASS).first();
        if (linkElement == null) {
            return Optional.empty();
        }
        return Optional.of(linkElement.text().strip());
    }

    private Optional<String> retrieveProductLink(Element searchHit) {
        Element linkElement = searchHit.select(SnipesConstants.PRODUCT_LINK_CLASS).first();
        if (linkElement == null) {
            return Optional.empty();
        }
        return Optional.of(SnipesConstants.BASE_URL + linkElement.attr(HTMLConstants.HREF));
    }

}
