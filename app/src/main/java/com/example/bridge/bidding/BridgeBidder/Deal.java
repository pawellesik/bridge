package com.example.bridge.bidding.BridgeBidder;

import java.util.EnumMap;

public class Deal extends EnumMap<Direction, Hand> {
    private final Game game;

    public Deal(Game game) {
        super(Direction.class);
        this.game = game;
        for (Direction direction : Direction.values()) {
            this.put(direction, null);
        }
    }

    public Game getGame() {
        return game;
    }

    public void setHand(Direction direction, Hand hand) {
        this.put(direction, hand);
    }

    @Override
    public String toString() {
        Direction dealer = game.dealer;
        StringBuilder sb = new StringBuilder(dealer.toString()).append(":");
        Direction direction = dealer;
        while (true) {
            Hand hand = this.get(direction);
            sb.append(hand == null ? "-" : hand.toString());
            direction = direction.leftHandOpponent();
            if (direction == dealer) {
                return sb.toString();
            }
            sb.append(" ");
        }
    }
}
