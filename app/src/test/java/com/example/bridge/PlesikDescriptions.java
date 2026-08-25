package com.example.bridge;

import com.example.bridge.bidding.Tools.*;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Set;

public class PlesikDescriptions {

    @Test
    public void testAILicytacja() throws Exception {
        // Wczytujemy dane WYŁĄCZNIE z test.pbn
        File file = new File("app/src/test/java/com/example/bridge/test.pbn");
        if (!file.exists()) {
            file = new File("src/test/java/com/example/bridge/test.pbn");
        }

        String content = new String(Files.readAllBytes(file.toPath()));
        Game game = Game.parse(content);

        // Czyścimy aukcję, aby AI licytowało samodzielnie od początku rozdania
        game.getAuction().clear();
        game.bidSystemNS = "NatC";
        game.bidSystemEW = "PassOnly";

        BiddingState state = new BiddingState(game);

        System.out.println("AI North trzyma rękę: " + game.getDeal().get(Direction.N));
        System.out.println("AI South trzyma rękę: " + game.getDeal().get(Direction.S));
        System.out.println("--- Rozpoczynamy licytację (Plesik verbose) ---\n");

        while (!state.getContract().isAuctionComplete()) {
            Direction turn = state.getNextToAct().getDirection();
            PositionCalls choices = state.getCallChoices();
            CallDetails best = choices.getBestCall();

            if (best == null) {
                System.err.println("BŁĄD: AI " + turn + " nie wie co zalicytować!");
                break;
            }
            if (turn.equals(Direction.N) || turn.equals(Direction.S)) {
                System.out.println(turn + " licytuje: " + best.getCall());
                String ruleId = best.getMatchedLogID(state.getNextToAct());
                if (ruleId != null) {
                    System.out.println("   [ID: " + ruleId + "]");
                }
                System.out.println("   [Uzasadnienie: ");
                String description = best.getDescription(state.getNextToAct());
                if (description != null && !description.isEmpty()) {
                    for (String line : description.split("\n")) {
                        System.out.println("      " + line);
                    }
                }
                System.out.println("   ]");
                state.makeCall(best);
                printPublicKnowledge(state);
            } else {
                state.makeCall(best);
            }


        }

        System.out.println("\n--- Koniec licytacji ---");
        System.out.println("Finalny kontrakt: " + state.getContract().toString());

    }

    private void printPublicKnowledge(BiddingState state) {
        System.out.println("   --- WIEDZA PUBLICZNA ---");
        for (Direction d : Direction.values()) {
            PositionState pos = state.getPositions().get(d);
            if (pos == null) continue;
            HandSummary summary = pos.getPublicHandSummary();
            if (summary == null) continue;

            StringBuilder sb = new StringBuilder();
            Range p = summary.getHighCardPoints();
            if (p != null)
                sb.append("HCP: ").append(p.getMin()).append("-").append(p.getMax()).append(" ");

            Set<Integer> aces = summary.getCountAces();
            if (aces != null && !aces.isEmpty()) {
                sb.append("Asy: ").append(aces).append(" ");
            }
            Set<Integer> kings = summary.getCountKings();
            if (kings != null && !kings.isEmpty()) {
                sb.append("Krole: ").append(kings).append(" ");
            }

            Suit[] orderedSuits = {Suit.Spades, Suit.Hearts, Suit.Diamonds, Suit.Clubs};
            for (Suit s : orderedSuits) {
                HandSummary.SuitSummary suitSum = summary.getSuits().get(s);
                if (suitSum != null) {
                    Range shape = suitSum.getShape();
                    if (shape != null && shape.getMin() > 0)
                        sb.append(s.toSymbol()).append(":").append(shape.getMin()).append("+ ");
                }
            }
            if (sb.length() > 0) System.out.println("   " + d + ": " + sb.toString());
        }

        Suit nsTrump = state.getPositions().get(Direction.N).getPairState().getTrumpSuit();
        if (nsTrump != null) System.out.println("   UZGODNIONY ATUT NS: " + nsTrump.toSymbol());
        System.out.println("   ------------------------");
    }
}