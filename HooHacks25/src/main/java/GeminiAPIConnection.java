import com.fasterxml.jackson.databind.util.JSONPObject;
import dev.langchain4j.agent.tool.*;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestBuilder;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

//import java.lang.classfile.attribute.LineNumberInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import dev.langchain4j.service.AiServices;
import org.json.JSONObject;

public class GeminiAPIConnection implements LLMAPIConnection {

    LLMInterface llmInterface;
    GoogleAiGeminiChatModel gemini;



    GeminiAPIConnection () {
        this.llmInterface = new LLMInterface(this);

        gemini = GoogleAiGeminiChatModel.builder()
            .apiKey(APIKey.GEMINI_APIKey)
            .modelName("gemini-1.5-flash")
            .build();

    }

    public LLMInterface getLLMPort () {
        return  llmInterface;
    }

    public void getCombatResult() {

    }

    public void executeCombatResult(String combatDescription, CombatEntity initiator, ArrayList<CombatEntity> targets, int D20) {

        CombatResultTools.initiator = initiator;
        CombatResultTools.targets = targets;

        String message = String.format("""
                You are a creative D&D Dungeon Master in charge of a game. Describe the result of the following
                round of combat after a %d die roll out of 20. After describing it, update the game to enforce the result, for example,
                by dealing damage to various combatants. If you are missing stats and have no way to access them, just use your judgement.

                %s.
                """, D20, combatDescription);

        List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(CombatResultTools.class);

        System.out.println(UserMessage.from(message));
        System.out.println(toolSpecifications);

        /*ChatRequest chatRequest = ChatRequest.builder()
                    .messages(UserMessage.from(message))
                    .toolSpecifications(toolSpecifications)
                    .build();*/

        GameAssistant gameAssistant = AiServices.builder(GameAssistant.class)
                .chatLanguageModel(gemini)
                .tools(toolSpecifications)
                .build();

        String combatResponse = gameAssistant.chat(message);

        System.out.println(combatResponse);

        /*ChatResponse combatResponse = gemini.chat(chatRequest);


        if (combatResponse.aiMessage().hasToolExecutionRequests()) {
            for (ToolExecutionRequest request : combatResponse.aiMessage().toolExecutionRequests()) {
                if ("inflictDamageToInitiator".equals(request.name())) {

                    JSONObject jsonObject = new JSONObject(request.arguments());

                    double damage = Double.parseDouble(jsonObject.get("arg0").toString());
                    String result = CombatResultTools.inflictDamageToInitiator(damage);
                    System.out.println("Tool Execution Result: " + result);
                } else if ("inflictDamage".equals(request.name())) {

                    JSONObject jsonObject = new JSONObject(request.arguments());

                    double index = Double.parseDouble(jsonObject.get("arg0").toString());
                    double damage = Double.parseDouble(jsonObject.get("arg1").toString());
                    String result = CombatResultTools.inflictDamage(index, damage);
                    System.out.println("Tool Execution Result: " + result);
                }
            }
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(combatResponse.aiMessage().text());
        System.out.println(combatResponse);
        System.out.println(combatResponse.aiMessage());
        System.out.println(combatResponse.finishReason());

        System.out.println(initiator);
        System.out.println(targets);

        if (combatResponse.aiMessage().text() == null) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            String fallbackQuery = """
                   It seems a tool method failed to execute. Tell me how you had already described the combat,
                   without calling any tool methods.
                   """;
            ChatRequest chatFallbackRequest = ChatRequest.builder()
                    .messages(UserMessage.from(message))
                    .toolSpecifications(toolSpecifications)
                    .build();


            ChatResponse combatFallbackResponse = gemini.chat(chatFallbackRequest);
            System.out.println(combatFallbackResponse.aiMessage().text());
            System.out.println(combatFallbackResponse);

        }*/

        System.out.println(initiator);
        System.out.println(targets);

    }

    private static interface GameAssistant {
        String chat(String userMessage);
    }

    private static class CombatResultTools {

        static CombatEntity initiator;
        static ArrayList<CombatEntity> targets;

        @Tool("Get strength stat of initiator")
        static public int getInitiatorStrength () {
            return initiator.getStrength();
        }

        @Tool("Get health stat of i-th target (of targets.get(i))")
        static public int getTargetHealth (int i) {
            return targets.get(i).getHealth();
        }

        @Tool("Get defense stat of i-th target (of targets.get(i))")
        static public int getTargetDefense (int i) {
            return targets.get(i).getDefense();
        }

        @Tool("Cause target at a given index of targets to receive a given amount of damage. Returned string confirms damage dealt.")
        static public String inflictDamage(@P("index") double i, @P("damage") double damage) {
            int index = (int) i;
            int dmg = (int) damage;

            if (index >= targets.size()) {
                System.out.println("Index out of bounds");
                index = targets.size() - 1;
            }

            targets.get(index).receiveDamage(dmg);
            System.out.println("Inflicted " + dmg + " damage");
            return "Inflicted " + dmg + " damage";
        }

        @Tool("Inflict damage on initiator. This means something went particularly wrong, since it was the initiator's turn. Returned string confirms damage dealt.")
        static public String inflictDamageToInitiator(@P("damage") double damage) {
            int dmg = (int) damage;
            initiator.receiveDamage(dmg);
            System.out.println("Inflicted " + dmg + " damage");
            return "Inflicted " + dmg + " damage";
        }

    }

}
