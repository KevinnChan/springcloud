package com.kevin.gateway.config;

import com.netflix.zuul.*;
import com.netflix.zuul.filters.FilterRegistry;
import com.netflix.zuul.groovy.GroovyCompiler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kevin on 2019/5/30.
 */
@Slf4j
public class DynamicFilterLoader {
	static final DynamicFilterLoader INSTANCE = new DynamicFilterLoader();

	private final ConcurrentHashMap<String, ZuulFilter> activeFilter = new ConcurrentHashMap<>();

	private DynamicFilterLoader() {
	}

	public static DynamicFilterLoader getInstance() {
		return INSTANCE;
	}

	private FilterRegistry filterRegistry = FilterRegistry.instance();

	static FilterFactory FILTER_FACTORY = new DefaultFilterFactory();

	DynamicCodeCompiler dynamicCodeCompiler = new GroovyCompiler();

	public void putAllFilter(List<GroovyZuulFilter> filterList) throws Exception {
		if (CollectionUtils.isEmpty(filterList))
			return;

		for (GroovyZuulFilter filter : filterList) {
			activeFilter(filter);
		}
		//refreshOriginCache();
	}

	public void activeFilter(GroovyZuulFilter filter) throws Exception {
		if (filter == null || StringUtils.isBlank(filter.getFilterCode())) {
			return;
		}

		log.info("active dynamic groovy filter {} - {}", filter.getApplicationName(), filter.getFilterName());
		Class clz = dynamicCodeCompiler.compile(filter.getFilterCode(), filter.getFilterName());
		ZuulFilter zuulFilter = FILTER_FACTORY.newInstance(clz);
		filterRegistry.put(filter.getFilterName(), zuulFilter);
		activeFilter.put(filter.getFilterName(), zuulFilter);
		refreshOriginCache(filter.getFilterType());
	}

	public void disableFilter(String filterName) throws Exception {
		if (!activeFilter.containsKey(filterName)) {
			return;
		}

		log.info("disable dynamic groovy filter {}", filterName);
		filterRegistry.remove(filterName);
		activeFilter.remove(filterName);
		refreshOriginCache(activeFilter.get(filterName).filterType());
	}

	public void refreshOriginCache(String type) throws Exception {
		FilterLoader loader = FilterLoader.getInstance();
		Class cls = loader.getClass();
		Field field = cls.getDeclaredField("hashFiltersByType");
		field.setAccessible(true);
		ConcurrentHashMap<String, List<ZuulFilter>> currentCache = (ConcurrentHashMap<String, List<ZuulFilter>>) field.get(loader);
		if (!currentCache.containsKey(type)) {
			return;
		}
		currentCache.remove(type);
		field.set(loader, currentCache);
	}
}
