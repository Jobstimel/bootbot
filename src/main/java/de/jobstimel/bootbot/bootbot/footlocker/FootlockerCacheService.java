package de.jobstimel.bootbot.bootbot.footlocker;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class FootlockerCacheService implements CacheService {

    private final Cache<String, Boolean> footlockerCache;

    public FootlockerCacheService() {
        this.footlockerCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }

    @Override
    public void addValue(String value) {
        this.footlockerCache.put(value, Boolean.TRUE);
    }

    @Override
    public boolean containsValue(String value) {
        return this.footlockerCache.getIfPresent(value) != null;
    }

}
