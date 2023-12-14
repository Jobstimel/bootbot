package de.jobstimel.bootbot.bootbot.snipes;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.jobstimel.bootbot.bootbot.footlocker.CacheService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SnipesCacheService implements CacheService {

    private final Cache<String, Boolean> snipesCache;

    public SnipesCacheService() {
        this.snipesCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }

    @Override
    public void addValue(String value) {
        this.snipesCache.put(value, Boolean.TRUE);
    }

    @Override
    public boolean containsValue(String value) {
        return this.snipesCache.getIfPresent(value) != null;
    }

}
