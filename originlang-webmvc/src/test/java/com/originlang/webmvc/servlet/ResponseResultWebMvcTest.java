package com.originlang.webmvc.servlet;

import com.originlang.webmvc.Result;
import com.originlang.webmvc.advice.IgnoreResponseResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ResponseResultWebMvcTest.SampleController.class)
@Import({ ResponseResultWebMvcTest.SampleController.class, ResponseResult.class })
class ResponseResultWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void wrapsPlainObjectResponses() throws Exception {
		this.mockMvc.perform(get("/response/object").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("succeed"))
			.andExpect(jsonPath("$.data.name").value("originlang"));
	}

	@Test
	void wrapsStringResponsesAsJson() throws Exception {
		this.mockMvc.perform(get("/response/string").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data").value("plain text"));
	}

	@Test
	void leavesExistingResultResponsesUntouched() throws Exception {
		this.mockMvc.perform(get("/response/result").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(false))
			.andExpect(jsonPath("$.code").value(409))
			.andExpect(jsonPath("$.message").value("already wrapped"));
	}

	@Test
	void ignoresMethodsAnnotatedWithIgnoreResponseResult() throws Exception {
		this.mockMvc.perform(get("/response/ignored").accept(MediaType.TEXT_PLAIN))
			.andExpect(status().isOk())
			.andExpect(content().string("ignored"));
	}

	@SpringBootConfiguration
	@EnableAutoConfiguration
	static class TestApplication {

	}

	@RestController
	static class SampleController {

		@GetMapping("/response/object")
		Map<String, String> object() {
			return Map.of("name", "originlang");
		}

		@GetMapping("/response/string")
		String string() {
			return "plain text";
		}

		@GetMapping("/response/result")
		Result<Void> result() {
			return Result.failed(409, "already wrapped");
		}

		@IgnoreResponseResult
		@GetMapping("/response/ignored")
		String ignored() {
			return "ignored";
		}

	}

}
