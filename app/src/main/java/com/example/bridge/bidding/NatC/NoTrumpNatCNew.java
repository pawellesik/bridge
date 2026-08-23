package com.example.bridge.bidding.NatC;

import static com.example.bridge.bidding.NatC.NatC.PAIR_GAME;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Bidder;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.CallFeaturesFactory;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NoTrumpNatCNew extends Bidder {

    public static final HandConstraint OPEN = highCardPoints(16, 17);
    public static final HandConstraint OPEN_DONT_ACCEPT_INVITE = highCardPoints(15, 15);
    public static final HandConstraint OPEN_ACCEPT_INVITE = highCardPoints(16, 17);
    public static final HandConstraint LESS_THAT_INVITE = highCardPoints(15, 15);
    public static final HandConstraint INVITE_GAME = highCardPoints(8, 9);
    public static final HandConstraint GAME_OR_BETTER = highCardPoints(10, 40);
    public static final HandConstraint INVITE_SLAM = highCardPoints(16, 17);
    public static final HandConstraint SMALL_SLAM = highCardPoints(18, 19);
    public static final HandConstraint GRAND_SLAM = highCardPoints(20, 40);

    public static Iterable<CallFeature> open1NTBid1(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(partnerBids(NoTrumpNatCNew::respond1NTBid1));
        bids.add(shows(Bid._1NT, OPEN, BALANCED, shape(Suit.Spades, 2, 4), shape(Suit.Hearts, 2, 4), id("NoTrumpNatC.OneNoTrumpBidderNatC 1NT")));
        return bids;
    }

    public static PositionCalls respond1NTBid1(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(NoTrumpNatCNew::open1NTBid2),

                shows(Bid._2C, GOOD_PLUS_SUIT, shape(5, 11), INVITE_GAME, id("NoTrumpNatC.Natural1NTNatC 2C")),
                shows(Bid._2D, GOOD_PLUS_SUIT, shape(5, 11), INVITE_GAME, id("NoTrumpNatC.Natural1NTNatC 2D")),

                shows(Bid._3C, GOOD_PLUS_SUIT, shape(5, 11), GAME_OR_BETTER, id("NoTrumpNatC.Natural1NTNatC 3C")),
                shows(Bid._3D, GOOD_PLUS_SUIT, shape(5, 11), GAME_OR_BETTER, id("NoTrumpNatC.Natural1NTNatC 3D")),

                shows(Bid._2H, DECENT_PLUS_SUIT, shape(7, 11), LESS_THAT_INVITE, id("NoTrumpNatC.Natural1NTNatC 2H")),
                shows(Bid._2S, DECENT_PLUS_SUIT, shape(7, 11), LESS_THAT_INVITE, id("NoTrumpNatC.Natural1NTNatC 2S")),

                shows(Bid._2H, DECENT_PLUS_SUIT, shape(5, 11), INVITE_GAME, id("NoTrumpNatC.Natural1NTNatC 2H")),
                shows(Bid._2S, DECENT_PLUS_SUIT, shape(5, 11), INVITE_GAME, id("NoTrumpNatC.Natural1NTNatC 2S")),

                shows(Bid._2NT, INVITE_GAME, longestMajor(4), id("NoTrumpNatC.Natural1NTNatC 2NT")),

                properties(Bid._3H, true),
                properties(Bid._3S, true),

                shows(Bid._3H, GAME_OR_BETTER, shape(5, 11), id("NoTrumpNatC.Natural1NTNatC 3H")),
                shows(Bid._3S, GAME_OR_BETTER, shape(5, 11), id("NoTrumpNatC.Natural1NTNatC 3S")),

                shows(Bid._3NT, GAME_OR_BETTER, longestMajor(4), id("NoTrumpNatC.Natural1NTNatC 3NT")),

                shows(Bid._6NT, FLAT, SMALL_SLAM, id("NoTrumpNatC.Natural1NTNatC 6NT")),
                shows(Bid._6NT, BALANCED, shape(Suit.Hearts, 2, 3), shape(Suit.Spades, 2, 3), SMALL_SLAM, id("NoTrumpNatC.Natural1NTNatC 6NT")),

                shows(Call.PASS, LESS_THAT_INVITE, id("NoTrumpNatC.Natural1NTNatC PASS"))
        );
        choices.addRules(CompeteNatC.compBids(ps));
        return choices;
    }

    public static PositionCalls open1NTBid2(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        //choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                partnerBids(NoTrumpNatCNew::respond1NTBid2),

                shows(Call.PASS, partner(isLastBid(Bid._3NT)), id("NoTrumpNatC.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2NT)), id("NoTrumpNatC.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2C)), id("NoTrumpNatC.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2D)), id("NoTrumpNatC.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2H)), id("NoTrumpNatC.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2S)), id("NoTrumpNatC.openerRebid PASS")),

                shows(Bid._4H, OPEN_ACCEPT_INVITE, partner(isLastBid(Bid._2H)), shape(3, 5)), id("NoTrumpNatC.openerRebid 4H"),
                shows(Bid._4S, OPEN_ACCEPT_INVITE, partner(isLastBid(Bid._2S)), shape(3, 5)), id("NoTrumpNatC.openerRebid 4S"),

                shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME)), partner(isLastBid(Bid._3C)), id("NoTrumpNatC.openerRebid 3NT"),
                shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME)), partner(isLastBid(Bid._3D)), id("NoTrumpNatC.openerRebid 3NT")
        );

        choices.addRules(
                propertiesAgreeTrump(new Call[]{Bid._4C, Bid._4D}, NoTrumpNatCNew::respond1NTBid2inviteMinor, true),
                shows(Bid._4D, fit(), partner(isLastBid(Bid._3D)), id("NoTrumpNatC.openerRebid 4D")),
                shows(Bid._4C, fit(), partner(isLastBid(Bid._3C)), id("NoTrumpNatC.openerRebid 4C"))
        );

        choices.addRules(
                shows(Bid._2H, partner(isLastBid(Bid._2C)), shape(4, 5), shape(Suit.Clubs, 0, 2), id("NoTrumpNatC.openerRebid 2H")),
                shows(Bid._2H, partner(isLastBid(Bid._2D)), shape(4, 5), shape(Suit.Diamonds, 0, 2), id("NoTrumpNatC.openerRebid 2H")),
                shows(Bid._2S, partner(isLastBid(Bid._2C)), shape(4, 5), shape(Suit.Clubs, 0, 2), id("NoTrumpNatC.openerRebid 2S")),
                shows(Bid._2S, partner(isLastBid(Bid._2D)), shape(4, 5), shape(Suit.Diamonds, 0, 2), id("NoTrumpNatC.openerRebid 2S"))
        );

        choices.addRules(properties(Bid._3H, true),
                shows(Bid._3H, partner(isLastBid(Bid._2NT)), OPEN_ACCEPT_INVITE, shape(5, 10), id("NoTrumpNatC.openerRebid 3H")));

        choices.addRules(
                properties(Bid._3S, true),
                shows(Bid._3S, partner(isLastBid(Bid._2NT)), OPEN_ACCEPT_INVITE, shape(5, 10), id("NoTrumpNatC.openerRebid 3S")));

        choices.addRules(
                shows(Bid._3NT, OPEN_ACCEPT_INVITE, partner(isLastBid(Bid._2NT)), id("NoTrumpNatC.openerRebid 3NT")),
                shows(Bid._3NT, partner(isLastBid(Bid._3H)), shape(Suit.Hearts, 0, 2)), id("NoTrumpNatC.openerRebid 3NT"),
                shows(Bid._3NT, partner(isLastBid(Bid._3S)), shape(Suit.Spades, 0, 2)), id("NoTrumpNatC.openerRebid 3NT"),
                shows(Bid._3NT, partner(isLastBid(Bid._3C)), shape(Suit.Clubs, 0, 2)), id("NoTrumpNatC.openerRebid 3NT"),
                shows(Bid._3NT, partner(isLastBid(Bid._3D)), shape(Suit.Diamonds, 0, 2)), id("NoTrumpNatC.openerRebid 3NT"),
                shows(Bid._4H, partner(isLastBid(Bid._3H)), shape(3, 5)), id("NoTrumpNatC.openerRebid 4H"),
                shows(Bid._4S, partner(isLastBid(Bid._3S)), shape(3, 5)), id("NoTrumpNatC.openerRebid 4S")
        );
        choices.addRules(CompeteNatC.compBids(ps));
        return choices;
    }

    public static Iterable<CallFeature> respond1NTBid2(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Bid._3NT, partner(isLastBid(Bid._3H)), shape(Suit.Hearts, 0, 2), id("NoTrumpNatC.responderRebid 3NT")));
        bids.add(shows(Bid._3NT, partner(isLastBid(Bid._3S)), shape(Suit.Spades, 0, 2), id("NoTrumpNatC.responderRebid 3NT")));

        bids.add(shows(Bid._4H, partner(isLastBid(Bid._3H)), shape(3, 4), id("NoTrumpNatC.responderRebid 4H")));
        bids.add(shows(Bid._4S, partner(isLastBid(Bid._3S)), shape(3, 4), id("NoTrumpNatC.responderRebid 4S")));

        bids.add(shows(Call.PASS, id("NoTrumpNatC.responderRebid Pass")));
        return bids;
    }

    public static PositionCalls respond1NTBid2inviteMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Bid._5C, partner(isLastBid(Bid._4C)), id("NoTrumpNatC.inviteMinor 5C")),
                shows(Bid._5D, partner(isLastBid(Bid._4D)), id("NoTrumpNatC.inviteMinor 5D"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }


}