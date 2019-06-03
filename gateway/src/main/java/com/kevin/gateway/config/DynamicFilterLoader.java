package com.kevin.gateway.config;

import com.netflix.zuul.*;
import com.netflix.zuul.filters.FilterRegistry;
import com.netflix.zuul.groovy.GroovyCompiler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kevin on 2019/5/30.
 */
public class DynamicFilterLoader {
	private static final Logger log = LoggerFactory.getLogger(DynamicFilterLoader.class);

	static final DynamicFilterLoader INSTANCE = new DynamicFilterLoader();

	private final ConcurrentHashMap<String, Integer> activeFilterVersionContainer = new ConcurrentHashMap<>();

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
	}

	public void activeFilter(GroovyZuulFilter filter) throws Exception {
		if (filter == null || StringUtils.isBlank(filter.getFilterCode())) {
			return;
		}

		if (activeFilterVersionContainer.containsKey(filter.getFilterName())) {
			if (activeFilterVersionContainer.get(filter.getFilterName()).intValue() == filter.getRevision().intValue()) {
				return;
			}
		}

		log.info("Active dynamic groovy filter {} - {} - {}", filter.getApplicationName(), filter.getFilterName(), filter.getRevision());
		Class clz = dynamicCodeCompiler.compile(filter.getFilterCode(), filter.getFilterName());
		ZuulFilter zuulFilter = FILTER_FACTORY.newInstance(clz);
		filterRegistry.put(filter.getFilterName(), zuulFilter);
		activeFilterVersionContainer.put(filter.getFilterName(), filter.getRevision());
		refreshOriginCache(filter.getFilterType());
	}

	public void disableFilter(String filterName) throws Exception {
		if (!activeFilterVersionContainer.containsKey(filterName)) {
			return;
		}

		log.info("Disable dynamic groovy filter {}", filterName);
		refreshOriginCache(filterRegistry.get(filterName).filterType());
		filterRegistry.remove(filterName);
		activeFilterVersionContainer.remove(filterName);
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
