package wss.items;

import wss.entities.Player;

public class WaterBonus extends Item {

    private int waterAmount;

    public WaterBonus(int waterAmount) {
        this.waterAmount = waterAmount;
        // 50% chance to be a repeating item
        repeating = Math.random() < 0.5;
    }

    public int getWaterAmount() {
        return waterAmount;
    }
    
    @Override
    public void replenish(int amount) {
        this.waterAmount += amount;
        isTaken = false;
    }

    @Override
    public void takeItem(Player player) { 
        if (!isTaken) {
            isTaken = true;
            player.changeWater(waterAmount);
        }
    }
    
}
