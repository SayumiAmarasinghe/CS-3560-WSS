package wss.trading;

/*
- stricter trader type that expects trades to be profitable
- less likely to accept than the normal trader and asks for more when making a counteroffer
*/
public class GreedyTrader extends Trader {
    private static final int REQUIRED_PROFIT = 3;
    private static final int COUNTER_THRESHOLD = -2;
    private static final double BASE_ACCEPT_CHANCE = 0.45;
    private static final double MAX_ACCEPT_CHANCE = 0.90;
    private static final double COUNTER_CHANCE = 0.80;

    public GreedyTrader(String name, int patienceLevel) {
        this(0, name, 10, 10, 10, patienceLevel);
    }

    public GreedyTrader(int traderID, String name, int gold, int food, int water, int patienceLevel) {
        super(traderID, "Greedy", name, gold, food, water, patienceLevel);
    }

    @Override
    public String evaluateOffer(TradeOffer offer) {
        int valueDifference = offer.getValueDifference();

        if (valueDifference >= REQUIRED_PROFIT && Math.random() < getAcceptChance(valueDifference)) {
            return ACCEPT;
        }
        if (valueDifference >= COUNTER_THRESHOLD && canCounter() && Math.random() < COUNTER_CHANCE) {
            return COUNTER;
        }
        return REJECT;
    }

    private double getAcceptChance(int valueDifference) {
        return Math.min(MAX_ACCEPT_CHANCE, BASE_ACCEPT_CHANCE + (valueDifference * 0.05));
    }

    @Override
    public TradeOffer counterOffer(TradeOffer offer) {
        if (!canCounter()) {
            return offer;
        }

        useCounterAttempt();
        int extraGoldRequested = Math.random() < 0.50 ? 2 : 3;

        return new TradeOffer(
                offer.getOfferedGold() + extraGoldRequested,
                offer.getOfferedFood(),
                offer.getOfferedWater(),
                offer.getRequestedGold(),
                offer.getRequestedFood(),
                offer.getRequestedWater());
    }
}
