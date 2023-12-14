package de.jobstimel.bootbot.bootbot.footlocker;

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
public class FootlockerSearchService implements SearchService {

    private final DiscordService discordService;
    private final FootlockerMapper footlockerMapper;
    private final FootlockerCacheService footlockerCacheService;

    @Autowired
    public FootlockerSearchService(DiscordService discordService, FootlockerMapper footlockerMapper, FootlockerCacheService footlockerCacheService) {
        this.discordService = discordService;
        this.footlockerMapper = footlockerMapper;
        this.footlockerCacheService = footlockerCacheService;
    }

    @Override
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
                                footlockerSearchHit.get().price(), FootlockerConstants.SHOP_NAME);
                    } else {
                        log.warn("Failed to parse search hit for '{}' at '{}'", boot.name(), FootlockerConstants.SHOP_NAME);
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
        Elements searchHits = webPage.select(FootlockerConstants.PRODUCT_CONTAINER);

        if (searchHits.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(searchHits.get(0));
    }

    private Optional<FootlockerSearchHit> parseSearchHit(Element productHit, String size) {
        String productLink = retrieveProductLink(productHit).orElse(BootbotConstants.FALLBACK_TEXT_VALUE);
        String productNamePrimary = retrieveText(productHit, FootlockerConstants.PRODUCT_NAME_PRIMARY)
                .orElse(BootbotConstants.FALLBACK_TEXT_VALUE);
        String productNameAlt = retrieveText(productHit, FootlockerConstants.PRODUCT_NAME_ALT)
                .orElse(BootbotConstants.FALLBACK_TEXT_VALUE);
        String productNameSeparator = retrieveText(productHit, FootlockerConstants.PRODUCT_NAME_SEPARATOR)
                .orElse(BootbotConstants.FALLBACK_TEXT_VALUE);
        String productPrice = retrieveText(productHit, FootlockerConstants.PRODUCT_PRICE)
                .orElse(BootbotConstants.FALLBACK_TEXT_VALUE);

        String altName = productNameAlt.split(productNameSeparator)[0].strip();
        String description = BootbotConstants.FALLBACK_TEXT_VALUE;
        if (productNameAlt.split(productNameSeparator).length > 1) {
            description = productNameAlt.split(productNameSeparator)[1].strip();
        }

        return Optional.of(new FootlockerSearchHit(
                productNamePrimary, altName, description, productPrice, size, FootlockerConstants.SIZE_TYPE, productLink));
    }

    private void sendDiscordMessage(FootlockerSearchHit footlockerSearchHit) {
        DiscordMessage discordMessage = this.footlockerMapper.map(footlockerSearchHit);
        this.discordService.sendMessage(discordMessage);
    }

    private String buildUrl(Boot boot) {
        return FootlockerConstants.SEARCH_URL
                .replace(FootlockerConstants.QUERY_PLACEHOLDER, boot.name())
                .replace(FootlockerConstants.GENDER_PLACEHOLDER, boot.gender().getValue())
                .replace(FootlockerConstants.SIZE_PLACEHOLDER, boot.size());
    }

    private Optional<String> retrieveProductLink(Element searchHit) {
        Element element = searchHit.select(FootlockerConstants.PRODUCT_CARD_LINK).first();
        if (element == null) {
            return Optional.empty();
        }
        return Optional.of(FootlockerConstants.BASE_URL + element.attr(HTMLConstants.HREF));
    }

    private Optional<String> retrieveText(Element productHit, String className) {
        Element element = productHit.select(className).first();
        if (element == null) {
            return Optional.empty();
        }
        return Optional.of(element.text());
    }

}
