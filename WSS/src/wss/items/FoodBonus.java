package wss.items;

import wss.entities.Player;

public class FoodBonus extends Item {

    private int foodAmount;

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
