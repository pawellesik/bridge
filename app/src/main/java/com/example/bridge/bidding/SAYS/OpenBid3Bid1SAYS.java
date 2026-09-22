package com.example.bridge.bidding.SAYS;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;

import java.util.ArrayList;
import java.util.List;

public class OpenBid3Bid1SAYS extends OpenBid1SAYS {

    public static PositionCalls thirdBidNegat2NTStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3D, shape(5, 10), noFit(7), id("OpenBid3SAYS.thirdBidNegat2NTStrong _3D")),
                shows(Bid._3H, shape(4, 10), noFit(7), DECENT_PLUS_SUIT, id("OpenBid3SAYS.thirdBidNegat2NTStrong _3H")),
                shows(Bid._3S, shape(4, 10), noFit(7), DECENT_PLUS_SUIT, id("OpenBid3SAYS.thirdBidNegat2NTStrong _3S")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls thirdBidToGameMinorClubStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls thirdBidMinorClubForcingStrong(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._5C, shape(6, 10), noFit(), IS_REBID, id("OpenBid3SAYS.thirdBidMinorClubForcingStrong _5C")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls thirdBidToGameHeart(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2SAYS.thirdBidToGameDiamond Pass")),
                shows(Bid._2S, shape(6, 10), noFit(), IS_REBID, id("OpenBid3SAYS.thirdBid _2S")),
                shows(Bid._3D, shape(6, 10), noFit(), id("OpenBid3SAYS.thirdBid _3D")),
                shows(Bid._3C, shape(6, 10), noFit(), id("OpenBid3SAYS.thirdBid _3C")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls thirdBidToGame1NTDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Call.PASS, fit(ps.getPartner().getBid().getSuit()), id("RespondBid2SAYS.thirdBidToGame1NTDiamond Pass")),
                shows(Bid._2S, noFit(), shape(4, 10), DECENT_PLUS_SUIT, id("OpenBid3SAYS.thirdBidToGame1NTDiamond _2S")),
                shows(Bid._3C, noFit(), shape(5, 10), id("OpenBid3SAYS.thirdBidToGame1NTDiamond _3C")),
                shows(Bid._3D, noFit(), shape(5, 10), id("OpenBid3SAYS.thirdBidToGame1NTDiamond _3D")),
                shows(Bid._2NT, PAIR_BALANCED, id("OpenBid3SAYS.thirdBidToGame1NTDiamond _2NT")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls thirdBidToGame2NTDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3S, shape(4, 10), noFit(), id("OpenBid3SAYS.thirdBidToGame2NTDiamond _3S")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls thirdBidToGameDiamond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        List<CallFeature> conventions = new ArrayList<>();
        CompeteSAYS.addAcesAskConventions(ps, conventions);
        choices.addRules(conventions);
        choices.addRules(
                shows(Bid._3S, shape(4, 10), noFit(), id("OpenBid3SAYS.thirdBidToGameDiamond _3S")),
                partnerBids(RecursionSAYS::recursionFindFitGame)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }
}
