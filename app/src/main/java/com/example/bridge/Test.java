package com.example.bridge;

import com.example.bridge.model.Card;
import com.example.bridge.model.Rank;
import com.example.bridge.model.Suit;

import java.util.ArrayList;
import java.util.List;


public class Test {

    static public int isDebugModel = 0;

    static public List<Card> getCards() {
        List<Card> cards = new ArrayList<>();

        for (Card card : getCardsN()) {
            cards.add(card);
        }
        for (Card card : getCardsE()) {
            cards.add(card);
        }
        for (Card card : getCardsS()) {
            cards.add(card);
        }
        for (Card card : getCardsW()) {
            cards.add(card);
        }
        return cards;
    }
    private static List<Card> getCardsN() {
        List<Card> cards = new ArrayList<>();
        Suit spades = Suit.SPADES;
        Suit hearts = Suit.HEARTS;
        Suit clubs = Suit.CLUBS;
        Suit diamonds = Suit.DIAMONDS;

        cards.add(new Card(spades, Rank.ACE));
        cards.add(new Card(spades, Rank.KING));
        cards.add(new Card(spades, Rank.QUEEN));
        cards.add(new Card(spades, Rank.JACK));
//        cards.add(new Card(spades, Rank.TEN));
//        cards.add(new Card(spades, Rank.NINE));
//        cards.add(new Card(spades, Rank.EIGHT));
//        cards.add(new Card(spades, Rank.SEVEN));
//        cards.add(new Card(spades, Rank.SIX));
//        cards.add(new Card(spades, Rank.FIVE));
//        cards.add(new Card(spades, Rank.FOUR));
        cards.add(new Card(spades, Rank.THREE));
        cards.add(new Card(spades, Rank.TWO));
//
//        cards.add(new Card(hearts, Rank.ACE));
//        cards.add(new Card(hearts, Rank.KING));
//        cards.add(new Card(hearts, Rank.QUEEN));
//        cards.add(new Card(hearts, Rank.JACK));
//        cards.add(new Card(hearts, Rank.TEN));
//        cards.add(new Card(hearts, Rank.NINE));
//        cards.add(new Card(hearts, Rank.EIGHT));
//        cards.add(new Card(hearts, Rank.SEVEN));
//        cards.add(new Card(hearts, Rank.SIX));
//        cards.add(new Card(hearts, Rank.FIVE));
//        cards.add(new Card(hearts, Rank.FOUR));
//        cards.add(new Card(hearts, Rank.THREE));
//        cards.add(new Card(hearts, Rank.TWO));
//
//        cards.add(new Card(clubs, Rank.ACE));
//        cards.add(new Card(clubs, Rank.KING));
//        cards.add(new Card(clubs, Rank.QUEEN));
//        cards.add(new Card(clubs, Rank.JACK));
//        cards.add(new Card(clubs, Rank.TEN));
//        cards.add(new Card(clubs, Rank.NINE));
        cards.add(new Card(clubs, Rank.EIGHT));
//        cards.add(new Card(clubs, Rank.SEVEN));
//        cards.add(new Card(clubs, Rank.SIX));
//        cards.add(new Card(clubs, Rank.FIVE));
        cards.add(new Card(clubs, Rank.FOUR));
        cards.add(new Card(clubs, Rank.THREE));
//        cards.add(new Card(clubs, Rank.TWO));
//
        cards.add(new Card(diamonds, Rank.ACE));
        cards.add(new Card(diamonds, Rank.KING));
//        cards.add(new Card(diamonds, Rank.QUEEN));
//        cards.add(new Card(diamonds, Rank.JACK));
//        cards.add(new Card(diamonds, Rank.TEN));
//        cards.add(new Card(diamonds, Rank.NINE));
//        cards.add(new Card(diamonds, Rank.EIGHT));
//        cards.add(new Card(diamonds, Rank.SEVEN));
        cards.add(new Card(diamonds, Rank.SIX));
//        cards.add(new Card(diamonds, Rank.FIVE));
//        cards.add(new Card(diamonds, Rank.FOUR));
        cards.add(new Card(diamonds, Rank.THREE));
//        cards.add(new Card(diamonds, Rank.TWO));
        return cards;
    }
//
    private static List<Card> getCardsE() {
        List<Card> cards = new ArrayList<>();
        Suit spades = Suit.SPADES;
        Suit hearts = Suit.HEARTS;
        Suit clubs = Suit.CLUBS;
        Suit diamonds = Suit.DIAMONDS;
//
//        cards.add(new Card(spades, Rank.ACE));
//        cards.add(new Card(spades, Rank.KING));
//        cards.add(new Card(spades, Rank.QUEEN));
//        cards.add(new Card(spades, Rank.JACK));
        cards.add(new Card(spades, Rank.TEN));
        cards.add(new Card(spades, Rank.NINE));
//        cards.add(new Card(spades, Rank.EIGHT));
//        cards.add(new Card(spades, Rank.SEVEN));
//        cards.add(new Card(spades, Rank.SIX));
        cards.add(new Card(spades, Rank.FIVE));
//        cards.add(new Card(spades, Rank.FOUR));
//        cards.add(new Card(spades, Rank.THREE));
//        cards.add(new Card(spades, Rank.TWO));
//
        cards.add(new Card(hearts, Rank.ACE));
        cards.add(new Card(hearts, Rank.KING));
//        cards.add(new Card(hearts, Rank.QUEEN));
//        cards.add(new Card(hearts, Rank.JACK));
//        cards.add(new Card(hearts, Rank.TEN));
//        cards.add(new Card(hearts, Rank.NINE));
//        cards.add(new Card(hearts, Rank.EIGHT));
//        cards.add(new Card(hearts, Rank.SEVEN));
        cards.add(new Card(hearts, Rank.SIX));
//        cards.add(new Card(hearts, Rank.FIVE));
        cards.add(new Card(hearts, Rank.FOUR));
        cards.add(new Card(hearts, Rank.THREE));
//        cards.add(new Card(hearts, Rank.TWO));
//
        cards.add(new Card(clubs, Rank.ACE));
//        cards.add(new Card(clubs, Rank.KING));
        cards.add(new Card(clubs, Rank.QUEEN));
//        cards.add(new Card(clubs, Rank.JACK));
        cards.add(new Card(clubs, Rank.TEN));
//        cards.add(new Card(clubs, Rank.NINE));
//        cards.add(new Card(clubs, Rank.EIGHT));
//        cards.add(new Card(clubs, Rank.SEVEN));
        cards.add(new Card(clubs, Rank.SIX));
        cards.add(new Card(clubs, Rank.FIVE));
//        cards.add(new Card(clubs, Rank.FOUR));
//        cards.add(new Card(clubs, Rank.THREE));
//        cards.add(new Card(clubs, Rank.TWO));
//
//        cards.add(new Card(diamonds, Rank.ACE));
//        cards.add(new Card(diamonds, Rank.KING));
//        cards.add(new Card(diamonds, Rank.QUEEN));
//        cards.add(new Card(diamonds, Rank.JACK));
//        cards.add(new Card(diamonds, Rank.TEN));
//        cards.add(new Card(diamonds, Rank.NINE));
//        cards.add(new Card(diamonds, Rank.EIGHT));
//        cards.add(new Card(diamonds, Rank.SEVEN));
//        cards.add(new Card(diamonds, Rank.SIX));
//        cards.add(new Card(diamonds, Rank.FIVE));
//        cards.add(new Card(diamonds, Rank.FOUR));
//        cards.add(new Card(diamonds, Rank.THREE));
//        cards.add(new Card(diamonds, Rank.TWO));
//
        return cards;
    }
//
    private static List<Card> getCardsS() {
        List<Card> cards = new ArrayList<>();
        Suit spades = Suit.SPADES;
        Suit hearts = Suit.HEARTS;
        Suit clubs = Suit.CLUBS;
        Suit diamonds = Suit.DIAMONDS;
//
//        cards.add(new Card(spades, Rank.ACE));
//        cards.add(new Card(spades, Rank.KING));
//        cards.add(new Card(spades, Rank.QUEEN));
//        cards.add(new Card(spades, Rank.JACK));
//        cards.add(new Card(spades, Rank.TEN));
//        cards.add(new Card(spades, Rank.NINE));
//        cards.add(new Card(spades, Rank.EIGHT));
//        cards.add(new Card(spades, Rank.SEVEN));
//        cards.add(new Card(spades, Rank.SIX));
//        cards.add(new Card(spades, Rank.FIVE));
        cards.add(new Card(spades, Rank.FOUR));
//        cards.add(new Card(spades, Rank.THREE));
//        cards.add(new Card(spades, Rank.TWO));
//
//        cards.add(new Card(hearts, Rank.ACE));
//        cards.add(new Card(hearts, Rank.KING));
        cards.add(new Card(hearts, Rank.QUEEN));
//        cards.add(new Card(hearts, Rank.JACK));
        cards.add(new Card(hearts, Rank.TEN));
        cards.add(new Card(hearts, Rank.NINE));
//        cards.add(new Card(hearts, Rank.EIGHT));
        cards.add(new Card(hearts, Rank.SEVEN));
//        cards.add(new Card(hearts, Rank.SIX));
//        cards.add(new Card(hearts, Rank.FIVE));
//        cards.add(new Card(hearts, Rank.FOUR));
//        cards.add(new Card(hearts, Rank.THREE));
//        cards.add(new Card(hearts, Rank.TWO));
//
//        cards.add(new Card(clubs, Rank.ACE));
        cards.add(new Card(clubs, Rank.KING));
//        cards.add(new Card(clubs, Rank.QUEEN));
//        cards.add(new Card(clubs, Rank.JACK));
//        cards.add(new Card(clubs, Rank.TEN));
//        cards.add(new Card(clubs, Rank.NINE));
//        cards.add(new Card(clubs, Rank.EIGHT));
        cards.add(new Card(clubs, Rank.SEVEN));
//        cards.add(new Card(clubs, Rank.SIX));
//        cards.add(new Card(clubs, Rank.FIVE));
//        cards.add(new Card(clubs, Rank.FOUR));
//        cards.add(new Card(clubs, Rank.THREE));
//        cards.add(new Card(clubs, Rank.TWO));
//
//        cards.add(new Card(diamonds, Rank.ACE));
//        cards.add(new Card(diamonds, Rank.KING));
        cards.add(new Card(diamonds, Rank.QUEEN));
        cards.add(new Card(diamonds, Rank.JACK));
        cards.add(new Card(diamonds, Rank.TEN));
//        cards.add(new Card(diamonds, Rank.NINE));
        cards.add(new Card(diamonds, Rank.EIGHT));
//        cards.add(new Card(diamonds, Rank.SEVEN));
//        cards.add(new Card(diamonds, Rank.SIX));
        cards.add(new Card(diamonds, Rank.FIVE));
//        cards.add(new Card(diamonds, Rank.FOUR));
//        cards.add(new Card(diamonds, Rank.THREE));
        cards.add(new Card(diamonds, Rank.TWO));
        return cards;
    }
//
    private static List<Card> getCardsW() {
        List<Card> cards = new ArrayList<>();
        Suit spades = Suit.SPADES;
        Suit hearts = Suit.HEARTS;
        Suit clubs = Suit.CLUBS;
        Suit diamonds = Suit.DIAMONDS;
//
//        cards.add(new Card(spades, Rank.ACE));
//        cards.add(new Card(spades, Rank.KING));
//        cards.add(new Card(spades, Rank.QUEEN));
//        cards.add(new Card(spades, Rank.JACK));
//        cards.add(new Card(spades, Rank.TEN));
//        cards.add(new Card(spades, Rank.NINE));
        cards.add(new Card(spades, Rank.EIGHT));
        cards.add(new Card(spades, Rank.SEVEN));
        cards.add(new Card(spades, Rank.SIX));
//        cards.add(new Card(spades, Rank.FIVE));
//        cards.add(new Card(spades, Rank.FOUR));
//        cards.add(new Card(spades, Rank.THREE));
//        cards.add(new Card(spades, Rank.TWO));
//
//        cards.add(new Card(hearts, Rank.ACE));
//        cards.add(new Card(hearts, Rank.KING));
//        cards.add(new Card(hearts, Rank.QUEEN));
        cards.add(new Card(hearts, Rank.JACK));
//        cards.add(new Card(hearts, Rank.TEN));
//        cards.add(new Card(hearts, Rank.NINE));
        cards.add(new Card(hearts, Rank.EIGHT));
//        cards.add(new Card(hearts, Rank.SEVEN));
//        cards.add(new Card(hearts, Rank.SIX));
        cards.add(new Card(hearts, Rank.FIVE));
//        cards.add(new Card(hearts, Rank.FOUR));
//        cards.add(new Card(hearts, Rank.THREE));
        cards.add(new Card(hearts, Rank.TWO));
//
//        cards.add(new Card(clubs, Rank.ACE));
//        cards.add(new Card(clubs, Rank.KING));
//        cards.add(new Card(clubs, Rank.QUEEN));
        cards.add(new Card(clubs, Rank.JACK));
//        cards.add(new Card(clubs, Rank.TEN));
        cards.add(new Card(clubs, Rank.NINE));
//        cards.add(new Card(clubs, Rank.EIGHT));
//        cards.add(new Card(clubs, Rank.SEVEN));
//        cards.add(new Card(clubs, Rank.SIX));
//        cards.add(new Card(clubs, Rank.FIVE));
//        cards.add(new Card(clubs, Rank.FOUR));
//        cards.add(new Card(clubs, Rank.THREE));
        cards.add(new Card(clubs, Rank.TWO));
//
//        cards.add(new Card(diamonds, Rank.ACE));
//        cards.add(new Card(diamonds, Rank.KING));
//        cards.add(new Card(diamonds, Rank.QUEEN));
//        cards.add(new Card(diamonds, Rank.JACK));
//        cards.add(new Card(diamonds, Rank.TEN));
        cards.add(new Card(diamonds, Rank.NINE));
//        cards.add(new Card(diamonds, Rank.EIGHT));
        cards.add(new Card(diamonds, Rank.SEVEN));
//        cards.add(new Card(diamonds, Rank.SIX));
//        cards.add(new Card(diamonds, Rank.FIVE));
        cards.add(new Card(diamonds, Rank.FOUR));
//        cards.add(new Card(diamonds, Rank.THREE));
//        cards.add(new Card(diamonds, Rank.TWO));
        return cards;
    }


//    private static List<Card> getCardsN() {
//        List<Card> cards = new ArrayList<>();
//        Suit spades = Suit.SPADES;
//        Suit hearts = Suit.HEARTS;
//        Suit clubs = Suit.CLUBS;
//        Suit diamonds = Suit.DIAMONDS;
//
//        cards.add(new Card(spades, Rank.ACE));
//        //cards.add(new Card(spades, Rank.KING));
//        //cards.add(new Card(spades, Rank.QUEEN));
//        //cards.add(new Card(spades, Rank.JACK));
//        //cards.add(new Card(spades, Rank.TEN));
//        //cards.add(new Card(spades, Rank.NINE));
//        //cards.add(new Card(spades, Rank.EIGHT));
//        //cards.add(new Card(spades, Rank.SEVEN));
//        //cards.add(new Card(spades, Rank.SIX));
//        //cards.add(new Card(spades, Rank.FIVE));
//        //cards.add(new Card(spades, Rank.FOUR));
//        //cards.add(new Card(spades, Rank.THREE));
//        //cards.add(new Card(spades, Rank.TWO));
//
//        //cards.add(new Card(hearts, Rank.ACE));
//        cards.add(new Card(hearts, Rank.KING));
//        //cards.add(new Card(hearts, Rank.QUEEN));
//        //cards.add(new Card(hearts, Rank.JACK));
//        //cards.add(new Card(hearts, Rank.TEN));
//        //cards.add(new Card(hearts, Rank.NINE));
//        cards.add(new Card(hearts, Rank.EIGHT));
//        //cards.add(new Card(hearts, Rank.SEVEN));
//        cards.add(new Card(hearts, Rank.SIX));
//        //cards.add(new Card(hearts, Rank.FIVE));
//        cards.add(new Card(hearts, Rank.FOUR));
//        //cards.add(new Card(hearts, Rank.THREE));
//        //cards.add(new Card(hearts, Rank.TWO));
//
//        cards.add(new Card(clubs, Rank.ACE));
//        //cards.add(new Card(clubs, Rank.KING));
//        cards.add(new Card(clubs, Rank.QUEEN));
//        cards.add(new Card(clubs, Rank.JACK));
//        cards.add(new Card(clubs, Rank.TEN));
//        //cards.add(new Card(clubs, Rank.NINE));
//        //cards.add(new Card(clubs, Rank.EIGHT));
//        //cards.add(new Card(clubs, Rank.SEVEN));
//        //cards.add(new Card(clubs, Rank.SIX));
//        //cards.add(new Card(clubs, Rank.FIVE));
//        //cards.add(new Card(clubs, Rank.FOUR));
//        //cards.add(new Card(clubs, Rank.THREE));
//        //cards.add(new Card(clubs, Rank.TWO));
//
//        //cards.add(new Card(diamonds, Rank.ACE));
//        cards.add(new Card(diamonds, Rank.KING));
//        //cards.add(new Card(diamonds, Rank.QUEEN));
//        cards.add(new Card(diamonds, Rank.JACK));
//        //cards.add(new Card(diamonds, Rank.TEN));
//        //cards.add(new Card(diamonds, Rank.NINE));
//        cards.add(new Card(diamonds, Rank.EIGHT));
//        //cards.add(new Card(diamonds, Rank.SEVEN));
//        //cards.add(new Card(diamonds, Rank.SIX));
//        //cards.add(new Card(diamonds, Rank.FIVE));
//        cards.add(new Card(diamonds, Rank.FOUR));
//        //cards.add(new Card(diamonds, Rank.THREE));
//        //cards.add(new Card(diamonds, Rank.TWO));
//        return cards;
//    }
//
//    private static List<Card> getCardsE() {
//        List<Card> cards = new ArrayList<>();
//        Suit spades = Suit.SPADES;
//        Suit hearts = Suit.HEARTS;
//        Suit clubs = Suit.CLUBS;
//        Suit diamonds = Suit.DIAMONDS;
//
//        //cards.add(new Card(spades, Rank.ACE));
//        //cards.add(new Card(spades, Rank.KING));
//        //cards.add(new Card(spades, Rank.QUEEN));
//        //cards.add(new Card(spades, Rank.JACK));
//        //cards.add(new Card(spades, Rank.TEN));
//        cards.add(new Card(spades, Rank.NINE));
//        //cards.add(new Card(spades, Rank.EIGHT));
//        //cards.add(new Card(spades, Rank.SEVEN));
//        //cards.add(new Card(spades, Rank.SIX));
//        //cards.add(new Card(spades, Rank.FIVE));
//        cards.add(new Card(spades, Rank.FOUR));
//        //cards.add(new Card(spades, Rank.THREE));
//        cards.add(new Card(spades, Rank.TWO));
//
//        cards.add(new Card(hearts, Rank.ACE));
//        //cards.add(new Card(hearts, Rank.KING));
//        //cards.add(new Card(hearts, Rank.QUEEN));
//        //cards.add(new Card(hearts, Rank.JACK));
//        cards.add(new Card(hearts, Rank.TEN));
//        cards.add(new Card(hearts, Rank.NINE));
//        //cards.add(new Card(hearts, Rank.EIGHT));
//        //cards.add(new Card(hearts, Rank.SEVEN));
//        //cards.add(new Card(hearts, Rank.SIX));
//        cards.add(new Card(hearts, Rank.FIVE));
//        //cards.add(new Card(hearts, Rank.FOUR));
//        cards.add(new Card(hearts, Rank.THREE));
//        //cards.add(new Card(hearts, Rank.TWO));
//
//        //cards.add(new Card(clubs, Rank.ACE));
//        //cards.add(new Card(clubs, Rank.KING));
//        //cards.add(new Card(clubs, Rank.QUEEN));
//        //cards.add(new Card(clubs, Rank.JACK));
//        //cards.add(new Card(clubs, Rank.TEN));
//        //cards.add(new Card(clubs, Rank.NINE));
//        cards.add(new Card(clubs, Rank.EIGHT));
//        //cards.add(new Card(clubs, Rank.SEVEN));
//        cards.add(new Card(clubs, Rank.SIX));
//        cards.add(new Card(clubs, Rank.FIVE));
//        //cards.add(new Card(clubs, Rank.FOUR));
//        cards.add(new Card(clubs, Rank.THREE));
//        //cards.add(new Card(clubs, Rank.TWO));
//
//        //cards.add(new Card(diamonds, Rank.ACE));
//        //cards.add(new Card(diamonds, Rank.KING));
//        //cards.add(new Card(diamonds, Rank.QUEEN));
//        //cards.add(new Card(diamonds, Rank.JACK));
//        //cards.add(new Card(diamonds, Rank.TEN));
//        //cards.add(new Card(diamonds, Rank.NINE));
//        //cards.add(new Card(diamonds, Rank.EIGHT));
//        cards.add(new Card(diamonds, Rank.SEVEN));
//        //cards.add(new Card(diamonds, Rank.SIX));
//        //cards.add(new Card(diamonds, Rank.FIVE));
//        //cards.add(new Card(diamonds, Rank.FOUR));
//        //cards.add(new Card(diamonds, Rank.THREE));
//        //cards.add(new Card(diamonds, Rank.TWO));
//
//        return cards;
//    }
//
//    private static List<Card> getCardsS() {
//        List<Card> cards = new ArrayList<>();
//        Suit spades = Suit.SPADES;
//        Suit hearts = Suit.HEARTS;
//        Suit clubs = Suit.CLUBS;
//        Suit diamonds = Suit.DIAMONDS;
//
//        //cards.add(new Card(spades, Rank.ACE));
//        cards.add(new Card(spades, Rank.KING));
//        cards.add(new Card(spades, Rank.QUEEN));
//        cards.add(new Card(spades, Rank.JACK));
//        cards.add(new Card(spades, Rank.TEN));
//        //cards.add(new Card(spades, Rank.NINE));
//        //cards.add(new Card(spades, Rank.EIGHT));
//        cards.add(new Card(spades, Rank.SEVEN));
//        cards.add(new Card(spades, Rank.SIX));
//        //cards.add(new Card(spades, Rank.FIVE));
//        //cards.add(new Card(spades, Rank.FOUR));
//        cards.add(new Card(spades, Rank.THREE));
//        //cards.add(new Card(spades, Rank.TWO));
//
//        //cards.add(new Card(hearts, Rank.ACE));
//        //cards.add(new Card(hearts, Rank.KING));
//        //cards.add(new Card(hearts, Rank.QUEEN));
//        //cards.add(new Card(hearts, Rank.JACK));
//        //cards.add(new Card(hearts, Rank.TEN));
//        //cards.add(new Card(hearts, Rank.NINE));
//        //cards.add(new Card(hearts, Rank.EIGHT));
//        //cards.add(new Card(hearts, Rank.SEVEN));
//        //cards.add(new Card(hearts, Rank.SIX));
//        //cards.add(new Card(hearts, Rank.FIVE));
//        //cards.add(new Card(hearts, Rank.FOUR));
//        //cards.add(new Card(hearts, Rank.THREE));
//        //cards.add(new Card(hearts, Rank.TWO));
//
//        //cards.add(new Card(clubs, Rank.ACE));
//        //cards.add(new Card(clubs, Rank.KING));
//        //cards.add(new Card(clubs, Rank.QUEEN));
//        //cards.add(new Card(clubs, Rank.JACK));
//        //cards.add(new Card(clubs, Rank.TEN));
//        cards.add(new Card(clubs, Rank.NINE));
//        //cards.add(new Card(clubs, Rank.EIGHT));
//        cards.add(new Card(clubs, Rank.SEVEN));
//        //cards.add(new Card(clubs, Rank.SIX));
//        //cards.add(new Card(clubs, Rank.FIVE));
//        cards.add(new Card(clubs, Rank.FOUR));
//        //cards.add(new Card(clubs, Rank.THREE));
//        cards.add(new Card(clubs, Rank.TWO));
//
//        //cards.add(new Card(diamonds, Rank.ACE));
//        //cards.add(new Card(diamonds, Rank.KING));
//        //cards.add(new Card(diamonds, Rank.QUEEN));
//        //cards.add(new Card(diamonds, Rank.JACK));
//        cards.add(new Card(diamonds, Rank.TEN));
//        //cards.add(new Card(diamonds, Rank.NINE));
//        //cards.add(new Card(diamonds, Rank.EIGHT));
//        //cards.add(new Card(diamonds, Rank.SEVEN));
//        //cards.add(new Card(diamonds, Rank.SIX));
//        //cards.add(new Card(diamonds, Rank.FIVE));
//        //cards.add(new Card(diamonds, Rank.FOUR));
//        cards.add(new Card(diamonds, Rank.THREE));
//        //cards.add(new Card(diamonds, Rank.TWO));
//        return cards;
//    }
//
//    private static List<Card> getCardsW() {
//        List<Card> cards = new ArrayList<>();
//        Suit spades = Suit.SPADES;
//        Suit hearts = Suit.HEARTS;
//        Suit clubs = Suit.CLUBS;
//        Suit diamonds = Suit.DIAMONDS;
//
//        //cards.add(new Card(spades, Rank.ACE));
//        //cards.add(new Card(spades, Rank.KING));
//        //cards.add(new Card(spades, Rank.QUEEN));
//        //cards.add(new Card(spades, Rank.JACK));
//        //cards.add(new Card(spades, Rank.TEN));
//        //cards.add(new Card(spades, Rank.NINE));
//        cards.add(new Card(spades, Rank.EIGHT));
//        //cards.add(new Card(spades, Rank.SEVEN));
//        //cards.add(new Card(spades, Rank.SIX));
//        cards.add(new Card(spades, Rank.FIVE));
//        //cards.add(new Card(spades, Rank.FOUR));
//        //cards.add(new Card(spades, Rank.THREE));
//        //cards.add(new Card(spades, Rank.TWO));
//
//        //cards.add(new Card(hearts, Rank.ACE));
//        //cards.add(new Card(hearts, Rank.KING));
//        cards.add(new Card(hearts, Rank.QUEEN));
//        cards.add(new Card(hearts, Rank.JACK));
//        //cards.add(new Card(hearts, Rank.TEN));
//        //cards.add(new Card(hearts, Rank.NINE));
//        //cards.add(new Card(hearts, Rank.EIGHT));
//        cards.add(new Card(hearts, Rank.SEVEN));
//        //cards.add(new Card(hearts, Rank.SIX));
//        //cards.add(new Card(hearts, Rank.FIVE));
//        //cards.add(new Card(hearts, Rank.FOUR));
//        //cards.add(new Card(hearts, Rank.THREE));
//        cards.add(new Card(hearts, Rank.TWO));
//
//        //cards.add(new Card(clubs, Rank.ACE));
//        cards.add(new Card(clubs, Rank.KING));
//        //cards.add(new Card(clubs, Rank.QUEEN));
//        //cards.add(new Card(clubs, Rank.JACK));
//        //cards.add(new Card(clubs, Rank.TEN));
//        //cards.add(new Card(clubs, Rank.NINE));
//        //cards.add(new Card(clubs, Rank.EIGHT));
//        //cards.add(new Card(clubs, Rank.SEVEN));
//        //cards.add(new Card(clubs, Rank.SIX));
//        //cards.add(new Card(clubs, Rank.FIVE));
//        //cards.add(new Card(clubs, Rank.FOUR));
//        //cards.add(new Card(clubs, Rank.THREE));
//        //cards.add(new Card(clubs, Rank.TWO));
//
//        cards.add(new Card(diamonds, Rank.ACE));
//        //cards.add(new Card(diamonds, Rank.KING));
//        cards.add(new Card(diamonds, Rank.QUEEN));
//        //cards.add(new Card(diamonds, Rank.JACK));
//        //cards.add(new Card(diamonds, Rank.TEN));
//        cards.add(new Card(diamonds, Rank.NINE));
//        //cards.add(new Card(diamonds, Rank.EIGHT));
//        //cards.add(new Card(diamonds, Rank.SEVEN));
//        cards.add(new Card(diamonds, Rank.SIX));
//        cards.add(new Card(diamonds, Rank.FIVE));
//        //cards.add(new Card(diamonds, Rank.FOUR));
//        //cards.add(new Card(diamonds, Rank.THREE));
//        cards.add(new Card(diamonds, Rank.TWO));
//        return cards;
//    }

}





        /*int[] cards = {
                0, 64, 1028, 17416,
                4112, 1536, 512, 576,
                1444, 256, 0, 8192,
                0, 160, 192, 384
        };
        int trump=0;
        int leader=3;
        int [] trickSuits={1, 1, -1};
        int [] trickRanks={3, 4, 0};
                int[] resultTab = ddsSolver.calcBestCards(cards, trump, leader, trickSuits, trickRanks);
        int result = ddsSolver.calcDDTable(cards, trump, leader, trickSuits, trickRanks);
        System.out.println("plesikaa "+resultTab.length + " plesik:"+result);

        */