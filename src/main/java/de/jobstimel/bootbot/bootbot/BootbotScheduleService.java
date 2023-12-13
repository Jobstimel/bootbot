package de.jobstimel.bootbot.bootbot;

import de.jobstimel.bootbot.bootbot.config.BootbotConfig;
import de.jobstimel.bootbot.bootbot.data.Boot;
import de.jobstimel.bootbot.bootbot.footlocker.FootlockerSearchService;
import de.jobstimel.bootbot.bootbot.zalando.ZalandoSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BootbotScheduleService {

    private final FootlockerSearchService footlockerSearchService;
    private final ZalandoSearchService zalandoSearchService;
    private final BootbotConfig bootbotConfig;

    @Autowired
    public BootbotScheduleService(FootlockerSearchService footlockerSearchService, ZalandoSearchService zalandoSearchService, BootbotConfig bootbotConfig) {
        this.footlockerSearchService = footlockerSearchService;
        this.zalandoSearchService = zalandoSearchService;
        this.bootbotConfig = bootbotConfig;
    }

    @Scheduled(fixedDelay = 60000L)
    private void executeAutomaticSearch() {
        for (Boot boot : bootbotConfig.getBootsToWatch()) {
            zalandoSearchService.search(boot);
            footlockerSearchService.search(boot);
        }
    }

}
