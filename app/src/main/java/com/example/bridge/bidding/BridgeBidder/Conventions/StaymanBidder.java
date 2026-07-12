package com.example.bridge.bidding.BridgeBidder.Conventions;

import com.example.licytacja.moje.BridgeBidder.*;
import com.example.licytacja.moje.BridgeBidder.LCStandard.NoTrump;
import com.example.licytacja.moje.BridgeBidder.LCStandard.UserText;
import java.util.ArrayList;
import java.util.List;

public class StaymanBidder extends Bidder {
    protected final NoTrumpDescription ntd;

    public StaymanBidder(NoTrumpDescription ntd) {
        this.ntd = ntd;
    }

    public static CallFeaturesFactory initiateConvention(NoTrumpDescription ntd) {
        return new StaymanBidder(ntd)::initiate;
    }

    private Iterable<CallFeature> initiate(PositionState ps) {
        Call call = Bid._2C;
        if (ps.getRHO().getBid() != null) {
            Bid rhoBid = ps.getRHO().getBid();
            if (call.equals(rhoBid)) {
                call = Call.DOUBLE;
            }
        }
        List<CallFeature> bids = new ArrayList<>();
        bids.add(convention(call, UserText.Stayman));
        bids.add(properties(call, (PositionCallsFactory) this::answer, true));

        bids.add(shows(call, ntd.RR.inviteOrBetter, shape(Suit.Hearts, 4), shape(Suit.Spades, 0, 4), NOT_FLAT));
        bids.add(shows(call, ntd.RR.inviteOrBetter, shape(Suit.Spades, 4), shape(Suit.Hearts, 0, 4), NOT_FLAT));
        bids.add(shows(call, ntd.RR.inviteOrBetter, shape(Suit.Hearts, 4), shape(Suit.Spades, 5)));
        bids.add(shows(call, ntd.RR.inviteOrBetter, shape(Suit.Hearts, 5), shape(Suit.Spades, 4)));
        
        bids.add(shows(call, ntd.RR.lessThanInvite, shape(Suit.Diamonds, 4, 5), shape(Suit.Hearts, 4), shape(Suit.Spades, 4)));
        return bids;
    }

    public PositionCalls answer(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            partnerBids(Bid._2D, (PositionCallsFactory) this::respondTo2D),
            partnerBids(Bid._2H, (PositionCallsFactory) (p -> respondTo2M(p, Suit.Hearts))),
            partnerBids(Bid._2S, (PositionCallsFactory) (p -> respondTo2M(p, Suit.Spades))),

            shows(Bid._2D, shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3)),
            shows(Bid._2H, shape(4, 5), longerOrEqualTo(Suit.Spades)),
            shows(Bid._2S, shape(4, 5), longerThan(Suit.Hearts))
        );
        return choices;
    }

    public PositionCalls respondTo2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            shows(Call.PASS, ntd.RR.lessThanInvite),
            properties(Bid._3H, (PositionCallsFactory) (p -> gameNewMajor(p, Suit.Hearts)), true),
            properties(Bid._3S, (PositionCallsFactory) (p -> gameNewMajor(p, Suit.Spades)), true),
            shows(Bid._3S, ntd.RR.gameOrBetter, shape(5)),
            shows(Bid._3H, ntd.RR.gameOrBetter, shape(5)),
            partnerBids(Bid._2H, (PositionCallsFactory) (p -> placeContractNewMajor(p, Suit.Hearts))),
            partnerBids(Bid._2S, (PositionCallsFactory) (p -> placeContractNewMajor(p, Suit.Spades))),
            shows(Bid._2H, ntd.RR.inviteGame, shape(5)),
            shows(Bid._2S, ntd.RR.inviteGame, shape(5)),
            properties(Bid._2NT, (PositionCallsFactory) this::placeContract2NTInvite),
            shows(Bid._2NT, ntd.RR.inviteGame),
            shows(Bid._3NT, ntd.RR.game),
            shows(Bid._4NT, pairPoints(30, 31)),
            Gerber.initiateConvention(ps)
        );
        return choices;
    }

    public PositionCalls respondTo2M(PositionState ps, Suit major) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            shows(Call.PASS, ntd.RR.lessThanInvite),
            shows(new Bid(7, major), shape(4, 5), ntd.RR.grandSlamAsDummy),
            shows(new Bid(6, major), shape(4, 5), ntd.RR.smallSlamAsDummy),
            shows(new Bid(4, major), shape(4, 5), ntd.RR.gameAsDummy),
            properties(new Bid(3, major), (PositionCallsFactory) (p -> placeContractMajorInvite(p, major))),
            shows(new Bid(3, major), shape(4, 5), ntd.RR.inviteAsDummy),
            partnerBids(Bid._3NT, (PositionCallsFactory) this::checkOpenerSpadeGame),
            shows(Bid._3NT, ntd.RR.game, shape(major, 0, 3), shape(major == Suit.Hearts ? Suit.Spades : Suit.Hearts, 4)),
            partnerBids(Bid._2NT, (PositionCallsFactory) this::placeContract2NTInvite),
            shows(Bid._2NT, ntd.RR.inviteGame, shape(major, 0, 3), (major == Suit.Hearts ? shape(Suit.Spades, 4) : shape(Suit.Hearts, 4)))
        );
        return choices;
    }

    public PositionCalls checkOpenerSpadeGame(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            shows(Bid._4S, FIT_8_PLUS),
            shows(Call.PASS)
        );
        return choices;
    }

    public PositionCalls gameNewMajor(PositionState ps, Suit major) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            shows(new Bid(4, major), FIT_8_PLUS),
            shows(Bid._3NT)
        );
        return choices;
    }

    public PositionCalls placeContractNewMajor(PositionState ps, Suit major) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            shows(Call.PASS, ntd.OR.dontAcceptInvite, fit(major)),
            shows(Bid._2NT, ntd.OR.dontAcceptInvite),
            shows(new Bid(4, major), FIT_8_PLUS, ntd.OR.acceptInvite),
            shows(Bid._3NT, ntd.OR.acceptInvite)
        );
        return choices;
    }

    public PositionCalls placeContract2NTInvite(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            properties(Bid._3S, (PositionCallsFactory) this::checkSpadeGame),
            shows(Bid._3S, ntd.OR.dontAcceptInvite, FIT_8_PLUS),
            shows(Bid._4S, ntd.OR.acceptInvite, FIT_8_PLUS),
            shows(Bid._3NT, ntd.OR.acceptInvite),
            shows(Call.PASS, ntd.OR.dontAcceptInvite)
        );
        return choices;
    }

    public PositionCalls placeContractMajorInvite(PositionState ps, Suit major) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            shows(new Bid(4, major), ntd.OR.acceptInvite, FIT_8_PLUS),
            shows(Call.PASS, ntd.OR.dontAcceptInvite)
        );
        return choices;
    }

    public PositionCalls checkSpadeGame(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            shows(Bid._4S, ntd.RR.gameAsDummy, shape(4, 5)),
            shows(Call.PASS)
        );
        return choices;
    }

    public static class Stayman2NT extends Bidder {
        private final com.example.licytacja.moje.BridgeBidder.LCStandard.TwoNoTrump ntb;

        public Stayman2NT(com.example.licytacja.moje.BridgeBidder.LCStandard.TwoNoTrump ntb) {
            this.ntb = ntb;
        }

        public Iterable<CallFeature> initiateConvention(PositionState ps) {
            Call call = Bid._3C;
            if (ps.getRHO().getBid() != null && ps.getRHO().getBid().equals(call)) {
                call = Call.DOUBLE;
            }
            List<CallFeature> bids = new ArrayList<>();
            bids.add(properties(call, (PositionCallsFactory) this::answer, true));
            bids.add(shows(call, ntb.respondGame, shape(Suit.Hearts, 4), NOT_FLAT));
            bids.add(shows(call, ntb.respondGame, shape(Suit.Spades, 4), NOT_FLAT));
            bids.add(shows(call, ntb.respondGame, shape(Suit.Hearts, 4), shape(Suit.Spades, 5)));
            bids.add(shows(call, ntb.respondGame, shape(Suit.Hearts, 5), shape(Suit.Spades, 4)));
            return bids;
        }

        public PositionCalls answer(PositionState ps) {
            PositionCalls choices = new PositionCalls(ps);
            choices.addRules(
                partnerBids((CallFeaturesFactory) Stayman2NT::responderRebid),
                shows(Bid._3D, shape(Suit.Hearts, 0, 3), shape(Suit.Spades, 0, 3)),
                shows(Bid._3H, shape(4, 5), longerOrEqualTo(Suit.Spades)),
                shows(Bid._3S, shape(4, 5), longerThan(Suit.Hearts))
            );
            return choices;
        }

        public static Iterable<CallFeature> responderRebid(PositionState ps) {
            List<CallFeature> bids = new ArrayList<>();
            bids.add(properties(new Bid[]{Bid._3H, Bid._3S}, (PositionCallsFactory) Stayman2NT::openerRebid, true));
            bids.add(shows(Bid._3H, shape(5), partner(isLastBid(Bid._3D))));
            bids.add(shows(Bid._3S, shape(5), partner(isLastBid(Bid._3D))));
            bids.add(shows(Bid._4H, FIT_8_PLUS));
            bids.add(shows(Bid._4S, FIT_8_PLUS));
            bids.add(shows(Bid._3NT));
            return bids;
        }

        public static PositionCalls openerRebid(PositionState ps) {
            PositionCalls choices = new PositionCalls(ps);
            choices.addRules(
                shows(Bid._4H, FIT_8_PLUS),
                shows(Bid._4S, FIT_8_PLUS),
                shows(Bid._3NT, fit(Suit.Hearts, false), fit(Suit.Spades, false))
            );
            return choices;
        }
    }
}
