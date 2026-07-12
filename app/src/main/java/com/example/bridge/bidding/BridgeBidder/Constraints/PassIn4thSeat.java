package com.example.bridge.bidding.BridgeBidder.Constraints;

import com.example.licytacja.moje.BridgeBidder.*;

/**
 * Implementacja Reguły 15 (Reguła Pearsona) dla otwarcia na 4. ręce.
 * Pomaga zdecydować, czy otwierać licytację po trzech pasach.
 * Reguła: otwórz, jeśli suma punktów HCP i liczby pików wynosi co najmniej 15.
 */
public class PassIn4thSeat extends HandConstraint {
    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        if (ps.getSeat() != 4) return false;
        Range hcp = hs.getHighCardPoints();
        if (hcp == null) return true;

        Range spadeShape = hs.getSuits().get(Suit.Spades).getShape();
        return (hcp.getMax() + spadeShape.getMax() < 15);
    }
}
