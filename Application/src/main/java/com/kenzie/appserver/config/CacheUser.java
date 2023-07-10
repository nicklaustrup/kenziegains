package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.service.model.User;

import java.util.concurrent.TimeUnit;

public class CacheUser {
    public Cache<String, User> cache;

    public CacheUser(int expiry, TimeUnit timeUnit) {
        // initalize the cache
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public User get(String key) {
        // Write your code here
        // Retrieve and return the concert
        return this.cache.getIfPresent(key);
    }

    public void evict(String key) {
        // Write your code here
        // Invalidate/evict the concert from cache
        if (key != null) {
            this.cache.invalidate(key);
        }
    }

    public void add(String key, User value) {
        // Write your code here
        // Add concert to cache
        if (key != null && value != null) {
            this.cache.put(key, value);
        }
    }
}
