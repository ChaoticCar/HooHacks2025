import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;
import dev.langchain4j.service.AiServices;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class SituationGen {

    private static Hostile currentMonster;
    private static int turnCounter = 0;

    private static ArrayList<String> playerActions = new ArrayList<>();

    public static void main(String[] args) {
        //String scenario = run(0, null);    // Start the battle
        //System.out.println("\nFinal Scenario:\n" + scenario);
        // Generate the image based on the scenario and save it in the image folder
    }

    // sorry for this
    public static void initialize() {
        playerActions.add("Attack");
        playerActions.add("");
        playerActions.add("");
    }

    public static ArrayList<String> getPlayerActions() {
        return playerActions;
    }

    public static int getTurnCount() {
        return turnCounter;
    }

    private static GoogleAiGeminiChatModel gemini = GoogleAiGeminiChatModel.builder()
            .apiKey(APIKey.GEMINI_APIKey)
            .modelName("gemini-1.5-flash")
            .build();;

    private static CombatResultTools combatResultTools;
    private static SituationAssistant gameAssistant;

    public static String run(int turn, Player player, Hostile monster, String playerAction) {
        currentMonster = monster;

        combatResultTools = new CombatResultTools(player, monster);

        gameAssistant = AiServices.builder(SituationAssistant.class)
                .chatLanguageModel(gemini)
                .tools(combatResultTools)
                .build();

        if (turn == 0) {
            currentMonster = monster != null ? monster : getMonster();
        }

        int d20Roll = rollD20();
        System.out.println("Simulated d20 Roll: " + d20Roll);

        String prompt = createScenarioPrompt(d20Roll, turn, currentMonster, playerAction);
        //prompt = .";

        System.out.println("Sending Prompt to Gemini:\n\"" + prompt + "\"\n---");

        CountDownLatch latch = new CountDownLatch(1);
        StringBuilder scenario = new StringBuilder();

        turnCounter++;
        return gameAssistant.chat(prompt/*, new StreamingChatResponseHandler() {
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
        }*/);

        /*try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }*/

        //turnCounter++;
        //return scenario.toString();
    }

    private static int rollD20() {
        return ThreadLocalRandom.current().nextInt(1, 21);
    }

    private static String createScenarioPrompt(int rollResult, int turn, Hostile monster, String playerAction) {
        if (turn % 2 == 0) {
            return String.format(
                    "You are a creative Dungeons & Dragons Dungeon Master. " +
                            "It's my turn in combat. I wield a sword, chose \"%s\" as my actions, and rolled a %d on a D20. " +
                            "The higher the roll, the more likely my attack succeeds. " +
                            "Describe my action against the %s, including its effectiveness, impact on the opponent, " +
                            "damage done, and any environmental details that enhance the scene. " +
                            "Provide three options for my next move, set those options to the player's actions, " +
                            "and always call tool methods to update the game state. This is turn %d.", playerAction, rollResult, monster.getName(), turn
            );
        } else {
            return String.format(
                    "You are a creative Dungeons & Dragons Dungeon Master. " +
                            "It's my opponent's turn in combat. They are a fearsome %s and rolled a %d on a D20. " +
                            "I chose \"%s\" as my action. The higher the roll, the more likely their attack succeeds. " +
                            "Describe their attack, including its effectiveness, impact on me, " +
                            "damage done, and any environmental details that enhance the scene. " +
                            "Provide three options for my next move, and call tool methods to update the game state" +
                            ". This is turn %d.", monster.getName(), rollResult, playerAction, turn
            );
        }
    }

    private static Hostile getMonster() {
        return currentMonster;
    }

    private static interface SituationAssistant {
        String chat(String userMessage);
    }

    private static class CombatResultTools {

        CombatEntity player;
        CombatEntity monster;
        //ArrayList<CombatEntity> monsters;

        CombatResultTools(CombatEntity player, CombatEntity monster) {
            this.player = player;
            this.monster = monster;
            //this.monsters = monsters;
        }

        @Tool("Get health stat of player")
        public int getPlayerHealth () {
            System.out.println("checked player health");
            return player.getHealth();
        }

        @Tool("Get strength stat of player")
        public int getPlayerStrength () {
            System.out.println("checked player strength");
            return player.getStrength();
        }

        @Tool("Get defense stat of player")
        public int getPlayerDefense () {
            System.out.println("checked player defense");
            return player.getDefense();
        }

        @Tool("Get health stat of monster")
        public int getMonsterHealth () {
            System.out.println("checked monster health");
            return monster.getHealth();
        }

        @Tool("Get strength stat of monster")
        public int getMonsterStrength () {
            System.out.println("checked monster strength");
            return monster.getStrength();
        }

        @Tool("Get defense stat of monster")
        public int getMonsterDefense () {
            System.out.println("checked monster defense");
            return monster.getDefense();
        }

        @Tool("Set player's next actions as you described them")
        public void setPlayerAction(@P("Action 1") String action1, @P("Action 2") String action2, @P("Action 3") String action3) {
            playerActions.set(0, action1);
            playerActions.set(1, action2);
            playerActions.set(2, action3);
        }

        @Tool("Detect if player is dead (return true if dead)")
        public boolean isPlayerDead() {
            System.out.println("checked player life");
            return player.getHealth() < 0;
        }

        @Tool("Detect if monster is dead (return true if dead)")
        public boolean isMonsterDead() {
            System.out.println("checked monster life");
            return monster.getHealth() < 0;
        }

        /*
        For if we have multiple monsters
        @Tool("Get health stat of i-th monster (of monsters.get(i))")
        public int getMonsterHealth (int i) {
            return monsters.get(i).getHealth();
        }

        @Tool("Get defense stat of i-th monster (of monsters.get(i))")
        public int getMonsterStrength (int i) {
            return monsters.get(i).getStrength();
        }

        @Tool("Get defense stat of i-th monster (of monsters.get(i))")
        public int getMonsterDefense (int i) {
            return monsters.get(i).getDefense();
        }*/

        @Tool("Inflict a given amount of damage to monster")
        public void inflictDamageToMonster(@P("damage") int damage) {
            //int index = (int) i;
            //int dmg = (int) damage;


            monster.receiveDamage(damage);
            System.out.println("Inflicted " + damage + " damage on " + monster.getName());
            //return "Inflicted " + dmg + " damage";
        }

        /*@Tool("Cause target at a given index of monsters to receive a given amount of damage")
        public void inflictDamageToMonster(@P("index") int i, @P("damage") int damage) {
            //int index = (int) i;
            //int dmg = (int) damage;

            if (index >= monsters.size()) {
                System.out.println("Index out of bounds");
                index = monsters.size() - 1;
            }

            monsters.get(index).receiveDamage(dmg);
            System.out.println("Inflicted " + dmg + " damage");
            //return "Inflicted " + dmg + " damage";
        }*/

        @Tool("Inflict damage on player")
        public void inflictDamageToPlayer(@P("damage") int damage) {
            //int dmg = (int) damage;
            player.receiveDamage(damage);
            System.out.println("Inflicted " + damage + " damage on " + player.getName());
            //return "Inflicted " + damage + " damage";
        }

    }
}