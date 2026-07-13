package com.example.bridge.bidding.NatC;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class TrialBidNatC extends NatC {
    public static Iterable<CallFeature> bids(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        // Help Suit Game Try - bid a new suit at level 3 to ask for help
        // Only if we have an agreed major and are at level 2
        Suit agreed = ps.getPairState().getTrumpSuit();
        if (agreed != null && agreed.isMajor()) {
            for (Suit suit : Suit.values()) {
                if (suit != agreed) {
                    Bid trialBid = new Bid(3, suit);
                    if (ps.isValidNextCall(trialBid)) {
                        bids.add(partnerBids(trialBid, RespondBid2NatC::openerInvitedGame));
                        bids.add(shows(trialBid, shape(suit, 4, 13), pairPoints(PAIR_GAME_INVITE), id("TrialBidNatC.helpSuitGameTry " + trialBid)));
                    }
                }
            }
        }
        return bids;
    }
}
