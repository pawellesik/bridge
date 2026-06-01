package com.example.bridge.model;

import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public void resortHand(Suit trumpSuit) {
        // Define circular order: Spades -> Hearts -> Clubs -> Diamonds -> Spades...
        final List<Suit> fullCycle = new ArrayList<>();
        fullCycle.add(Suit.SPADES);
        fullCycle.add(Suit.HEARTS);
        fullCycle.add(Suit.CLUBS);
        fullCycle.add(Suit.DIAMONDS);

        final List<Suit> customOrder = new ArrayList<>();
        if (trumpSuit != null) {
            int startIndex = fullCycle.indexOf(trumpSuit);
            for (int i = 0; i < 4; i++) {
                customOrder.add(fullCycle.get((startIndex + i) % 4));
            }
        } else {
            // Default S -> H -> C -> D
            customOrder.addAll(fullCycle);
        }

        Collections.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                Suit s1 = c1.getSuit();
                Suit s2 = c2.getSuit();

                if (s1 == s2) {
                    return c2.getRank().ordinal() - c1.getRank().ordinal();
                }

                int p1 = customOrder.indexOf(s1);
                int p2 = customOrder.indexOf(s2);
                
                return p1 - p2;
            }
        });
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

    public int countAcesAndKings() {
        int count = 0;
        for (Card card : hand) {
            if (card.getRank() == Rank.ACE || card.getRank() == Rank.KING) {
                count++;
            }
        }
        return count;
    }

    public int countSuit(Suit suit) {
        int count = 0;
        for (Card card : hand) {
            if (card.getSuit() == suit) count++;
        }
        return count;
    }

    public boolean hasHold(Suit suit) {
        boolean hasAce = false;
        boolean hasKing = false;
        boolean hasQueen = false;
        boolean hasJack = false;
        int count = 0;

        for (Card card : hand) {
            if (card.getSuit() == suit) {
                count++;
                switch (card.getRank()) {
                    case ACE: hasAce = true; break;
                    case KING: hasKing = true; break;
                    case QUEEN: hasQueen = true; break;
                    case JACK: hasJack = true; break;
                }
            }
        }

        // Standard Bridge "hold" (stopper) rules:
        // A, Kx, Qxx, Jxxx
        if (hasAce) return true;
        if (hasKing && count >= 2) return true;
        if (hasQueen && count >= 3) return true;
        if (hasJack && count >= 4) return true;
        
        return false;
    }
}
