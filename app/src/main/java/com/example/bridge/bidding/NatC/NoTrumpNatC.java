package com.example.bridge.bidding.NatC;

import static com.example.bridge.bidding.NatC.NatC.PAIR_GAME;

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

public class NoTrumpNatC extends Bidder {

    public static final HandConstraint OPEN = highCardPoints(15, 17);
    public static final HandConstraint OPEN_DONT_ACCEPT_INVITE = highCardPoints(15, 15);
    public static final HandConstraint OPEN_ACCEPT_INVITE = highCardPoints(16, 17);
    public static final HandConstraint LESS_THAT_INVITE = highCardPoints(0, 7);
    public static final HandConstraint INVITE_GAME = highCardPoints(8, 9);
    public static final HandConstraint GAME_OR_BETTER = highCardPoints(10, 40);
    public static final HandConstraint INVITE_SLAM = highCardPoints(16, 17);
    public static final HandConstraint SMALL_SLAM = highCardPoints(18, 19);
    public static final HandConstraint GRAND_SLAM = highCardPoints(20, 40);

    public static Iterable<CallFeature> open1NTBid1(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Bid._1NT, OPEN, shape(Suit.Spades, 2, 4), shape(Suit.Hearts, 2, 4), BALANCED, ruleShow(1), id("NoTrumpNatC.OneNoTrumpBidderNatC 1NT")));
        bids.add(partnerBids(NoTrumpNatC::respond1NTBid1));
        return bids;
    }

    //1NT -->
    public static PositionCalls respond1NTBid1(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Bid._2C, INVITE_GAME, shape(5, 11), GOOD_PLUS_SUIT, id("NoTrumpNatC.Natural1NTNatC 2C")),
                shows(Bid._2D, INVITE_GAME, shape(5, 11), GOOD_PLUS_SUIT, id("NoTrumpNatC.Natural1NTNatC 2D")),

                shows(Bid._3C, GAME_OR_BETTER, shape(5, 11), GOOD_PLUS_SUIT, id("NoTrumpNatC.Natural1NTNatC 3C")),
                shows(Bid._3D, GAME_OR_BETTER, shape(5, 11), GOOD_PLUS_SUIT, id("NoTrumpNatC.Natural1NTNatC 3D")),

                shows(Bid._2H, LESS_THAT_INVITE, shape(7, 11), DECENT_PLUS_SUIT, id("NoTrumpNatC.Natural1NTNatC 2H")),
                shows(Bid._2S, LESS_THAT_INVITE, shape(7, 11), DECENT_PLUS_SUIT, id("NoTrumpNatC.Natural1NTNatC 2S")),

                shows(Bid._2H, INVITE_GAME, shape(5, 11), DECENT_PLUS_SUIT, id("NoTrumpNatC.Natural1NTNatC 2H")),
                shows(Bid._2S, INVITE_GAME, shape(5, 11), DECENT_PLUS_SUIT, id("NoTrumpNatC.Natural1NTNatC 2S")),

                shows(Bid._2NT, INVITE_GAME, longestMajor(4), id("NoTrumpNatC.Natural1NTNatC 2NT")),

                shows(Bid._3H, GAME_OR_BETTER, shape(5, 11), id("NoTrumpNatC.Natural1NTNatC 3H")),
                shows(Bid._3S, GAME_OR_BETTER, shape(5, 11), id("NoTrumpNatC.Natural1NTNatC 3S")),

                shows(Bid._3NT, GAME_OR_BETTER, longestMajor(4), id("NoTrumpNatC.Natural1NTNatC 3NT")),

                shows(Bid._6NT, SMALL_SLAM, FLAT, id("NoTrumpNatC.Natural1NTNatC 6NT")),
                shows(Bid._6NT, SMALL_SLAM, shape(Suit.Hearts, 2, 3), shape(Suit.Spades, 2, 3), BALANCED, id("NoTrumpNatC.Natural1NTNatC 6NT")),

                shows(Call.PASS, LESS_THAT_INVITE, id("NoTrumpNatC.Natural1NTNatC PASS")),
                partnerBids(NoTrumpNatC::open1NTBid2),
                properties(new Call[]{Bid._3H, Bid._3S}, NoTrumpNatC::open1NTBid2Major)

        );
        choices.addRules(CompeteNatC.compBids(ps));
        return choices;
    }

    public static PositionCalls open1NTBid2Major(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        //choices.addRules(AcesAsk.initiateConvention(ps));
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Bid._4H, fit(), partner(isLastBid(Bid._3H)), setTrumpColor(Suit.Hearts), id("NoTrumpNatC.openerRebid OPEN_ACCEPT_INVITE 4H")),
                    shows(Bid._4S, fit(), partner(isLastBid(Bid._3S)), setTrumpColor(Suit.Spades), id("NoTrumpNatC.openerRebid OPEN_ACCEPT_INVITE 4S"))
            );
        } else {
            choices.addRules(AcesAsk.initiateConventionBlok(ps));
        }
        choices.addRules(CompeteNatC.compBids(ps));
        return choices;
    }

    //1NT --> X
    //        -->
    public static PositionCalls open1NTBid2(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        //choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Call.PASS, partner(isLastBid(Bid._3NT)), id("NoTrumpNatC.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2NT)), id("NoTrumpNatC.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2C)), id("NoTrumpNatC.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2D)), id("NoTrumpNatC.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2H)), id("NoTrumpNatC.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2S)), id("NoTrumpNatC.openerRebid PASS")),

                shows(Bid._4H, OPEN_ACCEPT_INVITE, fit(), partner(isLastBid(Bid._2H)), setTrumpColor(Suit.Hearts), id("NoTrumpNatC.openerRebid OPEN_ACCEPT_INVITE 4H")),
                shows(Bid._4S, OPEN_ACCEPT_INVITE, fit(), partner(isLastBid(Bid._2S)), setTrumpColor(Suit.Spades), id("NoTrumpNatC.openerRebid OPEN_ACCEPT_INVITE 4S")),

                shows(Bid._3H, shape(4), partner(isLastBid(Bid._2NT)), id("NoTrumpNatC.openerRebid shape 3H)")),
                shows(Bid._3S, shape(4), partner(isLastBid(Bid._2NT)), id("NoTrumpNatC.openerRebid shape 3S")),

                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, partner(isLastBid(Bid._3C)), id("NoTrumpNatC.openerRebid PAIR_BALANCED 3C 3NT")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, partner(isLastBid(Bid._3D)), id("NoTrumpNatC.openerRebid PAIR_BALANCED 3D 3NT")),
                partnerBids(NoTrumpNatC::respond1NTBid2)
        );

        choices.addRules(
                shows(Bid._4D, fit(), partner(isLastBid(Bid._3D)), setTrumpColor(Suit.Diamonds), id("NoTrumpNatC.openerRebid 4D")),
                shows(Bid._4C, fit(), partner(isLastBid(Bid._3C)), setTrumpColor(Suit.Clubs), id("NoTrumpNatC.openerRebid 4C")),
                propertiesAgreeTrump(new Call[]{Bid._4C, Bid._4D}, NoTrumpNatC::respond1NTBid2inviteMinor, true)
        );

        choices.addRules(
                shows(Bid._2H, shape(4), noFit(), partner(isLastBid(Bid._2C)), id("NoTrumpNatC.openerRebid 2H")),
                shows(Bid._2H, shape(4), noFit(), partner(isLastBid(Bid._2D)), id("NoTrumpNatC.openerRebid 2H")),
                shows(Bid._2S, shape(4), noFit(), partner(isLastBid(Bid._2C)), id("NoTrumpNatC.openerRebid 2S")),
                shows(Bid._2S, shape(4), noFit(), partner(isLastBid(Bid._2D)), id("NoTrumpNatC.openerRebid 2S"))
        );

        choices.addRules(CompeteNatC.compBids(ps));
        return choices;
    }

    //1NT --> X
    //        --> Y
    //            -->
    public static PositionCalls respond1NTBid2(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Bid._3NT, shape(Suit.Hearts, 0, 2), partner(isLastBid(Bid._3H)), id("NoTrumpNatC.responderRebid 3NT")),
                shows(Bid._3NT, shape(Suit.Spades, 0, 2), partner(isLastBid(Bid._3S)), id("NoTrumpNatC.responderRebid 3NT")),

                shows(Bid._4H, partner(isLastBid(Bid._3H)), fit(), setTrumpColor(Suit.Hearts), id("NoTrumpNatC.responderRebid 4H")),
                shows(Bid._4S, partner(isLastBid(Bid._3S)), fit(), setTrumpColor(Suit.Spades), id("NoTrumpNatC.responderRebid 4S")),

                shows(Call.PASS, id("NoTrumpNatC.responderRebid Pass"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls respond1NTBid2inviteMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Bid._5C, partner(isLastBid(Bid._4C)), setTrumpColor(Suit.Clubs), id("NoTrumpNatC.inviteMinor 5C")),
                shows(Bid._5D, partner(isLastBid(Bid._4D)), setTrumpColor(Suit.Diamonds), id("NoTrumpNatC.inviteMinor 5D"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }


}