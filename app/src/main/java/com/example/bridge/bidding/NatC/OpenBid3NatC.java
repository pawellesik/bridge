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

public class OpenBid3NatC extends OpenNatC {

    public static PositionCalls thirdBidToGameDiamond(PositionState ps) {
        //1D ->
        //     Bid._1S, Bid._1H, Bid._2C->
        //                              Bid._2H, Bid._1S, Bid._2S, Bid._2C ->
        //                                                              Bid._2H, Bid._2S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2NatC.thirdBidToGameDiamond Pass")),
                shows(Bid._2S, noFit(), IS_REBID, shape(6, 10), id("OpenBid3NatC.thirdBid _2S")),
                shows(Bid._3D, noFit(), shape(6, 10), id("OpenBid3NatC.thirdBid _3D")),
                shows(Bid._3C, noFit(), shape(6, 10), id("OpenBid3NatC.thirdBid _3C"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls thirdBidToGame1NTDiamond(PositionState ps) {
        //1D ->
        //     Bid._1NT->
        //              Bid._2D, Bid._2C, Bid._2H, Bid._2S->
        //                                                 Bid._2H, Bid._2S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2NatC.thirdBidToGame1NTDiamond Pass")),
                shows(Bid._2S, noFit(), shape(4, 10), id("OpenBid3NatC.thirdBidToGame1NTDiamond _2S")),
                shows(Bid._3C, noFit(), shape(5,10), id("OpenBid3NatC.thirdBidToGame1NTDiamond _3C")),
                shows(Bid._3D, noFit(), shape(2), id("OpenBid3NatC.thirdBidToGame1NTDiamond _3D")),
                shows(Bid._2NT, noFit(), PAIR_BALANCED, id("OpenBid3NatC.thirdBidToGame1NTDiamond _2NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static PositionCalls thirdBidToGame2NTDiamond(PositionState ps) {
        //1D ->
        //     Bid._2NT->
        //              Bid._3D, Bid._3C, Bid._3H, Bid._3S ->
        //                                                 Bid._3H, Bid._3S
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                //shows(Bid._4H, fit(), shape(4, 10), setTrumpColor(Suit.Hearts), id("OpenBid3NatC.thirdBidToGame2NTDiamond _3S")),
                //shows(Bid._4S, fit(), shape(4, 10), setTrumpColor(Suit.Spades), id("OpenBid3NatC.thirdBidToGame2NTDiamond _3S")),
                shows(Bid._3S, noFit(), shape(4, 10), id("OpenBid3NatC.thirdBidToGame2NTDiamond _3S"))
                //shows(Bid._3NT, noFit(), PAIR_BALANCED, id("OpenBid3NatC.thirdBidToGame2NTDiamond _3NT"))
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }

    public static Iterable<CallFeature> thirdBid(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Bid._1NT, BALANCED, points(Rebid1NT), id("OpenBid3NatC.thirdBid _1NT")));
        bids.add(shows(Bid._2NT, PAIR_BALANCED, points(Rebid2NT), id("OpenBid3NatC.thirdBid _2NT")));

        bids.add(shows(Bid._4S, fit(), OpeningStrongBidding, setTrumpColor(Suit.Spades), id("OpenBid3NatC.thirdBid _4S")));
        bids.add(shows(Bid._4H, fit(), OpeningStrongBidding, setTrumpColor(Suit.Hearts), id("OpenBid3NatC.thirdBid _4H")));

        bids.add(shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid3NatC.thirdBid _3NT")));
        bids.add(shows(Bid._3NT, PAIR_BALANCED, OpeningStrongBidding, shape(ps.getPartner().getBid().getSuit(), 0, 1), id("OpenBid3NatC.thirdBid _3NT")));

        bids.add(shows(Call.PASS, partner(isJump(1)), OpenBidding));

        for (CallFeature cf : CompeteNatC.compBids(ps)) {
            bids.add(cf);
        }
        return bids;
    }

    public static PositionCalls thirdBidToGameHeart(PositionState ps) {
        //odpowiedzi na: Bid._1H ->
        //                          Bid._3C, Bid._3D, Bid._2S ->
        //                                                     Bid._3H ->
        //                                                              Bid._3S ->
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
               //shows(Bid._4S, fit(), setTrumpColor(Suit.Spades), pairHighCardPoints(PAIR_GAME), id("OpenBid3NatC.thirdBidToGameHeart _4S"))
                //jest w compBids:
                //shows(Bid._3NT, PAIR_BALANCED, pairHighCardPoints(PAIR_GAME), id("OpenBid3NatC.thirdBidToGameHeart _4S")),
        );
        choices.addRules(CompeteNatC::compBids);
        return choices;
    }
}




























































































































































































































































































































































































































































































