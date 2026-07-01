package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;
import java.util.function.Function;

public abstract class Bidder {

    // --- Core Call Features ---
    public static BidRule shows(Call call, Constraint... constraints) {
        return new BidRule(call, constraints);
    }

    public static CallFeature alert(Call call, String text, Constraint.StaticConstraint... constraints) {
        return new CallAnnotation(call, CallAnnotation.AnnotationType.Alert, text, constraints);
    }

    public static CallFeature announce(Call call, String text, Constraint.StaticConstraint... constraints) {
        return new CallAnnotation(call, CallAnnotation.AnnotationType.Announce, text, constraints);
    }

    public static CallFeature convention(Call call, String text, Constraint.StaticConstraint... constraints) {
        return new CallAnnotation(call, CallAnnotation.AnnotationType.Convention, text, constraints);
    }

    public static CallFeature partnerBids(Function<PositionState, PositionCalls> factory) {
        return new CallProperties(null, factory, false, false, false, null);
    }

    public static CallFeature partnerBids(Call call, Function<PositionState, PositionCalls> factory, Constraint.StaticConstraint... constraints) {
        return new CallProperties(call, factory, false, false, false, null, constraints);
    }

    public static CallFeatureGroup properties(Call call, Function<PositionState, PositionCalls> partnerBids, 
                                            boolean forcing1Round, boolean forcingToGame, 
                                            boolean agreeTrump, Suit trump,
                                            String alert, String announce, String convention,
                                            Constraint.StaticConstraint onlyIf) {
        CallFeatureGroup group = new CallFeatureGroup();
        group.addFeature(new CallProperties(call, partnerBids, forcing1Round, forcingToGame, agreeTrump, trump, onlyIf));
        if (alert != null) group.addFeature(alert(call, alert, onlyIf));
        if (announce != null) group.addFeature(announce(call, announce, onlyIf));
        if (convention != null) group.addFeature(convention(call, convention, onlyIf));
        return group;
    }

    // --- Static Constraints (Sytuacyjne) ---
    public static Constraint.StaticConstraint isSeat(int... seats) {
        return new SimpleStaticConstraint((call, ps) -> {
            for (int s : seats) if (ps.getSeat() == s) return true;
            return false;
        }, (call, ps) -> "seat " + Arrays.toString(seats));
    }

    public static Constraint.StaticConstraint isLastBid(Call call) {
        return new BidHistory(0, call);
    }

    public static Constraint.StaticConstraint isVul() {
        return new SimpleStaticConstraint((call, ps) -> ps.isVulnerable(), "vul");
    }

    public static Constraint.StaticConstraint isNotVul() {
        return new SimpleStaticConstraint((call, ps) -> !ps.isVulnerable(), "not vul");
    }

    public static Constraint.StaticConstraint isJump(int... levels) {
        return new JumpBid(levels);
    }

    public static Constraint.StaticConstraint isNewSuit(Suit suit) {
        return new NewSuit(suit);
    }

    public static Constraint.StaticConstraint cueBid(Suit suit) {
        return new CueBid(suit);
    }

    public static Constraint.StaticConstraint partner(Constraint c) {
        return new PositionProxy(PositionProxy.RelativePosition.Partner, c);
    }

    public static Constraint and(Constraint... constraints) {
        return new ConstraintGroup(constraints);
    }

    // --- Dynamic Constraints (Karta) ---
    public static Constraint.HandConstraint points(int min, int max) {
        return new ShowsPoints(null, min, max, HasPoints.PointType.Starting);
    }

    public static Constraint.HandConstraint highCardPoints(int min, int max) {
        return new ShowsPoints(null, min, max, HasPoints.PointType.HighCard);
    }

    public static Constraint.HandConstraint dummyPoints(int min, int max) {
        return new ShowsPoints(null, min, max, HasPoints.PointType.Dummy);
    }

    public static Constraint.HandConstraint shape(int min, int max) {
        return new ShowsShape(null, min, max);
    }

    public static Constraint.HandConstraint shape(Suit suit, int min, int max) {
        return new ShowsShape(suit, min, max);
    }

    public static Constraint longestMajor(int max) {
        return and(shape(Suit.Hearts, 0, max), shape(Suit.Spades, 0, max));
    }

    public static Constraint.HandConstraint balanced = new ShowsBalanced(true);
    public static Constraint.HandConstraint notBalanced = new ShowsBalanced(false);
    public static Constraint.HandConstraint flat = new ShowsFlat(true);

    public static Constraint.HandConstraint quality(Suit suit, SuitQuality min, SuitQuality max) {
        return new ShowsQuality(suit, min, max);
    }

    public static Constraint.HandConstraint fit(int count, Suit suit) {
        return new PairShowsMinShape(suit, count, true);
    }

    public static Constraint.HandConstraint fit(int count) {
        return fit(count, null);
    }

    public static Constraint.HandConstraint fit() {
        return fit(8, null);
    }

    public static Constraint.HandConstraint pairPoints(int min, int max) {
        return new PairShowsPoints(null, min, max);
    }

    public static Constraint.HandConstraint pairPoints(Range range) {
        return pairPoints(range.min, range.max);
    }
}
