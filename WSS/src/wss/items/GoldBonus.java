package wss.items;

import wss.entities.Player;

public class GoldBonus extends Item {

    private int goldAmount;

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
