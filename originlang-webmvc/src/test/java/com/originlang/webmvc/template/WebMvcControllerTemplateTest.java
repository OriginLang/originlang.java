package com.originlang.webmvc.template;

import com.originlang.webmvc.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WebMvcControllerTemplateTest.TemplateController.class)
@Import(WebMvcControllerTemplateTest.TemplateController.class)
class WebMvcControllerTemplateTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TemplateService templateService;

	@Test
	void getGreetingShouldReturnWrappedPayload() throws Exception {
		given(this.templateService.greet("originlang")).willReturn("hello originlang");

		this.mockMvc.perform(get("/template/greet/originlang").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.data").value("hello originlang"));
	}

	@SpringBootConfiguration
	@EnableAutoConfiguration
	static class TestApplication {

	}

	@RestController
	static class TemplateController {

		private final TemplateService templateService;

		TemplateController(TemplateService templateService) {
			this.templateService = templateService;
		}

		@GetMapping("/template/greet/{name}")
		Result<String> greet(@PathVariable String name) {
			return Result.succeed("succeed", this.templateService.greet(name));
		}

	}

	interface TemplateService {

		String greet(String name);

	}

}
