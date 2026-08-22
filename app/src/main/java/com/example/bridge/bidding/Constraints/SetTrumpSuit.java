package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.IDescribeConstraint;
import com.example.bridge.bidding.Tools.IShowsHand;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

/**
 * Akcja ustalająca kolor atutowy w stanie pary.
 * Może być używana wewnątrz metody shows().
 */
public class SetTrumpSuit extends HandConstraint implements IShowsHand, IDescribeConstraint {
    private final Suit suit;

    public SetTrumpSuit(Suit suit) {
        this.suit = suit;
    }

    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        return true; // Akcja jest zawsze możliwa do wykonania
    }

    @Override
    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
        if (suit != null) {
            ps.getPairState().setTrumpSuit(suit);
        } else if (call instanceof Bid) {
            ps.getPairState().setTrumpSuit(((Bid) call).getSuit());
        }
    }

    @Override
    public String describe(Call call, PositionState ps) {
        Suit s = suit;
        if (s == null && call instanceof Bid) {
            s = ((Bid) call).getSuit();
        }
        return s != null ? "agreed trump " + s.toSymbol() : "agreed trump";
    }
}
