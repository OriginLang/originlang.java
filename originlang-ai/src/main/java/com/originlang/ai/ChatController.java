package com.originlang.ai;

import com.originlang.webmvc.annotation.AnonymousAccess;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController

public class ChatController {

	private final OllamaChatModel chatModel;

	@Autowired
	public ChatController(OllamaChatModel chatModel) {
		this.chatModel = chatModel;
	}

	@AnonymousAccess
	@GetMapping("/ai/generate")
	public Map<String, String> generate(
			@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
		return Map.of("generation", this.chatModel.call(message));
	}

	// @GetMapping("/ai/generateStream")
	// public Flux<ChatResponse> generateStream(@RequestParam(value = "message",
	// defaultValue = "Tell me a joke") String message) {
	// Prompt prompt = new Prompt(new UserMessage(message));
	// return this.chatModel.stream(prompt);
	// }

}