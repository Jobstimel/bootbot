package de.jobstimel.bootbot.bootbot;

import de.jobstimel.bootbot.bootbot.config.BootbotConfig;
import de.jobstimel.bootbot.bootbot.data.Boot;
import de.jobstimel.bootbot.bootbot.footlocker.FootlockerSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SearchService {

    private final FootlockerSearchService footlockerSearchService;
    private final BootbotConfig bootbotConfig;

    @Autowired
    public SearchService(FootlockerSearchService footlockerSearchService, BootbotConfig bootbotConfig) {
        this.footlockerSearchService = footlockerSearchService;
        this.bootbotConfig = bootbotConfig;
    }

    @Scheduled(fixedDelay = 60000L)
    private void executeAutomaticSearch() {
        for (Boot boot : bootbotConfig.getBootsToWatch()) {
            footlockerSearchService.search(boot);
        }
    }

}
