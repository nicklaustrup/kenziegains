package com.kenzie.capstone.service.caching;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.inject.Inject;

public class CacheClient {
        private final JedisPool jedisPool;
        @Inject
        public CacheClient(JedisPool jedisPool) {
            this.jedisPool = jedisPool;
        }
        public void setValue(String key, String instructorName, String className) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.hset(key, instructorName, className);
            }
        }
        public String getValue(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                return jedis.get(key);
            }
        }
        public void deleteValue(String key) {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.del(key);
            }
        }
}
