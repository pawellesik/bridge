package com.example.bridge.bridgit;

import java.util.*;

public class HandSummary extends State {

    public static class SuitSummary {
        public Range shape;
        public Range dummyPoints;
        public Range longHandPoints;
        public Integer ruleOf9Points;
        public Range losers;
        public Set<Integer> keyCards;
        public Boolean haveQueen;
        public Boolean stopped;
        public Boolean firstRoundControl;
        public Boolean secondRoundControl;
        public Range quality;

        public SuitSummary() {}

        public SuitSummary(SuitSummary other) {
            this.shape = other.shape;
            this.dummyPoints = other.dummyPoints;
            this.longHandPoints = other.longHandPoints;
            this.ruleOf9Points = other.ruleOf9Points;
            this.losers = other.losers;
            this.keyCards = other.keyCards != null ? new HashSet<>(other.keyCards) : null;
            this.haveQueen = other.haveQueen;
            this.stopped = other.stopped;
            this.firstRoundControl = other.firstRoundControl;
            this.secondRoundControl = other.secondRoundControl;
            this.quality = other.quality;
        }

        public void combine(SuitSummary other, CombineRule cr) {
            this.shape = combineRange(this.shape, other.shape, cr);
            this.dummyPoints = combineRange(this.dummyPoints, other.dummyPoints, cr);
            this.longHandPoints = combineRange(this.longHandPoints, other.longHandPoints, cr);
            this.ruleOf9Points = combineInt(this.ruleOf9Points, other.ruleOf9Points, cr);
            this.losers = combineRange(this.losers, other.losers, cr);
            this.keyCards = combineIntSet(this.keyCards, other.keyCards, cr);
            this.haveQueen = combineBool(this.haveQueen, other.haveQueen, cr);
            this.stopped = combineBool(this.stopped, other.stopped, cr);
            this.firstRoundControl = combineBool(this.firstRoundControl, other.firstRoundControl, cr);
            this.secondRoundControl = combineBool(this.secondRoundControl, other.secondRoundControl, cr);
            this.quality = combineRange(this.quality, other.quality, cr);
        }

        public Range getShape() {
            return shape != null ? shape : new Range(0, 13);
        }

        public void trimShape(int claimed) {
            Range s = getShape();
            if (s.max + claimed - s.min > 13) {
                this.shape = new Range(s.min, 13 - claimed + s.min);
            }
        }
        
        public static class ShowState {
            private final HandSummary handSummary;
            private final SuitSummary suitSummary;

            public ShowState(HandSummary handSummary, SuitSummary suitSummary) {
                this.handSummary = handSummary;
                this.suitSummary = suitSummary;
            }

            public void showShape(int min, int max) {
                suitSummary.shape = combineRange(suitSummary.shape, new Range(min, max), CombineRule.Show);
            }

            public void showDummyPoints(int min, int max) {
                suitSummary.dummyPoints = combineRange(suitSummary.dummyPoints, new Range(min, max), CombineRule.Show);
                handSummary.showPoints(min, max);
            }

            public void showLongHandPoints(int min, int max) {
                suitSummary.longHandPoints = combineRange(suitSummary.longHandPoints, new Range(min, max), CombineRule.Show);
                handSummary.showPoints(min, max);
            }

            public void showQuality(SuitQuality min, SuitQuality max) {
                suitSummary.quality = combineRange(suitSummary.quality, new Range(min.getValue(), max.getValue()), CombineRule.Show);
            }

            public void showLosers(int min, int max) {
                suitSummary.losers = combineRange(suitSummary.losers, new Range(min, max), CombineRule.Show);
            }

            public void showKeyCards(Set<Integer> keyCards) {
                suitSummary.keyCards = combineIntSet(suitSummary.keyCards, keyCards, CombineRule.Show);
            }

            public void showHaveQueen(boolean haveQueen) {
                suitSummary.haveQueen = combineBool(suitSummary.haveQueen, haveQueen, CombineRule.Show);
            }

            public void showStopped(boolean stopped) {
                suitSummary.stopped = combineBool(suitSummary.stopped, stopped, CombineRule.Show);
            }

            public void showRuleOf9Points(int points) {
                suitSummary.ruleOf9Points = combineInt(suitSummary.ruleOf9Points, points, CombineRule.Show);
            }

            public void showFirstRoundControl(boolean control) {
                suitSummary.firstRoundControl = combineBool(suitSummary.firstRoundControl, control, CombineRule.Show);
            }

            public void showSecondRoundControl(boolean control) {
                suitSummary.secondRoundControl = combineBool(suitSummary.secondRoundControl, control, CombineRule.Show);
            }
        }
    }

    public Range highCardPoints;
    public Range startingPoints;
    public Range points;
    public Range noTrumpLongHandPoints;
    public Range noTrumpDummyPoints;
    public Range losers;
    public Boolean isBalanced;
    public Boolean isFlat;
    public Set<Integer> countAces;
    public Set<Integer> countKings;
    public final Map<Suit, SuitSummary> suits = new HashMap<>();

    public HandSummary() {
        for (Suit s : Suit.values()) {
            suits.put(s, new SuitSummary());
        }
    }

    public HandSummary(HandSummary other) {
        this.highCardPoints = other.highCardPoints;
        this.startingPoints = other.startingPoints;
        this.points = other.points;
        this.noTrumpLongHandPoints = other.noTrumpLongHandPoints;
        this.noTrumpDummyPoints = other.noTrumpDummyPoints;
        this.losers = other.losers;
        this.isBalanced = other.isBalanced;
        this.isFlat = other.isFlat;
        this.countAces = other.countAces != null ? new HashSet<>(other.countAces) : null;
        this.countKings = other.countKings != null ? new HashSet<>(other.countKings) : null;
        for (Suit s : Suit.values()) {
            suits.put(s, new SuitSummary(other.suits.get(s)));
        }
    }

    protected void showPoints(int min, int max) {
        this.points = combineRange(this.points, new Range(min, max), CombineRule.Show);
    }

    public static class ShowState {
        public final HandSummary handSummary;
        public final Map<Suit, SuitSummary.ShowState> suits = new HashMap<>();

        public ShowState() {
            this(null);
        }

        public ShowState(HandSummary startState) {
            this.handSummary = (startState == null) ? new HandSummary() : new HandSummary(startState);
            for (Suit s : Suit.values()) {
                this.suits.put(s, new SuitSummary.ShowState(handSummary, handSummary.suits.get(s)));
            }
        }

        public void showStartingPoints(int min, int max) {
            handSummary.startingPoints = combineRange(handSummary.startingPoints, new Range(min, max), CombineRule.Show);
            handSummary.showPoints(min, max);
        }

        public void showHighCardPoints(int min, int max) {
            handSummary.highCardPoints = combineRange(handSummary.highCardPoints, new Range(min, max), CombineRule.Show);
            handSummary.showPoints(min, max);
        }

        public void showNoTrumpLongHandPoints(int min, int max) {
            handSummary.noTrumpLongHandPoints = combineRange(handSummary.noTrumpLongHandPoints, new Range(min, max), CombineRule.Show);
        }

        public void showNoTrumpDummyPoints(int min, int max) {
            handSummary.noTrumpDummyPoints = combineRange(handSummary.noTrumpDummyPoints, new Range(min, max), CombineRule.Show);
        }

        public void showLosers(int min, int max) {
            handSummary.losers = combineRange(handSummary.losers, new Range(min, max), CombineRule.Show);
        }

        public void showIsBalanced(boolean isBalanced) {
            handSummary.isBalanced = combineBool(handSummary.isBalanced, isBalanced, CombineRule.Show);
        }

        public void showIsFlat(boolean isFlat) {
            handSummary.isFlat = combineBool(handSummary.isFlat, isFlat, CombineRule.Show);
        }

        public void showCountAces(Set<Integer> countAces) {
            handSummary.countAces = combineIntSet(handSummary.countAces, countAces, CombineRule.Show);
        }

        public void showCountKings(Set<Integer> countKings) {
            handSummary.countKings = combineIntSet(handSummary.countKings, countKings, CombineRule.Show);
        }

        public void combine(HandSummary other, CombineRule cr) {
            handSummary.combine(other, cr);
        }
    }

    public void combine(HandSummary other, CombineRule cr) {
        this.points = combineRange(this.points, other.points, cr);
        this.highCardPoints = combineRange(this.highCardPoints, other.highCardPoints, cr);
        this.startingPoints = combineRange(this.startingPoints, other.startingPoints, cr);
        this.noTrumpLongHandPoints = combineRange(this.noTrumpLongHandPoints, other.noTrumpLongHandPoints, cr);
        this.noTrumpDummyPoints = combineRange(this.noTrumpDummyPoints, other.noTrumpDummyPoints, cr);
        this.losers = combineRange(this.losers, other.losers, cr);
        this.isBalanced = combineBool(this.isBalanced, other.isBalanced, cr);
        this.isFlat = combineBool(this.isFlat, other.isFlat, cr);
        this.countAces = combineIntSet(this.countAces, other.countAces, cr);
        this.countKings = combineIntSet(this.countKings, other.countKings, cr);
        for (Suit s : Suit.values()) {
            this.suits.get(s).combine(other.suits.get(s), cr);
        }
        trimShape();
    }

    public void trimShape() {
        int claimed = 0;
        for (Suit s : Suit.values()) {
            claimed += suits.get(s).getShape().min;
        }
        for (Suit s : Suit.values()) {
            suits.get(s).trimShape(claimed);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(rangeToString("Points", points));
        sb.append(rangeToString("HCP", highCardPoints));
        for (Suit s : Suit.values()) {
            sb.append(rangeToString(s.toString(), suits.get(s).shape));
        }
        return sb.toString();
    }

    private String rangeToString(String name, Range r) {
        if (r != null) {
            if (r.min == r.max) return name + ": " + r.min + "\n";
            return name + ": " + r.min + "-" + r.max + "\n";
        }
        return "";
    }
}
