package wss.ai.brain;

import wss.ai.Path;
import wss.entities.Player;
import wss.vision.Vision;

public class Brain {
    
    private static Player player;
    private static Vision vision;

    private Path suggestedPath;

    // Constructor, initializes the brain with the player it belongs to
    public Brain(Player player){
        Brain.player = player;
        Brain.vision = player.getVision();
    }

    // Calls upon the brain to decide on a move to make based on the player's available resources, also updates internal suggestion
    public Boolean suggestPath(){
        int food = player.getCurrentFood();
        int water = player.getCurrentWater();
        int gold = player.getGold();

        if( food > 35 && water > 35){
            if (food >= 50 && water >= 50) {
                suggestedPath = GreedyMovement();
            } else {
                suggestedPath = DefaultMovement();
            }
        } else if ( gold > 50){
            suggestedPath = BargainingMovement();
        } else if (food < 35 && water < 35){
            suggestedPath = FiendingMovement();
        } else if (food < 35){
            suggestedPath = HungryMovement();
        } else if (water < 35){
            suggestedPath = ThirstyMovement();
        }

        return suggestedPath != null;

    }

    public int getSuggestedRow(){
        return suggestedPath.getTargetRow();
    }

    public int getSuggestedCol(){
        return suggestedPath.getTargetCol();
    }

    // Checks if the given path can be taken with the player's current resources
    private static Boolean compareResources(Path path){
        try {
            return path.getTotalFoodCost() <= player.getCurrentFood() && 
                path.getTotalWaterCost() <= player.getCurrentWater() &&
                path.getTotalMovementCost() <= player.getMovementPoints();
        } catch (NullPointerException e) {
            return true;
        }
    }

    // Updates the player's resources based on the costs of the given path
    private static void changePlayerResources(Path path) { 
        player.changeFood(path.getTotalFoodCost());
        player.changeWater(path.getTotalWaterCost());
        player.changeMovementPoints(path.getTotalMovementCost());
    }

    // Executes the suggested move by updating the player's resources and position on the map
    private void makeMove(){
        if(suggestedPath == null){
            System.out.println("No valid path suggestion found, skipping!");
            return;
        }
        changePlayerResources(suggestedPath);
        player.setMapPosition(suggestedPath.getTargetRow(), suggestedPath.getTargetCol());
    }

    // Default movement takes the easiest path to the closest visible square
    private static Path DefaultMovement(){
            Path path = vision.easiestPath(player.getMap(), player.getXMapPos(), player.getYMapPos());

            if(!compareResources(path)){
                System.out.println("No valid moves available, skipping turn!");
                return null;
            }

            return path;

        }

    // Priotizies movement towards gold
    private static Path GreedyMovement(){
        Path path = vision.closestGold(player.getMap(), player.getXMapPos(), player.getYMapPos());

        if (!compareResources(path)) {
            path = vision.secondClosestGold(player.getMap(), player.getXMapPos(), player.getYMapPos());
            if (!compareResources(path)) {
                return DefaultMovement();
            }
        }

        return path;
    }  

    // Prioritizes movement towards traders
    private static Path BargainingMovement(){

        Path path = vision.closestTrader(player.getMap(), player.getXMapPos(), player.getYMapPos());

        if (!compareResources(path)) {
            path = vision.secondClosestTrader(player.getMap(), player.getXMapPos(), player.getYMapPos());
            if (!compareResources(path)) {
                return DefaultMovement();
                
            }
        }

        return path;

    }

    // Prioritizes movement towards food
    private static Path HungryMovement(){

        Path path = vision.closestFood(player.getMap(), player.getXMapPos(), player.getYMapPos());

        if (!compareResources(path)) {
            path = vision.secondClosestFood(player.getMap(), player.getXMapPos(), player.getYMapPos());
            if (!compareResources(path)) {
                return DefaultMovement();
                
            }
        }

        return path;
    }

    // Similar to HungryMovement, but prioritizes water instead of food
    private static Path ThirstyMovement(){
        Path path = vision.closestWater(player.getMap(), player.getXMapPos(), player.getYMapPos());

        if (!compareResources(path)) {
            path = vision.secondClosestWater(player.getMap(), player.getXMapPos(), player.getYMapPos());
            if (!compareResources(path)) {
                return DefaultMovement();
                
            }
        }

        return path;
    }

    // Looks for the closest resources of food or water, prioritizng movement to the resource with the lowest total cost
    private static Path FiendingMovement(){

        try {
            Path foodPath = vision.closestFood(player.getMap(), player.getXMapPos(), player.getYMapPos());
            Path waterPath = vision.closestWater(player.getMap(), player.getXMapPos(), player.getYMapPos());

            Path betterPath = foodPath.getTotalCost() <= waterPath.getTotalCost() ? foodPath : waterPath;
            if (!compareResources(betterPath)) {

                foodPath = vision.closestFood(player.getMap(), player.getXMapPos(), player.getYMapPos());
                waterPath = vision.closestWater(player.getMap(), player.getXMapPos(), player.getYMapPos());

                betterPath = foodPath.getTotalCost() <= waterPath.getTotalCost() ? foodPath : waterPath;

                if (!compareResources(betterPath)) {
                    return DefaultMovement();
                    
                }
            }

            return betterPath;
            
        } catch (NullPointerException e) {
            return DefaultMovement();
        }

       
    }


    
}
