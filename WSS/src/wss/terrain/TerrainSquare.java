package wss.terrain;

import java.util.ArrayList;
import java.util.List;

import wss.entities.Player;

//one square of terrain on the map, with its own terrain type and costs for food, water, and movement
public class TerrainSquare {
    private int foodCost;
    private int waterCost;
    private int movementCost;
    private TerrainType terrainType;
    private List<Object> items;

    //constructor for terrain square
    public TerrainSquare(TerrainType type, int food, int water, int movement) {
        this.terrainType = type;
        this.foodCost = food;
        this.waterCost = water;
        this.movementCost = movement;
        this.items = new ArrayList<>();
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
        return player.getFood() >= foodCost && 
        player.getWater() >= waterCost && 
        player.getMovement() >= movementCost;
    }

    //deduct costs from player's reserves when they enter a square 
    public void enterSQuare(Player player) {
        //if the player can enter, deduct costs 
        if (canEnter(player)) {
            player.deductFood(foodCost);
            player.deductWater(waterCost);
            player.deductMovement(movementCost);
        }
    }

    //if player stays in same terrain square 
    //player gains 2 movement units, but players still need waater nad food at 1/2 normal rate 
    public void stayInSquare(Player player) {
        player.deductFood(foodCost / 2);
        player.deductWater(waterCost / 2);
        player.addMovement(2); //gain 2 movement units
    }
}
