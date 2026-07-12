package com.example.bridge.bidding.BridgeBidder.LCStandard;

import com.example.bridge.bidding.BridgeBidder.*;
import com.example.bridge.bidding.BridgeBidder.Conventions.StaymanBidder;
import com.example.bridge.bidding.BridgeBidder.Conventions.TransferBidder;
import java.util.ArrayList;
import java.util.List;

public class TwoNoTrump extends Bidder {
    public final Constraint openPoints;
    public final Constraint respondNoGame;
    public final Constraint respondGame;

    public static final TwoNoTrump OPEN = new TwoNoTrump(20, 21);
    public static final TwoNoTrump AFTER_2C_OPEN = new TwoNoTrump(22, 24);

    private TwoNoTrump(int min, int max) {
        this.openPoints = and(highCardPoints(min, max), points(min, max + 1));
        this.respondNoGame = points(0, Math.max(0, 25 - min - 1));
        this.respondGame = points(Math.max(0, 25 - min), 31 - min);
    }

    public Iterable<CallFeature> bids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(partnerBids(Bid._2NT, this::respond));
        bids.add(shows(Bid._2NT, openPoints, BALANCED));
        return bids;
    }

    public PositionCalls respond(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(new StaymanBidder.Stayman2NT(this)::initiateConvention);
        choices.addRules(new TransferBidder.Transfer2NT(this)::initiateConvention);
        choices.addRules(new Natural2NT(this)::response);
        return choices;
    }

    public static class Natural2NT extends Bidder {
        private final TwoNoTrump ntb;
        public Natural2NT(TwoNoTrump ntb) {
            this.ntb = ntb;
        }

        public Iterable<CallFeature> response(PositionState ps) {
            List<CallFeature> bids = new ArrayList<>();
            // Dodajemy instrukcję: jeśli partner zalicytuje 4NT, użyj metody openerRebidTo4NT
            bids.add(partnerBids(Bid._4NT, this::openerRebidTo4NT));

            bids.add(shows(Bid._3C, ntb.respondNoGame, shape(5, 11), longestMajor(4)));
            bids.add(shows(Bid._3D, ntb.respondNoGame, shape(5, 11), longestMajor(4)));
            bids.add(shows(Bid._3H, ntb.respondNoGame, shape(5, 11)));
            bids.add(shows(Bid._3S, ntb.respondNoGame, shape(5, 11)));

            bids.add(shows(Call.PASS, ntb.respondNoGame));

            bids.add(shows(Bid._3NT, ntb.respondGame, longestMajor(4)));

            bids.add(shows(Bid._4H, ntb.respondGame, shape(5, 11), betterThan(Suit.Spades)));
            bids.add(shows(Bid._4S, ntb.respondGame, shape(5, 11), betterOrEqualTo(Suit.Hearts)));

            // Definicja inwitu ilościowego 4NT (np. 10-11 pkt przy otwarciu 20-21)
            bids.add(shows(Bid._4NT, pairPoints(30, 31))); 
            
            return bids;
        }

        private PositionCalls openerRebidTo4NT(PositionState ps) {
            PositionCalls choices = new PositionCalls(ps);
            // Z 21 pkt (Twoja ręka) North akceptuje inwit licytując 6NT
            choices.addRules(shows(Bid._6NT, highCardPoints(21, 35)));
            // Z 20 pkt North pasuje
            choices.addRules(shows(Call.PASS, highCardPoints(20, 20)));
            return choices;
        }
    }
}




























































































































































































































































































































































































































































































