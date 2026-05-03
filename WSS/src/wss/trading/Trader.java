package wss.trading;

public abstract class Trader {
    public static final String ACCEPT = "ACCEPT";
    public static final String REJECT = "REJECT";
    public static final String COUNTER = "COUNTER";

    private final String name;
    private final int patienceLevel;
    private int counterCount;

    public Trader(String name, int patienceLevel) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Trader name cannot be empty");
        }
        if (patienceLevel < 0) {
            throw new IllegalArgumentException("Patience level cannot be negative");
        }

        this.name = name;
        this.patienceLevel = patienceLevel;
        this.counterCount = 0;
    }

    public String proposeTrade(TradeOffer offer) {
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

    public String getName() {
        return name;
    }

    public int getPatienceLevel() {
        return patienceLevel;
    }

    public int getCounterCount() {
        return counterCount;
    }

    @Override
    public String toString() {
        return name + " with patience level " + patienceLevel;
    }
}
