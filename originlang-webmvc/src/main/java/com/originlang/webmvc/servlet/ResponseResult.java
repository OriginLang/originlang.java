package com.originlang.webmvc.servlet;

import com.originlang.webmvc.Result;
import com.originlang.webmvc.advice.IgnoreResponseResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

/**
 * 统一返回结果封装， Controller层直接返回数据，不用再包装。
 */
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
class ResponseResult implements ResponseBodyAdvice<Object> {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		// 方法上有注解，不拦截进行封装
		if (returnType.getDeclaringClass().isAnnotationPresent(IgnoreResponseResult.class)) {
			// 类上有注解，不拦截进行封装
			return false;
		}
		else {
			return !Objects.requireNonNull(returnType.getMethod()).isAnnotationPresent(IgnoreResponseResult.class);
		}
	}

	@Override
	public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		// response.getHeaders().set("Access-Control-Allow-Origin", "*");
		if (body instanceof Result<?>) {
			// 如果返回的数据是Result类型，直接返回不做处理
			return body;
		}
		if (body instanceof String) {
			// 解决返回字符串时，不能正常包装的问题

			return objectMapper.writeValueAsString(Result.succeed(body));

		}

		return Result.succeed(body);
	}

}