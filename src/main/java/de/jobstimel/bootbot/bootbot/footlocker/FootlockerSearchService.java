package de.jobstimel.bootbot.bootbot.footlocker;

import de.jobstimel.bootbot.bootbot.BootbotConstants;
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
public class FootlockerSearchService {

    private final DiscordService discordService;
    private final FootlockerMapper footlockerMapper;
    private final FootlockerCacheService footlockerCacheService;

    @Autowired
    public FootlockerSearchService(DiscordService discordService, FootlockerMapper footlockerMapper, FootlockerCacheService footlockerCacheService) {
        this.discordService = discordService;
        this.footlockerMapper = footlockerMapper;
        this.footlockerCacheService = footlockerCacheService;
    }

    public void search(Boot boot) {
        if (!this.footlockerCacheService.containsValue(boot.buildKey())) {
            try {
                Optional<Element> optSearchHit = executeSearch(boot);
                if (optSearchHit.isPresent()) {
                    Optional<FootlockerSearchHit> footlockerSearchHit = parseSearchHit(optSearchHit.get(), boot.size());
                    if (footlockerSearchHit.isPresent()) {
                        sendDiscordMessage(footlockerSearchHit.get());
                        this.footlockerCacheService.addValue(boot.buildKey());
                        log.info("[NEW HIT]: {} | EU {} | {} | {}", footlockerSearchHit.get().name(), footlockerSearchHit.get().size(),
                                footlockerSearchHit.get().price(), FootlockerConstants.NAME);
                    }
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private Optional<Element> executeSearch(Boot boot) throws IOException {
        String searchUrl = buildUrl(boot.name(), boot.size());
        Document webPage = Jsoup.connect(searchUrl)
                .userAgent(BootbotConstants.USER_AGENT)
                .get();
        Elements searchHits = webPage.select(FootlockerConstants.PRODUCT_CONTAINER);

        if (searchHits.isEmpty()) {
            return Optional.empty();
        }

        return findRelevantSearchHit(searchHits, boot.name());
    }

    private void sendDiscordMessage(FootlockerSearchHit footlockerSearchHit) {
        DiscordMessage discordMessage = this.footlockerMapper.map(footlockerSearchHit);
        this.discordService.sendMessage(discordMessage);
    }

    private Optional<Element> findRelevantSearchHit(Elements searchHits, String query) {
        for (Element searchHit : searchHits) {
            String productNamePrimary = retrieveText(searchHit, FootlockerConstants.PRODUCT_NAME_PRIMARY)
                    .orElse(FootlockerConstants.FALLBACK_VALUE);
            if (productNamePrimary.equalsIgnoreCase(query) || productNamePrimary.contains(query)) {
                return Optional.of(searchHit);
            }
        }
        return Optional.empty();
    }

    private Optional<FootlockerSearchHit> parseSearchHit(Element productHit, String size) {
        String productLink = retrieveLink(productHit).orElse(FootlockerConstants.FALLBACK_VALUE);
        String productNamePrimary = retrieveText(productHit, FootlockerConstants.PRODUCT_NAME_PRIMARY)
                .orElse(FootlockerConstants.FALLBACK_VALUE);
        String productNameAlt = retrieveText(productHit, FootlockerConstants.PRODUCT_NAME_ALT)
                .orElse(FootlockerConstants.FALLBACK_VALUE);
        String productNameSeparator = retrieveText(productHit, FootlockerConstants.PRODUCT_NAME_SEPERATOR)
                .orElse(FootlockerConstants.FALLBACK_VALUE);
        String productPrice = retrieveText(productHit, FootlockerConstants.PRODUCT_PRICE)
                .orElse(FootlockerConstants.FALLBACK_VALUE);
        
        String altName = productNameAlt.split(productNameSeparator)[0].strip();
        String description = FootlockerConstants.FALLBACK_VALUE;
        if (productNameAlt.split(productNameSeparator).length > 1) {
            description = productNameAlt.split(productNameSeparator)[1].strip();
        }

        productPrice = formatPrice(productPrice);
        
        return Optional.of(new FootlockerSearchHit(
                productNamePrimary, altName, description, productPrice, size, FootlockerConstants.SIZE_TYPE, productLink));
    }

    private String formatPrice(String productPrice) {
        return productPrice.replace("€", "").strip() + "€";
    }

    private Optional<String> retrieveLink(Element searchHit) {
        Element element = searchHit.select(FootlockerConstants.PRODUCT_CARD_LINK).first();
        if (element == null) {
            return Optional.empty();
        }
        return Optional.of(FootlockerConstants.BASE_URL + element.attr("href"));
    }

    private Optional<String> retrieveText(Element productHit, String className) {
        Element element = productHit.select(className).first();
        if (element == null) {
            return Optional.empty();
        }
        return Optional.of(element.text());
    }

    private String buildUrl(String name, String size) {
        return FootlockerConstants.SEARCH_URL
                .replace(FootlockerConstants.QUERY_PLACEHOLDER, name)
                .replace(FootlockerConstants.SIZE_PLACEHOLDER, size);
    }

}
