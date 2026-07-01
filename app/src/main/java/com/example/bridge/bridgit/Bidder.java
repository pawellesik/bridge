package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;
import java.util.function.Function;

public abstract class Bidder {

    public interface CallFeaturesFactory extends Function<PositionState, Iterable<CallFeature>> {}
    public interface PositionCallsFactory extends Function<PositionState, PositionCalls> {}

    // --- Core Call Features ---
    public static BidRule shows(Call call, Constraint... constraints) {
        return new BidRule(call, constraints);
    }

    public static BidRule Shows(Call call, Constraint... constraints) {
        return shows(call, constraints);
    }

    public static CallFeature alert(Call call, String text, Constraint.StaticConstraint... constraints) {
        return new CallAnnotation(call, CallAnnotation.AnnotationType.Alert, text, constraints);
    }

    public static CallFeature Alert(Call call, String text, Constraint.StaticConstraint... constraints) {
        return alert(call, text, constraints);
    }

    public static CallFeature announce(Call call, String text, Constraint.StaticConstraint... constraints) {
        return new CallAnnotation(call, CallAnnotation.AnnotationType.Announce, text, constraints);
    }

    public static CallFeature Announce(Call call, String text, Constraint.StaticConstraint... constraints) {
        return announce(call, text, constraints);
    }

    public static CallFeature convention(Call call, String text, Constraint.StaticConstraint... constraints) {
        return new CallAnnotation(call, CallAnnotation.AnnotationType.Convention, text, constraints);
    }

    public static CallFeature Convention(Call call, String text, Constraint.StaticConstraint... constraints) {
        return convention(call, text, constraints);
    }

    public static CallAnnotation convention(String text, Constraint.StaticConstraint... constraints) {
        return new CallAnnotation(null, CallAnnotation.AnnotationType.Convention, text, constraints);
    }

    public static CallAnnotation Convention(String text, Constraint.StaticConstraint... constraints) {
        return convention(text, constraints);
    }

    public static CallFeature partnerBids(PositionCallsFactory factory) {
        return new CallProperties(null, factory, false, false, false, null);
    }

    public static CallFeature PartnerBids(PositionCallsFactory factory) {
        return partnerBids(factory);
    }

    public static CallFeature partnerBids(CallFeaturesFactory factory) {
        return partnerBids(PositionCalls.fromCallFeaturesFactory(factory));
    }

    public static CallFeature PartnerBids(CallFeaturesFactory factory) {
        return partnerBids(factory);
    }

    public static CallFeature partnerBids(Call call, PositionCallsFactory factory, Constraint.StaticConstraint... constraints) {
        return new CallProperties(call, factory, false, false, false, null, constraints);
    }

    public static CallFeature PartnerBids(Call call, PositionCallsFactory factory, Constraint.StaticConstraint... constraints) {
        return partnerBids(call, factory, constraints);
    }

    public static CallFeature partnerBids(Call call, CallFeaturesFactory factory, Constraint.StaticConstraint... constraints) {
        return partnerBids(call, PositionCalls.fromCallFeaturesFactory(factory), constraints);
    }

    public static CallFeature PartnerBids(Call call, CallFeaturesFactory factory, Constraint.StaticConstraint... constraints) {
        return partnerBids(call, factory, constraints);
    }

    public static CallFeatureGroup properties(Call call, PositionCallsFactory partnerBids, 
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

    public static CallFeatureGroup Properties(Call call, PositionCallsFactory partnerBids, 
                                            boolean forcing1Round, boolean forcingToGame, 
                                            boolean agreeTrump, Suit trump,
                                            String alert, String announce, String convention,
                                            Constraint.StaticConstraint onlyIf) {
        return properties(call, partnerBids, forcing1Round, forcingToGame, agreeTrump, trump, alert, announce, convention, onlyIf);
    }

    public static CallFeatureGroup properties(Call[] calls, PositionCallsFactory partnerBids, 
                                            boolean forcing1Round, boolean forcingToGame, 
                                            boolean agreeTrump, Suit trump,
                                            String alert, String announce, String convention,
                                            Constraint.StaticConstraint onlyIf) {
        CallFeatureGroup group = new CallFeatureGroup();
        for (Call call : calls) {
            group.addFeature(new CallProperties(call, partnerBids, forcing1Round, forcingToGame, agreeTrump, trump, onlyIf));
            if (alert != null) group.addFeature(alert(call, alert, onlyIf));
            if (announce != null) group.addFeature(announce(call, announce, onlyIf));
            if (convention != null) group.addFeature(convention(call, convention, onlyIf));
        }
        return group;
    }

    public static CallFeatureGroup Properties(Call[] calls, PositionCallsFactory partnerBids, 
                                            boolean forcing1Round, boolean forcingToGame, 
                                            boolean agreeTrump, Suit trump,
                                            String alert, String announce, String convention,
                                            Constraint.StaticConstraint onlyIf) {
        return properties(calls, partnerBids, forcing1Round, forcingToGame, agreeTrump, trump, alert, announce, convention, onlyIf);
    }

    public static CallFeatureGroup properties(Call call, PositionCallsFactory partnerBids, 
                                            boolean forcing1Round, boolean forcingToGame, 
                                            boolean agreeTrump, Suit trump,
                                            Constraint.StaticConstraint onlyIf) {
        return properties(call, partnerBids, forcing1Round, forcingToGame, agreeTrump, trump, null, null, null, onlyIf);
    }

    public static CallFeatureGroup Properties(Call call, PositionCallsFactory partnerBids, 
                                            boolean forcing1Round, boolean forcingToGame, 
                                            boolean agreeTrump, Suit trump,
                                            Constraint.StaticConstraint onlyIf) {
        return properties(call, partnerBids, forcing1Round, forcingToGame, agreeTrump, trump, onlyIf);
    }

    public static CallFeatureGroup properties(Call[] calls, PositionCallsFactory partnerBids, 
                                            boolean forcing1Round, boolean forcingToGame, 
                                            boolean agreeTrump, Suit trump,
                                            Constraint.StaticConstraint onlyIf) {
        return properties(calls, partnerBids, forcing1Round, forcingToGame, agreeTrump, trump, null, null, null, onlyIf);
    }

    public static CallFeatureGroup Properties(Call[] calls, PositionCallsFactory partnerBids, 
                                            boolean forcing1Round, boolean forcingToGame, 
                                            boolean agreeTrump, Suit trump,
                                            Constraint.StaticConstraint onlyIf) {
        return properties(calls, partnerBids, forcing1Round, forcingToGame, agreeTrump, trump, onlyIf);
    }

    // --- Static Constraints (Sytuacyjne) ---
    public static Constraint.StaticConstraint isSeat(int... seats) {
        return new SimpleStaticConstraint((call, ps) -> {
            for (int s : seats) if (ps.getSeat() == s) return true;
            return false;
        }, (call, ps) -> {
            StringBuilder sb = new StringBuilder("seat ");
            for (int j = 0; j < seats.length; j++) {
                sb.append(seats[j]);
                if (j < seats.length - 1) sb.append(",");
            }
            return sb.toString();
        });
    }

    public static Constraint.StaticConstraint IsSeat(int... seats) {
        return isSeat(seats);
    }

    public static Constraint.StaticConstraint isLastBid(Call call) {
        return new BidHistory(0, call);
    }

    public static Constraint.StaticConstraint IsLastBid(Call call) {
        return isLastBid(call);
    }

    public static Constraint.StaticConstraint isLastBid(int level, Suit suit) {
        return new BidHistory(0, new Call.Bid(level, suit));
    }

    public static Constraint.StaticConstraint IsLastBid(int level, Suit suit) {
        return isLastBid(level, suit);
    }

    public static Constraint.StaticConstraint isVul() {
        return new SimpleStaticConstraint((call, ps) -> ps.isVulnerable(), "vul");
    }

    public static Constraint.StaticConstraint IsVul = isVul();

    public static Constraint.StaticConstraint isNotVul() {
        return new SimpleStaticConstraint((call, ps) -> !ps.isVulnerable(), "not vul");
    }

    public static Constraint.StaticConstraint IsNotVul = isNotVul();

    public static Constraint.StaticConstraint isJump(int... levels) {
        return new JumpBid(levels);
    }

    public static Constraint.StaticConstraint IsJump(int... levels) {
        return isJump(levels);
    }

    public static final Constraint.StaticConstraint isNonJump = isJump(0);
    public static final Constraint.StaticConstraint IsNonJump = isNonJump;
    public static final Constraint.StaticConstraint isSingleJump = isJump(1);
    public static final Constraint.StaticConstraint IsSingleJump = isSingleJump;

    public static Constraint.StaticConstraint isNewSuit(Suit suit) {
        return new NewSuit(suit);
    }

    public static Constraint.StaticConstraint IsNewSuit(Suit suit) {
        return isNewSuit(suit);
    }

    public static final Constraint.StaticConstraint isCueBid = new CueBid(null);
    public static final Constraint.StaticConstraint IsCueBid = isCueBid;
    public static final Constraint.StaticConstraint isNotCueBid = not(isCueBid);
    public static final Constraint.StaticConstraint IsNotCueBid = isNotCueBid;

    public static final Constraint.StaticConstraint isRebid = new BidHistory(0, null);
    public static final Constraint.StaticConstraint IsRebid = isRebid;
    public static final Constraint.StaticConstraint isNotRebid = not(isRebid);
    public static final Constraint.StaticConstraint IsNotRebid = isNotRebid;

    public static final Constraint.StaticConstraint isNewSuit = andStatic(isNotCueBid, new NewSuit(null));
    public static final Constraint.StaticConstraint IsNewSuit = isNewSuit;

    public static Constraint.StaticConstraint not(Constraint.StaticConstraint c) {
        return new SimpleStaticConstraint(
            (call, ps) -> !c.conforms(call, ps),
            (call, ps) -> {
                if (c instanceof Constraint.IDescribeConstraint) {
                    String desc = ((Constraint.IDescribeConstraint) c).describe(call, ps);
                    return (desc == null || desc.isEmpty()) ? null : "not " + desc;
                }
                return null;
            }
        );
    }

    public static Constraint.StaticConstraint Not(Constraint.StaticConstraint c) {
        return not(c);
    }

    public static Constraint.StaticConstraint andStatic(Constraint.StaticConstraint... constraints) {
        return new SimpleStaticConstraint((call, ps) -> {
            for (Constraint.StaticConstraint c : constraints) {
                if (!c.conforms(call, ps)) return false;
            }
            return true;
        }, (call, ps) -> {
            List<String> descs = new ArrayList<>();
            for (Constraint.StaticConstraint c : constraints) {
                if (c instanceof Constraint.IDescribeConstraint) {
                    String d = ((Constraint.IDescribeConstraint) c).describe(call, ps);
                    if (d != null) descs.add(d);
                }
            }
            return descs.isEmpty() ? null : String.join(" and ", descs);
        });
    }

    public static Constraint.StaticConstraint And(Constraint.StaticConstraint... constraints) {
        return andStatic(constraints);
    }

    public static Constraint.StaticConstraint cueBid(Suit suit) {
        return new CueBid(suit);
    }

    public static Constraint.StaticConstraint partner(Constraint c) {
        return new PositionProxy(PositionProxy.RelativePosition.Partner, c);
    }

    public static Constraint.StaticConstraint Partner(Constraint c) {
        return partner(c);
    }

    public static Constraint.StaticConstraint rho(Constraint c) {
        return new PositionProxy(PositionProxy.RelativePosition.RHO, c);
    }

    public static Constraint.StaticConstraint RHO(Constraint c) {
        return rho(c);
    }

    public static Constraint and(Constraint... constraints) {
        return new ConstraintGroup(constraints);
    }

    public static Constraint And(Constraint... constraints) {
        return and(constraints);
    }

    public static Constraint.StaticConstraint isBidAvailable(int level, Suit suit) {
        return new SimpleStaticConstraint((call, ps) -> ps.isValidNextCall(new Call.Bid(level, suit)), null);
    }

    public static Constraint.StaticConstraint IsBidAvailable(int level, Suit suit) {
        return isBidAvailable(level, suit);
    }

    public static final Constraint.StaticConstraint isFinalCall = new SimpleStaticConstraint((call, ps) -> ps.getBiddingState().getContract().isAuctionComplete(), "pass ends auction");
    public static final Constraint.StaticConstraint IsFinalCall = isFinalCall;

    public static final Constraint.StaticConstraint isNotFinalCall = not(isFinalCall);
    public static final Constraint.StaticConstraint IsNotFinalCall = isNotFinalCall;

    public static final Constraint.StaticConstraint isOppsContract = new SimpleStaticConstraint((call, ps) -> ps.isOpponentsContract(), "opps contract");
    public static final Constraint.StaticConstraint IsOppsContract = isOppsContract;

    public static final Constraint.StaticConstraint isForcedToBid = new SimpleStaticConstraint((call, ps) -> ps.isForcedToBid(), null);
    public static final Constraint.StaticConstraint IsForcedToBid = isForcedToBid;

    // --- Dynamic Constraints (Karta) ---
    public static Constraint.HandConstraint points(int min, int max) {
        return new ShowsPoints(null, min, max, HasPoints.PointType.Starting);
    }

    public static Constraint.HandConstraint Points(int min, int max) {
        return points(min, max);
    }

    public static Constraint.HandConstraint points(Range range) {
        return points(range.min, range.max);
    }

    public static Constraint.HandConstraint Points(Range range) {
        return points(range);
    }

    public static Constraint.HandConstraint highCardPoints(int min, int max) {
        return new ShowsPoints(null, min, max, HasPoints.PointType.HighCard);
    }

    public static Constraint.HandConstraint HighCardPoints(int min, int max) {
        return highCardPoints(min, max);
    }

    public static Constraint.HandConstraint dummyPoints(int min, int max) {
        return new ShowsPoints(null, min, max, HasPoints.PointType.Dummy);
    }

    public static Constraint.HandConstraint DummyPoints(int min, int max) {
        return dummyPoints(min, max);
    }

    public static Constraint.HandConstraint dummyPoints(Suit trumpSuit, int min, int max) {
        return new ShowsPoints(trumpSuit, min, max, HasPoints.PointType.Dummy);
    }

    public static Constraint.HandConstraint DummyPoints(Suit trumpSuit, int min, int max) {
        return dummyPoints(trumpSuit, min, max);
    }

    public static Constraint.HandConstraint dummyPoints(Suit trumpSuit, Range range) {
        return dummyPoints(trumpSuit, range.min, range.max);
    }

    public static Constraint.HandConstraint DummyPoints(Suit trumpSuit, Range range) {
        return dummyPoints(trumpSuit, range);
    }

    public static Constraint.HandConstraint shape(int min, int max) {
        return new ShowsShape(null, min, max);
    }

    public static Constraint.HandConstraint Shape(int min, int max) {
        return shape(min, max);
    }

    public static Constraint.HandConstraint shape(Suit suit, int min, int max) {
        return new ShowsShape(suit, min, max);
    }

    public static Constraint.HandConstraint Shape(Suit suit, int min, int max) {
        return shape(suit, min, max);
    }

    public static Constraint.HandConstraint shape(int count) {
        return shape(count, count);
    }

    public static Constraint.HandConstraint Shape(int count) {
        return shape(count);
    }

    public static Constraint.HandConstraint shape(Suit suit, int count) {
        return shape(suit, count, count);
    }

    public static Constraint.HandConstraint Shape(Suit suit, int count) {
        return shape(suit, count);
    }

    public static Constraint longestMajor(int max) {
        return and(shape(Suit.Hearts, 0, max), shape(Suit.Spades, 0, max));
    }

    public static Constraint LongestMajor(int max) {
        return longestMajor(max);
    }

    public static final Constraint.HandConstraint balanced = new ShowsBalanced(true);
    public static final Constraint.HandConstraint Balanced = balanced;
    public static final Constraint.HandConstraint notBalanced = new ShowsBalanced(false);
    public static final Constraint.HandConstraint NotBalanced = notBalanced;
    public static final Constraint.HandConstraint flat = new ShowsFlat(true);
    public static final Constraint.HandConstraint Flat = flat;

    public static Constraint.HandConstraint quality(Suit suit, SuitQuality min, SuitQuality max) {
        return new ShowsQuality(suit, min, max);
    }

    public static Constraint.HandConstraint Quality(Suit suit, SuitQuality min, SuitQuality max) {
        return quality(suit, min, max);
    }

    public static Constraint.HandConstraint fit(int count, Suit suit) {
        return new PairShowsMinShape(suit, count, true);
    }

    public static Constraint.HandConstraint Fit(int count, Suit suit) {
        return fit(count, suit);
    }

    public static Constraint.HandConstraint fit(int count) {
        return fit(count, null);
    }

    public static Constraint.HandConstraint Fit(int count) {
        return fit(count);
    }

    public static Constraint.HandConstraint fit() {
        return fit(8, null);
    }

    public static Constraint.HandConstraint Fit() {
        return fit();
    }

    public static Constraint.HandConstraint pairPoints(int min, int max) {
        return new PairShowsPoints(null, min, max);
    }

    public static Constraint.HandConstraint PairPoints(int min, int max) {
        return pairPoints(min, max);
    }

    public static Constraint.HandConstraint pairPoints(Range range) {
        return pairPoints(range.min, range.max);
    }

    public static Constraint.HandConstraint PairPoints(Range range) {
        return pairPoints(range);
    }

    public static Constraint.HandConstraint aces(int... counts) {
        return new KeyCards(null, null, counts);
    }

    public static Constraint.HandConstraint Aces(int... counts) {
        return aces(counts);
    }

    public static Constraint.HandConstraint kings(int... counts) {
        return new Kings(counts);
    }

    public static Constraint.HandConstraint Kings(int... counts) {
        return kings(counts);
    }

    public static Constraint.HandConstraint pairAces(int... counts) {
        return new PairKeyCards(null, null, counts);
    }

    public static Constraint.HandConstraint PairAces(int... counts) {
        return pairAces(counts);
    }

    public static Constraint.HandConstraint pairKings(int... counts) {
        return new PairKings(counts);
    }

    public static Constraint.HandConstraint PairKings(int... counts) {
        return pairKings(counts);
    }

    public static Constraint takeoutSuit(Suit suit) {
        return and(new TakeoutSuit(suit), isNotCueBid);
    }

    public static Constraint TakeoutSuit(Suit suit) {
        return takeoutSuit(suit);
    }

    public static Constraint takeoutSuit() {
        return takeoutSuit(null);
    }

    public static Constraint TakeoutSuit() {
        return takeoutSuit();
    }

    public static Constraint.HandConstraint betterMinor(Suit suit) {
        return new BetterMinor(suit);
    }

    public static Constraint.HandConstraint BetterMinor(Suit suit) {
        return betterMinor(suit);
    }

    public static Constraint.HandConstraint ruleOf9() {
        return new RuleOf9();
    }

    public static Constraint.HandConstraint RuleOf9() {
        return ruleOf9();
    }

    public static Constraint.HandConstraint ruleOf17(Suit suit) {
        return new RuleOf17(suit);
    }

    public static Constraint.HandConstraint RuleOf17(Suit suit) {
        return ruleOf17(suit);
    }

    public static Constraint raisePartner(Suit suit, int jump, int fit) {
        return and(partner(new HasShownSuit(suit, false)), fit(fit, suit), isJump(jump));
    }

    public static Constraint RaisePartner(Suit suit, int jump, int fit) {
        return raisePartner(suit, jump, fit);
    }

    public static Constraint raisePartner() {
        return raisePartner(null, 0, 8);
    }

    public static Constraint RaisePartner() {
        return raisePartner();
    }

    public static final Constraint.HandConstraint ReverseShape = new ReverseShape();
}
