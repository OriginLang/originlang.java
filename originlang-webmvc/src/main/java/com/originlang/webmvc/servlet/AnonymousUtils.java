package com.originlang.webmvc.servlet;

import com.originlang.base.context.ApplicationContextProvider;
import com.originlang.webmvc.annotation.AnonymousAccess;
import com.originlang.webmvc.enums.RequestMethodEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.server.PathContainer;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.*;

public class AnonymousUtils {

	public static Map<String, Set<PathPattern>> getAnonymousUrl(ApplicationContext applicationContext) {
		RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) applicationContext
			.getBean("requestMappingHandlerMapping");
		Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();

		Map<String, Set<PathPattern>> anonymousUrls = new HashMap<>(8);
		Set<PathPattern> get = new HashSet<>();
		Set<PathPattern> post = new HashSet<>();
		Set<PathPattern> put = new HashSet<>();
		Set<PathPattern> patch = new HashSet<>();
		Set<PathPattern> delete = new HashSet<>();

		// 获取 context-path 配置
		String contextPath = applicationContext.getEnvironment().getProperty("server.servlet.context-path", "");

		for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
			HandlerMethod handlerMethod = infoEntry.getValue();
			RequestMappingInfo requestMappingInfo = infoEntry.getKey();
			AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
			if (anonymousAccess == null) {
				continue;
			}
			RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
			// for spring 5.x + ,for spring 5.x - Set<String> patterns1 =
			// infoEntry.getKey().getPatternsCondition().getPatterns();
			PathPatternsRequestCondition pathPatternsCondition = requestMappingInfo.getPathPatternsCondition();
			if (pathPatternsCondition == null) {
				continue;
			}

			Set<RequestMethod> methods = methodsCondition.getMethods();

			Set<PathPattern> patterns = pathPatternsCondition.getPatterns();

			// 处理全局 context-path
			PathPatternParser parser = new PathPatternParser();

			for (PathPattern pattern : patterns) {
				// 在每个路径模式前加上 context-path
				String modifiedPath = contextPath + pattern.getPatternString();
				PathPattern modifiedPattern = parser.parse(modifiedPath);
				if (CollectionUtils.isEmpty(methods)) {
					// default is get
					get.add(modifiedPattern);
					continue;
				}
				for (RequestMethod requestMethod : methods) {
					switch (requestMethod) {
						case GET -> get.add(modifiedPattern);
						case POST -> post.add(modifiedPattern);
						case PUT -> put.add(modifiedPattern);
						case DELETE -> delete.add(modifiedPattern);
						case PATCH -> patch.add(modifiedPattern);
						default -> throw new IllegalArgumentException("unsupported request method: " + requestMethod);
					}
				}

			}

		}

		anonymousUrls.put(RequestMethodEnum.GET.getType(), get);
		anonymousUrls.put(RequestMethodEnum.POST.getType(), post);
		anonymousUrls.put(RequestMethodEnum.PUT.getType(), put);
		anonymousUrls.put(RequestMethodEnum.PATCH.getType(), patch);
		anonymousUrls.put(RequestMethodEnum.DELETE.getType(), delete);
		return anonymousUrls;
	}

	private static final AntPathMatcher pathMatcher = new AntPathMatcher();

	public static boolean matchUrl(String pattern, String requestPath) {
		return pathMatcher.match(pattern, requestPath);
	}

	public static boolean isAnonymous(String url, String method) {
		Map<String, Set<PathPattern>> anonymousUrl = getAnonymousUrl(ApplicationContextProvider.getContext());
		Set<PathPattern> patterns = anonymousUrl.get(method);
		if (patterns != null) {
			// 使用 PathPattern 比对实际请求路径
			// PathPatternParser parser = new PathPatternParser();
			// PathPattern pathPattern = parser.parse(url);
			PathContainer pathContainer = PathContainer.parsePath(url);
			// 检查路径是否匹配
			return patterns.stream().anyMatch(pattern -> pattern.matches(pathContainer));
		}

		return false;
	}

}
