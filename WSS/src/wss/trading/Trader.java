package wss.trading;

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

    public abstract TradeOffer counterOffer(TradeOffer offer);

    public String acceptTrade(TradeOffer offer) {
        return name + " accepted the trade: " + offer;
    }

    public String rejectTrade(TradeOffer offer) {
        return name + " rejected the trade: " + offer;
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
