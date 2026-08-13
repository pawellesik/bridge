package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Conventions.AcesAsk;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Bidder;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.CallFeaturesFactory;
import com.example.bridge.bidding.Tools.NoTrumpDescription;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionCallsFactory;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class NoTrumpNatC extends Bidder {

    public static class UserText {
        public static final String OneNoTrumpRange = "15 to 17";
    }

    public static class Open1NTDescription extends NoTrumpDescription {
        public Open1NTDescription() {
            openType = "Open1NT";
            OR.open = highCardPoints(15, 17);
            OR.dontAcceptInvite = highCardPoints(15, 15);
            OR.acceptInvite = highCardPoints(16, 17);

            RR.lessThanInvite = highCardPoints(0, 7);
            RR.inviteGame = highCardPoints(8, 9);
            RR.gameOrBetter = highCardPoints(10, 40);
            RR.inviteSlam = highCardPoints(16, 17);
            RR.smallSlam = highCardPoints(18, 19);
            RR.grandSlam = highCardPoints(20, 40);
        }
    }


    public static class OneNoTrumpBidderNatC extends Bidder {
        public static final OneNoTrumpBidderNatC OPEN = new OneNoTrumpBidderNatC(new Open1NTDescription());

        protected final NoTrumpDescription ntd;

        protected OneNoTrumpBidderNatC(NoTrumpDescription ntd) {
            this.ntd = ntd;
        }

        public static Iterable<CallFeature> open(PositionState ps) {
            return OPEN.bids(ps);
        }

        public Iterable<CallFeature> bids(PositionState ps) {
            List<CallFeature> bids = new ArrayList<>();
            bids.add(properties(Bid._1NT, (PositionCallsFactory) this::conventionalResponses, false, UserText.OneNoTrumpRange, true));
            bids.add(shows(Bid._1NT, ntd.OR.open, BALANCED, shape(Suit.Spades,2,4), shape(Suit.Hearts,2,4), id("NoTrumpNatC.OneNoTrumpBidderNatC 1NT")));
            return bids;
        }

        protected PositionCalls conventionalResponses(PositionState ps) {
            PositionCalls choices = new PositionCalls(ps);
            choices.addRules(AcesAsk.initiateConvention(ps),
                    Natural1NTNatC.respond(ntd));
            return choices;
        }
    }

    public static class Natural1NTNatC extends OneNoTrumpBidderNatC {
        public Natural1NTNatC(NoTrumpDescription ntd) {
            super(ntd);
        }

        public static CallFeaturesFactory respond(NoTrumpDescription ntd) {
            return new Natural1NTNatC(ntd)::naturalResponse;
        }

        private Iterable<CallFeature> naturalResponse(PositionState ps) {
            List<CallFeature> bids = new ArrayList<>();

            bids.add(partnerBids((PositionCallsFactory) this::openerRebid));

            bids.add(shows(Bid._2C, GOOD_PLUS_SUIT, shape(5, 11), ntd.RR.inviteGame, id("NoTrumpNatC.Natural1NTNatC 2C")));
            bids.add(shows(Bid._2D, GOOD_PLUS_SUIT, shape(5, 11), ntd.RR.inviteGame, id("NoTrumpNatC.Natural1NTNatC 2D")));

            bids.add(shows(Bid._3C, GOOD_PLUS_SUIT, shape(5, 11), ntd.RR.gameOrBetter, id("NoTrumpNatC.Natural1NTNatC 3C")));
            bids.add(shows(Bid._3D, GOOD_PLUS_SUIT, shape(5, 11), ntd.RR.gameOrBetter, id("NoTrumpNatC.Natural1NTNatC 3D")));

            bids.add(shows(Bid._2H, DECENT_PLUS_SUIT, shape(7, 11), ntd.RR.lessThanInvite, id("NoTrumpNatC.Natural1NTNatC 2H")));
            bids.add(shows(Bid._2S, DECENT_PLUS_SUIT, shape(7, 11), ntd.RR.lessThanInvite, id("NoTrumpNatC.Natural1NTNatC 2S")));

            bids.add(shows(Bid._2H, DECENT_PLUS_SUIT, shape(5, 11), ntd.RR.inviteGame, id("NoTrumpNatC.Natural1NTNatC 2H")));
            bids.add(shows(Bid._2S, DECENT_PLUS_SUIT, shape(5, 11), ntd.RR.inviteGame, id("NoTrumpNatC.Natural1NTNatC 2S")));

            bids.add(shows(Bid._2NT, ntd.RR.inviteGame, longestMajor(4), id("NoTrumpNatC.Natural1NTNatC 2NT")));

            bids.add(properties(Bid._3H, true));
            bids.add(properties(Bid._3S, true));
            bids.add(shows(Bid._3H, ntd.RR.gameOrBetter, shape(5, 11), id("NoTrumpNatC.Natural1NTNatC 3H")));
            bids.add(shows(Bid._3S, ntd.RR.gameOrBetter, shape(5, 11), id("NoTrumpNatC.Natural1NTNatC 3S")));

            bids.add(shows(Bid._3NT, ntd.RR.gameOrBetter, longestMajor(4), id("NoTrumpNatC.Natural1NTNatC 3NT")));

            bids.add(shows(Bid._6NT, FLAT, ntd.RR.smallSlam, id("NoTrumpNatC.Natural1NTNatC 6NT")));
            bids.add(shows(Bid._6NT, BALANCED, shape(Suit.Hearts, 2, 3), shape(Suit.Spades, 2, 3), ntd.RR.smallSlam, id("NoTrumpNatC.Natural1NTNatC 6NT")));

            bids.add(shows(Call.PASS, ntd.RR.lessThanInvite, id("NoTrumpNatC.Natural1NTNatC PASS")));
            return bids;
        }

        private PositionCalls openerRebid(PositionState ps) {
            PositionCalls choices = new PositionCalls(ps);

            choices.addRules(partnerBids((CallFeaturesFactory) this::responderRebid));
            choices.addRules(shows(Call.PASS, partner(isLastBid(Bid._3NT)), id("NoTrumpNatC.openerRebid PASS")));
            choices.addRules(shows(Call.PASS, ntd.OR.dontAcceptInvite, partner(isLastBid(Bid._2NT)), id("NoTrumpNatC.openerRebid PASS")));
            choices.addRules(shows(Call.PASS, ntd.OR.dontAcceptInvite, partner(isLastBid(Bid._2C)), id("NoTrumpNatC.openerRebid PASS")));
            choices.addRules(shows(Call.PASS, ntd.OR.dontAcceptInvite, partner(isLastBid(Bid._2D)), id("NoTrumpNatC.openerRebid PASS")));
            choices.addRules(shows(Call.PASS, ntd.OR.dontAcceptInvite, partner(isLastBid(Bid._2H)), id("NoTrumpNatC.openerRebid PASS")));
            choices.addRules(shows(Call.PASS, ntd.OR.dontAcceptInvite, partner(isLastBid(Bid._2S)), id("NoTrumpNatC.openerRebid PASS")));

            choices.addRules(shows(Bid._4H, ntd.OR.acceptInvite, partner(isLastBid(Bid._2H)), shape(3, 5)), id("NoTrumpNatC.openerRebid 4H"));
            choices.addRules(shows(Bid._4S, ntd.OR.acceptInvite, partner(isLastBid(Bid._2S)), shape(3, 5)), id("NoTrumpNatC.openerRebid 4S"));

            choices.addRules(
                    propertiesAgreeTrump(new Call[]{Bid._4C, Bid._4D}, this::inviteMinor, true),
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
                    shows(Bid._3H, partner(isLastBid(Bid._2NT)), ntd.OR.acceptInvite, shape(5, 10), id("NoTrumpNatC.openerRebid 3H")));

            choices.addRules(properties(Bid._3S, true),
                    shows(Bid._3S, partner(isLastBid(Bid._2NT)), ntd.OR.acceptInvite, shape(5, 10), id("NoTrumpNatC.openerRebid 3S")));

            choices.addRules(shows(Bid._3NT, ntd.OR.acceptInvite, partner(isLastBid(Bid._2NT)), id("NoTrumpNatC.openerRebid 3NT")));
            choices.addRules(shows(Bid._3NT, partner(isLastBid(Bid._3H)), shape(Suit.Hearts, 0, 2)), id("NoTrumpNatC.openerRebid 3NT"));
            choices.addRules(shows(Bid._3NT, partner(isLastBid(Bid._3S)), shape(Suit.Spades, 0, 2)), id("NoTrumpNatC.openerRebid 3NT"));
            choices.addRules(shows(Bid._3NT, partner(isLastBid(Bid._3C)), shape(Suit.Clubs, 0, 2)), id("NoTrumpNatC.openerRebid 3NT"));
            choices.addRules(shows(Bid._3NT, partner(isLastBid(Bid._3D)), shape(Suit.Diamonds, 0, 2)), id("NoTrumpNatC.openerRebid 3NT"));

            choices.addRules(shows(Bid._4H, partner(isLastBid(Bid._3H)), shape(3, 5)), id("NoTrumpNatC.openerRebid 4H"));
            choices.addRules(shows(Bid._4S, partner(isLastBid(Bid._3S)), shape(3, 5)), id("NoTrumpNatC.openerRebid 4S"));
            return choices;
        }

        private Iterable<CallFeature> responderRebid(PositionState ps) {
            List<CallFeature> bids = new ArrayList<>();
            bids.add(shows(Bid._3NT, partner(isLastBid(Bid._3H)), shape(Suit.Hearts, 0, 2), id("NoTrumpNatC.responderRebid 3NT")));
            bids.add(shows(Bid._3NT, partner(isLastBid(Bid._3S)), shape(Suit.Spades, 0, 2), id("NoTrumpNatC.responderRebid 3NT")));

            bids.add(shows(Bid._4H, partner(isLastBid(Bid._3H)), shape(3, 4), id("NoTrumpNatC.responderRebid 4H")));
            bids.add(shows(Bid._4S, partner(isLastBid(Bid._3S)), shape(3, 4), id("NoTrumpNatC.responderRebid 4S")));

            bids.add(shows(Call.PASS, id("NoTrumpNatC.responderRebid Pass")));
            return bids;
        }

        public PositionCalls inviteMinor(PositionState ps) {
            PositionCalls choices = new PositionCalls(ps);
            choices.addRules(AcesAsk.initiateConvention(ps));
            choices.addRules(AcesAsk.initiateConventionBlok(ps));
            choices.addRules(
                    shows(Bid._5C, partner(isLastBid(Bid._4C)), id("NoTrumpNatC.inviteMinor 5C")),
                    shows(Bid._5D, partner(isLastBid(Bid._4D)), id("NoTrumpNatC.inviteMinor 5D"))
            );
            choices.addRules(CompeteNatC::compBids);
            return choices;
        }
    }

    public static Iterable<CallFeature> open(PositionState ps) {
        return OneNoTrumpBidderNatC.open(ps);
    }
}