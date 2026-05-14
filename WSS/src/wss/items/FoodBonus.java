package wss.items;

import wss.entities.Player;

public class FoodBonus extends Item {

    private int foodAmount;

    public FoodBonus(int foodAmount) {
        this.foodAmount = foodAmount;
        // 50% chance to be a repeating item
        repeating = Math.random() < 0.5;
    }

    public int getFoodAmount() {
        return foodAmount;
    }

    @Override
    public void replenish(int amount) {
        this.foodAmount += amount;
        isTaken = false;
    }

    @Override
    public void takeItem(Player player) { 
        if (!isTaken) {
            isTaken = true;
            player.changeFood(foodAmount);
        }
    }
    
}
