package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;
import java.util.function.Function;

public class Stayman extends NoTrump.OneNoTrumpBidder {

    public Stayman(NoTrump.NoTrumpDescription ntd) {
        super(ntd);
    }

    public static Function<PositionState, Iterable<CallFeature>> initiateConvention(NoTrump.NoTrumpDescription ntd) {
        return new Stayman(ntd)::initiate;
    }

    private Iterable<CallFeature> initiate(PositionState ps) {
        Call call = new Call.Bid(2, Suit.Clubs);
        // Handle interference simplified (stolen bid)
        if (ps.rightHandOpponent().getBidHistory(0) instanceof Call.Bid) {
            if (call.equals(ps.rightHandOpponent().getBidHistory(0))) {
                call = Call.Double;
            }
        }
        
        List<CallFeature> rules = new ArrayList<>();
        rules.add(Bidder.partnerBids(call, this::answer));
        rules.add(Bidder.shows(call, ntd.rr.inviteOrBetter, Bidder.shape(Suit.Hearts, 4, 4), Bidder.shape(Suit.Spades, 0, 4)));
        rules.add(Bidder.shows(call, ntd.rr.inviteOrBetter, Bidder.shape(Suit.Spades, 4, 4), Bidder.shape(Suit.Hearts, 0, 4)));
        
        return rules;
    }

    private PositionCalls answer(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Arrays.asList(
            Bidder.partnerBids(new Call.Bid(2, Suit.Diamonds), this::respondTo2D),
            Bidder.shows(new Call.Bid(2, Suit.Diamonds), Bidder.shape(Suit.Hearts, 0, 3), Bidder.shape(Suit.Spades, 0, 3)),
            Bidder.shows(new Call.Bid(2, Suit.Hearts), Bidder.shape(Suit.Hearts, 4, 5)),
            Bidder.shows(new Call.Bid(2, Suit.Spades), Bidder.shape(Suit.Spades, 4, 5))
        ));
        return choices;
    }

    private PositionCalls respondTo2D(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(Arrays.asList(
            Bidder.shows(Call.Pass, ntd.rr.lessThanInvite),
            Bidder.shows(new Call.Bid(3, Suit.Hearts), ntd.rr.gameOrBetter, Bidder.shape(Suit.Hearts, 5, 5)),
            Bidder.shows(new Call.Bid(3, Suit.Spades), ntd.rr.gameOrBetter, Bidder.shape(Suit.Spades, 5, 5)),
            Bidder.shows(new Call.Bid(3, Strain.NoTrump), ntd.rr.game)
        ));
        return choices;
    }
}
