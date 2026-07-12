package com.example.bridge.bidding.BridgeBidder;

import java.util.*;

public class HandSummary extends State {
    public static class ShowState {
        private final HandSummary handSummary;
        private final Map<Suit, SuitSummary.ShowState> suits = new EnumMap<>(Suit.class);

        public ShowState() {
            this(null);
        }

        public ShowState(HandSummary startState) {
            this.handSummary = (startState == null) ? new HandSummary() : new HandSummary(startState);
            for (Suit suit : Suit.values()) {
                this.suits.put(suit, new SuitSummary.ShowState(handSummary, handSummary.suits.get(suit)));
            }
        }

        public HandSummary getHandSummary() {
            return handSummary;
        }

        public Map<Suit, SuitSummary.ShowState> getSuits() {
            return suits;
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

        public void combine(HandSummary other, CombineRule combineRule) {
            handSummary.combine(other, combineRule);
        }
    }

    public static class SuitSummary {
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
                suitSummary.quality = combineRange(suitSummary.quality, new Range(min.ordinal(), max.ordinal()), CombineRule.Show);
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

        private Range shape;
        private Range dummyPoints;
        private Range longHandPoints;
        private Range quality;
        private Range losers;
        private Set<Integer> keyCards;
        private Boolean haveQueen;
        private Boolean stopped;
        private Integer ruleOf9Points;
        private Boolean firstRoundControl;
        private Boolean secondRoundControl;

        public SuitSummary() {}

        public SuitSummary(SuitSummary other) {
            this.shape = other.shape;
            this.dummyPoints = other.dummyPoints;
            this.longHandPoints = other.longHandPoints;
            this.quality = other.quality;
            this.losers = other.losers;
            this.keyCards = other.keyCards != null ? new HashSet<>(other.keyCards) : null;
            this.haveQueen = other.haveQueen;
            this.stopped = other.stopped;
            this.ruleOf9Points = other.ruleOf9Points;
            this.firstRoundControl = other.firstRoundControl;
            this.secondRoundControl = other.secondRoundControl;
        }

        public Range getDummyPoints() {
            return dummyPoints;
        }

        public Range getLongHandPoints() {
            return longHandPoints;
        }

        public Range getLosers() {
            return losers;
        }

        public Set<Integer> getKeyCards() {
            return keyCards;
        }

        public Boolean getHaveQueen() {
            return haveQueen;
        }

        public Boolean getStopped() {
            return stopped;
        }

        public Integer getRuleOf9Points() {
            return ruleOf9Points;
        }

        public Boolean getFirstRoundControl() {
            return firstRoundControl;
        }

        public Boolean getSecondRoundControl() {
            return secondRoundControl;
        }

        public void combine(SuitSummary other, CombineRule cr) {
            this.shape = combineRange(this.shape, other.shape, cr);
            this.dummyPoints = combineRange(this.dummyPoints, other.dummyPoints, cr);
            this.longHandPoints = combineRange(this.longHandPoints, other.longHandPoints, cr);
            this.quality = combineRange(this.quality, other.quality, cr);
            this.losers = combineRange(this.losers, other.losers, cr);
            this.haveQueen = combineBool(this.haveQueen, other.haveQueen, cr);
            this.stopped = combineBool(this.stopped, other.stopped, cr);
            this.keyCards = combineIntSet(this.keyCards, other.keyCards, cr);
            this.ruleOf9Points = combineInt(this.ruleOf9Points, other.ruleOf9Points, cr);
            this.firstRoundControl = combineBool(this.firstRoundControl, other.firstRoundControl, cr);
            this.secondRoundControl = combineBool(this.secondRoundControl, other.secondRoundControl, cr);
        }

        public Range getShape() {
            return shape != null ? shape : new Range(0, 13);
        }

        public Range getQuality() {
            return quality != null ? quality : new Range(SuitQuality.Poor.ordinal(), SuitQuality.Solid.ordinal());
        }

        public void trimShape(int claimed) {
            Range s = getShape();
            if (s.getMax() + claimed - s.getMin() > 13) {
                int newMax = 13 - claimed + s.getMin();
                this.shape = new Range(s.getMin(), newMax);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SuitSummary that = (SuitSummary) o;
            return Objects.equals(shape, that.shape) &&
                    Objects.equals(dummyPoints, that.dummyPoints) &&
                    Objects.equals(longHandPoints, that.longHandPoints) &&
                    Objects.equals(quality, that.quality) &&
                    Objects.equals(losers, that.losers) &&
                    Objects.equals(keyCards, that.keyCards) &&
                    Objects.equals(haveQueen, that.haveQueen) &&
                    Objects.equals(stopped, that.stopped) &&
                    Objects.equals(ruleOf9Points, that.ruleOf9Points) &&
                    Objects.equals(firstRoundControl, that.firstRoundControl) &&
                    Objects.equals(secondRoundControl, that.secondRoundControl);
        }

        @Override
        public int hashCode() {
            return Objects.hash(shape, dummyPoints, longHandPoints, quality, losers, keyCards, haveQueen, stopped, ruleOf9Points, firstRoundControl, secondRoundControl);
        }
    }

    private Range points;
    private Range highCardPoints;
    private Range startingPoints;
    private Range noTrumpLongHandPoints;
    private Range noTrumpDummyPoints;
    private Range losers;
    private Boolean isBalanced;
    private Boolean isFlat;
    private Set<Integer> countAces;
    private Set<Integer> countKings;
    private final Map<Suit, SuitSummary> suits = new EnumMap<>(Suit.class);

    public Range getPoints() {
        return points;
    }

    public Range getHighCardPoints() {
        return highCardPoints;
    }

    public Range getStartingPoints() {
        return startingPoints;
    }

    public Range getNoTrumpLongHandPoints() {
        return noTrumpLongHandPoints;
    }

    public Range getNoTrumpDummyPoints() {
        return noTrumpDummyPoints;
    }

    public Range getLosers() {
        return losers;
    }

    public Boolean getIsBalanced() {
        return isBalanced;
    }

    public Boolean getIsFlat() {
        return isFlat;
    }

    public Set<Integer> getCountAces() {
        return countAces;
    }

    public Set<Integer> getCountKings() {
        return countKings;
    }

    public HandSummary() {
        for (Suit suit : Suit.values()) {
            suits.put(suit, new SuitSummary());
        }
    }

    public HandSummary(HandSummary other) {
        this.points = other.points;
        this.highCardPoints = other.highCardPoints;
        this.startingPoints = other.startingPoints;
        this.noTrumpLongHandPoints = other.noTrumpLongHandPoints;
        this.noTrumpDummyPoints = other.noTrumpDummyPoints;
        this.losers = other.losers;
        this.isBalanced = other.isBalanced;
        this.isFlat = other.isFlat;
        this.countAces = other.countAces != null ? new HashSet<>(other.countAces) : null;
        this.countKings = other.countKings != null ? new HashSet<>(other.countKings) : null;
        for (Suit suit : Suit.values()) {
            suits.put(suit, new SuitSummary(other.suits.get(suit)));
        }
    }

    protected void showPoints(int min, int max) {
        this.points = combineRange(this.points, new Range(min, max), CombineRule.Show);
    }

    protected void combine(HandSummary other, CombineRule cr) {
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
        for (Suit suit : Suit.values()) {
            this.suits.get(suit).combine(other.suits.get(suit), cr);
        }
        trimShape();
    }

    public void trimShape() {
        int claimed = 0;
        for (Suit suit : Suit.values()) {
            claimed += suits.get(suit).getShape().getMin();
        }
        for (Suit suit : Suit.values()) {
            suits.get(suit).trimShape(claimed);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandSummary that = (HandSummary) o;
        return Objects.equals(points, that.points) &&
                Objects.equals(highCardPoints, that.highCardPoints) &&
                Objects.equals(startingPoints, that.startingPoints) &&
                Objects.equals(noTrumpLongHandPoints, that.noTrumpLongHandPoints) &&
                Objects.equals(noTrumpDummyPoints, that.noTrumpDummyPoints) &&
                Objects.equals(losers, that.losers) &&
                Objects.equals(isBalanced, that.isBalanced) &&
                Objects.equals(isFlat, that.isFlat) &&
                Objects.equals(countAces, that.countAces) &&
                Objects.equals(countKings, that.countKings) &&
                Objects.equals(suits, that.suits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points, highCardPoints, startingPoints, noTrumpLongHandPoints, noTrumpDummyPoints, losers, isBalanced, isFlat, countAces, countKings, suits);
    }

    public Map<Suit, SuitSummary> getSuits() {
        return suits;
    }
}




























































































































































































































































































































































































































































































