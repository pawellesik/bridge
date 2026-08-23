package com.example.bridge.bidding.NatC;

import static com.example.bridge.bidding.NatC.NatC.PAIR_GAME;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Bidder;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.CallFeaturesFactory;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.NoTrumpDescription;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionCallsFactory;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
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


    public static class OneNoTrumpBidderNatC extends Bidder {

        public static Iterable<CallFeature> openBid1NT(PositionState ps) {
            List<CallFeature> bids = new ArrayList<>();
            bids.add(partnerBids(OneNoTrumpBidderNatC::respondBid1NT));
            bids.add(shows(Bid._1NT, OPEN, BALANCED, shape(Suit.Spades, 2, 4), shape(Suit.Hearts, 2, 4), id("NoTrumpNatC.OneNoTrumpBidderNatC 1NT")));
            return bids;
        }

        public static PositionCalls respondBid1NT(PositionState ps) {
            PositionCalls choices = new PositionCalls(ps);
            choices.addRules(AcesAsk.initiateConvention(ps));
            choices.addRules(AcesAsk.initiateConventionBlok(ps));
            choices.addRules(
                    partnerBids(OneNoTrumpBidderNatC::openBid2NT),

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

        public static PositionCalls openBid2NT(PositionState ps) {
            PositionCalls choices = new PositionCalls(ps);
            choices.addRules(AcesAsk.initiateConvention(ps));
            choices.addRules(AcesAsk.initiateConventionBlok(ps));
            choices.addRules(
            );
            choices.addRules(CompeteNatC.compBids(ps));
            return choices;
        }
    }

}