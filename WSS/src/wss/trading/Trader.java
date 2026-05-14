package wss.trading;

import wss.entities.Player;

/*
- shared parent class for all trader types in the game
- stores common trader data and defines the negotiation methods
*/
public abstract class Trader {
    public static final String ACCEPT = "ACCEPT";
    public static final String REJECT = "REJECT";
    public static final String COUNTER = "COUNTER";

    private final int traderID;
    private final String traderType;
    private final String name;
    private int gold;
    private int food;
    private int water;
    private final int patienceLevel;
    private int counterCount;
    private TradeOffer currentOffer;

    public Trader(int traderID, String traderType, String name, int gold, int food, int water, int patienceLevel) {
        if (traderID < 0) {
            throw new IllegalArgumentException("Trader ID cannot be negative");
        }
        if (traderType == null || traderType.isEmpty()) {
            throw new IllegalArgumentException("Trader type cannot be empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Trader name cannot be empty");
        }
        validateResourceAmount(gold, "gold");
        validateResourceAmount(food, "food");
        validateResourceAmount(water, "water");
        if (patienceLevel < 0) {
            throw new IllegalArgumentException("Patience level cannot be negative");
        }

        this.traderID = traderID;
        this.traderType = traderType;
        this.name = name;
        this.gold = gold;
        this.food = food;
        this.water = water;
        this.patienceLevel = patienceLevel;
        this.counterCount = 0;
        this.currentOffer = null;
    }

    private void validateResourceAmount(int amount, String resourceName) {
        if (amount < 0) {
            throw new IllegalArgumentException(resourceName + " cannot be negative");
        }
    }

    public String proposeTrade(TradeOffer offer) {
        currentOffer = offer;
        String decision = evaluateOffer(offer);

        if (decision.equals(COUNTER) && !canCounter()) {
            return REJECT;
        }

        return decision;
    }

    public boolean canCounter() {
        return counterCount < patienceLevel;
    }

    protected void useCounterAttempt() {
        counterCount++;
    }

    public abstract String evaluateOffer(TradeOffer offer);

    public abstract TradeOffer counterOffer(TradeOffer offer, Player player);

    protected TradeOffer addRandomAffordableDemand(TradeOffer offer, Player player, int minExtra, int maxExtra) {
        int availableGold = player.getGold() - offer.getOfferedGold();
        int availableFood = player.getCurrentFood() - offer.getOfferedFood();
        int availableWater = player.getCurrentWater() - offer.getOfferedWater();

        int optionCount = 0;
        if (availableGold > 0) {
            optionCount++;
        }
        if (availableFood > 0) {
            optionCount++;
        }
        if (availableWater > 0) {
            optionCount++;
        }

        if (optionCount == 0) {
            return offer;
        }

        int selectedOption = (int) (Math.random() * optionCount);

        if (availableGold > 0) {
            if (selectedOption == 0) {
                int extraGold = getRandomExtraAmount(availableGold, minExtra, maxExtra);
                return new TradeOffer(
                        offer.getOfferedGold() + extraGold,
                        offer.getOfferedFood(),
                        offer.getOfferedWater(),
                        offer.getRequestedGold(),
                        offer.getRequestedFood(),
                        offer.getRequestedWater());
            }
            selectedOption--;
        }

        if (availableFood > 0) {
            if (selectedOption == 0) {
                int extraFood = getRandomExtraAmount(availableFood, minExtra, maxExtra);
                return new TradeOffer(
                        offer.getOfferedGold(),
                        offer.getOfferedFood() + extraFood,
                        offer.getOfferedWater(),
                        offer.getRequestedGold(),
                        offer.getRequestedFood(),
                        offer.getRequestedWater());
            }
            selectedOption--;
        }

        int extraWater = getRandomExtraAmount(availableWater, minExtra, maxExtra);
        return new TradeOffer(
                offer.getOfferedGold(),
                offer.getOfferedFood(),
                offer.getOfferedWater() + extraWater,
                offer.getRequestedGold(),
                offer.getRequestedFood(),
                offer.getRequestedWater());
    }

    private int getRandomExtraAmount(int availableAmount, int minExtra, int maxExtra) {
        int highestExtra = Math.min(availableAmount, maxExtra);
        int lowestExtra = Math.min(minExtra, highestExtra);
        return lowestExtra + (int) (Math.random() * (highestExtra - lowestExtra + 1));
    }

    public String acceptTrade(TradeOffer offer) {
        return name + " accepted the trade: " + offer;
    }

    public String rejectTrade(TradeOffer offer) {
        return name + " rejected the trade: " + offer;
    }

    public boolean canCompleteTrade(Player player, TradeOffer offer) {
        return player.getGold() >= offer.getOfferedGold()
                && player.getCurrentFood() >= offer.getOfferedFood()
                && player.getCurrentWater() >= offer.getOfferedWater()
                && gold >= offer.getRequestedGold()
                && food >= offer.getRequestedFood()
                && water >= offer.getRequestedWater();
    }

    public boolean completeTrade(Player player, TradeOffer offer) {
        if (!canCompleteTrade(player, offer)) {
            return false;
        }

        player.changeGold(-offer.getOfferedGold());
        player.changeFood(-offer.getOfferedFood());
        player.changeWater(-offer.getOfferedWater());

        player.changeGold(offer.getRequestedGold());
        player.changeFood(offer.getRequestedFood());
        player.changeWater(offer.getRequestedWater());

        gold = gold + offer.getOfferedGold() - offer.getRequestedGold();
        food = food + offer.getOfferedFood() - offer.getRequestedFood();
        water = water + offer.getOfferedWater() - offer.getRequestedWater();

        return true;
    }

    public int getTraderID() {
        return traderID;
    }

    public String getTraderType() {
        return traderType;
    }

    public String getName() {
        return name;
    }

    public int getGold() {
        return gold;
    }

    public int getFood() {
        return food;
    }

    public int getWater() {
        return water;
    }

    public int getPatienceLevel() {
        return patienceLevel;
    }

    public int getCounterCount() {
        return counterCount;
    }

    public TradeOffer getCurrentOffer() {
        return currentOffer;
    }

    @Override
    public String toString() {
        return traderType + " trader " + name + " with patience level " + patienceLevel;
    }
}
