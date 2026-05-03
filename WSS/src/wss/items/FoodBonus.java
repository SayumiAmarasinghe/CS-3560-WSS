package wss.items;

public class FoodBonus extends Item {

    private int foodAmount;

    @Override
    public void replenish(int amount) {
        this.foodAmount += amount;
    }

    public int takeFood(int amount) { 
        if (this.foodAmount >= amount) {
            return this.foodAmount -= amount;
        } else {
            return -1; // Not enough food to take
        }
    }
    
}
