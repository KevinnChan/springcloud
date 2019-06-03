package com.kevin.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 2019/5/31.
 */
@Slf4j
@Component
public class DynamicFilterFileManager {
	Thread poller;

	boolean bRunning = true;

	int pollingIntervalSeconds;

	static DynamicFilterFileManager INSTANCE = new DynamicFilterFileManager();

	private DynamicFilterFileManager() {
	}

	public static DynamicFilterFileManager getInstance() {
		return INSTANCE;
	}

	public void init(int pollingIntervalSeconds) {
		INSTANCE.pollingIntervalSeconds = pollingIntervalSeconds;
		startPoller();
	}

	/**
	 * Shuts down the poller
	 */
	@PreDestroy
	public static void shutdown() {
		INSTANCE.stopPoller();
	}

	void stopPoller() {
		bRunning = false;
	}

	void startPoller() {
		log.info("DynamicGroovyFilterFileManager polling period is {} seconds", pollingIntervalSeconds);
		poller = new Thread("DynamicGroovyFilterFileManagerPoller") {
			public void run() {
				while (bRunning) {
					try {
						sleep(pollingIntervalSeconds * 1000);
						manageFiles();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		poller.setDaemon(true);
		poller.start();
	}

	private void manageFiles() throws Exception {
		DynamicFilterLoader.getInstance().putAllFilter(getAllFilter());
	}

	private List<GroovyZuulFilter> getAllFilter() {
		List<GroovyZuulFilter> filterList = new ArrayList<>();
		filterList.add(getFilter());
		return filterList;
	}

	private GroovyZuulFilter getFilter() {
		GroovyZuulFilter groovyZuulFilter = new GroovyZuulFilter();
		groovyZuulFilter.setId(1);
		groovyZuulFilter.setRevision(1);

		String name = "inputParamLogFilter";
		String code = "package com.kevin.gateway.filter;\n" +
				"import com.netflix.zuul.ZuulFilter;\n" +
				"import com.netflix.zuul.context.RequestContext;\n" +
				"import groovy.util.logging.Slf4j;\n" +
				"import javax.servlet.http.HttpServletRequest;\n" +
				"@Slf4j\n" +
				"public class InputParamLogFilter extends ZuulFilter {\n" +
				"\t@Override\n" +
				"\tpublic String filterType() {\n" +
				"\t\treturn \"pre\";\n" +
				"\t}\n" +
				"\n" +
				"\t@Override\n" +
				"\tpublic int filterOrder() {\n" +
				"\t\treturn 0;\n" +
				"\t}\n" +
				"\n" +
				"\t@Override\n" +
				"\tpublic boolean shouldFilter() {\n" +
				"\t\treturn true;\n" +
				"\t}\n" +
				"\n" +
				"\t@Override\n" +
				"\tpublic Object run() {\n" +
				"\t\tlog.info(\"======================================================\");\n" +
				"\t\tHttpServletRequest request = RequestContext.getCurrentContext().getRequest();\n" +
				"\t\tString verb = getVerb(request);\n" +
				"\t\tswitch (verb) {\n" +
				"\t\t\tcase \"POST\":\n" +
				"\t\t\t\tlog.info(\"Gateway log {}\", request);\n" +
				"\t\t\t\tbreak;\n" +
				"\t\t\tcase \"GET\":\n" +
				"\t\t\t\tlog.info(\"Gateway log {}\", request.getParameterMap());\n" +
				"\t\t\t\tbreak;\n" +
				"\t\t\tdefault:\n" +
				"\t\t\t\tlog.info(\"Gateway log {}\", request);\n" +
				"\t\t\t\tbreak;\n" +
				"\t\t}\n" +
				"\t\treturn null;\n" +
				"\t}\n" +
				"\n" +
				"\tprivate String getVerb(HttpServletRequest request) {\n" +
				"\t\tString sMethod = request.getMethod();\n" +
				"\t\treturn sMethod.toUpperCase();\n" +
				"\t}\n" +
				"}\n";
		groovyZuulFilter.setFilterName(name);
		groovyZuulFilter.setFilterCode(code);
		groovyZuulFilter.setFilterType("pre");
		return groovyZuulFilter;
	}
}
