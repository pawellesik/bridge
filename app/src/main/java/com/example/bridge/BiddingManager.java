package com.example.bridge;

import com.example.bridge.model.Card;
import com.example.bridge.model.Player;
import com.example.bridge.model.Suit;

import java.util.ArrayList;
import java.util.List;

public class BiddingManager {

    private final List<Player> players;
    private final GameController.GameCallback callback;

    public BiddingManager(List<Player> players, GameController.GameCallback callback) {
        this.players = players;
        this.callback = callback;
    }

    public String determineBestContract() {
        switchCardsIfAreToWeaks();
        int totalHCP = getHPCFromSNPlayers();
        String contractColor = getContractColor();

        //if (totalHCP >= 25) return "3NT";
        //if (totalHCP >= 20) return "2NT";
        //if (totalHCP >= 15) return "1NT";
        return contractColor;
    }

    private String getContractColor() {
        String contractColor;
        int countSpadesPlayerNorth = players.get(0).countSuit(Suit.SPADES);
        int countSpadesPlayerSouth = players.get(2).countSuit(Suit.SPADES);

        int countHeartsPlayerNorth = players.get(0).countSuit(Suit.HEARTS);
        int countHeartsPlayerSouth = players.get(2).countSuit(Suit.HEARTS);

        int countClubsPlayerNorth = players.get(0).countSuit(Suit.CLUBS);
        int countClubsPlayerSouth = players.get(2).countSuit(Suit.CLUBS);

        int countDiamondsPlayerNorth = players.get(0).countSuit(Suit.DIAMONDS);
        int countDiamondsPlayerSouth = players.get(2).countSuit(Suit.DIAMONDS);

        //SH
        if (countSpadesPlayerNorth + countSpadesPlayerSouth >= 8) {
            contractColor = "Spades";
        } else if (countHeartsPlayerNorth + countHeartsPlayerSouth >= 8) {
            contractColor = "Hearts";
        }
        //NT
        else if (playrsHaveHoldInAllColors(players.get(0), players.get(2))) {
            contractColor = "NT";
        }
        //DC
        else if (countClubsPlayerNorth + countClubsPlayerSouth >= 8) {
            contractColor = "Clubs";
        } else if (countDiamondsPlayerNorth + countDiamondsPlayerSouth >= 8) {
            contractColor = "Diamonds";
        }
        //SHDC
        else if (countSpadesPlayerNorth + countSpadesPlayerSouth == 7) {
            contractColor = "Spades";
        } else if (countHeartsPlayerNorth + countHeartsPlayerSouth == 7) {
            contractColor = "Hearts";
        } else if (countClubsPlayerNorth + countClubsPlayerSouth == 7) {
            contractColor = "Clubs";
        } else if (countDiamondsPlayerNorth + countDiamondsPlayerSouth == 7) {
            contractColor = "Diamonds";
        } else if (countSpadesPlayerNorth + countSpadesPlayerSouth == 7) {
            contractColor = "Spades";
        } else if (countHeartsPlayerNorth + countHeartsPlayerSouth == 7) {//TODO
            contractColor = "Hearts";
        } else {
            contractColor = "NT";
        }
        return contractColor;
    }

    private boolean playrsHaveHoldInAllColors(Player player1, Player player2) {
        if ((player1.haveHoldInColor("Spades") || (player2.haveHoldInColor("Spades")))
                && (player1.haveHoldInColor("Hearts") || (player2.haveHoldInColor("Hearts")))
                && (player1.haveHoldInColor("Clubs") || (player2.haveHoldInColor("Clubs")))
                && (player1.haveHoldInColor("Diamonds") || (player2.haveHoldInColor("Diamonds")))
        ) {
            return true;
        } else {
            return false;
        }
    }


    private void switchCardsIfAreToWeaks() {
        int maxSuitS = numberLongestColor(players.get(2));
        int maxSuitN = numberLongestColor(players.get(0));
        int hcp = getHPCFromSNPlayers();

        boolean isStrongEnough = (maxSuitS > 6 && hcp >= 15) || (maxSuitN > 6 && hcp >= 15);

        if (!isStrongEnough && hcp < 20) {
            List<Card> h0 = new ArrayList<>(players.get(0).getHand());
            List<Card> h1 = new ArrayList<>(players.get(1).getHand());
            List<Card> h2 = new ArrayList<>(players.get(2).getHand());
            List<Card> h3 = new ArrayList<>(players.get(3).getHand());

            players.get(0).clearHand();
            players.get(1).clearHand();
            players.get(2).clearHand();
            players.get(3).clearHand();

            players.get(0).addCards(h1);
            players.get(1).addCards(h0);
            players.get(2).addCards(h3);
            players.get(3).addCards(h2);

            for (int i = 0; i < 4; i++) {
                callback.onHandUpdated(i);
            }
        }
    }

    private int numberLongestColor(Player player) {
        int maxCount = 0;
        for (Suit s : Suit.values()) {
            int count = player.countSuit(s);
            if (count > maxCount) {
                maxCount = count;
            }
        }
        return maxCount;
    }

    private int getHPCFromSNPlayers() {
        int hcpSouth = players.get(2).calculateHCP();
        int hcpNorth = players.get(0).calculateHCP();
        return hcpSouth + hcpNorth;
    }
}
