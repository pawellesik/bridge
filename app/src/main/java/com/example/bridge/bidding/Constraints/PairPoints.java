package com.example.bridge.bidding.Constraints;

import static com.example.bridge.bidding.Tools.Bidder.PARTNER_DID_NOT_SIGN_OFF;
import static com.example.bridge.bidding.Tools.Bidder.isLastBid;
import static com.example.bridge.bidding.Tools.Bidder.partner;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Bidder;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.Constraint;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.IDescribeConstraint;
import com.example.bridge.bidding.Tools.IShowsHand;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.StaticConstraint;
import com.example.bridge.bidding.Tools.Suit;

/**
 * Zaawansowana klasa do obsługi wspólnej siły punktowej pary.
 * Pozwala na dynamiczne sprawdzanie punktów na podstawie aktualnej wiedzy o obu rękach.
 */
public class PairPoints {
    protected final boolean useStartingPoints; // Czy używać punktów początkowych
    protected final boolean useAgreedStrain;   // Czy bazować na uzgodnionym kolorze atutowym
    protected final Suit suit;                 // Opcjonalny konkretny kolor atutowy
    protected final int min;                   // Minimalna suma punktów pary
    protected final int max;                   // Maksymalna suma punktów pary

    public PairPoints(Suit suit, int min, int max) {
        this.useStartingPoints = false;
        this.useAgreedStrain = false;
        this.suit = suit;
        this.min = min;
        this.max = max;
    }

    public PairPoints(int min, int max) {
        this.useStartingPoints = false;
        this.useAgreedStrain = true;
        this.suit = null;
        this.min = min;
        this.max = max;
    }

    public Suit getSuit(PositionState ps, Call call) {
        if (useAgreedStrain) {
            return ps.getPairState().getLastShownSuit();
        }
        return Constraint.getSuit(this.suit, call);
    }

    public Range getPoints(Call call, PositionState ps, HandSummary hs, boolean highCard) {
        if (highCard) {
            Range hcp = hs.getHighCardPoints();
            return hcp != null ? hcp : new Range(0, 40);
        }

        Range points = hs.getStartingPoints();
        Suit s = getSuit(ps, call);
        if (!useStartingPoints && s != null) {
            PositionState firstToShow = ps.getPairState().firstToShow(s);
            if (firstToShow == ps) {
                points = hs.getSuits().get(s).getLongHandPoints();
            } else if (firstToShow != null) {
                points = hs.getSuits().get(s).getDummyPoints();
            }
        }
        if (points == null) {
            points = hs.getPoints();
            if (!useStartingPoints && points != null) {
                points = new Range(points.getMin(), points.getMax() + 8);
            }
        }
        return points != null ? points : new Range(0, 100);
    }

    public boolean dynamicallyConforms(Call call, PositionState ps, HandSummary hs, boolean highCard) {
        Range posPoints = getPoints(call, ps, hs, highCard);
        Range partnerPoints = getPoints(ps.getPartner().getLastCall(), ps.getPartner(), ps.getPartner().getPublicHandSummary(), highCard);
        Bid partnerLastBid = ps.getPartner().getBid();

        // Pobieramy minimalne punkty obiecane przez przeciwników (jeśli używamy HCP)
        int minOppsPoints = 0;
        if (highCard) {
            Range lhoPoints = ps.getLHO().getPublicHandSummary().getHighCardPoints();
            Range rhoPoints = ps.getRHO().getPublicHandSummary().getHighCardPoints();
            if (lhoPoints != null) minOppsPoints += lhoPoints.getMin();
            if (rhoPoints != null) minOppsPoints += rhoPoints.getMin();
        }

        // Maksymalna możliwa liczba punktów dla naszej pary (40 - punkty przeciwników)
        int maxAvailableForPair = 40 - minOppsPoints;

        int minP = partnerPoints.getMin();
        int maxP = partnerPoints.getMax();
        int width = maxP - minP;
        int partnerExpected = minP;


        if (partnerLastBid != null) {
            if (!(partnerLastBid.equals(Bid._4H) ||partnerLastBid.equals(Bid._4S) || partnerLastBid.equals(Bid._3NT))) {
                if (width <= 8) {
                    partnerExpected = (minP + maxP) / 2;
                } else {
                    partnerExpected = minP + 2;
                }
            }
        } else {
            if (width <= 8) {
                partnerExpected = (minP + maxP) / 2;
            } else {
                partnerExpected = minP + 2;
            }
        }

        // Sprawdzamy, czy suma naszych punktów i oczekiwanych punktów partnera 
        // nie przekracza fizycznego limitu talii (uwzględniając licytację przeciwników).
        int totalPairExpected = posPoints.getMin() + partnerExpected;
        if (highCard && totalPairExpected > maxAvailableForPair) {
            // Jeśli licytacja przeciwników sugeruje, że partner nie może mieć aż tylu punktów,
            // obniżamy oczekiwania do fizycznie możliwego maksimum.
            partnerExpected = Math.max(minP, maxAvailableForPair - posPoints.getMin());
            totalPairExpected = posPoints.getMin() + partnerExpected;
        }
        System.out.println("posPoints.getMax() "+posPoints.getMax());
        System.out.println("min "+min);
        System.out.println("max "+max);
        System.out.println("partnerExpected "+partnerExpected);
        System.out.println("posPoints.getMin()  "+posPoints.getMin() );
        System.out.println("partnerExpected "+partnerExpected);

       // return (posPoints.getMax() + partnerExpected >= min && posPoints.getMin() + minP <= max);
        if (highCard) {
            // Dla HCP stosujemy mechanizm optymistyczny (partnerExpected) przy dolnej granicy
            return (posPoints.getMax() + partnerExpected >= min && posPoints.getMin() + partnerExpected <= max);
        } else {
            // Dla pozostałych punktów (np. układowych) pozostajemy przy bezpiecznym minP
            return (posPoints.getMax() + partnerExpected >= min && posPoints.getMin() + minP <= max);
        }
    }

    public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand, boolean highCard) {
        Range pointsPartner = getPoints(call, ps.getPartner(), ps.getPartner().getPublicHandSummary(), highCard);
        int showMin = Math.max(min - pointsPartner.getMin(), 0);
        int showMax = Math.max(max - pointsPartner.getMin(), 0);
        if (highCard) {
            showHand.showHighCardPoints(showMin, showMax);
            return;
        }
        Suit s = getSuit(ps, call);
        PositionState firstToShow = s == null ? null : ps.getPairState().firstToShow(s);
        if (useStartingPoints || firstToShow == null) {
            showHand.showStartingPoints(showMin, showMax);
        } else if (firstToShow == ps) {
            showHand.getSuits().get(s).showLongHandPoints(showMin, showMax);
        } else {
            showHand.getSuits().get(s).showDummyPoints(showMin, showMax);
        }
    }

    /**
     * Weryfikuje sumę punktów pary bez pokazywania nowych informacji.
     */
    public static class PairHasShownPoints extends StaticConstraint {
        private final PairPoints pairPoints;
        private final boolean highCard;

        public PairHasShownPoints(Suit suit, int min, int max, boolean highCard) {
            this.pairPoints = new PairPoints(suit, min, max);
            this.highCard = highCard;
        }

        @Override
        public boolean conforms(Call call, PositionState ps) {
            Range posPoints = pairPoints.getPoints(call, ps, ps.getPublicHandSummary(), highCard);
            Range partnerPoints = pairPoints.getPoints(call, ps.getPartner(), ps.getPartner().getPublicHandSummary(), highCard);
            int minP = posPoints.getMin() + partnerPoints.getMin();
            return (minP >= pairPoints.min && minP <= pairPoints.max);
        }
    }

    /**
     * Pokazuje partnerowi brakujące punkty do osiągnięcia sumy pary.
     */
    public static class PairShowsPoints extends HandConstraint implements IShowsHand, IDescribeConstraint {
        private final PairPoints pairPoints;
        private final boolean highCard;

        public PairShowsPoints(Suit suit, int min, int max, boolean highCard) {
            this.pairPoints = new PairPoints(suit, min, max);
            this.highCard = highCard;
        }

        public PairShowsPoints(int min, int max, boolean highCard) {
            this.pairPoints = new PairPoints(min, max);
            this.highCard = highCard;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            return pairPoints.dynamicallyConforms(call, ps, hs, highCard);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            pairPoints.showHand(call, ps, showHand, highCard);
        }

        @Override
        public String describe(Call call, PositionState ps) {
            return pairPoints.min + (pairPoints.min == pairPoints.max ? "" : "-" + pairPoints.max) + " pair " + (highCard ? "HCP" : "points");
        }
    }
}