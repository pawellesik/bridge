package com.example.bridge.bidding.BridgeBidder.LCStandard;

import com.example.bridge.bidding.BridgeBidder.Conventions.Jacoby2NT;
import com.example.bridge.bidding.BridgeBidder.Conventions.NegativeDouble;
import com.example.bridge.bidding.BridgeBidder.Tools.Bid;
import com.example.bridge.bidding.BridgeBidder.Tools.BidRule;
import com.example.bridge.bidding.BridgeBidder.Tools.Call;
import com.example.bridge.bidding.BridgeBidder.Tools.CallFeature;
import com.example.bridge.bidding.BridgeBidder.Tools.HandConstraint;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionCalls;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionCallsFactory;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionState;
import com.example.bridge.bidding.BridgeBidder.Tools.Range;
import com.example.bridge.bidding.BridgeBidder.Tools.Strain;
import com.example.bridge.bidding.BridgeBidder.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class Respond extends LCStandard {
    public static final Range RESPOND_PASS = new Range(0, 5);
    public static final Range RESPOND_1_LEVEL = new Range(6, 40);
    public static final Range RAISE_1 = new Range(6, 10);
    public static final Range LIMIT_RAISE = new Range(11, 12);
    public static final Range NEW_SUIT_2_LEVEL = new Range(13, 40);
    public static final Range RESPOND_1NT_OVER_MAJOR = new Range(6, 12);
    public static final Range MINIMUM_HAND = new Range(6, 10);
    public static final Range MEDIUM_HAND = new Range(11, 13);
    public static final Range RAISE_TO_3NT = new Range(13, 16);
    public static final Range RAISE_TO_4M = new Range(13, 16);
    public static final Range LIMIT_RAISE_OR_BETTER = new Range(11, 40);
    public static final Range WEAK_4_LEVEL = new Range(0, 10);
    public static final Range WEAK_5_LEVEL = new Range(0, 10);
    public static final Range GAME_OR_BETTER = new Range(13, 40);
    public static final Range MAX_PASSED = new Range(10, 11);
    public static final Range RESPOND_REDOUBLE = new Range(10, 40);
    public static final Range RESPOND_X_1_LEVEL = new Range(6, 9);
    public static final Range RESPOND_X_JUMP = new Range(0, 6);
    protected static final Range WEAK_JUMP_SHIFT_POINTS = new Range(0, 5);

    protected static final Range RESPOND_1NT_OVER_MINOR = new Range(6, 10);
    protected static final Range RESPOND_2NT_OVER_MINOR = new Range(11, 12);
    protected static final Range RESPOND_3NT_OVER_CLUBS = new Range(13, 17);

    public static PositionCalls oneClub(PositionState ps) {
        if (!ps.getRHO().isPassed()) return oppsInterferred(ps, Suit.Clubs);
        PositionCalls choices = new PositionCalls(ps);
        if (ps.isPassedHand()) {
            choices.addRules(
                partnerBids(OpenBid2::responderChangedSuits),
                shows(Bid._1D, points(RESPOND_1_LEVEL), shape(5, 10), longestMajor(3)),
                shows(Bid._1H, points(RESPOND_1_LEVEL), shape(4), shape(Suit.Spades, 0, 4)),
                shows(Bid._1H, points(RESPOND_1_LEVEL), shape(5, 10), longerThan(Suit.Spades)),
                shows(Bid._1S, points(RESPOND_1_LEVEL), shape(4, 10), longerOrEqualTo(Suit.Hearts)),
                propertiesAgreeTrump(new Call[]{Bid._2C, Bid._3C, Bid._4C, Bid._5C}, OpenBid2::responderRaisedMinor, true),
                shows(Bid._2C, points(RAISE_1), shape(5), longestMajor(3)),
                shows(Bid._3C, points(LIMIT_RAISE), shape(5), longestMajor(3)),
                shows(Bid._5C, points(WEAK_5_LEVEL), shape(7, 10)),
                shows(Bid._4C, points(WEAK_4_LEVEL), shape(6))
            );
        } else {
            choices.addRules(SolidSuit.BIDS(ps));
            choices.addRules(
                properties(new Call[]{Bid._1D, Bid._1H, Bid._1S}, OpenBid2::responderChangedSuits, true),
                shows(Bid._1D, points(RESPOND_1_LEVEL), shape(5, 10), longestMajor(3)),
                shows(Bid._1D, points(LIMIT_RAISE_OR_BETTER), shape(5, 10), longerThan(Suit.Hearts), longerThan(Suit.Spades)),
                shows(Bid._1H, points(RESPOND_1_LEVEL), shape(4), shape(Suit.Spades, 0, 4)),
                shows(Bid._1H, points(RESPOND_1_LEVEL), shape(5, 10), longerThan(Suit.Spades)),
                shows(Bid._1S, points(RESPOND_1_LEVEL), shape(4, 10), longerOrEqualTo(Suit.Hearts)),
                propertiesAgreeTrump(new Call[]{Bid._2C, Bid._3C, Bid._4C, Bid._5C}, OpenBid2::responderRaisedMinor, true),
                shows(Bid._2C, points(RAISE_1), shape(5), longestMajor(3)),
                shows(Bid._3C, points(LIMIT_RAISE), shape(5), longestMajor(3)),
                shows(Bid._5C, points(WEAK_5_LEVEL), shape(7, 10)),
                shows(Bid._4C, points(WEAK_4_LEVEL), shape(6))
            );
        }
        choices.addRules(noTrumpResponsesToMinor(Suit.Clubs));
        choices.addRules(weakJumpShift(Suit.Clubs));
        choices.addPassRule(points(RESPOND_PASS));
        return choices;
    }

    public static PositionCalls oneDiamond(PositionState ps) {
        if (!ps.getRHO().isPassed()) return oppsInterferred(ps, Suit.Diamonds);
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(SolidSuit.BIDS(ps));
        choices.addRules(
            propertiesAgreeTrump(new Call[]{Bid._2D, Bid._3D, Bid._4D, Bid._5D}, OpenBid2::responderRaisedMinor, true),
            properties(Bid._2C, OpenBid2::twoOverOne, false, true, false, null, null, null, null, null),
            properties(new Call[]{Bid._1H, Bid._1S}, OpenBid2::responderChangedSuits, true),
            shows(Bid._2C, points(GAME_OR_BETTER), longestMajor(4)),
            shows(Bid._1H, points(RESPOND_1_LEVEL), shape(4), shape(Suit.Spades, 0, 4)),
            shows(Bid._1H, points(RESPOND_1_LEVEL), shape(5, 10), longerThan(Suit.Spades)),
            shows(Bid._1S, points(RESPOND_1_LEVEL), shape(4, 10), longerOrEqualTo(Suit.Hearts)),
            shows(Bid._2D, points(RAISE_1), shape(5), longestMajor(3)),
            shows(Bid._3D, points(LIMIT_RAISE), shape(5), longestMajor(3)),
            shows(Bid._5D, points(WEAK_5_LEVEL), shape(7, 10)),
            shows(Bid._4D, points(WEAK_4_LEVEL), shape(6))
        );
        choices.addRules(noTrumpResponsesToMinor(Suit.Diamonds));
        choices.addRules(weakJumpShift(Suit.Diamonds));
        choices.addPassRule(points(RESPOND_PASS));
        return choices;
    }

    public static PositionCalls oneHeart(PositionState ps) {
        if (!ps.getRHO().isPassed()) return oppsInterferred(ps, Suit.Hearts);
        PositionCalls choices = new PositionCalls(ps);
        Call[] raises = new Call[]{Bid._2H, Bid._3H, Bid._4H};
        if (ps.isPassedHand()) {
            choices.addRules(
                partnerBids(OpenBid2::responderChangedSuits),
                propertiesAgreeTrump(raises, OpenBid2::responderRaisedMajor, true),
                shows(Bid._2H, dummyPoints(RAISE_1), shape(3, 5)),
                shows(Bid._3H, dummyPoints(MEDIUM_HAND), shape(3, 5)),
                shows(Bid._4H, points(WEAK_4_LEVEL), shape(5, 10)),
                shows(Bid._1S, shape(4, 10), points(RESPOND_1_LEVEL), shape(Suit.Hearts, 0, 3)),
                shows(Bid._2C, points(MAX_PASSED), shape(5, 10)),
                shows(Bid._2D, points(MAX_PASSED), shape(5, 10)),
                shows(Bid._1NT, points(6, 10), shape(Suit.Hearts, 0, 2), shape(Suit.Spades, 0, 3)),
                shows(Bid._2NT, points(11, 12), shape(Suit.Hearts, 0, 2), shape(Suit.Spades, 0, 3))
            );
        } else {
            choices.addRules(SolidSuit.BIDS(ps));
            choices.addRules(Jacoby2NT.initiateConvention(ps));
            choices.addRules(
                partnerBids(OpenBid2::responderChangedSuits),
                properties(new Call[]{Bid._2C, Bid._2D}, OpenBid2::twoOverOne, false, true, false, null, null, null, null, null),
                shows(Bid._2C, points(GAME_OR_BETTER), longerThan(Suit.Diamonds), shape(Suit.Spades, 0, 4)),
                shows(Bid._2C, points(GAME_OR_BETTER), shape(4), longerOrEqual(Suit.Clubs, Suit.Diamonds), shape(Suit.Spades, 0, 4)),
                shows(Bid._2C, dummyPoints(Suit.Hearts, GAME_OR_BETTER), longerThan(Suit.Diamonds), shape(Suit.Spades, 0, 4)),
                shows(Bid._2C, dummyPoints(Suit.Hearts, GAME_OR_BETTER), shape(4), longerOrEqual(Suit.Clubs, Suit.Diamonds), shape(Suit.Spades, 0, 4)),
                shows(Bid._2D, points(GAME_OR_BETTER), longerOrEqual(Suit.Diamonds, Suit.Clubs), shape(Suit.Spades, 0, 4)),
                shows(Bid._2D, dummyPoints(Suit.Hearts, GAME_OR_BETTER), longerOrEqual(Suit.Diamonds, Suit.Clubs), shape(Suit.Spades, 0, 4)),
                propertiesAgreeTrump(raises, OpenBid2::responderRaisedMajor, true),
                shows(Bid._2H, dummyPoints(RAISE_1), shape(3, 5)),
                shows(Bid._3H, dummyPoints(MEDIUM_HAND), shape(4, 5)),
                shows(Bid._4H, points(WEAK_4_LEVEL), shape(5, 10)),
                properties(Bid._1S, true),
                shows(Bid._1S, points(RESPOND_1_LEVEL), shape(4, 10), shape(Suit.Hearts, 0, 3)),
                properties(Bid._1NT, OpenBid2::semiForcingNT, false, false, false, null, null, null, UserText.SemiForcing, null),
                shows(Bid._1NT, points(RESPOND_1NT_OVER_MAJOR), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3)),
                shows(Bid._3NT, FLAT, points(RAISE_TO_3NT))
            );
            choices.addRules(weakJumpShift(Suit.Hearts));
        }
        choices.addPassRule(points(RESPOND_PASS));
        return choices;
    }

    public static PositionCalls oneSpade(PositionState ps) {
        if (!ps.getRHO().isPassed()) return oppsInterferred(ps, Suit.Spades);
        PositionCalls choices = new PositionCalls(ps);
        Call[] raises = new Call[]{Bid._2S, Bid._3S, Bid._4S};
        if (ps.isPassedHand()) {
            choices.addRules(
                partnerBids(OpenBid2::responderChangedSuits),
                propertiesAgreeTrump(raises, OpenBid2::responderRaisedMajor, true),
                shows(Bid._2S, dummyPoints(6, 10), shape(3, 5)),
                shows(Bid._3S, dummyPoints(11, 12), shape(3, 5)),
                shows(Bid._4S, points(WEAK_4_LEVEL), shape(5, 10)),
                shows(Bid._2C, points(MAX_PASSED), shape(5, 10)),
                shows(Bid._2D, points(MAX_PASSED), shape(5, 10)),
                shows(Bid._2H, points(MAX_PASSED), shape(5, 10)),
                shows(Bid._1NT, points(6, 10), shape(Suit.Spades, 0, 2)),
                shows(Bid._2NT, points(11, 12), shape(Suit.Spades, 0, 2))
            );
        } else {
            choices.addRules(SolidSuit.BIDS(ps));
            choices.addRules(Jacoby2NT.initiateConvention(ps));
            choices.addRules(
                partnerBids(OpenBid2::responderChangedSuits),
                properties(new Call[]{Bid._2C, Bid._2D, Bid._2H}, OpenBid2::twoOverOne, false, true, false, null, null, null, null, null),
                shows(Bid._2C, points(GAME_OR_BETTER), longerThan(Suit.Diamonds), shape(Suit.Hearts, 0, 4)),
                shows(Bid._2C, points(GAME_OR_BETTER), shape(4), longerOrEqual(Suit.Clubs, Suit.Diamonds), shape(Suit.Hearts, 0, 4)),
                shows(Bid._2C, dummyPoints(Suit.Spades, GAME_OR_BETTER), longerThan(Suit.Diamonds), shape(Suit.Hearts, 0, 4)),
                shows(Bid._2C, dummyPoints(Suit.Spades, GAME_OR_BETTER), shape(4), longerOrEqual(Suit.Clubs, Suit.Diamonds), shape(Suit.Hearts, 0, 4)),
                shows(Bid._2D, points(GAME_OR_BETTER), longerOrEqual(Suit.Diamonds, Suit.Clubs), shape(Suit.Hearts, 0, 4)),
                shows(Bid._2D, dummyPoints(Suit.Spades, GAME_OR_BETTER), longerOrEqual(Suit.Diamonds, Suit.Clubs), shape(Suit.Hearts, 0, 4)),
                shows(Bid._2H, shape(5, 10), points(GAME_OR_BETTER)),
                propertiesAgreeTrump(raises, OpenBid2::responderRaisedMajor, true),
                shows(Bid._2S, dummyPoints(RAISE_1), shape(3, 5)),
                shows(Bid._3S, dummyPoints(MEDIUM_HAND), shape(4, 5)),
                shows(Bid._4S, points(WEAK_4_LEVEL), shape(5, 10)),
                properties(Bid._1NT, OpenBid2::semiForcingNT, false, false, false, null, null, null, UserText.SemiForcing, null),
                shows(Bid._1NT, points(RESPOND_1NT_OVER_MAJOR), shape(Suit.Spades, 0, 3)),
                shows(Bid._3NT, FLAT, points(RAISE_TO_3NT))
            );
            choices.addRules(weakJumpShift(Suit.Spades));
        }
        choices.addPassRule(points(RESPOND_PASS));
        return choices;
    }

    private static PositionCalls oppsInterferred(PositionState ps, Suit openSuit) {
        if (ps.getRHO().isDoubled()) return oppsDoubled(ps, openSuit);
        Call rhoCall = ps.getRHO().getLastCall();
        if (rhoCall instanceof Bid) {
            Bid rhoBid = (Bid) rhoCall;
            if (rhoBid.getSuit() != null) {
                return oppsOvercalledSuit(ps, openSuit, rhoBid.getLevel(), rhoBid.getSuit());
            }
        }
        return ps.getPairState().getBiddingSystem().getPositionCalls(ps);
    }

    public static PositionCalls oppsOvercalledSuit(PositionState ps, Suit openSuit, int rhoBidLevel, Suit rhoBidSuit) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(SolidSuit.BIDS(ps));
        choices.addRules(NegativeDouble.initiateConvention(ps));
        choices.addRules(weakJumpShift(openSuit));

        Call raisePartner = ps.getBiddingState().getContract().nextAvailableBid(openSuit);
        Bid cueBidRaise = new Bid(rhoBidLevel + 1, rhoBidSuit);
        Bid weakRaise = new Bid(raisePartner instanceof Bid ? ((Bid) raisePartner).getLevel() + 1 : 3, openSuit);
        HandConstraint weakFit = openSuit.isMinor() ? fit(8) : fit(9);
        PositionCallsFactory raiseHandler = openSuit.isMinor() ? OpenBid2::responderRaisedMinor : OpenBid2::responderRaisedMajor;

        List<Suit> suits = new ArrayList<>(List.of(Suit.values()));
        suits.remove(openSuit);
        suits.remove(rhoBidSuit);
        Suit lowerUnbid = suits.get(0);
        Suit higherUnbid = suits.get(1);
        Call bidNew1 = ps.getBiddingState().getContract().nextAvailableBid(lowerUnbid);
        Call bidNew2 = ps.getBiddingState().getContract().nextAvailableBid(higherUnbid);
        boolean newSuitForcing = !ps.isPassedHand();

        choices.addRules(
                partnerBids(OpenBid2::responderChangedSuits),
                properties(new Call[]{Bid._1NT, Bid._2NT, Bid._3NT}, OpenBid2::responderBidNT),
                shows(Bid._1H, points(RESPOND_1_LEVEL), shape(4), longerOrEqualTo(Suit.Spades)),
                shows(Bid._1H, points(RESPOND_1_LEVEL), shape(5, 11), longerThan(Suit.Spades)),
                shows(Bid._1S, points(RESPOND_1_LEVEL), shape(4), shape(Suit.Hearts, 0, 3)),
                shows(Bid._1S, points(RESPOND_1_LEVEL), shape(5, 11), longerOrEqualTo(Suit.Hearts)),
                properties(new Call[]{raisePartner, weakRaise}, raiseHandler, false, false, true, openSuit, null, null, null, null),
                properties(cueBidRaise, raiseHandler, true, false, true, openSuit, null, null, null, null),
                shows(raisePartner, fit(8), dummyPoints(RAISE_1)),
                shows(cueBidRaise, fit(openSuit), dummyPoints(openSuit, LIMIT_RAISE_OR_BETTER)),
                shows(weakRaise, weakFit, dummyPoints(0, 8)),
                shows(Bid._1NT, OPPS_STOPPED, points(RAISE_1)),
                shows(Bid._2NT, OPPS_STOPPED, points(11, 12)),
                properties(new Call[]{bidNew1, bidNew2}, OpenBid2::responderChangedSuits, newSuitForcing),
                shows(bidNew1, shape(4), shape(higherUnbid, 0, 4), points(NEW_SUIT_2_LEVEL)),
                shows(bidNew2, shape(5, 10), longerThan(higherUnbid), points(NEW_SUIT_2_LEVEL)),
                shows(bidNew2, shape(4, 10), shape(lowerUnbid, 0, 3), points(NEW_SUIT_2_LEVEL)),
                partnerBids(Call.PASS, OpenBid2::responderPassedInCompetition),
                shows(Call.PASS)
        );
        return choices;
    }

    public static PositionCalls oppsDoubled(PositionState ps, Suit openSuit) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(SolidSuit.BIDS(ps));
        choices.addRules(
                properties(Call.REDOUBLE, true),
                shows(Call.REDOUBLE, points(RESPOND_REDOUBLE)),
                shows(Bid._1H, points(RESPOND_X_1_LEVEL), shape(4), longerOrEqualTo(Suit.Spades)),
                shows(Bid._1H, points(RESPOND_X_1_LEVEL), shape(5, 11), longerThan(Suit.Spades)),
                shows(Bid._1S, points(RESPOND_X_1_LEVEL), shape(4), shape(Suit.Hearts, 0, 3)),
                shows(Bid._1S, points(RESPOND_X_1_LEVEL), shape(5, 11), longerOrEqualTo(Suit.Hearts)),
                shows(Bid._1D, shape(4, 11), points(RESPOND_X_1_LEVEL)),
                shows(new Bid(3, openSuit), fit(9), points(RESPOND_X_JUMP)),
                shows(Bid._2C, partner(hasShownSuit(null, false)), fit(8), points(RESPOND_X_1_LEVEL)),
                shows(Bid._2C, shape(5, 11), points(RESPOND_X_1_LEVEL)),
                shows(Bid._2D, partner(hasShownSuit(null, false)), fit(8), points(RESPOND_X_1_LEVEL)),
                shows(Bid._2D, IS_NON_JUMP, shape(5, 11), points(RESPOND_X_1_LEVEL)),
                shows(Bid._2H, partner(hasShownSuit(null, false)), fit(8), points(RESPOND_X_1_LEVEL)),
                shows(Bid._2H, IS_NON_JUMP, shape(5, 11), points(RESPOND_X_1_LEVEL)),
                shows(Bid._2S, partner(hasShownSuit(null, false)), fit(8), points(RESPOND_X_1_LEVEL)),
                shows(Bid._1NT, points(RESPOND_X_1_LEVEL)),
                shows(Call.PASS, points(RESPOND_PASS))
        );
        return choices;
    }

    public static Iterable<CallFeature> weakOpen(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Bid._4H, FIT_8_PLUS, ruleOf17()));
        bids.add(shows(Bid._4H, fit(10)));
        bids.add(shows(Bid._4S, FIT_8_PLUS, ruleOf17()));
        bids.add(shows(Bid._4S, fit(10)));
        bids.add(shows(Bid._3D, fit(9)));
        bids.add(shows(Bid._3H, fit(9)));
        bids.add(shows(Bid._3S, fit(9)));
        bids.add(shows(Call.PASS));
        return bids;
    }

    public static Iterable<CallFeature> weakJumpShift(Suit openSuit) {
        List<CallFeature> bids = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            if (suit != openSuit) {
                bids.add(shows(new Bid(2, suit), IS_SINGLE_JUMP, points(WEAK_JUMP_SHIFT_POINTS), shape(6, 10), DECENT_PLUS_SUIT));
                bids.add(shows(new Bid(3, suit), IS_SINGLE_JUMP, points(WEAK_JUMP_SHIFT_POINTS), shape(6, 10), DECENT_PLUS_SUIT));
            }
        }
        return bids;
    }

    private static Iterable<CallFeature> noTrumpResponsesToMinor(Suit minor) {
        List<CallFeature> bids = new ArrayList<>();
        bids.addAll(ntResponseToMinor(minor, 1, RESPOND_1NT_OVER_MINOR, OpenBid2::oneNTOverMinorOpen));
        bids.addAll(ntResponseToMinor(minor, 2, RESPOND_2NT_OVER_MINOR, OpenBid2::twoNTOverMinorOpen));
        if (minor == Suit.Clubs) {
            bids.addAll(ntResponseToMinor(minor, 3, RESPOND_3NT_OVER_CLUBS, OpenBid2::threeNTOverClubOpen));
        }
        return bids;
    }

    private static List<CallFeature> ntResponseToMinor(Suit minor, int level, Range pointRange, PositionCallsFactory partnerBids) {
        Bid bid = new Bid(level, Strain.NoTrump);
        List<CallFeature> rules = new ArrayList<>();
        rules.add(partnerBids(bid, partnerBids));
        BidRule rule = shows(bid, points(pointRange), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3));
        if (minor == Suit.Clubs) rule.addConstraint(shape(Suit.Diamonds, 0, 4));
        rules.add(rule);
        return rules;
    }
}




























































































































































































































































































































































































































































































