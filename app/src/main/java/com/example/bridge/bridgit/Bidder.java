package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public abstract class Bidder {

    public static BidRule shows(Call call, Constraint... constraints) {
        return new BidRule(call, constraints);
    }

    public static CallAnnotation alert(Call call, String text, Constraint.StaticConstraint... constraints) {
        return new CallAnnotation(call, CallAnnotation.AnnotationType.Alert, text, constraints);
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
}
