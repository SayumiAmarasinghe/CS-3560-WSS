package wss.trading;

import wss.entities.Player;

/*
- fair trader type that is usually willing to accept balanced trades
- might counter offers that are close, but rejects offers that are too low or when patience runs out
*/
public class NormalTrader extends Trader {
    private static final int COUNTER_THRESHOLD = -4;
    private static final double BASE_ACCEPT_CHANCE = 0.85;
    private static final double MAX_ACCEPT_CHANCE = 0.98;
    private static final double COUNTER_CHANCE = 0.70;

    public NormalTrader(String name, int patienceLevel) {
        this(0, name, 25, 50, 50, patienceLevel);
    }

    public NormalTrader(int traderID, String name, int gold, int food, int water, int patienceLevel) {
        super(traderID, "Normal", name, gold, food, water, patienceLevel);
    }

    @Override
    public String evaluateOffer(TradeOffer offer) {
        int valueDifference = offer.getValueDifference();

        if (valueDifference >= 0 && Math.random() < getAcceptChance(valueDifference)) {
            return ACCEPT;
        }
        if (valueDifference >= COUNTER_THRESHOLD && canCounter() && Math.random() < COUNTER_CHANCE) {
            return COUNTER;
        }
        return REJECT;
    }

    private double getAcceptChance(int valueDifference) {
        return Math.min(MAX_ACCEPT_CHANCE, BASE_ACCEPT_CHANCE + (valueDifference * 0.03));
    }

    @Override
    public TradeOffer counterOffer(TradeOffer offer, Player player) {
        if (!canCounter()) {
            return offer;
        }

        useCounterAttempt();
        return addRandomAffordableDemand(offer, player, 1, 2);
    }
}
