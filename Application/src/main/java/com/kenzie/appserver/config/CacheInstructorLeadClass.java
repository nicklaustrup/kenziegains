package com.kenzie.appserver.config;

import com.kenzie.appserver.service.model.InstructorLeadClass;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheInstructorLeadClass {
    public Cache<String, InstructorLeadClass> cache;

    public CacheInstructorLeadClass(int expiry, TimeUnit timeUnit) {
        // initalize the cache
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public InstructorLeadClass get(String key) {
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

    public void add(String key, InstructorLeadClass value) {
        // Write your code here
        // Add concert to cache
        if (key != null && value != null) {
            this.cache.put(key, value);
        }
    }
}
