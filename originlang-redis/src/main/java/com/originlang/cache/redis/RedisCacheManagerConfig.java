package com.originlang.cache.redis;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.util.regex.Pattern;

public class RedisCacheManagerConfig extends RedisCacheManager {

	public RedisCacheManagerConfig(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
		super(cacheWriter, defaultCacheConfiguration);
	}

	private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

	/**
	 * 创建RedisCache,支持自定义过期时间,格式:cacheName#ttl,ttl为过期时间,单位秒
	 * @param name cacheName#ttl
	 * @param cacheConfig 缓存配置
	 * @return RedisCache
	 */

	@Override
	protected RedisCache createRedisCache(@Nonnull String name, @Nullable RedisCacheConfiguration cacheConfig) {
		final int lastIndexOf = name.lastIndexOf("#");
		final String ttl = name.substring(lastIndexOf + 1);
		if (lastIndexOf != -1 && lastIndexOf != name.length() - 1 && NUMBER_PATTERN.matcher(ttl).matches()
				&& Long.parseLong(ttl) > 0 && cacheConfig != null) {
			final Duration duration = Duration.ofSeconds(Long.parseLong(ttl));
			cacheConfig = cacheConfig.entryTtl(duration);
			final String cacheName = name.substring(0, lastIndexOf);
			return super.createRedisCache(cacheName, cacheConfig);

		}
		else {
			return super.createRedisCache(name, cacheConfig);
		}

	}

}
