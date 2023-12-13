package de.jobstimel.bootbot.bootbot.discord;

import de.jobstimel.bootbot.bootbot.config.BootbotConfig;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DiscordService {

    private TextChannel textChannel;

    public DiscordService(BootbotConfig bootbotConfig) {
        if (!bootbotConfig.getBotToken().isEmpty() && !bootbotConfig.getChannelId().isEmpty()) {
            try {
                JDABuilder builder = JDABuilder.createDefault(bootbotConfig.getBotToken());
                JDA jda = builder.build().awaitReady();
                this.textChannel = jda.getTextChannelById(bootbotConfig.getChannelId());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.exit(1);
            log.error("Failed to create discord text channel - botToken / channelId are empty");
        }
    }

    public void sendMessage(DiscordMessage discordMessage) {
        if (this.textChannel != null) {
            this.textChannel.sendMessage(discordMessage.format()).queue();
        } else {
            log.error("Unable to establish discord text channel");
        }
    }

}
