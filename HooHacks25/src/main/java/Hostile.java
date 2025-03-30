import java.util.ArrayList;

public class Hostile implements Entity, CombatEntity {

    String name;
    int health;
    int maxHealth;
    int strength;
    int defense;

    int posX;
    int posY;

    ArrayList<Item> inventory;

    public Hostile(int health, String name, int maxHealth, int strength, int defense, int posX, int posY) {
        this.health = health;
        this.name = name;
        this.maxHealth = maxHealth;
        this.strength = strength;
        this.defense = defense;

        this.posX = posX;
        this.posY = posY;

        this.inventory = new ArrayList<>();
    }

    @Override
    public boolean isPlayerControlled() {
        return false;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public int getDefense() {
        return defense;
    }

    @Override
    public void receiveDamage(int damage) {
        this.health -= damage;
    }

    @Override
    public ArrayList<Item> getInventory() {
        return inventory;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("""
            Hostile: {
                health: %d/%d
            }
            """, health, maxHealth);
    }
}
