import java.util.ArrayList;

public class CombatState implements GameState {

    private Game game;

    /** If you edit these, you must recalculate playerControlledEntities! */
    private ArrayList<CombatEntity> side1;
    /** If you edit these, you must recalculate playerControlledEntities! */
    private ArrayList<CombatEntity> side2;

    //private ArrayList<CombatEntity> playerControlledEntities;


    /** It is the caller's responsibility to ensure side1 and side2 don't have strange duplicates */
    public CombatState(Game game, ArrayList<CombatEntity> side1, ArrayList<CombatEntity> side2) {

        this.game = game;
        this.side1 = side1;
        this.side2 = side2;

        /*for (CombatEntity combatEntity : side1) {
            if (combatEntity.isPlayerControlled()) {
                playerControlledEntities.add((combatEntity));
            }
        }
        for (CombatEntity combatEntity : side2) {
            if (combatEntity.isPlayerControlled()) {
                playerControlledEntities.add((combatEntity));
            }
        }*/
    }

    @Override
    public GameStateName getGameStateName() {
        return GameStateName.COMBAT;
    }

    @Override
    public void enterState() {

    }

    @Override
    public void update() {

    }

    @Override
    public void handleInput(GameInput input) {
        
    }

    @Override
    public void exit() {

    }

    public void performAction() {

    }

    public ArrayList<CombatEntity> getSide1() {
        return this.side1;
    }
    public ArrayList<CombatEntity> getSide2() {
        return this.side2;
    }

}
