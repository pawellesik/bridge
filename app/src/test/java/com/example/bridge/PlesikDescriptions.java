package com.example.bridge;

import com.example.bridge.bidding.Tools.*;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

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
                System.out.println("   [Uzasadnienie: " + best.getDescription(state.getNextToAct()) + "]");
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
            if (p != null && p.getMin() > 0)
                sb.append("HCP: ").append(p.getMin()).append("-").append(p.getMax()).append(" ");

            for (Suit s : Suit.values()) {
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