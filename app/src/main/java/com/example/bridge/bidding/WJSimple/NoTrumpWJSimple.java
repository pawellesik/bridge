package com.example.bridge.bidding.WJSimple;

import static com.example.bridge.bidding.WJSimple.WJSimple.PAIR_GAME;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Bidder;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class NoTrumpWJSimple extends Bidder {

    public static final HandConstraint OPEN = highCardPoints(16, 17);
    public static final HandConstraint LESS_THAT_INVITE = highCardPoints(0, 6);
    public static final HandConstraint INVITE_GAME = highCardPoints(7, 10);
    public static final HandConstraint GAME_OR_BETTER = highCardPoints(11, 40);
    public static final HandConstraint SMALL_SLAM = highCardPoints(18, 19);

    public static Iterable<CallFeature> open1NTBid1(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Bid._1NT, shape(Suit.Spades, 2, 4), shape(Suit.Hearts, 2, 4), OPEN, BALANCED, ruleShow(1), id("NoTrumpWJSimple.OneNoTrumpBidderWJSimple 1NT")));
        bids.add(partnerBids(NoTrumpWJSimple::respond1NTBid1));
        return bids;
    }

    //1NT -->
    public static PositionCalls respond1NTBid1(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Bid._2C, shape(6, 13), INVITE_GAME, id("NoTrumpWJSimple.Natural1NTWJSimple 2C")),
                shows(Bid._2D, shape(6, 13), INVITE_GAME, id("NoTrumpWJSimple.Natural1NTWJSimple 2D")),

                shows(Bid._2C, shape(5, 13), INVITE_GAME, GOOD_PLUS_SUIT, ruleShow(1),id("NoTrumpWJSimple.Natural1NTWJSimple 2C")),
                shows(Bid._2D, shape(5, 13), INVITE_GAME, GOOD_PLUS_SUIT, ruleShow(1),id("NoTrumpWJSimple.Natural1NTWJSimple 2D")),

                shows(Bid._2H, shape(6, 13), INVITE_GAME, ruleShow(1),id("NoTrumpWJSimple.Natural1NTWJSimple 2H")),
                shows(Bid._2S, shape(6, 13), INVITE_GAME, ruleShow(1), id("NoTrumpWJSimple.Natural1NTWJSimple 2S")),

                shows(Bid._2H, shape(5, 13), INVITE_GAME, DECENT_PLUS_SUIT, id("NoTrumpWJSimple.Natural1NTWJSimple 2H")),
                shows(Bid._2S, shape(5, 13), INVITE_GAME, DECENT_PLUS_SUIT, id("NoTrumpWJSimple.Natural1NTWJSimple 2S")),

                shows(Bid._3C, shape(5, 13), GAME_OR_BETTER, GOOD_PLUS_SUIT, ruleShow(1),id("NoTrumpWJSimple.Natural1NTWJSimple 3C")),
                shows(Bid._3D, shape(5, 13), GAME_OR_BETTER, GOOD_PLUS_SUIT, ruleShow(1),id("NoTrumpWJSimple.Natural1NTWJSimple 3D")),

                shows(Bid._3H, shape(5, 13), GAME_OR_BETTER, ruleShow(1), id("NoTrumpWJSimple.Natural1NTWJSimple 3H")),
                shows(Bid._3S, shape(5, 13), GAME_OR_BETTER, ruleShow(1), id("NoTrumpWJSimple.Natural1NTWJSimple 3S")),

                shows(Bid._2NT, INVITE_GAME, longestMajor(4), id("NoTrumpWJSimple.Natural1NTWJSimple 2NT")),

                shows(Bid._3NT, GAME_OR_BETTER, longestMajor(4), id("NoTrumpWJSimple.Natural1NTWJSimple 3NT")),

                shows(Bid._6NT, SMALL_SLAM, FLAT, id("NoTrumpWJSimple.Natural1NTWJSimple 6NT")),
                shows(Bid._6NT, shape(Suit.Hearts, 2, 3), shape(Suit.Spades, 2, 3), SMALL_SLAM, BALANCED, id("NoTrumpWJSimple.Natural1NTWJSimple 6NT")),

                shows(Call.PASS, LESS_THAT_INVITE, id("NoTrumpWJSimple.Natural1NTWJSimple PASS")),
                partnerBids(NoTrumpWJSimple::open1NTBid2),
                properties(new Call[]{Bid._3H, Bid._3S}, NoTrumpWJSimple::open1NTBid2Major)

        );
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    public static PositionCalls open1NTBid2Major(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Bid._4H, fit(), partner(isLastBid(Bid._3H)), setTrumpColor(Suit.Hearts), id("NoTrumpWJSimple.openerRebid OPEN_ACCEPT_INVITE 4H")),
                    shows(Bid._4S, fit(), partner(isLastBid(Bid._3S)), setTrumpColor(Suit.Spades), id("NoTrumpWJSimple.openerRebid OPEN_ACCEPT_INVITE 4S"))
            );
        } else {
            choices.addRules(AcesAsk.initiateConventionBlok(ps));
        }
        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    //1NT --> X
    //        -->
    public static PositionCalls open1NTBid2(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Call.PASS, partner(isLastBid(Bid._3NT)), id("NoTrumpWJSimple.openerRebid PASS")),

                shows(Bid._4H, pairPoints(PAIR_GAME), fit(), partner(isLastBid(Bid._2H)), setTrumpColor(Suit.Hearts), id("NoTrumpWJSimple.openerRebid OPEN_ACCEPT_INVITE 4H")),
                shows(Bid._4S, pairPoints(PAIR_GAME), fit(), partner(isLastBid(Bid._2S)), setTrumpColor(Suit.Spades), id("NoTrumpWJSimple.openerRebid OPEN_ACCEPT_INVITE 4S")),

                shows(Bid._2H, shape(4), noFit(), partner(isLastBid(Bid._2C)), id("NoTrumpWJSimple.openerRebid 2H")),
                shows(Bid._2H, shape(4), noFit(), partner(isLastBid(Bid._2D)), id("NoTrumpWJSimple.openerRebid 2H")),
                shows(Bid._2S, shape(4), noFit(), partner(isLastBid(Bid._2C)), id("NoTrumpWJSimple.openerRebid 2S")),
                shows(Bid._2S, shape(4), noFit(), partner(isLastBid(Bid._2D)), id("NoTrumpWJSimple.openerRebid 2S")),

                shows(Bid._3C, fit(), id("NoTrumpWJSimple.openerRebid shape 3C)")),
                shows(Bid._3D, fit(), id("NoTrumpWJSimple.openerRebid shape 3D")),

                shows(Bid._3H, shape(4), partner(isLastBid(Bid._2NT)), id("NoTrumpWJSimple.openerRebid shape 3H)")),
                shows(Bid._3S, shape(4), partner(isLastBid(Bid._2NT)), id("NoTrumpWJSimple.openerRebid shape 3S")),

                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, partner(isLastBid(Bid._3C, Bid._3D, Bid._2NT)), id("NoTrumpWJSimple.openerRebid PAIR_BALANCED 3C 3NT")),
                partnerBids(NoTrumpWJSimple::respond1NTBid2)
        );

        choices.addRules(
                shows(Bid._4D, fit(), partner(isLastBid(Bid._3D)), setTrumpColor(Suit.Diamonds), id("NoTrumpWJSimple.openerRebid 4D")),
                shows(Bid._4C, fit(), partner(isLastBid(Bid._3C)), setTrumpColor(Suit.Clubs), id("NoTrumpWJSimple.openerRebid 4C")),
                propertiesAgreeTrump(new Call[]{Bid._4C, Bid._4D}, NoTrumpWJSimple::respond1NTBid2inviteMinor, true)
        );


        choices.addRules(CompeteWJSimple.compBids(ps));
        return choices;
    }

    //1NT --> X
    //        --> Y
    //            -->
    public static PositionCalls respond1NTBid2(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Bid._3NT, shape(Suit.Hearts, 0, 2), partner(isLastBid(Bid._3H)), id("NoTrumpWJSimple.responderRebid 3NT")),
                shows(Bid._3NT, shape(Suit.Spades, 0, 2), partner(isLastBid(Bid._3S)), id("NoTrumpWJSimple.responderRebid 3NT")),

                shows(Bid._4H, partner(isLastBid(Bid._3H)), fit(), setTrumpColor(Suit.Hearts), id("NoTrumpWJSimple.responderRebid 4H")),
                shows(Bid._4S, partner(isLastBid(Bid._3S)), fit(), setTrumpColor(Suit.Spades), id("NoTrumpWJSimple.responderRebid 4S"))
        );
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }

    public static PositionCalls respond1NTBid2inviteMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Bid._5C, partner(isLastBid(Bid._4C)), setTrumpColor(Suit.Clubs), id("NoTrumpWJSimple.inviteMinor 5C")),
                shows(Bid._5D, partner(isLastBid(Bid._4D)), setTrumpColor(Suit.Diamonds), id("NoTrumpWJSimple.inviteMinor 5D"))
        );
        choices.addRules(CompeteWJSimple::compBids);
        return choices;
    }
}
