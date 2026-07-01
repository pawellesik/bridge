package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;
import java.util.function.Function;

public abstract class Bidder {

    public static BidRule shows(Call call, Constraint... constraints) {
        return new BidRule(call, constraints);
    }

    public static CallAnnotation alert(Call call, String text, Constraint.StaticConstraint... constraints) {
        return new CallAnnotation(call, CallAnnotation.AnnotationType.Alert, text, constraints);
    }

    public static CallFeature partnerBids(Function<PositionState, PositionCalls> factory) {
        return new CallProperties(null, factory, false, false, false, null);
    }

    public static CallFeature partnerBids(Call call, Function<PositionState, PositionCalls> factory, Constraint.StaticConstraint... constraints) {
        return new CallProperties(call, factory, false, false, false, null, constraints);
    }

    public static Constraint.StaticConstraint isSeat(int... seats) {
        Set<Integer> seatSet = new HashSet<>();
        for (int s : seats) seatSet.add(s);
        return new SimpleStaticConstraint(
            (call, ps) -> seatSet.contains(ps.getSeat()),
            (call, ps) -> "seat " + Arrays.toString(seats),
            null
        );
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

    public static Constraint and(Constraint... constraints) {
        return new ConstraintGroup(constraints);
    }

    public static Constraint.HandConstraint points(int min, int max) {
        return new ShowsPoints(null, min, max, HasPoints.PointType.Starting);
    }

    public static Constraint.HandConstraint highCardPoints(int min, int max) {
        return new ShowsPoints(null, min, max, HasPoints.PointType.HighCard);
    }

    public static Constraint.HandConstraint shape(Suit suit, int min, int max) {
        return new ShowsShape(suit, min, max);
    }

    public static Constraint.HandConstraint balanced() {
        return new ShowsBalanced(true);
    }

    public static Constraint.HandConstraint flat() {
        return new ShowsFlat(true);
    }

    public static Constraint.HandConstraint quality(Suit suit, SuitQuality min, SuitQuality max) {
        return new ShowsQuality(suit, min, max);
    }

    public static Constraint.HandConstraint losers(int min, int max, Suit suit) {
        return new ShowsLosers(false, suit, min, max);
    }

    public static Constraint.StaticConstraint hasShownSuit(Suit suit, boolean eitherPartner) {
        return new HasShownSuit(suit, eitherPartner);
    }

    public static Constraint.StaticConstraint isNewSuit(Suit suit) {
        return new NewSuit(suit);
    }

    public static Constraint.StaticConstraint isJump(int... levels) {
        return new JumpBid(levels);
    }

    public static Constraint partner(Constraint c) {
        return new PositionProxy(PositionProxy.RelativePosition.Partner, c);
    }
}
