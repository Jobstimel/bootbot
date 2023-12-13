package de.jobstimel.bootbot.bootbot.config;

import de.jobstimel.bootbot.bootbot.BootbotConstants;
import de.jobstimel.bootbot.bootbot.data.Boot;
import de.jobstimel.bootbot.bootbot.data.Gender;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Component
@ConfigurationProperties
@PropertySource("file:${config.properties.path}")
public class BootbotConfig {

    @Value("${bot.token}")
    private String botToken;

    @Value("${channel.id}")
    private String channelId;

    @Value("${boots.file.path}")
    private String bootsFilePath;

    private List<Boot> bootsToWatch = new ArrayList<>();

    @PostConstruct
    private void init() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.bootsFilePath))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                } else {
                    String name = line.split(BootbotConstants.BOOT_SEPERATOR)[0].strip();
                    String size = line.split(BootbotConstants.BOOT_SEPERATOR)[1].strip();
                    Gender gender = Gender.valueOf(line.split(BootbotConstants.BOOT_SEPERATOR)[2].strip().toUpperCase());
                    bootsToWatch.add(new Boot(name, size, gender));
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
