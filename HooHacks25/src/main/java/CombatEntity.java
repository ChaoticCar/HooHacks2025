import java.util.ArrayList;

public interface CombatEntity {

    public boolean isPlayerControlled();

    public int getHealth();
    public int getMaxHealth();
    public int getStrength();
    public int getDefense();
    public void receiveDamage(int damage);

    public ArrayList<Item> getInventory();

    public String getName();
}
