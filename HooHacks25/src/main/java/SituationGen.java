import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class SituationGen {

    private static String currentMonster;
    private static int turnCounter = 0;

    public static void main(String[] args) {
        String scenario = run(0, null);    // Start the battle
        System.out.println("\nFinal Scenario:\n" + scenario);
        // Generate the image based on the scenario and save it in the image folder
    }

    public static String run(int turn, String monster) {
        currentMonster = monster;

        StreamingChatLanguageModel gemini = GoogleAiGeminiStreamingChatModel.builder()
                .apiKey(APIKey.PROKEY)
                .modelName("gemini-2.0-flash")
                .build();

        if (turn == 0) {
            currentMonster = monster != null ? monster : getMonster();
        }

        int d20Roll = rollD20();
        System.out.println("Simulated d20 Roll: " + d20Roll);

        String prompt = createScenarioPrompt(d20Roll, turn, currentMonster);
        System.out.println("Sending Prompt to Gemini:\n\"" + prompt + "\"\n---");

        CountDownLatch latch = new CountDownLatch(1);
        StringBuilder scenario = new StringBuilder();

        gemini.chat(prompt, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                System.out.print(partialResponse);
                scenario.append(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                latch.countDown();
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }

        turnCounter++;
        return scenario.toString();
    }

    private static int rollD20() {
        return ThreadLocalRandom.current().nextInt(1, 21);
    }

    private static String createScenarioPrompt(int rollResult, int turn, String monster) {
        if (turn % 2 == 0) {
            return String.format(
                    "You are a creative Dungeons & Dragons Dungeon Master. " +
                            "It's my turn in combat. I wield a sword and rolled a %d on a D20. " +
                            "The higher the roll, the more likely my attack succeeds. " +
                            "Describe my attack on the %s, including its effectiveness, impact on the opponent, " +
                            "damage done, and any environmental details that enhance the scene. " +
                            "Provide three options for my next move. This is turn %d.", rollResult, monster, turn
            );
        } else {
            return String.format(
                    "You are a creative Dungeons & Dragons Dungeon Master. " +
                            "It's my opponent's turn in combat. They are a fearsome %s and rolled a %d on a D20. " +
                            "The higher the roll, the more likely their attack succeeds. " +
                            "Describe their attack, including its effectiveness, impact on me, " +
                            "damage done, and any environmental details that enhance the scene. " +
                            "Provide three options for my next move. This is turn %d.", monster, rollResult, turn
            );
        }
    }

    private static String getMonster() {
        return currentMonster;
    }
}