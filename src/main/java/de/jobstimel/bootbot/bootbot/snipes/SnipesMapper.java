package de.jobstimel.bootbot.bootbot.snipes;

import de.jobstimel.bootbot.bootbot.discord.DiscordMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SnipesMapper {

    @Mapping(target = "shop", constant = SnipesConstants.SHOP_NAME)
    DiscordMessage map(SnipesSearchHit snipesSearchHit);

}
