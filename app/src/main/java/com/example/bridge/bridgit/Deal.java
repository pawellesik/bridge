package com.example.bridge.bridgit;

import java.util.*;

public class Deal extends HashMap<Direction, Hand> {
    private final Game game;

    public Deal(Game game) {
        this.game = game;
        for (Direction d : Direction.values()) {
            this.put(d, null);
        }
    }

    @Override
    public String toString() {
        Direction dealer = game.getDealer();
        StringBuilder sb = new StringBuilder(dealer.name() + ":");
        Direction current = dealer;
        while (true) {
            Hand hand = this.get(current);
            sb.append(hand == null ? "-" : hand.toString());
            current = current.leftHandOpponent();
            if (current == dealer) {
                break;
            }
            sb.append(" ");
        }
        return sb.toString();
    }
}
