package com.romajs.soa;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

public class ServiceFactory {

	public static LoadingCache<Class<?>, Object> cache = Caffeine.newBuilder()
			.build(key -> key.newInstance());

	public static <T> T getInstance(Class<T> clazz) {
		return (T) cache.get(clazz);
	}

}
