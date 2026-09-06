package com.frodo.glamdring.infrastructure.adapters.out.ai;

import com.frodo.glamdring.application.ports.out.DiscussionInitiatorPort;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.stereotype.Component;

/**
 * Outbound adapter implementing DiscussionInitiatorPort via the local Ollama
 * model — the "Qwen" side of the two-LLM tech discussion. Uses the default
 * model configured for the app (qwen2:0.5b), and opens with a remark for the
 * responder to answer.
 */
@Component
public class OllamaDiscussionInitiatorAdapter implements DiscussionInitiatorPort {

    private static final String SYSTEM_PROMPT = """
            You are Qwen, a curious software engineer starting a short hallway
            conversation with a colleague about a matter of software technology
            — architecture, a language, a tool, a practice. Open with one or two
            short sentences: state an opinion or observation and invite a reply.
            Do not greet or introduce yourself, just start talking shop.
            """;

    private static final String PROMPT = "Start the conversation.";

    private static final String FALLBACK_OPENING =
            "I've been thinking hexagonal architecture only pays off once you actually swap an adapter — otherwise it's just ceremony. Agree?";

    private final ChatClient chatClient;

    public OllamaDiscussionInitiatorAdapter(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String initiate() {
        String opening = ask();
        return (opening == null || opening.isBlank()) ? FALLBACK_OPENING : opening;
    }

    private String ask() {
        String content = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(PROMPT)
                .options(OllamaChatOptions.builder()
                        .temperature(0.6)
                        .numPredict(80))
                .call()
                .content();
        return content == null ? null : content.strip();
    }
}
