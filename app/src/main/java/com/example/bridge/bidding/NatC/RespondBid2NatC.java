package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class RespondBid2NatC extends RespondNatC {

    public static PositionCalls responderClubJumpMinorChangeMajor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(partnerBids(OpenBid3NatC::thirdBid),
                shows(Bid._4S, fit(), pairHighCardPoints(PAIR_GAME),id("RespondBid2NatC.responderClubJumpMinorChangeMajor _4S")),
                shows(Bid._4H, fit(), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.responderClubJumpMinorChangeMajor _4H")),
                shows(Bid._3NT, othersAtLeast(3), partnerLastSuitShape(0, 3), pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.responderClubJumpMinorChangeMajor _3NT")),
                shows(Bid._3NT, othersAtLeast(3), partnerLastSuitShape(0, 1), pairHighCardPoints(PAIR_GAME_INVITE), id("RespondBid2NatC.responderClubJumpMinorChangeMajor _3NT")),
                shows(Call.PASS)
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }
    public static PositionCalls colorAfterPass(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(partnerBids(OpenBid3NatC::thirdBid),
                shows(Bid._2S, shape(5, 10), id("RespondBid2NatC.colorAfterPass _2S")),
                shows(Bid._2H, shape(5, 10), id("RespondBid2NatC.colorAfterPass _2H")),
                shows(Bid._2D, shape(5, 10), id("RespondBid2NatC.colorAfterPass _2D")),
                shows(Bid._2C, shape(5, 10), id("RespondBid2NatC.colorAfterPass _2C")),

                shows(Bid._3S, shape(4, 10), highCardPoints(5, 6), id("RespondBid2NatC.colorAfterPass _3S")),
                shows(Bid._3H, shape(4, 10), highCardPoints(5, 6), id("RespondBid2NatC.colorAfterPass _3H")),

                shows(Call.PASS, id("RespondBid2NatC.colorAfterPass PASS"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }


    public static Iterable<CallFeature> secondBid(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(partnerBids(OpenBid3NatC::thirdBid));
        bids.add(shows(Bid._2S, IS_PARTNERS_SUIT, betterThan(Suit.Hearts), id("RespondBid2NatC.secondBid _2S")));
        bids.add(shows(Bid._2H, IS_PARTNERS_SUIT, betterThan(Suit.Spades), id("RespondBid2NatC.secondBid _2H")));

        bids.add(shows(Bid._2S, fit(), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBid _2S")));
        bids.add(shows(Bid._2H, fit(), pairHighCardPoints(PAIR_LOW_GAME), id("RespondBid2NatC.secondBid _2H")));

        for (CallFeature cf : CompeteNatC.compBids(ps)) {
            bids.add(cf);
        }

        return bids;
    }

    public static PositionCalls secondBidToGame(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConvention(ps));
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Bid._4H, fit(), pairHighCardPoints(PAIR_GAME)), id("RespondBid2NatC.secondBidToGame _4H"),
                shows(Bid._4S, fit(), pairHighCardPoints(PAIR_GAME)), id("RespondBid2NatC.secondBidToGame _4S"),

                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.secondBidToGame _3NT")),
                shows(Call.PASS), id("RespondBid2NatC.secondBidToGame PASS"));

        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls openerInvitedGame(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._4H, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME)),
                shows(Bid._4S, FIT_8_PLUS, pairHighCardPoints(PAIR_GAME), id("RespondBid2NatC.openerInvitedGame _4S")),
                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME)),
                shows(Call.PASS, id("RespondBid2NatC.openerInvitedGame PASS")));
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

}




























































































































































































































































































































































































































































































