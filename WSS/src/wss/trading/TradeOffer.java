package wss.trading;

/*
- represents one proposed trade between the player and a trader
- offered are what the player gives
- requested are what the player wants from them
*/
public class TradeOffer {
    private static final int GOLD_VALUE = 3;
    private static final int FOOD_VALUE = 2;
    private static final int WATER_VALUE = 2;

    private final int offeredGold;
    private final int offeredFood;
    private final int offeredWater;
    private final int requestedGold;
    private final int requestedFood;
    private final int requestedWater;

    public TradeOffer(int offeredGold, int offeredFood, int offeredWater,
                        int requestedGold, int requestedFood, int requestedWater) {
        validateResourceAmount(offeredGold, "offeredGold");
        validateResourceAmount(offeredFood, "offeredFood");
        validateResourceAmount(offeredWater, "offeredWater");
        validateResourceAmount(requestedGold, "requestedGold");
        validateResourceAmount(requestedFood, "requestedFood");
        validateResourceAmount(requestedWater, "requestedWater");

        this.offeredGold = offeredGold;
        this.offeredFood = offeredFood;
        this.offeredWater = offeredWater;
        this.requestedGold = requestedGold;
        this.requestedFood = requestedFood;
        this.requestedWater = requestedWater;
    }

    private void validateResourceAmount(int amount, String resourceName) {
        if (amount < 0) {
            throw new IllegalArgumentException(resourceName + " cannot be negative");
        }
    }

    public int getOfferedGold() {
        return offeredGold;
    }

    public int getOfferedFood() {
        return offeredFood;
    }

    public int getOfferedWater() {
        return offeredWater;
    }

    public int getRequestedGold() {
        return requestedGold;
    }

    public int getRequestedFood() {
        return requestedFood;
    }

    public int getRequestedWater() {
        return requestedWater;
    }

    public int getOfferedValue() {
        return calculateValue(offeredGold, offeredFood, offeredWater);
    }

    public int getRequestedValue() {
        return calculateValue(requestedGold, requestedFood, requestedWater);
    }

    public int getValueDifference() {
        return getOfferedValue() - getRequestedValue();
    }

    private int calculateValue(int gold, int food, int water) {
        return (gold * GOLD_VALUE) + (food * FOOD_VALUE) + (water * WATER_VALUE);
    }

    @Override
    public String toString() {
        return "Player offers " + offeredGold + " gold, " + offeredFood + " food, " + offeredWater + " water for "
                + requestedGold + " gold, " + requestedFood + " food, " + requestedWater + " water";
    }
}
