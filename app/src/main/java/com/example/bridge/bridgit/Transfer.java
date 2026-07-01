package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;
import java.util.function.Function;

public class Transfer extends NoTrump.OneNoTrumpBidder {

    public Transfer(NoTrump.NoTrumpDescription ntd) {
        super(ntd);
    }

    public static Function<PositionState, Iterable<CallFeature>> initiateConvention(NoTrump.NoTrumpDescription ntd) {
        return new Transfer(ntd)::initiate;
    }

    private Iterable<CallFeature> initiate(PositionState ps) {
        List<CallFeature> rules = new ArrayList<>();
        
        rules.add(Bidder.partnerBids(this::acceptTransfer));
        
        // Transfer to Hearts (2D -> 2H)
        rules.add(Bidder.shows(new Call.Bid(2, Suit.Diamonds), ntd.rr.lessThanInvite, Bidder.shape(Suit.Hearts, 5, 11)));
        
        // Transfer to Spades (2H -> 2S)
        rules.add(Bidder.shows(new Call.Bid(2, Suit.Hearts), ntd.rr.lessThanInvite, Bidder.shape(Suit.Spades, 5, 11)));
        
        return rules;
    }

    private PositionCalls acceptTransfer(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        
        // Simplified acceptance: always bid the major
        choices.addRules(Arrays.asList(
            Bidder.shows(new Call.Bid(2, Suit.Hearts), Bidder.partner(Bidder.isLastBid(new Call.Bid(2, Suit.Diamonds)))),
            Bidder.shows(new Call.Bid(2, Suit.Spades), Bidder.partner(Bidder.isLastBid(new Call.Bid(2, Suit.Hearts))))
        ));
        
        return choices;
    }
}
