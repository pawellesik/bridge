package com.example.bridge.bidding.SAYS;

import com.example.bridge.bidding.Conventions.Strong2Clubs;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class OpenBid1SAYS extends SAYS {

    public static final HandConstraint OpenBidding = highCardPoints(13, 21);
    public static final HandConstraint OpeningInviteBidding = highCardPoints(14, 17);
    public static final HandConstraint OpeningLowBidding = highCardPoints(13, 14);
    public static final HandConstraint OpeningWeakBidding = highCardPoints(5, 11);
    public static final HandConstraint OpeningStrongBidding = highCardPoints(22, 40);
    public static final HandConstraint OpenBiddingThirtSeat = highCardPoints(11, 21);
    public static final HandConstraint DontOpen = highCardPoints(0, 11);

    public static PositionCalls getOpenPositionCalls(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(SolidSuitSAYS.BIDS(ps));
        choices.addRules(NoTrumpSAYS.open1NTBid1(ps));
        choices.addRules(Strong2Clubs.open(ps));
        choices.addRules(openSuitWeak(ps));
        choices.addRules(openSuit(ps));

        return choices;
    }

    public static Iterable<CallFeature> openSuit(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();

        if (ps.getSeat() < 3) {
            // Equal 5-5 suits: higher suit opened first
            // 1S (5+ spades)
            bids.add(shows(Bid._1S, OpenBidding, shape(Suit.Spades, 5, 13), id("OpenSAYS.openSuit _1S")));
            // 1H (5+ hearts, exclude 5+ spades if 5-5 since 1S takes precedence)
            bids.add(shows(Bid._1H, OpenBidding, shape(Suit.Hearts, 5, 13), shape(Suit.Spades, 0, 4), id("OpenSAYS.openSuit _1H")));
            // 1D (4+ diamonds, or 4-4-3-2 with 3 diamonds)
            bids.add(shows(Bid._1D, OpenBidding, shape(Suit.Diamonds, 4, 13), shape(Suit.Hearts, 0, 4), shape(Suit.Spades, 0, 4), id("OpenSAYS.openSuit _1D")));
            bids.add(shows(Bid._1D, OpenBidding, shape(Suit.Diamonds, 3, 3), shape(Suit.Clubs, 4, 4), shape(Suit.Hearts, 2, 4), shape(Suit.Spades, 2, 4), id("OpenSAYS.openSuit _1D_4432")));
            // 1C (3+ clubs)
            bids.add(shows(Bid._1C, OpenBidding, shape(Suit.Clubs, 3, 13), shape(Suit.Hearts, 0, 4), shape(Suit.Spades, 0, 4), id("OpenSAYS.openSuit _1C")));
        } else {
            // 3rd and 4th seat: 11-21 PC
            bids.add(shows(Bid._1S, OpenBiddingThirtSeat, shape(Suit.Spades, 5, 13), id("OpenSAYS.openSuit _1S")));
            bids.add(shows(Bid._1H, OpenBiddingThirtSeat, shape(Suit.Hearts, 5, 13), shape(Suit.Spades, 0, 4), id("OpenSAYS.openSuit _1H")));
            bids.add(shows(Bid._1D, OpenBiddingThirtSeat, shape(Suit.Diamonds, 4, 13), shape(Suit.Hearts, 0, 4), shape(Suit.Spades, 0, 4), id("OpenSAYS.openSuit _1D")));
            bids.add(shows(Bid._1D, OpenBiddingThirtSeat, shape(Suit.Diamonds, 3, 3), shape(Suit.Clubs, 4, 4), shape(Suit.Hearts, 2, 4), shape(Suit.Spades, 2, 4), id("OpenSAYS.openSuit _1D_4432")));
            bids.add(shows(Bid._1C, OpenBiddingThirtSeat, shape(Suit.Clubs, 3, 13), shape(Suit.Hearts, 0, 4), shape(Suit.Spades, 0, 4), id("OpenSAYS.openSuit _1C")));
        }
        bids.add(shows(Call.PASS, DontOpen, id("OpenSAYS.openSuit _PASS")));

        bids.add(partnerBids(Bid._1S, RespondBid1SAYS::oneSpade));
        bids.add(partnerBids(Bid._1H, RespondBid1SAYS::oneHeart));
        bids.add(partnerBids(Bid._1D, RespondBid1SAYS::oneDiamond));
        bids.add(partnerBids(Bid._1C, RespondBid1SAYS::oneClub));

        return bids;
    }

    private static List<CallFeature> openSuitWeak(PositionState ps) {
        List<CallFeature> rules = new ArrayList<>();
        // Weak 2 openings: 2D, 2H, 2S (5-11 PC, good 6-card suit)
        rules.add(shows(Bid._2D, OpeningWeakBidding, shape(Suit.Diamonds, 6, 13), shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3), GOOD_PLUS_SUIT, id("OpenSAYS.openSuitWeak _2D")));
        rules.add(shows(Bid._2H, OpeningWeakBidding, shape(Suit.Hearts, 6, 13), shape(Suit.Spades, 0, 3), GOOD_PLUS_SUIT, id("OpenSAYS.openSuitWeak _2H")));
        rules.add(shows(Bid._2S, OpeningWeakBidding, shape(Suit.Spades, 6, 13), shape(Suit.Hearts, 0, 3), GOOD_PLUS_SUIT, id("OpenSAYS.openSuitWeak _2S")));

        // Preempts 3-level: 3C, 3D, 3H, 3S (5-11 PC, good 7+ card suit)
        rules.add(shows(Bid._3S, OpeningWeakBidding, shape(Suit.Spades, 7, 13), GOOD_PLUS_SUIT, id("OpenSAYS.openSuitWeak _3S")));
        rules.add(shows(Bid._3H, OpeningWeakBidding, shape(Suit.Hearts, 7, 13), GOOD_PLUS_SUIT, id("OpenSAYS.openSuitWeak _3H")));
        rules.add(shows(Bid._3D, OpeningWeakBidding, shape(Suit.Diamonds, 7, 13), GOOD_PLUS_SUIT, id("OpenSAYS.openSuitWeak _3D")));
        rules.add(shows(Bid._3C, OpeningWeakBidding, shape(Suit.Clubs, 7, 13), GOOD_PLUS_SUIT, id("OpenSAYS.openSuitWeak _3C")));

        // 3NT: 25-27 PC balanced
        rules.add(shows(Bid._3NT, highCardPoints(25, 27), BALANCED, id("OpenSAYS.open3NT")));

        // Preempts 4/5-level: 4H, 4S, 5C, 5D (5-11 PC, 8+ card suit)
        rules.add(shows(Bid._4H, OpeningWeakBidding, shape(Suit.Hearts, 8, 13), GOOD_PLUS_SUIT, id("OpenSAYS.openSuitWeak _4H")));
        rules.add(shows(Bid._4S, OpeningWeakBidding, shape(Suit.Spades, 8, 13), GOOD_PLUS_SUIT, id("OpenSAYS.openSuitWeak _4S")));
        rules.add(shows(Bid._5C, OpeningWeakBidding, shape(Suit.Clubs, 8, 13), GOOD_PLUS_SUIT, id("OpenSAYS.openSuitWeak _5C")));
        rules.add(shows(Bid._5D, OpeningWeakBidding, shape(Suit.Diamonds, 8, 13), GOOD_PLUS_SUIT, id("OpenSAYS.openSuitWeak _5D")));

        rules.add(partnerBids(RespondBid1SAYS::weakOpen));
        return rules;
    }

}
