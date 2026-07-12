package com.example.bridge.bidding.BridgeBidder.Constraints;

import com.example.bridge.bidding.BridgeBidder.Tools.Call;
import com.example.bridge.bidding.BridgeBidder.Tools.HandConstraint;
import com.example.bridge.bidding.BridgeBidder.Tools.HandSummary;
import com.example.bridge.bidding.BridgeBidder.Tools.IDescribeConstraint;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionState;
import com.example.bridge.bidding.BridgeBidder.Tools.Suit;

import java.util.*;

/**
 * Constraint sprawdzający łączną liczbę Key Cards (asy + król atutowy) posiadanych przez parę.
 * Kluczowe przy ocenie szans na szlemika lub szlema w oparciu o wiedzę o obu rękach.
 */
public class PairKeyCards extends HandConstraint implements IDescribeConstraint {
    private final int[] count;      // Dopuszczalne sumy Key Cards w parze
    private final Suit trumpSuit;   // Kolor atutu
    private final Boolean hasQueen; // Informacja o damie atutowej (czy para ją posiada)

    public PairKeyCards(Suit trumpSuit, Boolean hasQueen, int... count) {
        this.trumpSuit = trumpSuit;
        this.hasQueen = hasQueen;
        this.count = count;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Set<Integer> ourKeyCards = hs.getCountAces();
        Set<Integer> partnerKeyCards = ps.getPartner().getPublicHandSummary().getCountAces();
        Boolean partnerHasQueen = null;
        Boolean weHaveQueen = null;

        if (trumpSuit != null) {
            ourKeyCards = hs.getSuits().get(trumpSuit).getKeyCards();
            weHaveQueen = hs.getSuits().get(trumpSuit).getHaveQueen();
            partnerKeyCards = ps.getPartner().getPublicHandSummary().getSuits().get(trumpSuit).getKeyCards();
            partnerHasQueen = ps.getPartner().getPublicHandSummary().getSuits().get(trumpSuit).getHaveQueen();
        }

        if (ourKeyCards == null) {
            if (partnerKeyCards == null) return true;
            return Arrays.stream(count).max().orElse(0) >= partnerKeyCards.stream().min(Integer::compare).orElse(0);
        }
        if (partnerKeyCards == null) {
            return Arrays.stream(count).max().orElse(0) >= ourKeyCards.stream().min(Integer::compare).orElse(0);
        }

        if (hasQueen != null) {
            if (!hasQueen && (Boolean.TRUE.equals(weHaveQueen) || Boolean.TRUE.equals(partnerHasQueen))) return false;
            if (hasQueen && (Boolean.FALSE.equals(weHaveQueen) && Boolean.FALSE.equals(partnerHasQueen))) return false;
        }

        for (int ourCount : ourKeyCards) {
            for (int pCount : partnerKeyCards) {
                for (int c : count) {
                    if (c == ourCount + pCount) return true;
                }
            }
        }
        return false;
    }

    @Override
    public String describe(Call call, PositionState ps) {
        String s = (count.length == 1 && count[0] == 1) ? "" : "s";
        String countStr = Arrays.toString(count).replace("[", "").replace("]", "").replace(",", " or");
        if (trumpSuit != null) {
            String str = countStr + " key card" + s;
            if (hasQueen != null) {
                str += hasQueen ? " and queen" : " no queen";
            }
            return str;
        } else {
            return countStr + " Ace" + s;
        }
    }
}




























































































































































































































































































































































































































































































