package com.example.bridge.bidding.Tools;

import com.example.bridge.bidding.Tools.PBN.FromString;

import java.util.*;

public class Game {
    public String event = null;
    public int board = 0;
    public Scoring scoring = Scoring.MP;

    public String bidSystemEW = null;
    public String bidSystemNS = null;

    public Vulnerable vulnerable = Vulnerable.None;
    public Direction dealer = Direction.N;
    private final Deal deal;
    public Direction declarer = null;

    public Contract contract = null;

    private final Auction auction;

    public Game() {
        this.deal = new Deal(this);
        this.auction = new Auction(this);
    }

    public Deal getDeal() {
        return deal;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setStandardBoard(int board) {
        if (board <= 0) {
            throw new IllegalArgumentException("Board number must be >=0. Value is " + board);
        }
        this.board = board;
        this.dealer = Direction.values()[(board - 1) % 4];
        int vulOffset = ((board - 1) / 4);
        this.vulnerable = Vulnerable.values()[(board - 1 + vulOffset) % 4];
    }

    public static Game parse(String dealStr, String vulnerableStr) {
        Game game = new Game();
        game.parseDeal(dealStr, true);
        if (vulnerableStr != null) {
            try {
                game.vulnerable = Vulnerable.valueOf(vulnerableStr);
            } catch (IllegalArgumentException e) {
                // Handle different names for Vulnerable if necessary
            }
        }
        return game;
    }

    public static Game parse(String pbnGame) {
        Game game = new Game();
        List<FromString.PBNTag> tags = FromString.tokenizeTags(pbnGame);
        for (FromString.PBNTag tag : tags) {
            game.tags.put(tag.name, tag.value);
            switch (tag.name) {
                case "Dealer":
                    game.dealer = Direction.valueOf(tag.value);
                    break;
                case "Vulnerable":
                    game.vulnerable = Vulnerable.valueOf(tag.value);
                    break;
                case "Deal":
                    game.parseDeal(tag.value, false);
                    break;
                case "Auction":
                    game.parseAuction(String.join(" ", tag.data));
                    break;
                // Add more tags as needed
            }
        }
        return game;
    }

    public void parseAuction(String auctionStr) {
        this.auction.parse(auctionStr);
    }

    public void parseDeal(String dealStr, boolean overrideDealer) {
        if (dealStr == null) {
            throw new NullPointerException("deal");
        }
        if (dealStr.length() < 9) {
            throw new RuntimeException("deal parameter is too short to be valid PBN deal format");
        }
        Direction direction;
        int dealDataStart = 0;
        if (dealStr.length() > 2 && dealStr.charAt(1) == ':') {
            try {
                direction = Direction.valueOf(dealStr.substring(0, 1));
                dealDataStart = 2;
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Deal prefix " + dealStr.substring(0, 2) + " is invalid");
            }

            if (overrideDealer) {
                this.dealer = direction;
            } else if (direction != this.dealer) {
                throw new IllegalArgumentException("Deal direction prefix " + direction + " does not match game Dealer " + this.dealer);
            }
        } else {
            direction = this.dealer;
            dealDataStart = 0;
        }

        String[] handStrings = dealStr.substring(dealDataStart).split(" ");
        if (handStrings.length != 4) {
            throw new IllegalArgumentException("deal must contain 4 hands");
        }

        for (String handString : handStrings) {
            deal.setHand(direction, Hand.parse(handString));
            direction = direction.leftHandOpponent();
        }

        // Card validation logic could be added here
    }

    public void dealCards(List<Card> deck) {
        if (deck.size() != 52) {
            throw new IllegalArgumentException("Deck must contain 52 cards. deck parameter count = " + deck.size());
        }
        Direction direction = Direction.N;
        for (int h = 0; h < 4; h++) {
            deal.setHand(direction, new Hand(deck.subList(h * 13, (h + 1) * 13)));
            direction = direction.leftHandOpponent();
        }
    }

    public void dealRandomHands() {
        dealCards(Card.newDeck(true));
    }
    public Map<String, String> tags = new HashMap<>();
    public Map<String, List<String>> tagData = new HashMap<>();
    public Map<String, String> tagCommentary = new HashMap<>();

    public static final String[] MTS = new String[]{
            "Event", "Site", "Date", "Board", "West", "North", "East", "South",
            "Dealer", "Vulnerable", "Deal", "Scoring", "Declarer", "Contract", "Result"
    };

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String tagName : MTS) {
            switch (tagName) {
                case "Event":
                    sb.append(tagString(tagName, event));
                    break;
                case "Dealer":
                    sb.append(tagString(tagName, dealer.toString()));
                    break;
                case "Deal":
                    sb.append(tagString(tagName, deal.toString()));
                    break;
                case "Vulnerable":
                    sb.append(tagString(tagName, vulnerable.toString()));
                    break;
                case "Scoring":
                    sb.append(tagString(tagName, scoring.toString()));
                    break;
                case "Board":
                    sb.append(tagString(tagName, board == 0 ? "" : String.valueOf(board)));
                    break;
                case "Declarer":
                    sb.append(tagString(tagName, declarer == null ? "" : declarer.toString()));
                    break;
                case "Contract":
                    sb.append(tagString(tagName, contract == null ? "" : contract.toString()));
                    break;
                default:
                    if (tags.containsKey(tagName)) {
                        sb.append(tagString(tagName, tags.get(tagName)));
                        // Add tagData and commentary if needed
                    } else {
                        sb.append(tagString(tagName, ""));
                    }
                    break;
            }
        }
        sb.append(auction.toString());
        return sb.toString();
    }

    private String tagString(String key, String value) {
        if (value == null) return "[" + key + " \"\"]\n";
        return "[" + key + " \"" + value + "\"]\n";
    }
}




























































































































































































































































































































































































































































































