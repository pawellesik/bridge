package com.example.bridge.bidding.BridgeBidder.WJSimple;

import com.example.licytacja.moje.BridgeBidder.*;
import java.util.ArrayList;
import java.util.List;

public class RespondBid2WJSimple extends RespondWJSimple {

    public static Iterable<CallFeature> secondBid(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(partnerBids(OpenBid3WJSimple::thirdBid));

        PositionState partner = ps.getPartner();
        Call firstCall = partner.getCallCount() > 0 ? partner.getCallDetails(0).getCall() : null;
        Bid firstBid = (firstCall instanceof Bid) ? (Bid) firstCall : null;
        Bid lastBid = partner.getBid();

        if (lastBid != null) {
            Suit lastSuit = lastBid.getSuit();
            for (int level = 1; level <= 7; level++) {
                Bid b = new Bid(level, lastSuit);
                if (ps.isValidNextCall(b)) {
                    bids.add(shows(b, shape(lastSuit, 4, 13), note("Poparcie ostatniego koloru partnera (4+)")));
                    break;
                }
            }
        }

        if (firstBid != null) {
            Suit firstSuit = firstBid.getSuit();
            for (int level = 1; level <= 7; level++) {
                Bid b = new Bid(level, firstSuit);
                if (ps.isValidNextCall(b)) {
                    bids.add(shows(b, shape(firstSuit, 2, 2), note("Preferencja do pierwszego koloru (2 karty)")));
                    break;
                }
            }
        }

        bids.add(shows(Bid._2S, IS_PARTNERS_SUIT, betterThan(Suit.Hearts)));
        bids.add(shows(Bid._2H, IS_PARTNERS_SUIT, betterThan(Suit.Spades)));

        return bids;
    }

    public static Iterable<CallFeature> secondBidToGame(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        bids.add(shows(Call.PASS));
        return bids;
    }
}
