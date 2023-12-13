package de.jobstimel.bootbot.bootbot.footlocker;

import de.jobstimel.bootbot.bootbot.discord.DiscordMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FootlockerMapper {

    @Mapping(target = "shop", constant = FootlockerConstants.NAME)
    DiscordMessage map(FootlockerSearchHit footlockerSearchHit);

}
