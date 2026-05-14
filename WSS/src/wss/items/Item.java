package wss.items;

import wss.entities.Player;

public abstract class Item {

    protected Boolean isTaken = false;
    // Can the item be replenished?
    protected Boolean repeating = true;
    
    // Abstract methods to be implemented in item subclasses
    public abstract void replenish(int amount);
    public abstract void takeItem(Player player);

}
