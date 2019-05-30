package com.kevin.gateway.config;

import com.google.common.collect.Maps;
import com.netflix.zuul.DefaultFilterFactory;
import com.netflix.zuul.DynamicCodeCompiler;
import com.netflix.zuul.FilterFactory;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.filters.FilterRegistry;
import com.netflix.zuul.groovy.GroovyCompiler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by Kevin on 2019/5/30.
 */
@Slf4j
@Component
public class DynamicFilterLoader {
	private FilterRegistry filterRegistry = FilterRegistry.instance();

	static FilterFactory FILTER_FACTORY = new DefaultFilterFactory();

	@PostConstruct
	private void loadFilter() {
		DynamicCodeCompiler dynamicCodeCompiler = new GroovyCompiler();
		Map<String, String> rawData = getRawGroovy();
		rawData.forEach((k, v) -> {
			try {
				log.info("load groovy filter {}", k);
				Class clz = dynamicCodeCompiler.compile(v,k);
				ZuulFilter filter = FILTER_FACTORY.newInstance(clz);
				filterRegistry.put(k, filter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}


	private Map<String, String> getRawGroovy() {
		Map<String, String> rawData = Maps.newHashMap();
		String name = "inputParamLogFilter";
		String code = "package com.kevin.gateway.filter;\n" +
				"import com.netflix.zuul.ZuulFilter;\n" +
				"import com.netflix.zuul.context.RequestContext;\n" +
				"import groovy.util.logging.Slf4j;\n" +
				"import org.springframework.stereotype.Component;\n" +
				"import javax.servlet.http.HttpServletRequest;\n" +
				"@Slf4j\n" +
				"@Component\n" +
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
		rawData.put(name, code);
		return rawData;
	}
}
