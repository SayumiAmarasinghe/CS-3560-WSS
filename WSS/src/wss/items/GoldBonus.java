package wss.items;

import wss.entities.Player;

public class GoldBonus extends Item {

    private int goldAmount;

    public GoldBonus(int goldAmount) {
        this.goldAmount = goldAmount;
        // 50% chance to be a repeating item
        repeating = Math.random() < 0.5;
    }

    public int getGoldAmount() {
        return goldAmount;
    }
    
    @Override
    public void replenish(int amount) {
        this.goldAmount += amount;
        isTaken = false;
    }

    @Override
    public void takeItem(Player player) { 
        if (!isTaken) {
            isTaken = true;
            player.changeGold(goldAmount);
        }
    }
    
}
