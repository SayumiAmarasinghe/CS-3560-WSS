package wss.items;

public class WaterBonus extends Item {

    private int waterAmount;

    @Override
    public void replenish(int amount) {
        this.waterAmount += amount;
    }

    public int takeWater(int amount) { 
        if (this.waterAmount >= amount) {
            return this.waterAmount -= amount;
        } else {
            return -1; // Not enough water to take
        }
    }
    
}
