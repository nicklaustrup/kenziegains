package com.kenzie.appserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    // Create a Cache here if needed
    @Bean
    public CacheUser UserCache() {
        return new CacheUser(120, TimeUnit.SECONDS);
    }
    @Bean
    public CacheInstructorLeadClass InstructorLeadClassCache() {
        return new CacheInstructorLeadClass(120, TimeUnit.SECONDS);
    }
    @Bean
    public CacheClassAttendance ClassAttendanceCache() {
        return new CacheClassAttendance(120, TimeUnit.SECONDS);
    }
}
