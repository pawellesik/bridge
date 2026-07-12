package com.example.bridge.bidding;

import com.example.licytacja.moje.BridgeBidder.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Plesik {

    @Test
    public void testAILicytacja() {
        // 1. Tworzymy obiekt gry
        Game game = new Game();

        game.getDeal().put(Direction.N, Hand.parse("AK9.K4T.QJT3.842"));
        game.getDeal().put(Direction.S, Hand.parse("T2.QJT3.T5432.98"));


        // 3. Konfiguracja licytacji
        game.dealer = Direction.N;
        game.bidSystemNS = "NatC";
        game.bidSystemEW = "NatC";

        BiddingState state = new BiddingState(game);

        System.out.println("AI North trzyma rękę: " + game.getDeal().get(Direction.N));
        System.out.println("AI South trzyma rękę: " + game.getDeal().get(Direction.S));
        System.out.println("--- Rozpoczynamy licytację ---\n");

        // 4. Pętla licytacji aż do końca (3 pasy)
        while (!state.getContract().isAuctionComplete()) {
            Direction turn = state.getNextToAct().getDirection();

            if (turn == Direction.N || turn == Direction.S) {
                PositionCalls choices = state.getCallChoices();
                CallDetails best = choices.getBestCall();
                
                if (best == null) {
                    System.err.println("BŁĄD: AI " + turn + " nie wie co zalicytować!");
                    break;
                }
                
                System.out.println(turn + " licytuje: " + best.getCall());
                String ruleId = best.getMatchedLogID(state.getNextToAct());
                if (ruleId != null) {
                    System.out.println("   [ID: " + ruleId + "]");
                }
                System.out.println("   [Uzasadnienie: " + best.getDescription(state.getNextToAct()) + "]");
                state.makeCall(best);
                printPublicKnowledge(state);
            }
            else {
                state.makeCall(Call.PASS);
            }
        }

        // 5. Wyświetlamy ostateczny kontrakt
        System.out.println("\n--- Koniec licytacji ---");
        System.out.println("Finalny kontrakt: " + state.getContract().toString());
        System.out.println("\n------------------------------");
    }

    private void printPublicKnowledge(BiddingState state) {
        System.out.println("   --- WIEDZA PUBLICZNA ---");
        for (Direction d : Direction.values()) {
            PositionState pos = state.getPositions().get(d);
            if (pos == null) continue;
            HandSummary summary = pos.getPublicHandSummary();
            if (summary == null) continue;
            
            StringBuilder sb = new StringBuilder();
            
            Range p = summary.getPoints();
            if (p != null && p.getMin() > 0) {
                sb.append("Pkt: ").append(p.getMin()).append("-").append(p.getMax()).append(" ");
            }
            
            for (Suit s : Suit.values()) {
                HandSummary.SuitSummary suitSum = summary.getSuits().get(s);
                if (suitSum != null) {
                    Range shape = suitSum.getShape();
                    if (shape != null && shape.getMin() > 0) {
                        sb.append(s.toSymbol()).append(":").append(shape.getMin()).append("+ ");
                    }
                }
            }
            
            if (summary.getCountAces() != null && !summary.getCountAces().isEmpty()) {
                sb.append("Asy: ").append(summary.getCountAces()).append(" ");
            }
            if (summary.getCountKings() != null && !summary.getCountKings().isEmpty()) {
                sb.append("króle: ").append(summary.getCountKings()).append(" ");
            }

            if (sb.length() > 0) {
                System.out.println("   " + d + ": " + sb.toString());
            }
        }
        
        // Wyświetlamy wspólne HCP i punkty dla par
        printPairHCP(state, Direction.N, Direction.S, "NS");
        printPairPoints(state, Direction.N, Direction.S, "NS");
        printPairHCP(state, Direction.E, Direction.W, "EW");
        printPairPoints(state, Direction.E, Direction.W, "EW");
        
        // Wyświetlamy uzgodnione atuty dla obu par (NS i EW)
        Suit nsTrump = state.getPositions().get(Direction.N).getPairState().getTrumpSuit();
        if (nsTrump != null) System.out.println("   UZGODNIONY ATUT NS: " + nsTrump.toSymbol());

        System.out.println("   ------------------------");
    }

    private void printPairHCP(BiddingState state, Direction d1, Direction d2, String pairName) {
        PositionState p1 = state.getPositions().get(d1);
        PositionState p2 = state.getPositions().get(d2);
        if (p1 == null || p2 == null) return;

        Range hcp1 = p1.getPublicHandSummary().getHighCardPoints();
        Range hcp2 = p2.getPublicHandSummary().getHighCardPoints();

        if (hcp1 != null && hcp2 != null && (hcp1.getMin() > 0 || hcp2.getMin() > 0)) {
            int min = hcp1.getMin() + hcp2.getMin();
            int max = hcp1.getMax() + hcp2.getMax();
            System.out.println("   WSPÓLNE HCP " + pairName + ": " + min + "-" + max);
        }
    }

    private void printPairPoints(BiddingState state, Direction d1, Direction d2, String pairName) {
        PositionState p1 = state.getPositions().get(d1);
        PositionState p2 = state.getPositions().get(d2);
        if (p1 == null || p2 == null) return;

        Range pts1 = p1.getPublicHandSummary().getPoints();
        Range pts2 = p2.getPublicHandSummary().getPoints();

        if (pts1 != null && pts2 != null && (pts1.getMin() > 0 || pts2.getMin() > 0)) {
            int min = pts1.getMin() + pts2.getMin();
            int max = pts1.getMax() + pts2.getMax();
            System.out.println("   WSPÓLNE PKT " + pairName + ": " + min + "-" + max);
        }
    }
}
