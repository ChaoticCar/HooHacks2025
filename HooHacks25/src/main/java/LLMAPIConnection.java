import java.util.ArrayList;

public interface LLMAPIConnection {
    public void executeCombatResult(String combatDescription, CombatEntity initiator, ArrayList<CombatEntity> targets, int D20);
    public LLMInterface getLLMPort ();
}
