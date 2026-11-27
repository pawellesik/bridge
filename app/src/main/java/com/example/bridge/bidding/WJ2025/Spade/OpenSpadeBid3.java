package com.example.bridge.bidding.WJ2025.Spade;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.CompeteWJ2025;
import com.example.bridge.bidding.WJ2025.RecursionWJ2025;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class OpenSpadeBid3 extends WJ2025 {

    public static final Range OPEN_12_14 = new Range(12, 14);
    public static final Range OPEN_15_17 = new Range(15, 17);
    public static final Range OPEN_12_17 = new Range(12, 17);

    public static PositionCalls spade_2C_2D_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3NT, SHAPE_5422, highCardPoints(openRange17(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3NT")),
                shows(Bid._3S, shape(6, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3S")),//nigdy nie zajdzie
                shows(Bid._3H, shape(3, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3H")),
                shows(Bid._3D, shape(5, 13), highCardPoints(openRange14(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3D")),
                shows(Bid._3C, shape(3, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3C")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_2C_2S_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3NT, shape5422(), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3NT")),
                shows(Bid._3S, shape(6, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3H")),
                shows(Bid._3H, shape(3, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3S")),
                shows(Bid._3D, shape(5, 13), highCardPoints(openRange14(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3D")),// nie jest mozliwe
                shows(Bid._3C, shape(3, 13), highCardPoints(openRange17(ps)), setTrumpColor(Suit.Clubs), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3C")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)

        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls spade_2D_2H_2NT(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._3NT, shape5422(), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3NT")),
                shows(Bid._3S, shape(6, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3S")),
                shows(Bid._3H, shape(3, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3H")),
                shows(Bid._3C, shape(5, 13), highCardPoints(openRange14(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3C")),//nie jestr mozliwe
                shows(Bid._3D, shape(3, 13), highCardPoints(openRange17(ps)), ruleShow(1), id("OpenSpadeBid3.spade_2C_2D_2NT _3D")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }
    public static PositionCalls spade_2H_3NT_4C(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);
        choices.addRules(
                shows(Bid._4D, shape(0, 1), ruleShow(1), ruleShow(1), id("OpenSpadeBid3.spade_2H_3NT_4C _4D")),
                shows(Bid._4H, shape(Suit.Clubs, 0, 1), ruleShow(1), id("OpenSpadeBid3.spade_2H_3NT_4C _4H")),
                partnerBids(RecursionWJ2025::recursionFindFitGame)
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }
}
