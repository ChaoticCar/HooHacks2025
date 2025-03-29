import java.util.ArrayList;

public interface CombatEntity {

    public boolean isPlayerControlled();

    public int getHealth();
    public int getStrength();
    public int getDefense();
    public ArrayList<Item> getInventory();

    public String getName();
}
