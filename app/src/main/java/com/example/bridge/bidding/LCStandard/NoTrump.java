package com.example.bridge.bidding.LCStandard;

import com.example.bridge.bidding.Conventions.Gerber;
import com.example.bridge.bidding.Conventions.StaymanBidder;
import com.example.bridge.bidding.Conventions.TransferBidder;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Bidder;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.CallFeaturesFactory;
import com.example.bridge.bidding.Tools.NoTrumpDescription;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionCallsFactory;
import com.example.bridge.bidding.Tools.PositionRole;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class NoTrump extends Bidder {

    public static class Open1NTDescription extends NoTrumpDescription {
        public Open1NTDescription() {
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

    public static class Overcall1NTDescription extends NoTrumpDescription {
        public Overcall1NTDescription() {
            openType = "Overcall1NT";
            OR.open = and(highCardPoints(15, 18), points(15, 19));
            OR.dontAcceptInvite = and(highCardPoints(15, 15), points(15, 16));
            OR.acceptInvite = and(highCardPoints(16, 18), points(16, 19));
            OR.lessThanSuperAccept = and(highCardPoints(15, 16), points(15, 17));
            OR.superAccept = and(highCardPoints(18, 19), points(18, 20));

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

    public static class Balancing1NTDescription extends NoTrumpDescription {
        public Balancing1NTDescription() {
            openType = "Balancing1NT";
            OR.open = and(highCardPoints(13, 15), points(13, 16));
            OR.dontAcceptInvite = and(highCardPoints(13, 14), points(13, 15));
            OR.acceptInvite = and(highCardPoints(15, 15), points(15, 16));
            OR.lessThanSuperAccept = highCardPoints(13, 15);
            OR.superAccept = highCardPoints(40, 40);

            RR.lessThanInvite = points(0, 10);
            RR.inviteGame = points(11, 12);
            RR.inviteOrBetter = points(11, 40);
            RR.game = points(13, 15);
            RR.gameOrBetter = points(10, 40);
            RR.gameIfSuperAccept = points(40, 40);
            RR.inviteSlam = points(40, 40);
            RR.smallSlam = points(40, 40);
            RR.grandSlam = points(40, 40);

            RR.gameAsDummy = dummyPoints(13, 15);
            RR.inviteAsDummy = dummyPoints(11, 12);
            RR.smallSlamAsDummy = dummyPoints(40, 40);
            RR.grandSlamAsDummy = dummyPoints(40, 40);
        }
    }

    public static class OneNoTrumpBidder extends Bidder {
        public static final OneNoTrumpBidder OPEN = new OneNoTrumpBidder(new Open1NTDescription());
        public static final OneNoTrumpBidder OVERCALL = new OneNoTrumpBidder(new Overcall1NTDescription());
        public static final OneNoTrumpBidder BALANCING = new OneNoTrumpBidder(new Balancing1NTDescription());

        protected final NoTrumpDescription ntd;

        protected OneNoTrumpBidder(NoTrumpDescription ntd) {
            this.ntd = ntd;
        }

        public Iterable<CallFeature> bids(PositionState ps) {
            List<CallFeature> bids = new ArrayList<>();
            if (ps.getRole() == PositionRole.Opener && ps.getRoleRound() == 1) {
                bids.add(properties(Bid._1NT, (PositionCallsFactory) this::conventionalResponses, false, UserText.OneNoTrumpRange, true));
                bids.add(shows(Bid._1NT, ntd.OR.open, BALANCED));
            } else if (ps.getRole() == PositionRole.Overcaller && ps.getRoleRound() == 1) {
                if (ps.getBiddingState().getContract().isPassEndsAuction() && ntd.openType.equals("Balancing1NT")) {
                    bids.add(partnerBids(Bid._1NT, (PositionCallsFactory) this::conventionalResponses));
                    bids.add(shows(Bid._1NT, ntd.OR.open, IS_FINAL_CALL));
                } else if (ntd.openType.equals("Overcall1NT")) {
                    bids.add(partnerBids(Bid._1NT, (PositionCallsFactory) this::conventionalResponses));
                    bids.add(shows(Bid._1NT, ntd.OR.open, BALANCED, OPPS_STOPPED, IS_NOT_FINAL_CALL));
                }
            }
            return bids;
        }

        private PositionCalls conventionalResponses(PositionState ps) {
            if (ps.getRHO().getLastCall() instanceof Bid && !ps.getRHO().getLastCall().equals(Bid._2C)) {
                return ps.getPairState().getBiddingSystem().getPositionCalls(ps);
            }
            PositionCalls choices = new PositionCalls(ps);
            choices.addRules(StaymanBidder.initiateConvention(ntd));
            choices.addRules(TransferBidder.initiateConvention(ntd));
            choices.addRules(Gerber::initiateConvention);
            choices.addRules(Natural1NT.respond(ntd));
            return choices;
        }
    }

    public static class Natural1NT extends OneNoTrumpBidder {
        public Natural1NT(NoTrumpDescription ntd) {
            super(ntd);
        }

        public static CallFeaturesFactory respond(NoTrumpDescription ntd) {
            return new Natural1NT(ntd)::naturalResponse;
        }

        private Iterable<CallFeature> naturalResponse(PositionState ps) {
            List<CallFeature> bids = new ArrayList<>();
            bids.add(partnerBids((PositionCallsFactory) this::openerRebid));
            bids.add(partnerBids(Bid._4NT, Compete::compBids));

            if (!ntd.openType.equals("Balancing1NT")) {
                bids.add(shows(Bid._2C, shape(5, 11), ntd.RR.lessThanInvite));
                bids.add(shows(Bid._2D, shape(5, 11), ntd.RR.lessThanInvite));
                bids.add(shows(Bid._2H, shape(5, 11), ntd.RR.lessThanInvite));
                bids.add(shows(Bid._2S, shape(5, 11), ntd.RR.lessThanInvite));
            }

            bids.add(shows(Bid._2NT, ntd.RR.inviteGame, longestMajor(4)));

            bids.add(properties(Bid._3H, true));
            bids.add(properties(Bid._3S, true));
            bids.add(shows(Bid._3H, ntd.RR.gameOrBetter, shape(5, 11)));
            bids.add(shows(Bid._3S, ntd.RR.gameOrBetter, shape(5, 11)));

            bids.add(shows(Bid._3NT, ntd.RR.game, longestMajor(4)));

            bids.add(shows(Bid._4NT, ntd.RR.inviteSlam));

            bids.add(shows(Bid._6NT, FLAT, ntd.RR.smallSlam));
            bids.add(shows(Bid._6NT, BALANCED, shape(Suit.Hearts, 2, 3), shape(Suit.Spades, 2, 3), ntd.RR.smallSlam));

            bids.add(shows(Call.PASS, ntd.RR.lessThanInvite));
            return bids;
        }

        private PositionCalls openerRebid(PositionState ps) {
            PositionCalls choices = new PositionCalls(ps);
            choices.addRules(
                partnerBids((CallFeaturesFactory) this::responderRebid),

                shows(Bid._3H, partner(isLastBid(Bid._2NT)), ntd.OR.acceptInvite, shape(5)),
                shows(Bid._3S, partner(isLastBid(Bid._2NT)), ntd.OR.acceptInvite, shape(5)),

                shows(Bid._3NT, ntd.OR.acceptInvite, partner(isLastBid(Bid._2NT))),

                shows(Call.PASS, ntd.OR.dontAcceptInvite, partner(isLastBid(Bid._2NT))),
                shows(Call.PASS, partner(isLastBid(Bid._3NT))),
                
                shows(Bid._3NT, partner(isLastBid(Bid._3H)), shape(Suit.Hearts, 0, 2)),
                shows(Bid._3NT, partner(isLastBid(Bid._3S)), shape(Suit.Spades, 0, 2)),

                shows(Bid._4H, partner(isLastBid(Bid._3H)), shape(3, 5)),
                shows(Bid._4S, partner(isLastBid(Bid._3S)), shape(3, 5)),

                shows(Call.PASS)
            );
            return choices;
        }

        private Iterable<CallFeature> responderRebid(PositionState ps) {
            List<CallFeature> bids = new ArrayList<>();
            bids.add(shows(Bid._3NT, partner(isLastBid(Bid._3H)), shape(Suit.Hearts, 0, 2)));
            bids.add(shows(Bid._3NT, partner(isLastBid(Bid._3S)), shape(Suit.Spades, 0, 2)));

            bids.add(shows(Bid._4H, partner(isLastBid(Bid._3H)), shape(3, 4)));
            bids.add(shows(Bid._4S, partner(isLastBid(Bid._3S)), shape(3, 4)));

            bids.add(shows(Call.PASS));
            return bids;
        }
    }

    public static Iterable<CallFeature> open(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        for (CallFeature cf : OneNoTrumpBidder.OPEN.bids(ps)) {
            bids.add(cf);
        }
        for (CallFeature cf : TwoNoTrump.OPEN.bids(ps)) {
            bids.add(cf);
        }
        for (CallFeature cf : ThreeNoTrump.OPEN.bids(ps)) {
            bids.add(cf);
        }
        return bids;
    }

    public static Iterable<CallFeature> strongOvercall(PositionState ps) {
        return OneNoTrumpBidder.OVERCALL.bids(ps);
    }

    public static Iterable<CallFeature> balancingOvercall(PositionState ps) {
        return OneNoTrumpBidder.BALANCING.bids(ps);
    }
}




























































































































































































































































































































































































































































































