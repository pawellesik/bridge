package com.example.bridge.model;

import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();
    private boolean isCurrentMove = false;
    private final FrameLayout playedCardContainer;

    public Player(String name, FrameLayout playedCardContainer) {
        this.name = name;
        this.playedCardContainer = playedCardContainer;
    }

    public FrameLayout getPlayedCardContainer() {
        return playedCardContainer;
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

    public int calculateHCP() {
        int total = 0;
        for (Card card : hand) {
            if (card.getRank() != null) {
                total += card.getRank().hcp;
            }
        }
        return total;
    }

    public int countSuit(Suit suit) {
        int count = 0;
        for (Card card : hand) {
            if (card.getSuit() == suit) count++;
        }
        return count;
    }

    public boolean haveHoldInColor(String color) {
        return false;
    }


}
