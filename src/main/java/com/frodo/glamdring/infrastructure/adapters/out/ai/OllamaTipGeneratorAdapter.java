package com.frodo.glamdring.infrastructure.adapters.out.ai;

import com.frodo.glamdring.application.ports.out.TipGeneratorPort;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Outbound adapter implementing TipGeneratorPort via the local Ollama model,
 * through Spring AI's ChatClient.
 * <p>
 * qwen2:0.5b is small enough that it sometimes ignores the system prompt
 * entirely, so this adapter also checks the reply for DDD vocabulary and
 * retries once before giving up and returning a canned tip — the caller
 * (TipOfTheDayApplicationService) never sees the difference.
 */
@Component
public class OllamaTipGeneratorAdapter implements TipGeneratorPort {

    private static final String SYSTEM_PROMPT = """
            You are a mentor who ONLY gives tips about Domain-Driven Design (DDD).
            Respond with exactly one short sentence, and always mention one of these
            DDD concepts by name: bounded context, aggregate, entity, value object,
            ubiquitous language, domain event, repository, anti-corruption layer,
            domain service, or invariant.

            Examples of correct answers:
            - "Keep your aggregates small — a large aggregate is a sign the domain boundary is wrong."
            - "Use the ubiquitous language in code, not just in meetings, so the model and the conversation never drift apart."
            - "A value object with no identity should always be immutable."

            Never answer with anything unrelated to DDD, such as general coding
            advice, tooling tips, or personal anecdotes.
            """;

    private static final String PROMPT =
            "Give me one tip of the day. Name a specific DDD concept and say something concrete about it — "
                    + "don't just say you're doing DDD.";

    // Deliberately specific concept names only — generic terms like "DDD" or
    // "domain model" are too easy to satisfy without saying anything real,
    // as seen when the model wrote a whole sentence just to name-drop DDD.
    private static final List<String> DDD_KEYWORDS = List.of(
            "bounded context", "aggregate", "entity", "value object",
            "ubiquitous language", "domain event", "repository",
            "anti-corruption", "domain service", "invariant",
            "hexagonal", "ports and adapters"
    );

    private static final List<String> FALLBACK_TIPS = List.of(
            "Keep aggregates small — the bigger they get, the more likely your bounded context is wrong.",
            "Model behavior, not data — an anemic domain model is a red flag in DDD.",
            "Use the ubiquitous language in your code, not just your meetings.",
            "A value object should be immutable and defined entirely by its attributes, not an identity.",
            "Domain events are how bounded contexts talk without coupling to each other's internals."
    );

    // Matches the first sentence-ending punctuation followed by whitespace or
    // end of string, so a rambling reply can be cut down to one sentence.
    private static final Pattern SENTENCE_END = Pattern.compile("[.!?](?=\\s|$)");

    private final ChatClient chatClient;

    public OllamaTipGeneratorAdapter(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String generateTip() {
        String tip = firstSentence(askModel());
        if (isOnTopic(tip)) {
            return tip;
        }

        String retry = firstSentence(askModel());
        if (isOnTopic(retry)) {
            return retry;
        }

        return FALLBACK_TIPS.get(ThreadLocalRandom.current().nextInt(FALLBACK_TIPS.size()));
    }

    private String askModel() {
        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(PROMPT)
                .options(OllamaChatOptions.builder()
                        .temperature(0.4)
                        .numPredict(60))
                .call()
                .content();
    }

    /**
     * Keeps only the first sentence — the model is asked for one, but a small
     * model like qwen2:0.5b doesn't reliably stop there on its own.
     */
    private String firstSentence(String text) {
        if (text == null) {
            return null;
        }
        String trimmed = text.strip();
        Matcher matcher = SENTENCE_END.matcher(trimmed);
        return matcher.find() ? trimmed.substring(0, matcher.end()).strip() : trimmed;
    }

    private boolean isOnTopic(String tip) {
        if (tip == null || tip.isBlank()) {
            return false;
        }
        String lower = tip.toLowerCase(Locale.ROOT);
        return DDD_KEYWORDS.stream().anyMatch(lower::contains);
    }
}
