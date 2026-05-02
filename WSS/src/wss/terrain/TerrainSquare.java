package wss.terrain;

import java.util.List;
import wss.entities.Player;
import wss.items.Item;
import wss.trading.Trader;

//one square of terrain on the map, with its own terrain type and costs for food, water, and movement
public class TerrainSquare {
    private int foodCost;
    private int waterCost;
    private int movementCost;
    private TerrainType terrainType;
    private List<Item> items;
    private Trader trader;

    //constructor for terrain square
    public TerrainSquare(TerrainType type, List<Item> items, Trader trader) {
        this.terrainType = type;
        // set up costs based on terrain type
        if (type == TerrainType.PLAINS) {
            this.foodCost = 1;
            this.waterCost = 1;
            this.movementCost = 1;
        } 
        else if (type == TerrainType.FOREST) {
            this.foodCost = 2;
            this.waterCost = 2;
            this.movementCost = 2;
        } 
        else if (type == TerrainType.MOUNTAIN) {
            this.foodCost = 3;
            this.waterCost = 3;
            this.movementCost = 4;
        } 
        else if (type == TerrainType.SWAMP) {
            this.foodCost = 2;
            this.waterCost = 3;
            this.movementCost = 3;
        } 
        else if (type == TerrainType.DESERT) {
            this.foodCost = 4;
            this.waterCost = 5;
            this.movementCost = 4;
        } 
        else if (type == TerrainType.TUNDRA) {
            this.foodCost = 3;
            this.waterCost = 4;
            this.movementCost = 3;
        } 
        else {
            throw new IllegalArgumentException("Invalid terrain type!");
        }
        this.items = items;
        this.trader = trader;
    }

    //getters for food, water, movement, terrain type
    public int getFoodCost() {
        return foodCost;
    }
    public int getWaterCost() {
        return waterCost;
    }
    public int getMovementCost() {
        return movementCost;
    }
    public TerrainType getTerrainType() {
        return terrainType;
    }

    //boolean class to see if player has enough reserves to enter this square 
    //get player stats from player class 
    //TO DO: wait for implementation of player class 
    public boolean canEnter (Player player) {
        return player.getCurrentFood() >= foodCost && 
        player.getCurrentWater() >= waterCost && 
        player.getMovementPoints() >= movementCost;
    }

    //deduct costs from player's reserves when they enter a square 
    public void enterSquare(Player player) {
        //if the player can enter, deduct costs 
        if (canEnter(player)) {
            player.changeFood(-foodCost);
            player.changeWater(-waterCost);
            player.changeMovementPoints(-movementCost);
        }
    }

    //if player stays in same terrain square 
    //player gains 2 movement units, but players still need waater and food at 1/2 normal rate 
    public void stayInSquare(Player player) {
        player.changeFood(-foodCost / 2);
        player.changeWater(-waterCost / 2);
        player.changeMovementPoints(2);
    }

    // print the terrain square
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("------------------\n");
        // terrain types and costs
        sb.append("|Terrain: ").append(terrainType.name()).append("|\n");
        sb.append("|Food Cost: ").append(foodCost).append("|\n");
        sb.append("|Water Cost: ").append(waterCost).append("|\n");
        sb.append("|Movement Cost: ").append(movementCost).append("|\n\n");
        // items in the square if any
        if (items != null && !items.isEmpty())
        {
            for (Item item : items) {
                sb.append("|Item: ").append(item.toString()).append("|\n");
            }
        }
        // trader in the square if any
        if (trader != null) {
            sb.append("|Trader: ").append(trader.toString()).append("|\n");
        }
        sb.append("------------------");
        return sb.toString();
    }
}
