package wss.items;

public class GoldBonus extends Item {

    private int goldAmount;

    @Override
    public void replenish(int amount) {
        this.goldAmount += amount;
    }

    public int takeGold(int amount) { 
        if (this.goldAmount >= amount) {
            return this.goldAmount -= amount;
        } else {
            return -1; // Not enough gold to take
        }
    }
    
}
