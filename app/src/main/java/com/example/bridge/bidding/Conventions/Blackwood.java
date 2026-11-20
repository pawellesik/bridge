package com.example.bridge.bidding.Conventions;

import com.example.bridge.bidding.LCStandard.UserText;
import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Bidder;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.CallFeature;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;

import java.util.ArrayList;
import java.util.List;

public class Blackwood extends Bidder {
    private static final Range PAIR_32_40 = new Range(32, 100);
    private static final Range PAIR_36_40= new Range(36, 100);
    public static final Range OPEN_15_17 = new Range(15, 17);

    public static Iterable<CallFeature> initiateConvention(PositionState ps) {
        List<CallFeature> bids = new ArrayList<>();
        Suit suit = getAgreedSuit(ps);
        boolean hasRealFit = hasAgreedFit(ps, suit);

        if (suit != null) {
            bids.add(properties(Bid._4NT, Blackwood::respondKeyCards, true, UserText.Blackwood));

            if (hasRealFit) {
                // Gdy Mamy faktyczny fit (8+ kart w parze): punkty pary doliczają krótkości i ustawiamy atut
                bids.add(shows(Bid._4NT, setTrumpColor(suit), fit(suit), pairPoints(suit, PAIR_32_40.getMin(), PAIR_32_40.getMax()), ruleShow(1), ruleDescription("Blackwood"), id("Blackwood.initiateConvention _4NT")));
            } else {
                // Gdy NIE Mamy fitu: sprawdzamy czyste punkty honorowe HCP (32+ HCP) bez doliczania krótkości i nie ustawiamy atutu
                bids.add(shows(Bid._4NT, pairHighCardPoints(PAIR_32_40.getMin(), PAIR_32_40.getMax()), ruleShow(1), ruleDescription("Blackwood"), id("Blackwood.initiateConvention _4NT")));
            }

            bids.add(shows(Bid._4NT, setTrumpColor(suit), highCardPoints(OPEN_15_17), fit(2, Suit.Hearts, false), partner(isLastBid(Bid._3H)), ruleShow(1), ruleDescription("Blackwood"), id("Blackwood.initiateConvention _4NT")));
            bids.add(shows(Bid._4NT, setTrumpColor(suit), highCardPoints(OPEN_15_17), fit(2, Suit.Spades, false), partner(isLastBid(Bid._3S)), ruleShow(1), ruleDescription("Blackwood"), id("Blackwood.initiateConvention _4NT")));
            bids.add(shows(Bid._4NT, setTrumpColor(suit), highCardPoints(OPEN_15_17), fit(2, Suit.Clubs, false), partner(isLastBid(Bid._4C)), ruleShow(1), ruleDescription("Blackwood"), id("Blackwood.initiateConvention _4NT")));
        }
        return bids;
    }

    private static boolean hasAgreedFit(PositionState ps, Suit suit) {
        if (ps == null || suit == null) return false;
        if (ps.getPairState().getTrumpSuit() != null) return true;

        HandSummary hs1 = ps.getPrivateHandSummary() != null ? ps.getPrivateHandSummary() : ps.getPublicHandSummary();
        HandSummary hs2 = ps.getPartner() != null ? ps.getPartner().getPublicHandSummary() : null;

        if (hs1 != null && hs2 != null) {
            HandSummary.SuitSummary ss1 = hs1.getSuits().get(suit);
            HandSummary.SuitSummary ss2 = hs2.getSuits().get(suit);
            if (ss1 != null && ss2 != null) {
                Range shape1 = ss1.getShape();
                Range shape2 = ss2.getShape();
                if (shape1 != null && shape2 != null) {
                    return (shape1.getMin() + shape2.getMin() >= 8);
                }
            }
        }
        return false;
    }

    private static Suit getAgreedSuit(PositionState ps) {
        if (ps == null) return null;
        Suit trump = ps.getPairState().getTrumpSuit();
        if (trump != null) return trump;

        HandSummary hs1 = ps.getPrivateHandSummary() != null ? ps.getPrivateHandSummary() : ps.getPublicHandSummary();
        HandSummary hs2 = ps.getPartner() != null ? ps.getPartner().getPublicHandSummary() : null;

        if (hs1 != null && hs2 != null) {
            Suit bestSuit = null;
            int maxCards = 0;

            Suit lastSuit = ps.getPairState().getLastShownSuit();
            Suit[] suitsToCheck = lastSuit != null 
                    ? new Suit[]{lastSuit, Suit.Spades, Suit.Hearts, Suit.Diamonds, Suit.Clubs}
                    : new Suit[]{Suit.Spades, Suit.Hearts, Suit.Diamonds, Suit.Clubs};

            for (Suit s : suitsToCheck) {
                HandSummary.SuitSummary ss1 = hs1.getSuits().get(s);
                HandSummary.SuitSummary ss2 = hs2.getSuits().get(s);
                if (ss1 != null && ss2 != null) {
                    Range shape1 = ss1.getShape();
                    Range shape2 = ss2.getShape();
                    if (shape1 != null && shape2 != null) {
                        int total = shape1.getMin() + shape2.getMin();
                        if (total >= 8 && total > maxCards) {
                            maxCards = total;
                            bestSuit = s;
                        }
                    }
                }
            }
            if (bestSuit != null) return bestSuit;
        }

        return ps.getPairState().getLastShownSuit();
    }

    public static PositionCalls respondKeyCards(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit suit = getAgreedSuit(ps);
        if (suit != null) {
            choices.addRules(
                properties(new Call[]{Bid._5C, Bid._5D, Bid._5H, Bid._5S}, Blackwood::placeContract, true),
                shows(Bid._5C, keyCards(suit, 1, 4), ruleShow(1), ruleDescription("Blackwood: 1 or 4 Key Cards")),
                shows(Bid._5D, keyCards(suit, 0, 3), ruleShow(1), ruleDescription("Blackwood: 0 or 3 Key Cards")),
                shows(Bid._5H, keyCards(suit, false, 2, 5), ruleShow(1), ruleDescription("Blackwood: 2 or 5 Key Cards (without Queen)")),
                shows(Bid._5S, keyCards(suit, true, 2, 5), ruleShow(1), ruleDescription("Blackwood: 2 or 5 Key Cards with Queen"))
            );
            return choices;
        }
        throw new RuntimeException("This should never happen. No agreed suit.");
    }

    public static PositionCalls placeContract(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit suit = getAgreedSuit(ps);
        if (suit != null) {
            choices.addRules(
                properties(Bid._5NT, Blackwood::respondKings, true),
                shows(Bid._5NT, pairKeyCards(suit, true, 5), pairPoints(PAIR_36_40), ruleShow(1), ruleDescription("Blackwood: Ask for Kings")),
                shows(Bid._5NT, pairKeyCards(suit, true, 5), pairPoints(PAIR_32_40), fit(9, suit), ruleShow(1), ruleDescription("Blackwood: Ask for Kings")),

                shows(new Bid(6, suit), fit(suit), pairPoints(PAIR_32_40), pairKeyCards(suit, null, 4, 5, 6)),
                shows(Bid._6NT, not(fit(suit)), pairPoints(PAIR_32_40), pairKeyCards(suit, null, 4, 5, 6)),

                shows(new Bid(5, suit), fit(suit), pairKeyCards(suit, null, 0, 1, 2, 3)),
                shows(Bid._5NT, not(fit(suit)), pairKeyCards(suit, null, 0, 1, 2, 3)),

                shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN, pairKeyCards(suit, null, 0, 1, 2, 3)),

                shows(new Bid(6, suit), fit(suit), IS_NON_JUMP, pairKeyCards(suit, null, 0, 1, 2, 3)),
                shows(Bid._6NT, not(fit(suit)), IS_NON_JUMP, pairKeyCards(suit, null, 0, 1, 2, 3))
            );
            return choices;
        }
        throw new RuntimeException("This should not happen");
    }

    public static PositionCalls respondKings(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
            properties(new Call[]{Bid._6C, Bid._6D, Bid._6H, Bid._6S}, Blackwood::tryGrandSlam, true),
            shows(Bid._6C, kings(0, 4), ruleShow(1), ruleDescription("Blackwood: 0 or 4 Kings")),
            shows(Bid._6D, kings(1), ruleShow(1), ruleDescription("Blackwood: 1 King")),
            shows(Bid._6H, kings(2), ruleShow(1), ruleDescription("Blackwood: 2 Kings")),
            shows(Bid._6S, kings(3), ruleShow(1), ruleDescription("Blackwood: 3 Kings"))
        );
        return choices;
    }

    public static PositionCalls tryGrandSlam(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        Suit suit = getAgreedSuit(ps);
        if (suit != null) {
            choices.addRules(
                shows(new Bid(7, suit), fit(suit), pairKeyCards(suit, true, 5), pairKings(4)),
                shows(Bid._7NT, not(fit(suit)), pairKeyCards(suit, true, 5), pairKings(4)),
                shows(Call.PASS, CONTRACT_IS_AGREED_STRAIN),
                shows(new Bid(6, suit), fit(suit)),
                shows(Bid._6NT, not(fit(suit))),
                shows(new Bid(7, suit), fit(suit)),
                shows(Bid._7NT, not(fit(suit)))
            );
            return choices;
        }
        throw new RuntimeException("This should not happen");
    }
}




























































































































































































































































































































































































































































































