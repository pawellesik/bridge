package com.example.bridge.ui.game;

import com.example.bridge.DdsSolver;
import com.example.bridge.model.Card;
import com.example.bridge.model.Contract;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;

import java.util.List;
import java.util.Map;

public class BiddingManager {
    private final Map<String, Player> players;
    private final GameController.GameCallback callback;
    private final DdsSolver ddsSolver;

    public BiddingManager(Map<String, Player> players, GameController.GameCallback callback, DdsSolver ddsSolver) {
        this.players = players;
        this.callback = callback;
        this.ddsSolver = ddsSolver;
    }

    public Contract determineBestContract() {
        int[] ddsCards = new int[16];
        String[] handNames = {"North", "East", "South", "West"};
        
        for (int h = 0; h < 4; h++) {
            List<Card> hand = players.get(handNames[h]).getHand();
            for (Card c : hand) {
                int suitIdx = mapSuitToDdsIndex(c.getSuit());
                ddsCards[h * 4 + suitIdx] |= (1 << (c.getRank().ordinal() + 2));
            }
        }

        int[] fullTable = ddsSolver.calcFullDDTable(ddsCards);
        
        int bestLevel = 0;
        Suit bestSuit = null;
        int maxTricks = 0;

        // Trumps: 0=NT, 1=S, 2=H, 3=D, 4=C
        // Directions: 0=N, 1=E, 2=S, 3=W
        // index = trump * 4 + direction
        
        // Check only for South (us) or North (partner)
        for (int trumpIdx = 0; trumpIdx < 5; trumpIdx++) {
            int tricksNS = Math.max(fullTable[trumpIdx * 4 + 0], fullTable[trumpIdx * 4 + 2]);
            if (tricksNS > maxTricks) {
                maxTricks = tricksNS;
                bestLevel = tricksNS - 6;
                bestSuit = mapDdsIndexToSuit(trumpIdx);
            }
        }

        if (bestLevel <= 0) return new Contract(true); // PASS
        return new Contract(Math.min(bestLevel, 7), bestSuit);
    }

    private int mapSuitToDdsIndex(Suit suit) {
        switch (suit) {
            case SPADES: return 0;
            case HEARTS: return 1;
            case DIAMONDS: return 2;
            case CLUBS: return 3;
            default: return 0;
        }
    }

    private Suit mapDdsIndexToSuit(int idx) {
        switch (idx) {
            case 0: return null; // NT
            case 1: return Suit.SPADES;
            case 2: return Suit.HEARTS;
            case 3: return Suit.DIAMONDS;
            case 4: return Suit.CLUBS;
            default: return null;
        }
    }
}
