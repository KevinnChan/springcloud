package com.kevin.gateway.rest;

import com.kevin.gateway.config.DynamicFilterLoader;
import com.kevin.gateway.config.GroovyZuulFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Kevin on 2019/6/3.
 */
@Slf4j
@RestController
@RequestMapping("/groovyFilter")
public class GroovyFilterRestConrtoller {
	@RequestMapping(value = "/disable", method = RequestMethod.GET)
	public void disable(@RequestParam("filterName") String filterName) {
		try {
			if (StringUtils.isBlank(filterName)) {
				return;
			}
			DynamicFilterLoader.getInstance().disableFilter(filterName);
		} catch (Exception e) {
			log.error("Disable filter error ", e);
		}
	}

	@RequestMapping(value = "/active", method = RequestMethod.GET)
	public void active(@RequestParam("version") Integer version) {
		try {
			GroovyZuulFilter groovyZuulFilter = new GroovyZuulFilter();
			groovyZuulFilter.setId(1);
			groovyZuulFilter.setRevision(version);

			String name = "inputParamLogFilter2";
			String code = "package com.kevin.gateway.filter;\n" +
					"import com.netflix.zuul.ZuulFilter;\n" +
					"import com.netflix.zuul.context.RequestContext;\n" +
					"import groovy.util.logging.Slf4j;\n" +
					"import javax.servlet.http.HttpServletRequest;\n" +
					"@Slf4j\n" +
					"public class InputParamLogFilter2 extends ZuulFilter {\n" +
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
					"\t\t\t\tlog.info(\"Gateway log  version 2 {}\", request.getParameterMap());\n" +
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
			DynamicFilterLoader.getInstance().activeFilter(groovyZuulFilter);
		} catch (Exception e) {
			log.error("Active filter error ", e);
		}
	}
}
