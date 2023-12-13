package de.jobstimel.bootbot.bootbot.zalando;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import de.jobstimel.bootbot.bootbot.footlocker.CacheService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ZalandoCacheService implements CacheService {

    private final Cache<String, Boolean> zalandoCache;

    public ZalandoCacheService() {
        this.zalandoCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }

    @Override
    public void addValue(String value) {
        this.zalandoCache.put(value, Boolean.TRUE);
    }

    @Override
    public boolean containsValue(String value) {
        return this.zalandoCache.getIfPresent(value) != null;
    }

}
