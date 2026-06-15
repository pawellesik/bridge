package com.example.bridge.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Trick {
    private final Map<String, Card> cardsOnTableMap = new LinkedHashMap<>();
    private final List<Card> cardsOnTable = new ArrayList<>();
    private String winnerTrick = "";

    public void setWinnerTrick (String winnerTrick){
        this.winnerTrick = winnerTrick;
    }
    public void addCard(String direction, Card card) {
        cardsOnTableMap.put(direction, card);
        cardsOnTable.add(card);
    }
    public Map<String, Card> getCardsOnTableMap() {
        return cardsOnTableMap;
    }

    public Card getCard(String direction) {
        return cardsOnTableMap.get(direction);
    }
    public List<Card> getCardsOnTable(){
        return cardsOnTable;
    }
}


