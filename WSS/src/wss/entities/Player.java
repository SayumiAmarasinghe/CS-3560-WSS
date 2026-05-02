package wss.entities;
import wss.ai.brain.Brain;
import wss.vision.Vision;

public class Player {
    private int maxStrength;
    private int maxWater;
    private int maxFood;

    private int curStrength;
    private int curWater;
    private int curFood;

    private int gold;
    private int movementPoints;

    private Vision vision;
    private Brain brain;

    public Player(int maxStrength, int maxWater, int maxFood,
                  int startingMovementPoints, Vision chosenVision, Brain chosenBrain) {

        if (maxStrength <= 0 || maxWater <= 0 || maxFood <= 0 || startingMovementPoints <= 0) {
            throw new IllegalArgumentException("Max or starting values cannot be 0 or less");
        }
        if (chosenVision == null || chosenBrain == null) {
            throw new IllegalArgumentException("Vision and Brain cannot be null");
        }

        this.maxStrength = maxStrength;
        this.maxWater = maxWater;
        this.maxFood = maxFood;

        this.curStrength = maxStrength;
        this.curWater = maxWater;
        this.curFood = maxFood;

        this.gold = 0;
        this.movementPoints = startingMovementPoints;

        this.vision = chosenVision;
        this.brain = chosenBrain;
    }

    // -------------------- Helper Methods --------------------

    private int bound(int value, int maxResource, String resourceName) {
        if (value > maxResource) {
            System.out.println("Maximum exceeded: wasted " + (value - maxResource) + " " + resourceName);
            return maxResource;
        }
        if (value < 0) {
            return 0;
        }
        return value;
    }

    private int changeResource(int currentValue, int change, int maxResource, String resourceName) {
        return bound(currentValue + change, maxResource, resourceName);
    }

    private int setResource(int newValue, int maxResource, String resourceName) {
        return bound(newValue, maxResource, resourceName);
    }

    // -------------------- Food --------------------

    public void changeFood(int change) {
        curFood = changeResource(curFood, change, maxFood, "food");
    }

    public void setFood(int newValue) {
        curFood = setResource(newValue, maxFood, "food");
    }

    public int getCurrentFood() {
        return curFood;
    }

    public int getMaxFood() {
        return maxFood;
    }

    // -------------------- Water --------------------

    public void changeWater(int change) {
        curWater = changeResource(curWater, change, maxWater, "water");
    }

    public void setWater(int newValue) {
        curWater = setResource(newValue, maxWater, "water");
    }

    public int getCurrentWater() {
        return curWater;
    }

    public int getMaxWater() {
        return maxWater;
    }

    // -------------------- Strength --------------------

    public void changeStrength(int change) {
        curStrength = changeResource(curStrength, change, maxStrength, "strength");
    }

    public void setStrength(int newValue) {
        curStrength = setResource(newValue, maxStrength, "strength");
    }

    public int getCurrentStrength() {
        return curStrength;
    }

    public int getMaxStrength() {
        return maxStrength;
    }

    // -------------------- Movement Points --------------------

    public void changeMovementPoints(int change) {
        movementPoints += change;
    }

    public void setMovementPoints(int newValue) {
        movementPoints = newValue;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    // -------------------- Gold --------------------

    public void changeGold(int change) {
        gold += change;
    }

    public void setGold(int newValue) {
        gold = newValue;
    }

    public int getGold() {
        return gold;
    }

    // -------------------- Vision & Brain --------------------

    public Vision getVision() {
        return vision;
    }

    public Brain getBrain() {
        return brain;
    }
}