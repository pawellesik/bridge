package com.example.bridge.bidding.BridgeBidder;

public class Bid extends Call {
    private final int level;
    private final Strain strain;

    public Bid(int level, Suit suit) {
        this(level, suit.toStrain());
    }

    public Bid(int level, Strain strain) {
        super((level - 1) * 5 + strain.ordinal() + 3);
        if (level < 1 || level > 7) {
            throw new IllegalArgumentException("Bid level " + level + " is invalid. Must be 1 through 7.");
        }
        this.level = level;
        this.strain = strain;
    }

    public int getLevel() {
        return level;
    }

    public Strain getStrain() {
        return strain;
    }

    public Suit getSuit() {
        return strain.toSuit();
    }

    @Override
    public String toString() {
        return level + STRAIN_TO_SYMBOL.get(strain);
    }

    public int jumpOver(Bid other) {
        return (this.rawValue - other.rawValue - 1) / 5;
    }

    public static final Bid _1C = new Bid(1, Strain.Clubs);
    public static final Bid _1D = new Bid(1, Strain.Diamonds);
    public static final Bid _1H = new Bid(1, Strain.Hearts);
    public static final Bid _1S = new Bid(1, Strain.Spades);
    public static final Bid _1NT = new Bid(1, Strain.NoTrump);

    public static final Bid _2C = new Bid(2, Strain.Clubs);
    public static final Bid _2D = new Bid(2, Strain.Diamonds);
    public static final Bid _2H = new Bid(2, Strain.Hearts);
    public static final Bid _2S = new Bid(2, Strain.Spades);
    public static final Bid _2NT = new Bid(2, Strain.NoTrump);

    public static final Bid _3C = new Bid(3, Strain.Clubs);
    public static final Bid _3D = new Bid(3, Strain.Diamonds);
    public static final Bid _3H = new Bid(3, Strain.Hearts);
    public static final Bid _3S = new Bid(3, Strain.Spades);
    public static final Bid _3NT = new Bid(3, Strain.NoTrump);

    public static final Bid _4C = new Bid(4, Strain.Clubs);
    public static final Bid _4D = new Bid(4, Strain.Diamonds);
    public static final Bid _4H = new Bid(4, Strain.Hearts);
    public static final Bid _4S = new Bid(4, Strain.Spades);
    public static final Bid _4NT = new Bid(4, Strain.NoTrump);

    public static final Bid _5C = new Bid(5, Strain.Clubs);
    public static final Bid _5D = new Bid(5, Strain.Diamonds);
    public static final Bid _5H = new Bid(5, Strain.Hearts);
    public static final Bid _5S = new Bid(5, Strain.Spades);
    public static final Bid _5NT = new Bid(5, Strain.NoTrump);

    public static final Bid _6C = new Bid(6, Strain.Clubs);
    public static final Bid _6D = new Bid(6, Strain.Diamonds);
    public static final Bid _6H = new Bid(6, Strain.Hearts);
    public static final Bid _6S = new Bid(6, Strain.Spades);
    public static final Bid _6NT = new Bid(6, Strain.NoTrump);

    public static final Bid _7C = new Bid(7, Strain.Clubs);
    public static final Bid _7D = new Bid(7, Strain.Diamonds);
    public static final Bid _7H = new Bid(7, Strain.Hearts);
    public static final Bid _7S = new Bid(7, Strain.Spades);
    public static final Bid _7NT = new Bid(7, Strain.NoTrump);
}
