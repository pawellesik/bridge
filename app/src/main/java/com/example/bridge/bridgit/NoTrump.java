package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class NoTrump extends Bidder {

    public static class NoTrumpDescription {
        public static class OpenerRanges {
            public Constraint open;
            public Constraint dontAcceptInvite;
            public Constraint acceptInvite;
        }

        public static class ResponderRanges {
            public Constraint lessThanInvite;
            public Constraint inviteGame;
            public Constraint game;
            public Constraint gameOrBetter;
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

            this.rr.lessThanInvite = points(0, 7);
            this.rr.inviteGame = points(8, 9);
            this.rr.game = points(10, 15);
            this.rr.gameOrBetter = points(10, 40);
        }
    }

    public static Iterable<CallFeature> open(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        if (ps.getBidRound() == 1 && ps.getSeat() <= 4) {
            Open1NTDescription ntd = new Open1NTDescription();
            bids.add(new CallProperties(new Call.Bid(1, Strain.NoTrump), p -> conventionalResponses(p, ntd), false, false, false, null));
            bids.add(shows(new Call.Bid(1, Strain.NoTrump), ntd.or.open, balanced()));
        }
        return bids;
    }

    private static PositionCalls conventionalResponses(PositionState ps, NoTrumpDescription ntd) {
        PositionCalls choices = new PositionCalls(ps);
        // Simple natural responses for demo
        choices.addRules(naturalResponses(ntd));
        return choices;
    }

    private static Iterable<CallFeature> naturalResponses(NoTrumpDescription ntd) {
        List<CallFeature> rules = new ArrayList<>();
        rules.add(shows(new Call.Bid(3, Strain.NoTrump), ntd.rr.game));
        rules.add(shows(new Call.Bid(2, Strain.NoTrump), ntd.rr.inviteGame));
        rules.add(shows(Call.Pass, ntd.rr.lessThanInvite));
        return rules;
    }
}
