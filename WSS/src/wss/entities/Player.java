package wss.entities;

public class Player {
    private int maxStrength;
    private int maxWater;
    private int maxFood;
    private int curStrength;
    private int curWater;
    private int curFood;
    private int gold;
    private int movementPoints;
    private Vision pVision;
    private Brain pBrain;

    public Player(int mStrength, int mWater, int mFood, int startingMovementPoints, Vision chosenVision, Brain chosenBrain){
        if (mStrength <= 0 || mWater <= 0 || mFood <= 0 || startingMovementPoints <= 0) {
            throw new IllegalArgumentException("Max or starting values cannot be 0 or less");
        }
        maxStrength = mStrength;
        maxWater = mWater;
        maxFood = mFood;
        curStrength = maxStrength;
        curWater = maxWater;
        curFood = maxFood;
        gold = 0;
        movementPoints = startingMovementPoints;
        pVision = chosenVision;
        pBrain = chosenBrain;
    }
    // Setting, adding/subtracting to current values -----------------------------------------------
    private int bounding(boolean set, int currentValue, int value, int maxResource, String resourceName) {
        if (set) {
            currentValue = value;
        }
        else {
            currentValue += value;
        }
        if (currentValue > maxResource){
            System.out.println("Maximum exceeded: wasted " + (currentValue - maxResource) + " " + resourceName);
            currentValue = maxResource;
        }
        return currentValue;
    }
    
    public void addOrSubToCurrentFood(int change) {
        curFood = bounding(false, curFood, change, maxFood, "food");
    }
    public void setCurrentFood(int newValue) {
        curFood = bounding(true, curFood, newValue, maxFood, "food");
    }

    public void addOrSubToCurrentWater(int change) {
        curWater = bounding(false, curWater, change, maxWater, "water");
    }
    public void setCurrentWater(int newValue) {
        curWater = bounding(true, curWater, newValue, maxWater, "water");
    }

    public void addOrSubToCurrentStrength(int change) {
        curStrength = bounding(false, curStrength, change, maxStrength, "strength");
    }
    public void setCurrentStrength(int newValue) {
        curStrength = bounding(true, curStrength, newValue, maxStrength, "strength");
    }

    public void addOrSubToCurrentMovementPoints(int change) {
        movementPoints += change;
    }
    public void setCurrentMovementPoints(int newValue) {
        movementPoints = newValue;
    }

    public void addOrSubToCurrentGold(int change) {
        gold += change;
    }
    public void setCurrentGold(int newValue) {
        gold = newValue;
    }
    // ---------------------------------------------------------------------------------------------------
}
