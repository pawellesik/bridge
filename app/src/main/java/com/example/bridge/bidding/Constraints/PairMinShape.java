package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.HandSummary;
import com.example.bridge.bidding.Tools.IDescribeConstraint;
import com.example.bridge.bidding.Tools.IShowsHand;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;

/**
 * Klasa weryfikująca minimalną łączną liczbę kart pary w danym kolorze.
 * Pozwala sprawdzić czy para posiada np. fit 8-kartowy (suma kart obu partnerów).
 */
public class PairMinShape {
    /**
     * Weryfikuje łączną liczbę kart pary bez deklarowania jej w wiedzy publicznej.
     */
    public static class PairHasMinShape extends HandConstraint {
        protected final Suit suit;
        protected final int min;
        protected final boolean desiredValue;
        protected final boolean useContractSuit;

        public PairHasMinShape(Suit suit, int min, boolean desiredValue) {
            this.suit = suit;
            this.min = min;
            this.desiredValue = desiredValue;
            this.useContractSuit = false;
        }

        public PairHasMinShape(int min, boolean desiredValue) {
            this.suit = null;
            this.min = min;
            this.desiredValue = desiredValue;
            this.useContractSuit = true;
        }

        @Override
        public boolean conforms(Call call, PositionState ps, HandSummary hs) {
            Suit s = null;
            if (useContractSuit) {
                if (ps.getBiddingState().getContract().isOurs(ps.getDirection())) {
                    Call contractBid = ps.getBiddingState().getContract().getBid();
                    if (contractBid instanceof Bid) {
                        s = ((Bid) contractBid).getSuit();
                    }
                }
                if (s == null) return false;
            } else {
                s = getSuit(this.suit, call);
            }
            if (s != null) {
                Range shape = hs.getSuits().get(s).getShape();
                Range partnerShape = ps.getPartner().getPublicHandSummary().getSuits().get(s).getShape();
                return (shape.getMax() + partnerShape.getMin() >= min) == desiredValue;
            }
            return false;
        }
    }

    /**
     * Pokazuje partnerowi brakującą liczbę kart do osiągnięcia sumy pary.
     */
    public static class PairShowsMinShape extends PairHasMinShape implements IShowsHand, IDescribeConstraint {
        public PairShowsMinShape(Suit suit, int min, boolean desiredValue) {
            super(suit, min, desiredValue);
        }

        public PairShowsMinShape(int min, boolean desiredValue) {
            super(min, desiredValue);
        }

        @Override
        public void showHand(Call call, PositionState ps, HandSummary.ShowState showHand) {
            Suit s = getSuit(this.suit, call);
            if (s != null) {
                Range shape = ps.getPublicHandSummary().getSuits().get(s).getShape();
                Range partnerShape = ps.getPartner().getPublicHandSummary().getSuits().get(s).getShape();
                int newMin = min - partnerShape.getMin();
                if (newMin > shape.getMin()) {
                    showHand.getSuits().get(s).showShape(newMin, Math.max(newMin, shape.getMax()));
                }
            }
        }

        @Override
        public String describe(Call call, PositionState ps) {
            Suit s = getSuit(this.suit, call);
            if (s != null) {
                return min + "+ pair " + s.toSymbol();
            }
            return null;
        }
    }
}


/**
 * 1. Dwie wersje klasy
 * Klasa posiada dwa warianty, które różnią się "wpływem na wiedzę publiczną":
 * •
 * PairHasMinShape: Jest to warunek "cichy". Sprawdza, czy faktycznie macie np. 8 kart w kolorze, ale nie informuje o tym partnera. Używany, gdy chcesz podjąć decyzję na podstawie faktów, ale nie chcesz jeszcze niczego deklarować.
 * •
 * PairShowsMinShape: Jest to warunek "informacyjny". Jeśli go użyjesz, system nie tylko sprawdzi dopasowanie, ale też zaktualizuje opis Twojej ręki. Partner będzie wiedział, że masz tyle kart, ile brakowało do sumy (np. jeśli on obiecał 3, a Ty licytujesz szukanie 8 kart, partner dowie się, że masz min. 5).
 * 2. Parametry konstruktora
 * •
 * Suit suit: Kolor, który sprawdzamy (np. Suit.Hearts). Jeśli podasz null, system użyje koloru aktualnie rozważanej odzywki.
 * •
 * int min: Próg kart, którego szukamy (najczęściej 8 dla fita majorowego lub 9 dla minorowego).
 * •
 * boolean desiredValue:
 * ◦
 * true: Szukamy sytuacji, gdzie mamy przynajmniej tyle kart.
 * ◦
 * false: Szukamy sytuacji, gdzie nie mamy tyle kart (np. brak fita).
 * •
 * useContractSuit (opcja): Jeśli użyjesz konstruktora bez koloru, system sprawdzi dopasowanie do aktualnie wylicytowanego kontraktu.
 * 3. Przykłady użycia w kodzie
 * Możesz użyć tej klasy w dowolnym miejscu, gdzie definiujesz reguły licytacyjne (np. w CompeteNatC lub RespondNatC).
 * Przykład 1: Sprawdzenie fita 8-kartowego w konkretnym kolorze
 * Chcesz zalicytować 4 Kier tylko wtedy, gdy wiesz, że macie razem min. 8 kart, i chcesz to "ogłosić" partnerowi:
 * Java
 * bids.add(shows(Bid._4H,
 *     new PairShowsMinShape(Suit.Hearts, 8, true),
 *     pairPoints(25, 30)
 * ));
 * Przykład 2: Automatyczne sprawdzenie koloru odzywki
 * Jeśli licytujesz 4 w "jakiś" kolor i chcesz sprawdzić, czy macie w nim 8 kart (bez wpisywania koloru na sztywno):
 * Java
 * // Konstruktor bez Suit używa koloru aktualnej odzywki (Bid._4S -> Spades)
 * bids.add(shows(Bid._4S,
 *     new PairShowsMinShape(8, true),
 *     id("Licytuj 4S tylko z fitem 8-kartowym")
 * ));
 * Przykład 3: Sprawdzenie braku dopasowania (Negatyw)
 * Chcesz zalicytować 3NT, upewniając się wcześniej, że NIE posiadacie fita 8-kartowego w pikach:
 * Java
 * bids.add(shows(Bid._3NT,
 *     new PairHasMinShape(Suit.Spades, 8, false), // desiredValue = false oznacza brak 8 kart
 *     PAIR_BALANCED
 * ));
 * Przykład 4: Użycie w licytacji konkurencyjnej (Blokowanie)
 * Chcesz zalicytować 5 Trefli, jeśli wiecie, że macie ich razem aż 10, aby utrudnić życie przeciwnikom:
 * Java
 * bids.add(shows(Bid._5C,
 *     new PairShowsMinShape(Suit.Clubs, 10, true)
 * ));
 * 4. Jak to działa pod maską? (Logika matematyczna)
 * Klasa wykonuje proste działanie: Twoje karty (Max z prywatnej ręki) + Karty partnera (Min z tego co obiecał publicznie) >= min.
 * Dlaczego Twoje Max a partnera Min? Ponieważ Ty wiesz dokładnie co masz (Twoje Max to Twoja faktyczna liczba kart), a o partnerze wiesz tylko tyle, ile "obiecał" dotychczasową licytacją (jego Min). Jeśli ta suma daje 8, to masz matematyczną pewność, że fit istnieje.
 * Podsumowanie
 * Używaj PairShowsMinShape, gdy chcesz znaleźć fita i jednocześnie dać partnerowi znać: "Licytuję to, bo obliczyłem, że mamy razem 8+ kart". To kluczowe narzędzie do przechodzenia z licytacji naturalnej do licytacji końcówek i szlemów.
 */

























































































































































































































































































































































































































































































