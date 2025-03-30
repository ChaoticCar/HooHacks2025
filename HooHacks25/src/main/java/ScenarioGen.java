/*import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.google.GoogleAiChatModel;
import dev.langchain4j.model.output.Response;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ScenarioGen {

    // Read the API key from environment variables
    private static final String GEMINI_API_KEY = System.getenv("GEMINI_API_KEY");
    private static final String GEMINI_MODEL_NAME = "gemini-pro";

    public static void main(String[] args) {

        // **Crucial Check:** Ensure the key was actually found
        if (GEMINI_API_KEY == null || GEMINI_API_KEY.trim().isEmpty()) {
            System.err.println("Error: GEMINI_API_KEY environment variable not set.");
            System.err.println("Please set the environment variable before running the application.");
            return; // Exit if the key is missing
        }

        // ... (rest of your main method remains the same) ...

        // 1. Simulate D20 Roll
        int d20Roll = rollD20();
        System.out.println("Simulated d20 Roll: " + d20Roll);
        System.out.println("---");

        // 2. Create the Chat Model (uses the key read from the environment)
        ChatLanguageModel model = GoogleAiChatModel.builder()
                .apiKey(GEMINI_API_KEY) // Pass the key read from environment
                .modelName(GEMINI_MODEL_NAME)
                .build();

        // 3. Construct the Prompt
        String prompt = createScenarioPrompt(d20Roll);
        System.out.println("Sending Prompt to Gemini:\n\"" + prompt + "\"\n---");

        // 4. Generate the Scenario
        try {
            Response<String> response = model.generate(prompt);
            String scenario = response.content();

            System.out.println("Generated D&D Combat Scenario:");
            System.out.println("==============================");
            System.out.println(scenario);
            System.out.println("==============================");

        } catch (Exception e) {
            System.err.println("Error interacting with Gemini API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ... (rollD20 and createScenarioPrompt methods remain the same) ...
    private static int rollD20() {
        return ThreadLocalRandom.current().nextInt(1, 21);
    }
    private static String createScenarioPrompt(int rollResult) {
        return String.format( /* your prompt string here *//* );
    }
}*/