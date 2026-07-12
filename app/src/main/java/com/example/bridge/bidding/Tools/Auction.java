package com.example.bridge.bidding.Tools;

import java.util.*;
import java.util.stream.Collectors;

public class Auction extends ArrayList<Auction.AnnotatedCall> {
    private final Game game;

    public Auction(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public List<Call> getCalls() {
        return this.stream().map(ac -> ac.call).collect(Collectors.toList());
    }

    public static class AnnotatedCall {
        public Call call;
        public String note;

        public boolean hasNote() {
            return note != null && !note.isEmpty();
        }
    }

    public void add(CallDetails callDetails) {
        String note = callDetails.getAnnotations().stream()
                .map(a -> a.getType() + ": " + a.getText())
                .collect(Collectors.joining(";"));
        add(callDetails.getCall(), note);
    }

    public void add(Call call, String note) {
        AnnotatedCall ac = new AnnotatedCall();
        ac.call = call;
        ac.note = note;
        this.add(ac);
    }

    public void parse(String auctionText) {
        this.clear();
        if (auctionText == null || auctionText.isEmpty()) return;
        String[] tokens = auctionText.split("\\s+");
        for (String token : tokens) {
            if (token.startsWith("=")) {
                // TODO: Handle notes
            } else if (!token.startsWith("$") && !token.equals("+")) {
                add(Call.parse(token), null);
            }
        }
    }

    @Override
    public String toString() {
        if (this.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        List<String> notes = new ArrayList<>();
        sb.append("[Auction \"").append(game.dealer.toString()).append("\"]\n");

        int numPasses = 0;
        int numPassesEndsAuction = 4;
        int numCallsThisLine = 0;

        for (int i = 0; i < this.size(); i++) {
            AnnotatedCall ac = this.get(i);
            Call call = ac.call;
            sb.append(call.toString());
            numCallsThisLine++;

            if (ac.hasNote()) {
                int noteIndex = notes.indexOf(ac.note);
                if (noteIndex < 0) {
                    noteIndex = notes.size();
                    notes.add(ac.note);
                }
                sb.append(" =").append(noteIndex + 1).append("=");
            }

            if (call.equals(Call.PASS)) {
                numPasses++;
            } else {
                numPasses = 0;
                numPassesEndsAuction = 3;
            }

            if (i + 1 == this.size() && numPasses < numPassesEndsAuction) {
                sb.append(" +");
            }

            if (numCallsThisLine == 4) {
                sb.append("\n");
                numCallsThisLine = 0;
            } else if (i + 1 < this.size()) {
                sb.append(" ");
            }
        }

        if (numCallsThisLine > 0) {
            sb.append("\n");
        }

        for (int i = 0; i < notes.size(); i++) {
            sb.append("[Note \"").append(i + 1).append(":").append(notes.get(i)).append("\"]\n");
        }

        return sb.toString();
    }
}




























































































































































































































































































































































































































































































