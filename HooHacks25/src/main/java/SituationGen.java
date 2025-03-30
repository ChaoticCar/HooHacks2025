import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.CountDownLatch;

public class SituationGen {

    public static void main(String[] args) {
        System.out.println(run());
    }

    public static String run() {
        System.out.println("We got here!");

        // Initialize the Gemini model
        StreamingChatLanguageModel gemini = GoogleAiGeminiStreamingChatModel.builder()
                .apiKey(APIKey.GEMINI_APIKey)
                .modelName("gemini-1.5-flash")
                .build();

        // Simulate a single d20 roll
        int d20Roll = rollD20();
        System.out.println("Simulated d20 Roll: " + d20Roll);

        // Construct the prompt based on the d20 roll
        String prompt = createScenarioPrompt(d20Roll);
        System.out.println("Sending Prompt to Gemini:\n\"" + prompt + "\"\n---");

        // Create a latch to block the main thread until the async task completes
        CountDownLatch latch = new CountDownLatch(1);
        StringBuilder scenario = new StringBuilder();

        // Generate the Scenario using the Gemini Model
        gemini.chat(prompt, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                System.out.print(partialResponse);  // Print partial responses
                scenario.append(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                latch.countDown();  // Signal completion
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                latch.countDown();
            }
        });

        // Block until the latch is released
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }

        // Return the final scenario
        return scenario.toString();
    }

    /**
     * Simulates rolling a 20-sided die.
     * @return An integer between 1 and 20 (inclusive).
     */
    private static int rollD20() {
        return ThreadLocalRandom.current().nextInt(1, 21);
    }

    /**
     * Creates the prompt string to send to Gemini, incorporating the d20 roll.
     * @param rollResult The result of the d20 roll (1-20).
     * @return A formatted prompt string.
     */
    private static String createScenarioPrompt(int rollResult) {
        return String.format(
                "You are a creative Dungeons & Dragons Dungeon Master. " +
                        "I just rolled a d20 to determine the nature of a random combat encounter, and the result was %d. " +
                        "Interpret this roll where 1 represents a trivially easy encounter, " +
                        "10 represents a standard, balanced encounter, " +
                        "and 20 represents a very difficult, challenging encounter. " +
                        "Based *only* on this d20 roll of %d, describe a brief D&D combat scenario suitable for a party of adventurers. " +
                        "Include the following details:\n" +
                        "- Location: An evocative location description.\n" +
                        "- Monsters: 1 to 3 types of thematic monsters.\n" +
                        "- Numbers: The approximate number of monsters.\n" +
                        "- Setup: A short sentence describing the initial situation.\n" +
                        "Ensure the challenge directly reflects the d20 roll of %d.",
                rollResult, rollResult, rollResult
        );
    }
}
