package de.jobstimel.bootbot.bootbot;

import de.jobstimel.bootbot.bootbot.config.BootbotConfig;
import de.jobstimel.bootbot.bootbot.data.Boot;
import de.jobstimel.bootbot.bootbot.footlocker.FootlockerSearchService;
import de.jobstimel.bootbot.bootbot.snipes.SnipesSearchService;
import de.jobstimel.bootbot.bootbot.zalando.ZalandoSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ScheduleService {

    private int searchRoundCounter = 0;
    private final BootbotConfig bootbotConfig;
    private final List<SearchService> searchServices = new ArrayList<>();

    @Autowired
    public ScheduleService(FootlockerSearchService footlockerSearchService, SnipesSearchService snipesSearchService, ZalandoSearchService zalandoSearchService, BootbotConfig bootbotConfig) {
        this.searchServices.addAll(List.of(
                footlockerSearchService, snipesSearchService, zalandoSearchService));
        this.bootbotConfig = bootbotConfig;
    }

    @Scheduled(fixedDelay = 60000L)
    private void executeAutomaticSearch() {
        for (Boot boot : bootbotConfig.getBootsToWatch()) {
            for (SearchService searchService : this.searchServices) {
                searchService.search(boot);
            }
        }
        this.searchRoundCounter += 1;
        log.info("Completed search run {}", this.searchRoundCounter);
    }

}
