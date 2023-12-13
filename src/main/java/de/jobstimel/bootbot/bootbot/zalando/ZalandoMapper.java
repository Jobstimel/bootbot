package de.jobstimel.bootbot.bootbot.zalando;

import de.jobstimel.bootbot.bootbot.discord.DiscordMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ZalandoMapper {

    @Mapping(target = "shop", constant = ZalandoConstants.NAME)
    DiscordMessage map(ZalandoSearchHit zalandoSearchHit);

}
