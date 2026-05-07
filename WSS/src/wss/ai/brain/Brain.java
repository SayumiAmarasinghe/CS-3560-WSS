package wss.ai.brain;

import wss.ai.Path;
import wss.entities.Player;
import wss.vision.Vision;

public class Brain {
    
    private static Player player;
    private static Vision vision;

    // Constructor, initializes the brain with the player it belongs to
    public Brain(Player player){
        Brain.player = player;
        Brain.vision = player.getVision();
    }

    // Calls upon the brain to decide on a move to make based on the player's available resources 
    public static void MakeMove(){
        int food = player.getCurrentFood();
        int water = player.getCurrentWater();
        int gold = player.getGold();

        if( food > 35 && water > 35){
            if (food >= 50 && water >= 50) {
                GreedyMovement();
            } else {
                DefaultMovement();
            }
        } else if ( gold > 50){
            BargainingMovement();
        } else if (food < 35 && water < 35){
            FiendingMovement();
        } else if (food < 35){
            HungryMovement();
        } else if (water < 35){
            ThirstyMovement();
        }

    }

    // Checks if the given path can be taken with the player's current resources
    private static Boolean compareResources(Path path){
        return path.getTotalFoodCost() <= player.getCurrentFood() && 
               path.getTotalWaterCost() <= player.getCurrentWater() &&
               path.getTotalMovementCost() <= player.getMovementPoints();
    }

    // Updates the player's resources based on the costs of the given path
    private static void changePlayerResources(Path path) { 
        player.changeFood(path.getTotalFoodCost());
        player.changeWater(path.getTotalWaterCost());
        player.changeMovementPoints(path.getTotalMovementCost());
    }

    // Default movement takes the easiest path to the closest visible square
    private static void DefaultMovement(){
            Path path = vision.easiestPath(player.getMap(), player.getXMapPos(), player.getYMapPos());

            if(!compareResources(path)){
                System.out.println("No valid moves available, skipping turn!");
                return;
            }

            changePlayerResources(path);
            player.setMapPosition(path.getTargetRow(), path.getTargetCol());

        }

    // Priotizies movement towards gold
    private static void GreedyMovement(){
        Path path = vision.closestGold(player.getMap(), player.getXMapPos(), player.getYMapPos());

        if (!compareResources(path)) {
            path = vision.secondClosestGold(player.getMap(), player.getXMapPos(), player.getYMapPos());
            if (!compareResources(path)) {
                DefaultMovement();
                return;
            }
        }

        changePlayerResources(path);
        player.setMapPosition(path.getTargetRow(), path.getTargetCol());
    }  

    // Prioritizes movement towards traders
    private static void BargainingMovement(){

        Path path = vision.closestTrader(player.getMap(), player.getXMapPos(), player.getYMapPos());

        if (!compareResources(path)) {
            path = vision.secondClosestTrader(player.getMap(), player.getXMapPos(), player.getYMapPos());
            if (!compareResources(path)) {
                DefaultMovement();
                return;
            }
        }

        changePlayerResources(path);
        player.setMapPosition(path.getTargetRow(), path.getTargetCol());

    }

    // Prioritizes movement towards food
    private static void HungryMovement(){

        Path path = vision.closestFood(player.getMap(), player.getXMapPos(), player.getYMapPos());

        if (!compareResources(path)) {
            path = vision.secondClosestFood(player.getMap(), player.getXMapPos(), player.getYMapPos());
            if (!compareResources(path)) {
                DefaultMovement();
                return;
            }
        }

        changePlayerResources(path);
        player.setMapPosition(path.getTargetRow(), path.getTargetCol());
    }

    // Similar to HungryMovement, but prioritizes water instead of food
    private static void ThirstyMovement(){
        Path path = vision.closestWater(player.getMap(), player.getXMapPos(), player.getYMapPos());

        if (!compareResources(path)) {
            path = vision.secondClosestWater(player.getMap(), player.getXMapPos(), player.getYMapPos());
            if (!compareResources(path)) {
                DefaultMovement();
                return;
            }
        }

        changePlayerResources(path);
        player.setMapPosition(path.getTargetRow(), path.getTargetCol());

    }

    // Looks for the closest resources of food or water, prioritizng movement to the resource with the lowest total cost
    private static void FiendingMovement(){

        Path foodPath = vision.closestFood(player.getMap(), player.getXMapPos(), player.getYMapPos());
        Path waterPath = vision.closestWater(player.getMap(), player.getXMapPos(), player.getYMapPos());

        Path betterPath = foodPath.getTotalCost() <= waterPath.getTotalCost() ? foodPath : waterPath;
        if (!compareResources(betterPath)) {

            foodPath = vision.closestFood(player.getMap(), player.getXMapPos(), player.getYMapPos());
            waterPath = vision.closestWater(player.getMap(), player.getXMapPos(), player.getYMapPos());

            betterPath = foodPath.getTotalCost() <= waterPath.getTotalCost() ? foodPath : waterPath;

            if (!compareResources(betterPath)) {
                DefaultMovement();
                return;
            }
        }

        changePlayerResources(betterPath);
        player.setMapPosition(betterPath.getTargetRow(), betterPath.getTargetCol());
    }

    
}
