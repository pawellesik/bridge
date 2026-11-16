package com.example.bridge.bidding.WJ2025.Diamond;

import com.example.bridge.bidding.Tools.Bid;
import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionCalls;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.Range;
import com.example.bridge.bidding.Tools.Suit;
import com.example.bridge.bidding.WJ2025.CompeteWJ2025;
import com.example.bridge.bidding.WJ2025.WJ2025;

public class OpenDiamondBid4 extends WJ2025 {

    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// /////////////////////////////////////////////////Licytacja bez rules show/////////////////////////////////////////////////////////
    /// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static PositionCalls diamond_1H_1NT1S_2D_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, stopped(Suit.Clubs), id("OpenDiamondBid4.diamond_1H_1NT1S_2D_2S 3NT")),
                shows(Bid._4H, fit(7),  id("OpenDiamondBid4.diamond_1H_1NT1S_2D_2S 4H")),
                shows(Bid._4S, fit(7),  id("OpenDiamondBid4.diamond_1H_1NT1S_2D_2S 4S"))
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }

    public static PositionCalls diamond_1S_1NT_2D_2S(PositionState ps) {
        PositionCalls choices = new PositionCalls(ps);

        choices.addRules(
                shows(Bid._3NT, stopped(Suit.Clubs), id("OpenDiamondBid4.diamond_1S_1NT_2D_2S 3NT")),
                shows(Bid._4H, fit(7),  id("OpenDiamondBid4.diamond_1S_1NT_2D_2S 4H")),
                shows(Bid._4S, fit(7),  id("OpenDiamondBid4.diamond_1S_1NT_2D_2S 4S"))
        );
        choices.addRules(CompeteWJ2025.compBids(ps));
        return choices;
    }




}
