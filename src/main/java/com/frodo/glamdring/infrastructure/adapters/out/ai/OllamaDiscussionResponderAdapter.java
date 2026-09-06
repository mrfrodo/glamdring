package com.frodo.glamdring.infrastructure.adapters.out.ai;

import com.frodo.glamdring.application.ports.out.DiscussionResponderPort;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Outbound adapter implementing DiscussionResponderPort via a second local
 * Ollama model, a code-specialised Qwen variant — the "Qwen Coder" side of
 * the discussion, answering the initiator's opening remark exactly once.
 */
@Component
public class OllamaDiscussionResponderAdapter implements DiscussionResponderPort {

    private static final String SYSTEM_PROMPT = """
            You are Qwen Coder, a pragmatic software engineer replying to a
            colleague's opening remark in a short hallway conversation about
            software technology. Give one direct reply of one or two short
            sentences — agree, disagree, or add a concrete detail. This is
            the last word in the conversation, so don't ask a question back.
            """;

    private static final String FALLBACK_REPLY =
            "Fair — the ceremony only earns its keep the day you actually need a second adapter, not before.";

    private final ChatClient chatClient;
    private final String codeModel;

    public OllamaDiscussionResponderAdapter(
            ChatClient.Builder chatClientBuilder,
            @Value("${glamdring.ollama.models.code}") String codeModel) {
        this.chatClient = chatClientBuilder.build();
        this.codeModel = codeModel;
    }

    @Override
    public String respond(String openingMessage) {
        String reply = ask(openingMessage);
        return (reply == null || reply.isBlank()) ? FALLBACK_REPLY : reply;
    }

    private String ask(String openingMessage) {
        String content = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(openingMessage)
                .options(OllamaChatOptions.builder()
                        .model(codeModel)
                        .temperature(0.6)
                        .numPredict(80))
                .call()
                .content();
        return content == null ? null : content.strip();
    }
}
