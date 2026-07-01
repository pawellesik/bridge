package com.example.bridge.bridgit.constraints;

import com.example.bridge.bridgit.*;
import java.util.List;

public class ShowsPoints extends HasPoints implements Constraint.IShowsHand, Constraint.IDescribeConstraint, Constraint.IDescribeMultipleConstraints {

    public ShowsPoints(Suit trumpSuit, int min, int max, PointType pointType) {
        super(trumpSuit, min, max, pointType);
    }

    @Override
    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
        switch (pointType) {
            case HighCard:
                showHand.showHighCardPoints(min, max);
                break;
            case Starting:
                showHand.showStartingPoints(min, max);
                break;
            case Dummy:
                Suit suit = Constraint.getSuit(trumpSuit, call);
                if (suit != null) {
                    showHand.suits.get(suit).showDummyPoints(min, max);
                }
                break;
        }
    }

    @Override
    public String describe(Call call, PositionState ps, List<Constraint> constraints) {
        ShowsPoints best = this;
        if (constraints.size() > 1) {
            for (Constraint c : constraints) {
                if (c instanceof ShowsPoints) {
                    ShowsPoints sp = (ShowsPoints) c;
                    if (sp.pointType == PointType.Dummy ||
                        (sp.pointType == PointType.HighCard && best.pointType != PointType.Dummy)) {
                        best = sp;
                    }
                }
            }
        }
        return best.describe(call, ps);
    }

    @Override
    public String describe(Call call, PositionState ps) {
        String rangeStr = Range.getString(min, max, 40);
        switch (pointType) {
            case HighCard: return rangeStr + " HCP";
            case Starting: return rangeStr + " points";
            case Dummy:
                Suit suit = Constraint.getSuit(trumpSuit, call);
                if (suit != null) return rangeStr + " dummy points";
                return rangeStr + " points";
            default: return null;
        }
    }
}
