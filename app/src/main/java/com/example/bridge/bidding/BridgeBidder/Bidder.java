package com.example.bridge.bidding.BridgeBidder;

import com.example.licytacja.moje.BridgeBidder.Constraints.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * KLASA BAZOWA LICYTACJI.
 * Zawiera narzędzia do budowania reguł (Constraints) używanych przez AI.
 */
public abstract class Bidder {

    // =================================================================================
    // --- SEKCJA 1: KLUCZOWE NARZĘDZIA (Z KOMENTARZAMI) ---
    // =================================================================================

    /** GŁÓWNA METODA: Definiuje odzywkę i warunki jej użycia. */
    public static BidRule shows(Call call, Constraint... constraints) {
        return new BidRule(call, constraints);
    }

    /** CECHY ODZYWKI: Ustawia wymuszenia (forcing), uzgodnienie atutu i odpowiedzi partnera. */
    public static CallFeatureGroup properties(Call call, PositionCallsFactory partnerBids, boolean forcing1Round, boolean forcingToGame, boolean agreeTrump, Suit trump, String alert, String announce, String convention, StaticConstraint onlyIf) {
        CallFeatureGroup group = new CallFeatureGroup();
        group.getFeatures().add(new CallProperties(call, partnerBids, forcing1Round, forcingToGame, agreeTrump, trump, onlyIf));
        if (alert != null) group.getFeatures().add(alert(call, alert, onlyIf));
        if (announce != null) group.getFeatures().add(announce(call, announce, onlyIf));
        if (convention != null) group.getFeatures().add(convention(call, convention, onlyIf));
        return group;
    }

    /** PUNKTY LICYTACYJNE: HCP + bonus za długość. Podstawowa siła ręki. */
    public static HandConstraint points(int min, int max) { return new Points.ShowsPoints(null, min, max, Points.PointType.Starting); }
    public static HandConstraint points(Range range) { return points(range.getMin(), range.getMax()); }

    /** PUNKTY HONOROWE (HCP): Tylko figury (A=4, K=3, D=2, W=1). */
    public static HandConstraint highCardPoints(int min, int max) { return new Points.ShowsPoints(null, min, max, Points.PointType.HighCard); }
    public static HandConstraint highCardPoints(Range range) { return highCardPoints(range.getMin(), range.getMax()); }

    /** PUNKTY PARY: Suma punktów Twoich i partnera (obiecanych w licytacji). */
    public static HandConstraint pairPoints(int min, int max) { return new PairPoints.PairShowsPoints(min, max, false); }
    public static HandConstraint pairPoints(Range range) { return pairPoints(range.getMin(), range.getMax()); }
    public static HandConstraint pairPoints(Suit suit, int min, int max) { return new PairPoints.PairShowsPoints(suit, min, max, false); }

    /** ASY (0-4): Sprawdza liczbę asów tylko w Twojej ręce. */
    public static Constraint aces(int... count) { return new Aces(count); }
    
    /** ASY PARY (0-4): Sprawdza sumę asów w obu rękach partnerów. */
    public static Constraint pairAces(int... count) { return new PairAces(count); }

    /** FIT: Sprawdza czy para ma łącznie min. 8 kart w kolorze. */
    public static HandConstraint fit() { return fit(8, null); }
    public static HandConstraint fit(int count) { return new PairMinShape.PairShowsMinShape(null, count, true); }
    public static HandConstraint fit(Suit suit) { return new PairMinShape.PairShowsMinShape(suit, 8, true); }

    /** SKŁAD: Liczba kart w licytowanym mianie (np. shape(5,6)). */
    public static HandConstraint shape(int min, int max) { return new Shape.ShowsShape(null, min, max); }
    public static HandConstraint shape(Suit suit, int min, int max) { return new Shape.ShowsShape(suit, min, max); }

    // =================================================================================
    // --- SEKCJA 2: WSZYSTKIE POZOSTAŁE METODY (KOMPATYBILNOŚĆ) ---
    // =================================================================================

    public static CallFeature alert(Call call, String text, StaticConstraint... constraints) { return new CallAnnotation(call, CallAnnotation.AnnotationType.Alert, text, constraints); }
    public static CallFeature announce(Call call, String text, StaticConstraint... constraints) { return new CallAnnotation(call, CallAnnotation.AnnotationType.Announce, text, constraints); }
    public static CallFeature convention(Call call, String text, StaticConstraint... constraints) { return new CallAnnotation(call, CallAnnotation.AnnotationType.Convention, text, constraints); }
    public static CallFeature convention(String text, StaticConstraint... constraints) { return new CallAnnotation(null, CallAnnotation.AnnotationType.Convention, text, constraints); }

    public static CallProperties partnerBids(Call call, PositionCallsFactory pcf) { return new CallProperties(call, pcf, false, false, false, null); }
    public static CallProperties partnerBids(PositionCallsFactory pcf) { return new CallProperties(null, pcf, false, false, false, null); }
    public static CallProperties partnerBids(CallFeaturesFactory cff) { return partnerBids(PositionCalls.fromCallFeaturesFactory(cff)); }
    public static CallProperties partnerBids(Call call, CallFeaturesFactory cff) { return partnerBids(call, PositionCalls.fromCallFeaturesFactory(cff)); }

    public static CallFeatureGroup properties(Call[] calls, PositionCallsFactory partnerBids, boolean forcing1Round, boolean forcingToGame, boolean agreeTrump, Suit trump, String alert, String announce, String convention, StaticConstraint onlyIf) {
        CallFeatureGroup group = new CallFeatureGroup();
        for (Call call : calls) group.getFeatures().add(properties(call, partnerBids, forcing1Round, forcingToGame, agreeTrump, trump, alert, announce, convention, onlyIf));
        return group;
    }

    public static CallFeatureGroup properties(Call call, PositionCallsFactory partnerBids) { return properties(call, partnerBids, false, false, false, null, null, null, null, null); }
    public static CallFeatureGroup properties(Call call, PositionCallsFactory partnerBids, boolean forcing1Round) { return properties(call, partnerBids, forcing1Round, false, false, null, null, null, null, null); }
    public static CallFeatureGroup properties(Call call, PositionCallsFactory partnerBids, boolean forcing1Round, String convention) { return properties(call, partnerBids, forcing1Round, false, false, null, null, null, convention, null); }
    public static CallFeatureGroup properties(Call call, PositionCallsFactory partnerBids, boolean forcing1Round, StaticConstraint onlyIf) { return properties(call, partnerBids, forcing1Round, false, false, null, null, null, null, onlyIf); }
    public static CallFeatureGroup properties(Call call, PositionCallsFactory partnerBids, boolean forcing1Round, String text, boolean isAnnounce) { return properties(call, partnerBids, forcing1Round, false, false, null, null, isAnnounce ? text : null, isAnnounce ? null : text, null); }
    public static CallFeatureGroup properties(Call[] calls, PositionCallsFactory partnerBids) { return properties(calls, partnerBids, false, false, false, null, null, null, null, null); }
    public static CallFeatureGroup properties(Call[] calls, PositionCallsFactory partnerBids, boolean forcing1Round) { return properties(calls, partnerBids, forcing1Round, false, false, null, null, null, null, null); }
    public static CallFeatureGroup properties(Call[] calls, boolean forcing1Round, StaticConstraint onlyIf) { return properties(calls, null, forcing1Round, false, false, null, null, null, null, onlyIf); }
    public static CallFeatureGroup propertiesForcingToGame(Call[] calls, PositionCallsFactory partnerBids, boolean forcingToGame) { return properties(calls, partnerBids, false, forcingToGame, false, null, null, null, null, null); }
    public static CallFeatureGroup propertiesForcingToGame(Call[] calls, boolean forcingToGame, StaticConstraint onlyIf) { return properties(calls, null, false, forcingToGame, false, null, null, null, null, onlyIf); }
    public static CallFeatureGroup propertiesAgreeTrump(Call[] calls, PositionCallsFactory partnerBids, boolean agreeTrump) { return properties(calls, partnerBids, false, false, agreeTrump, null, null, null, null, null); }
    public static CallFeatureGroup properties(Call call, boolean forcing1Round, boolean agreeTrump, StaticConstraint onlyIf) { return properties(call, null, forcing1Round, false, agreeTrump, null, null, null, null, onlyIf); }
    public static CallFeatureGroup properties(Call call, boolean forcing1Round, String convention, StaticConstraint onlyIf) { return properties(call, null, forcing1Round, false, false, null, null, null, convention, onlyIf); }
    public static CallFeatureGroup properties(Call call, PositionCallsFactory partnerBids, boolean forcing1Round, String convention, StaticConstraint onlyIf) { return properties(call, partnerBids, forcing1Round, false, false, null, null, null, convention, onlyIf); }
    public static CallFeatureGroup properties(Call call, boolean forcing1Round) { return properties(call, null, forcing1Round, false, false, null, null, null, null, null); }
    public static CallFeatureGroup properties(Call call, boolean forcing1Round, String text, boolean isAnnounce) { return properties(call, null, forcing1Round, false, false, null, null, isAnnounce ? text : null, isAnnounce ? null : text, null); }
    public static CallFeatureGroup properties(Call[] calls, PositionCallsFactory partnerBids, boolean forcing1Round, boolean agreeTrump) { return properties(calls, partnerBids, forcing1Round, false, agreeTrump, null, null, null, null, null); }
    public static CallFeatureGroup properties(Call call, String alert) { return properties(call, null, false, false, false, null, alert, null, null, null); }
    public static CallFeatureGroup properties(Call call, String alert, StaticConstraint onlyIf) { return properties(call, null, false, false, false, null, alert, null, null, onlyIf); }

    public static StaticConstraint isSeat(int... seats) { return new SimpleStaticConstraint((call, ps) -> { for (int s : seats) if (ps.getSeat() == s) return true; return false; }, (call, ps) -> "seat " + Arrays.toString(seats)); }
    public static StaticConstraint isLastBid(Call call) { return new BidHistory(0, call); }
    public static StaticConstraint isLastBid(int level, Suit suit) { return new BidHistory(0, new Bid(level, suit)); }
    public static StaticConstraint isLastBid(int level, Strain strain) { return new BidHistory(0, new Bid(level, strain)); }
    public static StaticConstraint isOpeningBid(Bid bid) { return new SimpleStaticConstraint((call, ps) -> java.util.Objects.equals(ps.getBiddingState().getOpeningBid(), bid)); }

    public static final StaticConstraint IS_CUE_BID = new IsCueBid(null);
    public static final StaticConstraint IS_NEW_SUIT = new NewSuit(null);
    public static final StaticConstraint IS_NOT_CUE_BID = not(IS_CUE_BID);
    public static final StaticConstraint IS_REVERSE_BID = new SimpleStaticConstraint((call, ps) -> ps.isReverse(call), "reverse");
    public static final StaticConstraint IS_NOT_REVERSE = not(IS_REVERSE_BID);
    public static final StaticConstraint IS_REBID = new BidHistory(0, null);
    public static final StaticConstraint IS_NOT_REBID = not(IS_REBID);
    public static final StaticConstraint IS_FORCED_TO_BID = new SimpleStaticConstraint((call, ps) -> ps.isForcedToBid());
    public static final StaticConstraint IS_FORCED_TO_GAME = new SimpleStaticConstraint((call, ps) -> ps.getPairState().isForcedToGame());
    public static final StaticConstraint IS_OPPS_CONTRACT = new SimpleStaticConstraint((call, ps) -> ps.isOpponentsContract(), "opps contract");
    public static final StaticConstraint IS_OUR_CONTRACT = new SimpleStaticConstraint((call, ps) -> ps.isOurContract(), "our contract");
    public static final StaticConstraint CONTRACT_IS_AGREED_STRAIN = new SimpleStaticConstraint((call, ps) -> { Call contractBid = ps.getBiddingState().getContract().getBid(); if (contractBid instanceof Bid) { Bid bid = (Bid) contractBid; return ps.getBiddingState().getContract().isOurs(ps.getDirection()) && bid.getSuit() == ps.getPairState().getLastShownSuit(); } return false; });

    public static StaticConstraint id(String id) { return new LogID(id); }
    public static Constraint note(String text) { return new Note(text); }
    public static Constraint and(Constraint... constraints) { return new ConstraintGroup(constraints); }
    public static StaticConstraint staticAnd(StaticConstraint... constraints) { return new ConstraintGroup(constraints); }
    public static StaticConstraint not(StaticConstraint c) { return new SimpleStaticConstraint((call, ps) -> !c.conforms(call, ps), (call, ps) -> { if (c instanceof IDescribeConstraint) { String desc = ((IDescribeConstraint) c).describe(call, ps); return (desc == null || desc.isEmpty()) ? null : "not " + desc; } return null; }); }
    public static StaticConstraint isJump(int... jumpLevels) { return new JumpBid(jumpLevels); }
    
    public static final StaticConstraint IS_NON_JUMP = isJump(0);
    public static final StaticConstraint IS_SINGLE_JUMP = isJump(1);
    public static final StaticConstraint IS_DOUBLE_JUMP = isJump(2);
    public static final StaticConstraint IS_ANY_JUMP = isJump(1, 2);
    public static final StaticConstraint IS_JUMP_SHIFT = staticAnd(IS_SINGLE_JUMP, new NewSuit(null));

    public static final StaticConstraint IS_VUL = new SimpleStaticConstraint((call, ps) -> ps.isVulnerable(), "vul");
    public static final StaticConstraint IS_NOT_VUL = not(IS_VUL);
    public static final StaticConstraint IS_FAV_VUL = new SimpleStaticConstraint((call, ps) -> !ps.isVulnerable() && ps.getRHO().isVulnerable(), "favorable vul");
    public static final StaticConstraint IS_UNFAV_VUL = new SimpleStaticConstraint((call, ps) -> ps.isVulnerable() && !ps.getRHO().isVulnerable(), "unfavorable vul");
    public static final StaticConstraint BOTH_VUL = new SimpleStaticConstraint((call, ps) -> ps.isVulnerable() && ps.getRHO().isVulnerable(), "all vul");
    public static final StaticConstraint BOTH_NOT_VUL = new SimpleStaticConstraint((call, ps) -> !ps.isVulnerable() && !ps.getRHO().isVulnerable(), "none vul");
    public static final StaticConstraint IS_FINAL_CALL = new SimpleStaticConstraint((call, ps) -> ps.getBiddingState().getContract().isPassEndsAuction(), "pass ends auction");
    public static final StaticConstraint IS_NOT_FINAL_CALL = not(IS_FINAL_CALL);

    public static StaticConstraint partner(Constraint constraint) { return new PositionProxy(PositionProxy.RelativePosition.Partner, constraint); }
    public static StaticConstraint rho(Constraint constraint) { return new PositionProxy(PositionProxy.RelativePosition.RHO, constraint); }
    public static StaticConstraint hasShownSuit(Suit suit, boolean eitherPartner) { return new HasShownSuit(suit, eitherPartner); }
    public static final StaticConstraint IS_PARTNERS_SUIT = partner(hasShownSuit(null, false));
    public static StaticConstraint isPartnersSuit() { return IS_PARTNERS_SUIT; }
    public static StaticConstraint isPassedHand() { return new SimpleStaticConstraint((call, ps) -> ps.isPassedHand(), "passed hand"); }

    public static HandConstraint dummyPoints(int min, int max) { return new Points.ShowsPoints(null, min, max, Points.PointType.Dummy); }
    public static HandConstraint dummyPoints(Suit suit, Range range) { return new Points.ShowsPoints(suit, range.getMin(), range.getMax(), Points.PointType.Dummy); }
    public static HandConstraint dummyPoints(Suit suit, int min, int max) { return new Points.ShowsPoints(suit, min, max, Points.PointType.Dummy); }
    public static HandConstraint dummyPoints(Range range) { return dummyPoints(range.getMin(), range.getMax()); }
    public static HandConstraint shape(Suit suit, int count) { return new Shape.ShowsShape(suit, count, count); }
    public static HandConstraint shape(int count) { return new Shape.ShowsShape(null, count, count); }
    public static HandConstraint fit(int count, Suit suit, boolean desiredValue) { return new PairMinShape.PairShowsMinShape(suit, count, desiredValue); }
    public static HandConstraint fit(int count, Suit suit) { return fit(count, suit, true); }
    public static HandConstraint fit(Suit suit, boolean desiredValue) { return fit(8, suit, desiredValue); }
    public static final HandConstraint FIT_8_PLUS = fit(8);
    public static final HandConstraint BALANCED = new Balanced.ShowsBalanced(true);
    public static final HandConstraint NOT_BALANCED = new Balanced.ShowsBalanced(false);
    public static final HandConstraint FLAT = new Flat.ShowsFlat(true);
    public static final HandConstraint NOT_FLAT = new Flat.ShowsFlat(false);
    public static final HandConstraint LONGEST_SUIT = new LongestSuit.ShowsLongestSuit(null);
    public static HandConstraint longerThan(Suit worse) { return new BetterSuit.ShowsBetterSuit(null, worse, worse, true); }
    public static HandConstraint longerOrEqualTo(Suit worse) { return new BetterSuit.ShowsBetterSuit(null, worse, null, true); }
    public static HandConstraint longer(Suit better, Suit worse) { return new BetterSuit.ShowsBetterSuit(better, worse, worse, true); }
    public static HandConstraint longerOrEqual(Suit better, Suit worse) { return new BetterSuit.ShowsBetterSuit(better, worse, better, true); }
    public static HandConstraint betterThan(Suit worse) { return new BetterSuit.ShowsBetterSuit(null, worse, worse, false); }
    public static HandConstraint betterOrEqualTo(Suit worse) { return new BetterSuit.ShowsBetterSuit(null, worse, null, false); }
    public static Constraint longestMajor(int max) { return and(shape(Suit.Hearts, 0, max), shape(Suit.Spades, 0, max)); }
    public static Constraint kings(int... count) { return new Kings(count); }
    public static Constraint pairKings(int... count) { return new PairKings(count); }
    public static Constraint sumPairAcesAndKings(int... count) { return new SumPairAcesAndKings(count); }
    public static Constraint sumPairAcesAndKings(int min, int max) { return new SumPairAcesAndKings(new Range(min, max)); }
    public static Constraint sumPairAcesAndKings(String description, int... count) { return new SumPairAcesAndKings(description, count); }
    public static Constraint sumPairAcesAndKings(String description, int min, int max) { return new SumPairAcesAndKings(description, new Range(min, max)); }
    public static HandConstraint pairHighCardPoints(int min, int max) { return new PairPoints.PairShowsPoints(min, max, true); }

    public static HandConstraint pairHighCardPoints(Range range) { return new PairPoints.PairShowsPoints(range.getMin(), range.getMax(), true); }
    public static HandConstraint keyCards(Suit suit, int... count) { return new KeyCards(suit, null, count); }
    public static HandConstraint keyCards(Suit suit, Boolean hasQueen, int... count) { return new KeyCards(suit, hasQueen, count); }
    public static HandConstraint pairKeyCards(Suit suit, Boolean hasQueen, int... count) { return new PairKeyCards(suit, hasQueen, count); }
    public static HandConstraint quality(SuitQuality min, SuitQuality max) { return new HasQuality.ShowsQuality(null, min, max); }
    public static HandConstraint quality(Suit suit, SuitQuality min, SuitQuality max) { return new HasQuality.ShowsQuality(suit, min, max); }
    public static final HandConstraint DECENT_PLUS_SUIT = quality(SuitQuality.Decent, SuitQuality.Solid);
    public static final HandConstraint GOOD_PLUS_SUIT = quality(SuitQuality.Good, SuitQuality.Solid);
    public static final HandConstraint EXCELLENT_PLUS_SUIT = quality(SuitQuality.Excellent, SuitQuality.Solid);
    public static final HandConstraint BAD_SUIT = quality(SuitQuality.Poor, SuitQuality.Poor);
    public static HandConstraint showsBadSuit(Suit suit) { return quality(suit, SuitQuality.Poor, SuitQuality.Poor); }
    public static HandConstraint suitLosers(int min, int max, Suit suit) { return new Losers.ShowsLosers(false, suit, min, max); }
    public static HandConstraint suitLosers(int min, int max) { return suitLosers(min, max, null); }
    public static Constraint takeoutSuit(Suit suit) { return and(new TakeoutSuit(suit), IS_NOT_CUE_BID); }
    public static Constraint takeoutSuit() { return takeoutSuit(null); }
    public static Constraint ruleOf17(Suit suit) { return new RuleOf17(suit); }
    public static Constraint ruleOf17() { return ruleOf17(null); }
    public static HandConstraint betterMinor(Suit suit) { return new BetterMinor(suit); }
    public static HandConstraint betterMinor() { return betterMinor(null); }
    public static HandConstraint ruleOf9() { return new RuleOf9(); }
    public static HandConstraint secondSuit(Suit exclude, int min) { return new TwoSuiter(exclude, min); }
    public static HandConstraint twoSuiter(int min) { return new TwoSuiter(null, min); }
    public static HandConstraint hasShortness(int min, int max) { return new HasShortness(min, max); }

    public static final HandConstraint OPPS_STOPPED = new OppsStopped.ShowsOppsStopped(true);
    public static final HandConstraint OPPS_NOT_STOPPED = new OppsStopped.ShowsOppsStopped(false);
    public static final HandConstraint REVERSE_SHAPE = new ReverseShape.ShowsReverseShape();
    public static Constraint breakConstraint(boolean isStatic, String name) { if (isStatic) return new Break.StaticBreak(name); return new Break.HandBreak(name); }
    public static Constraint agreedStrain(Strain... strains) { return new AgreedStrain(strains); }
    public static StaticConstraint isBidAvailable(int level, Suit suit) { return new SimpleStaticConstraint((call, ps) -> ps.isValidNextCall(new Bid(level, suit))); }
    public static Constraint raisePartner(Suit suit, int jump, int fit) { return and(partner(hasShownSuit(suit, false)), fit(fit, suit), isJump(jump)); }
    public static Constraint raisePartner() { return raisePartner(null, 0, 8); }
    public static HandConstraint passIn4thSeat() { return new PassIn4thSeat(); }

    public static class Note extends StaticConstraint implements IDescribeConstraint {
        private final String text;
        public Note(String text) { this.text = text; }
        @Override public boolean conforms(Call call, PositionState ps) { return true; }
        @Override public String describe(Call call, PositionState ps) { return text; }
    }
}
