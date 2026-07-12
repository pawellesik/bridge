package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.IDescribeConstraint;
import com.example.bridge.bidding.Tools.IShowsHand;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;

/**
 * Sprawdza czy ręka posiada "drugi kolor" o określonej długości.
 * Używane przy licytacji dwukolorowej (np. 5-4, 5-5).
 */
public class TwoSuiter extends HandConstraint implements IDescribeConstraint {
    private final Suit primarySuit; // Kolor, który ignorujemy (zazwyczaj ten już uzgodniony lub licytowany)
    private final int minSecondLen; // Minimalna długość tego drugiego koloru (np. 4)

    /**
     * @param primarySuit Kolor do pominięcia.
     * @param minSecondLen Minimalna długość poszukiwanego drugiego koloru.
     */
    public TwoSuiter(Suit primarySuit, int minSecondLen) {
        this.primarySuit = primarySuit;
        this.minSecondLen = minSecondLen;
    }

    /**
     * Przeszukuje wszystkie kolory (oprócz primarySuit) i sprawdza czy któryś ma minSecondLen kart.
     */
    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        // Jeśli nie znamy koloru podstawowego (np. nie uzgodniono atutu), 
        // to szukamy czy w ogóle są dwa kolory o długości minSecondLen.
        int longSuitsFound = 0;
        
        for (Suit s : Suit.values()) {
            if (primarySuit != null && s == primarySuit) continue;
            
            Range shape = hs.getSuits().get(s).getShape();
            if (shape.getMin() >= minSecondLen) {
                if (primarySuit != null) return true; // Znaleźliśmy drugi kolor
                longSuitsFound++;
            }
        }
        
        return (primarySuit == null && longSuitsFound >= 2);
    }

    @Override
    public String describe(Call call, PositionState ps) {
        if (primarySuit != null) {
            return "at least " + minSecondLen + " cards in another suit (not " + primarySuit.toSymbol() + ")";
        }
        return "at least two suits with " + minSecondLen + "+ cards";
    }

    /**
     * Wersja, która informuje system o pokazywanej sile (IShowsHand).
     */
    public static class ShowsTwoSuiter extends TwoSuiter implements IShowsHand {
        public ShowsTwoSuiter(Suit primarySuit, int minSecondLen) {
            super(primarySuit, minSecondLen);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            // Ta metoda jest trudniejsza, bo nie wiemy KTÓRY to kolor bez dodatkowej logiki.
            // Zazwyczaj ShowsTwoSuiter używa się gdy wiadomo o jaki kolor chodzi (np. licytowany).
        }
    }
}




























































































































































































































































































































































































































































































