package com.originlang.webmvc.servlet;

import com.originlang.webmvc.annotation.AnonymousDeleteMapping;
import com.originlang.webmvc.annotation.AnonymousGetMapping;
import com.originlang.webmvc.annotation.AnonymousPatchMapping;
import com.originlang.webmvc.annotation.AnonymousPostMapping;
import com.originlang.webmvc.annotation.AnonymousPutMapping;
import com.originlang.webmvc.enums.RequestMethodEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(controllers = AnonymousUtilsWebMvcTest.AnonymousController.class,
		properties = "server.servlet.context-path=/api")
@Import(AnonymousUtilsWebMvcTest.AnonymousController.class)
class AnonymousUtilsWebMvcTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void discoversAnonymousUrlsGroupedByHttpMethodAndContextPath() {
		Map<String, Set<PathPattern>> anonymousUrls = AnonymousUtils.getAnonymousUrl(this.applicationContext);

		assertContains(anonymousUrls.get(RequestMethodEnum.GET.getType()), "/api/anonymous/get");
		assertContains(anonymousUrls.get(RequestMethodEnum.POST.getType()), "/api/anonymous/post");
		assertContains(anonymousUrls.get(RequestMethodEnum.PUT.getType()), "/api/anonymous/put");
		assertContains(anonymousUrls.get(RequestMethodEnum.PATCH.getType()), "/api/anonymous/patch");
		assertContains(anonymousUrls.get(RequestMethodEnum.DELETE.getType()), "/api/anonymous/delete");
	}

	@Test
	void matchesAntStylePatterns() {
		assertTrue(AnonymousUtils.matchUrl("/api/user/*", "/api/user/1"));
	}

	private static void assertContains(Set<PathPattern> patterns, String pattern) {
		assertTrue(patterns.stream().anyMatch(pathPattern -> pathPattern.getPatternString().equals(pattern)));
	}

	@SpringBootConfiguration
	@EnableAutoConfiguration
	static class TestApplication {

	}

	@RestController
	static class AnonymousController {

		@AnonymousGetMapping("/anonymous/get")
		String get() {
			return "get";
		}

		@AnonymousPostMapping("/anonymous/post")
		String post() {
			return "post";
		}

		@AnonymousPutMapping("/anonymous/put")
		String put() {
			return "put";
		}

		@AnonymousPatchMapping("/anonymous/patch")
		String patch() {
			return "patch";
		}

		@AnonymousDeleteMapping("/anonymous/delete")
		String delete() {
			return "delete";
		}

	}

}
