package com.example.bridge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();
    private boolean isCurrentMove = false;

    public Player(String name) {
        this.name = name;
    }

    public boolean isCurrentMove() {
        return isCurrentMove;
    }

    public void setCurrentMove(boolean currentMove) {
        isCurrentMove = currentMove;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCards(List<Card> newCards) {
        hand.addAll(newCards);
        Collections.sort(hand);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public void clearHand() {
        hand.clear();
    }

}
