package com.example.bridge.bidding.BridgeBidder.Conventions;

import com.example.licytacja.moje.BridgeBidder.*;
import com.example.licytacja.moje.BridgeBidder.LCStandard.Respond;
import com.example.licytacja.moje.BridgeBidder.LCStandard.UserText;
import java.util.ArrayList;
import java.util.List;

public class NegativeDouble extends Respond {
    public static Iterable<CallFeature> initiateConvention(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        if (ps.isOpponentsContract()) {
            Bid contractBid = ps.getBiddingState().getContract().getBid();
            if (contractBid != null && contractBid.getLevel() == 1 && contractBid.getStrain() != Strain.NoTrump) {
                Suit overcallSuit = contractBid.getSuit();
                Call partnerCall = ps.getPartner().getLastCall();
                if (partnerCall instanceof Bid) {
                    Suit openSuit = ((Bid) partnerCall).getSuit();
                    bids.add(convention(Call.DOUBLE, UserText.NegativeDouble));
                    if (overcallSuit == Suit.Diamonds) {
                        bids.add(shows(Call.DOUBLE, points(RESPOND_1_LEVEL), shape(Suit.Hearts, 4), shape(Suit.Spades, 4)));
                    } else if (overcallSuit == Suit.Hearts) {
                        bids.add(shows(Call.DOUBLE, points(RESPOND_1_LEVEL), shape(Suit.Spades, 4)));
                        bids.add(shows(Bid._1S, points(RESPOND_1_LEVEL), shape(5, 11)));
                    } else if (openSuit == Suit.Hearts) {
                        bids.add(shows(Call.DOUBLE, points(NEW_SUIT_2_LEVEL), shape(Suit.Clubs, 4, 9), shape(Suit.Diamonds, 4, 9)));
                    } else {
                        bids.add(shows(Call.DOUBLE, points(RESPOND_1_LEVEL), shape(Suit.Hearts, 4)));
                        bids.add(shows(Call.DOUBLE, points(6, 10), shape(Suit.Hearts, 5, 11)));
                    }
                }
            }
        }
        return bids;
    }
}
