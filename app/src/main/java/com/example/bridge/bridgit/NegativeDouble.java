package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class NegativeDouble extends Bidder {
    public static Iterable<CallFeature> initiateConvention(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        ContractState contract = ps.getBiddingState().getContract();
        
        if (contract.bid != null && contract.bid.getLevel() == 1 && contract.bid.getStrain() != Strain.NoTrump) {
            Suit overcallSuit = contract.bid.getSuit();
            Call partnerLast = ps.partner().getBidHistory(0);
            
            if (partnerLast instanceof Call.Bid) {
                Suit openSuit = ((Call.Bid) partnerLast).getSuit();
                bids.add(convention(Call.Double, "Negative Double"));
                
                if (overcallSuit == Suit.Diamonds) {
                    bids.add(shows(Call.Double, points(6, 40), shape(Suit.Hearts, 4, 13), shape(Suit.Spades, 4, 13)));
                } else if (overcallSuit == Suit.Hearts) {
                    bids.add(shows(Call.Double, points(6, 40), shape(Suit.Spades, 4, 13)));
                } else if (openSuit == Suit.Hearts) { // 1H - (1S) - X
                    bids.add(shows(Call.Double, points(10, 40), shape(Suit.Clubs, 4, 9), shape(Suit.Diamonds, 4, 9)));
                } else { // 1 minor - (1S) - X
                    bids.add(shows(Call.Double, points(6, 40), shape(Suit.Hearts, 4, 13)));
                }
            }
        }
        return bids;
    }
}
