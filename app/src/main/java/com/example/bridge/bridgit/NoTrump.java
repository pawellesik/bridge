package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;
import java.util.function.Function;

public class NoTrump extends Bidder {

    public static class NoTrumpDescription {
        public static class OpenerRanges {
            public Constraint open;
            public Constraint dontAcceptInvite;
            public Constraint acceptInvite;
            public Constraint lessThanSuperAccept;
            public Constraint superAccept;
        }

        public static class ResponderRanges {
            public Constraint lessThanInvite;
            public Constraint inviteGame;
            public Constraint inviteOrBetter;
            public Constraint game;
            public Constraint gameOrBetter;
            public Constraint gameIfSuperAccept;
            public Constraint inviteSlam;
            public Constraint smallSlam;
            public Constraint grandSlam;
            public Constraint gameAsDummy;
            public Constraint inviteAsDummy;
            public Constraint smallSlamAsDummy;
            public Constraint grandSlamAsDummy;
        }

        public String openType;
        public OpenerRanges or = new OpenerRanges();
        public ResponderRanges rr = new ResponderRanges();
    }

    public static class Open1NTDescription extends NoTrumpDescription {
        public Open1NTDescription() {
            this.openType = "Open1NT";
            this.or.open = and(highCardPoints(15, 17), points(15, 18));
            this.or.dontAcceptInvite = and(highCardPoints(15, 15), points(15, 16));
            this.or.acceptInvite = and(highCardPoints(16, 17), points(16, 18));
            this.or.lessThanSuperAccept = and(highCardPoints(15, 16), points(15, 17));
            this.or.superAccept = and(highCardPoints(17, 17), points(17, 18));

            this.rr.lessThanInvite = points(0, 7);
            this.rr.inviteGame = points(8, 9);
            this.rr.inviteOrBetter = points(8, 40);
            this.rr.game = points(10, 15);
            this.rr.gameOrBetter = points(10, 40);
            this.rr.gameIfSuperAccept = points(6, 15);
            this.rr.inviteSlam = points(16, 17);
            this.rr.smallSlam = points(18, 19);
            this.rr.grandSlam = points(20, 40);
            
            this.rr.inviteAsDummy = dummyPoints(8, 9);
            this.rr.gameAsDummy = dummyPoints(10, 16);
            this.rr.smallSlamAsDummy = dummyPoints(17, 20);
            this.rr.grandSlamAsDummy = dummyPoints(21, 40);
        }
    }

    public static abstract class OneNoTrumpBidder extends Bidder {
        protected final NoTrumpDescription ntd;
        
        protected OneNoTrumpBidder(NoTrumpDescription ntd) {
            this.ntd = ntd;
        }

        public Iterable<CallFeature> bids(PositionState ps) {
            if (ps.getRole() == PositionState.PositionRole.Opener && ps.getRoleRound() == 1) {
                List<CallFeature> res = new ArrayList<>();
                res.add(properties(new Call.Bid(1, Strain.NoTrump), this::conventionalResponses, false, false, false, null, null, "15-17", "1NT Opening", null));
                res.add(shows(new Call.Bid(1, Strain.NoTrump), ntd.or.open, balanced));
                return res;
            }
            return Collections.emptyList();
        }

        private PositionCalls conventionalResponses(PositionState ps) {
            PositionCalls choices = new PositionCalls(ps);
            choices.addRules(Stayman.initiateConvention(ntd).apply(ps));
            choices.addRules(Transfer.initiateConvention(ntd).apply(ps));
            // choices.addRules(Gerber.initiateConvention(ps)); // To be ported
            choices.addRules(naturalResponses());
            return choices;
        }

        private Iterable<CallFeature> naturalResponses() {
            List<CallFeature> rules = new ArrayList<>();
            rules.add(shows(new Call.Bid(3, Strain.NoTrump), ntd.rr.game));
            rules.add(shows(new Call.Bid(2, Strain.NoTrump), ntd.rr.inviteGame));
            rules.add(shows(Call.Pass, ntd.rr.lessThanInvite));
            return rules;
        }
    }

    public static class Open1NT extends OneNoTrumpBidder {
        public Open1NT() { super(new Open1NTDescription()); }
    }

    public static Iterable<CallFeature> open(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.addAll((Collection<? extends CallFeature>) new Open1NT().bids(ps));
        // Add 2NT open logic here later
        return bids;
    }
}
