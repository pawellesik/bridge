package com.example.bridge.bidding.SAYS;

import static com.example.bridge.bidding.SAYS.SAYS.PAIR_GAME;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Conventions.Gerber;
import com.example.bridge.bidding.Conventions.StaymanBidder;
import com.example.bridge.bidding.Conventions.TransferBidder;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Bidder;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.NoTrumpDescription;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class NoTrumpSAYS extends Bidder {

    public static final HandConstraint OPEN = highCardPoints(15, 17);
    public static final HandConstraint OPEN_DONT_ACCEPT_INVITE = highCardPoints(15, 15);
    public static final HandConstraint OPEN_ACCEPT_INVITE = highCardPoints(16, 17);
    public static final HandConstraint LESS_THAT_INVITE = highCardPoints(0, 7);
    public static final HandConstraint INVITE_GAME = highCardPoints(8, 9);
    public static final HandConstraint GAME_OR_BETTER = highCardPoints(10, 40);
    public static final HandConstraint INVITE_SLAM = highCardPoints(16, 17);
    public static final HandConstraint SMALL_SLAM = highCardPoints(18, 19);
    public static final HandConstraint GRAND_SLAM = highCardPoints(20, 40);

    public static class Open1NTDescriptionSAYS extends NoTrumpDescription {
        public Open1NTDescriptionSAYS() {
            openType = "Open1NT";
            OR.open = and(highCardPoints(15, 17), points(15, 18));
            OR.dontAcceptInvite = and(highCardPoints(15, 15), points(15, 16));
            OR.acceptInvite = and(highCardPoints(16, 17), points(16, 18));
            OR.lessThanSuperAccept = and(highCardPoints(15, 16), points(15, 17));
            OR.superAccept = and(highCardPoints(17, 17), points(17, 18));

            RR.lessThanInvite = points(0, 7);
            RR.inviteGame = points(8, 9);
            RR.inviteOrBetter = points(8, 40);
            RR.game = points(10, 15);
            RR.gameOrBetter = points(10, 40);
            RR.gameIfSuperAccept = points(6, 15);
            RR.inviteSlam = points(16, 17);
            RR.smallSlam = points(18, 19);
            RR.grandSlam = points(20, 40);

            RR.inviteAsDummy = dummyPoints(8, 9);
            RR.gameAsDummy = dummyPoints(10, 16);
            RR.smallSlamAsDummy = dummyPoints(17, 20);
            RR.grandSlamAsDummy = dummyPoints(21, 40);
        }
    }

    public static final NoTrumpDescription NT_DESC = new Open1NTDescriptionSAYS();

    public static Iterable<CallFeature> open1NTBid1(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Bid._1NT, OPEN, BALANCED, ruleShow(1), id("NoTrumpSAYS.OneNoTrumpBidderSAYS 1NT")));
        bids.add(partnerBids(NoTrumpSAYS::respond1NTBid1));
        return bids;
    }

    // 1NT -->
    public static PositionCalls respond1NTBid1(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        // Gerber 4C
        choices.addRules(Gerber.initiateConvention(ps));

        // Stayman 2C & Jacoby Transfers 2D/2H
        choices.addRules(StaymanBidder.initiateConvention(NT_DESC).apply(ps));
        choices.addRules(TransferBidder.initiateConvention(NT_DESC).apply(ps));

        choices.addRules(
                shows(Bid._3C, GAME_OR_BETTER, shape(Suit.Clubs, 6, 13), GOOD_PLUS_SUIT, id("NoTrumpSAYS.Natural1NTSAYS 3C")),
                shows(Bid._3D, GAME_OR_BETTER, shape(Suit.Diamonds, 6, 13), GOOD_PLUS_SUIT, id("NoTrumpSAYS.Natural1NTSAYS 3D")),

                shows(Bid._2NT, INVITE_GAME, shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), BALANCED, id("NoTrumpSAYS.Natural1NTSAYS 2NT")),

                shows(Bid._3H, GAME_OR_BETTER, shape(Suit.Hearts, 6, 13), id("NoTrumpSAYS.Natural1NTSAYS 3H")),
                shows(Bid._3S, GAME_OR_BETTER, shape(Suit.Spades, 6, 13), id("NoTrumpSAYS.Natural1NTSAYS 3S")),

                shows(Bid._3NT, GAME_OR_BETTER, shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), BALANCED, id("NoTrumpSAYS.Natural1NTSAYS 3NT")),

                shows(Bid._4NT, INVITE_SLAM, BALANCED, id("NoTrumpSAYS.Natural1NTSAYS 4NT")),

                shows(Bid._6NT, SMALL_SLAM, FLAT, id("NoTrumpSAYS.Natural1NTSAYS 6NT")),
                shows(Bid._6NT, SMALL_SLAM, shape(Suit.Hearts, 2, 3), shape(Suit.Spades, 2, 3), BALANCED, id("NoTrumpSAYS.Natural1NTSAYS 6NT")),

                shows(Call.PASS, LESS_THAT_INVITE, id("NoTrumpSAYS.Natural1NTSAYS PASS")),
                partnerBids(NoTrumpSAYS::open1NTBid2),
                properties(new Call[]{Bid._3H, Bid._3S}, NoTrumpSAYS::open1NTBid2Major)
        );
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls open1NTBid2Major(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        if (ps.getPartner().isPassedHand()) {
            choices.addRules(
                    shows(Bid._4H, fit(), partner(isLastBid(Bid._3H)), setTrumpColor(Suit.Hearts), id("NoTrumpSAYS.openerRebid OPEN_ACCEPT_INVITE 4H")),
                    shows(Bid._4S, fit(), partner(isLastBid(Bid._3S)), setTrumpColor(Suit.Spades), id("NoTrumpSAYS.openerRebid OPEN_ACCEPT_INVITE 4S"))
            );
        } else {
            choices.addRules(AcesAsk.initiateConventionBlok(ps));
        }
        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls open1NTBid2(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Call.PASS, partner(isLastBid(Bid._3NT)), id("NoTrumpSAYS.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2NT)), id("NoTrumpSAYS.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2C)), id("NoTrumpSAYS.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2D)), id("NoTrumpSAYS.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2H)), id("NoTrumpSAYS.openerRebid PASS")),
                shows(Call.PASS, OPEN_DONT_ACCEPT_INVITE, partner(isLastBid(Bid._2S)), id("NoTrumpSAYS.openerRebid PASS")),

                // Super-accept transfers with 17 PC and 4-card fit
                shows(Bid._3H, highCardPoints(17, 17), shape(Suit.Hearts, 4, 5), partner(isLastBid(Bid._2D)), setTrumpColor(Suit.Hearts), id("NoTrumpSAYS.openerRebid 3H SuperAccept")),
                shows(Bid._3S, highCardPoints(17, 17), shape(Suit.Spades, 4, 5), partner(isLastBid(Bid._2H)), setTrumpColor(Suit.Spades), id("NoTrumpSAYS.openerRebid 3S SuperAccept")),

                shows(Bid._4H, OPEN_ACCEPT_INVITE, fit(), partner(isLastBid(Bid._2H)), setTrumpColor(Suit.Hearts), id("NoTrumpSAYS.openerRebid OPEN_ACCEPT_INVITE 4H")),
                shows(Bid._4S, OPEN_ACCEPT_INVITE, fit(), partner(isLastBid(Bid._2S)), setTrumpColor(Suit.Spades), id("NoTrumpSAYS.openerRebid OPEN_ACCEPT_INVITE 4S")),

                shows(Bid._3H, shape(4), partner(isLastBid(Bid._2NT)), id("NoTrumpSAYS.openerRebid shape 3H)")),
                shows(Bid._3S, shape(4), partner(isLastBid(Bid._2NT)), id("NoTrumpSAYS.openerRebid shape 3S")),

                shows(Bid._3NT, pairHighCardPoints(PAIR_GAME), PAIR_BALANCED, partner(isLastBid(Bid._3C, Bid._3D, Bid._2NT)), id("NoTrumpSAYS.openerRebid PAIR_BALANCED 3C 3NT")),
                partnerBids(NoTrumpSAYS::respond1NTBid2)
        );

        choices.addRules(
                shows(Bid._4D, fit(), partner(isLastBid(Bid._3D)), setTrumpColor(Suit.Diamonds), id("NoTrumpSAYS.openerRebid 4D")),
                shows(Bid._4C, fit(), partner(isLastBid(Bid._3C)), setTrumpColor(Suit.Clubs), id("NoTrumpSAYS.openerRebid 4C")),
                propertiesAgreeTrump(new Call[]{Bid._4C, Bid._4D}, NoTrumpSAYS::respond1NTBid2inviteMinor, true)
        );

        choices.addRules(
                shows(Bid._2H, shape(4), noFit(), partner(isLastBid(Bid._2C)), id("NoTrumpSAYS.openerRebid 2H")),
                shows(Bid._2H, shape(4), noFit(), partner(isLastBid(Bid._2D)), id("NoTrumpSAYS.openerRebid 2H")),
                shows(Bid._2S, shape(4), noFit(), partner(isLastBid(Bid._2C)), id("NoTrumpSAYS.openerRebid 2S")),
                shows(Bid._2S, shape(4), noFit(), partner(isLastBid(Bid._2D)), id("NoTrumpSAYS.openerRebid 2S"))
        );

        choices.addRules(CompeteSAYS.compBids(ps));
        return choices;
    }

    public static PositionCalls respond1NTBid2(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Bid._3NT, shape(Suit.Hearts, 0, 2), partner(isLastBid(Bid._3H)), id("NoTrumpSAYS.responderRebid 3NT")),
                shows(Bid._3NT, shape(Suit.Spades, 0, 2), partner(isLastBid(Bid._3S)), id("NoTrumpSAYS.responderRebid 3NT")),

                shows(Bid._4H, partner(isLastBid(Bid._3H)), fit(), setTrumpColor(Suit.Hearts), id("NoTrumpSAYS.responderRebid 4H")),
                shows(Bid._4S, partner(isLastBid(Bid._3S)), fit(), setTrumpColor(Suit.Spades), id("NoTrumpSAYS.responderRebid 4S")),

                shows(Call.PASS, id("NoTrumpSAYS.responderRebid Pass"))
        );
        choices.addRules(CompeteSAYS::compBids);
        return choices;
    }

    public static PositionCalls respond1NTBid2inviteMinor(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(AcesAsk.initiateConventionBlok(ps));
        choices.addRules(
                shows(Bid._5C, partner(isLastBid(Bid._4C)), setTrumpColor(Suit.Clubs), id("NoTrumpSAYS.inviteMinor 5C")),
                shows(Bid._5D, partner(isLastBid(Bid._4D)), setTrumpColor(Suit.Diamonds), id("NoTrumpSAYS.inviteMinor 5D"))
        );
        choices.addRules(CompeteSAYS::compBids);
        return choices;
    }
}
