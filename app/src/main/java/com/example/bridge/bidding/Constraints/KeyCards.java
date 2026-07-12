package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.IDescribeConstraint;
import com.example.bridge.bidding.Tools.IShowsHand;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.*;

/**
 * Klasa obsługująca tzw. "Key Cards" (asy plus król atutowy).
 * Kluczowa dla konwencji Blackwood (RKCB 1430/0314).
 */
public class KeyCards extends HandConstraint implements IShowsHand, IDescribeConstraint {
    private final Set<Integer> count;
    private final Suit trumpSuit; // Kolor atutowy, w którym Król liczony jest jako as
    private final Boolean haveQueen; // Opcjonalna informacja o posiadaniu Damy atutowej

    /**
     * @param trumpSuit Kolor atutu.
     * @param haveQueen Czy gracz posiada damę atutową (true/false/null jeśli nieistotne).
     * @param count Lista dopuszczalnych liczb Key Cards.
     */
    public KeyCards(Suit trumpSuit, Boolean haveQueen, int... count) {
        this.trumpSuit = trumpSuit;
        this.haveQueen = haveQueen;
        this.count = new HashSet<>();
        for (int c : count) {
            this.count.add(c);
        }
    }

    /**
     * Weryfikuje czy liczba Key Cards w ręce zgadza się z licytacją.
     */
    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Set<Integer> keyCards = hs.getCountAces();
        if (trumpSuit != null) {
            if (haveQueen != null) {
                Boolean q = hs.getSuits().get(trumpSuit).getHaveQueen();
                if (q != null && !q.equals(haveQueen)) return false;
            }
            keyCards = hs.getSuits().get(trumpSuit).getKeyCards();
        }
        if (keyCards == null) return true;
        for (int c : count) {
            if (keyCards.contains(c)) return true;
        }
        return false;
    }

    /**
     * Pokazuje partnerowi liczbę Key Cards oraz informację o Damie atutowej.
     */
    @Override
    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
        if (trumpSuit != null) {
            showHand.getSuits().get(trumpSuit).showKeyCards(count);
            if (haveQueen != null) {
                showHand.getSuits().get(trumpSuit).showHaveQueen(haveQueen);
            }
        }
    }

    /**
     * Zwraca opis, np. "[1, 4] key card s and queen".
     */
    @Override
    public String describe(Call call, PositionState ps) {
        String s = (count.size() == 1 && count.contains(1)) ? "" : "s";
        if (trumpSuit != null) {
            String str = count.toString() + " key card" + s;
            if (haveQueen != null) {
                str += haveQueen ? " and queen" : " no queen";
            }
            return str;
        } else {
            return count.toString() + " Ace" + s;
        }
    }
}




























































































































































































































































































































































































































































































