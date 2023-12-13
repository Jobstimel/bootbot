package de.jobstimel.bootbot.bootbot;

import de.jobstimel.bootbot.bootbot.config.BootbotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(BootbotConfig.class)
public class BootbotApplication {

    public static void main(String[] args) {
		SpringApplication.run(BootbotApplication.class, args);
	}

}
