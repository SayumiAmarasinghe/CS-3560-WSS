package wss.items;

public class Item {

    // Can the item be replenished?
    protected Boolean repeating;
    
    // Virtual method to be overriden by item subclasses
    public void replenish(int amount){}

}
