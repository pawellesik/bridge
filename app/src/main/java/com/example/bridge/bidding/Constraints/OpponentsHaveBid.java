package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.StaticConstraint;

/**
 * Sprawdza, czy przeciwnicy włączyli się do licytacji (dali odzywkę inną niż Pas).
 */
public class OpponentsHaveBid extends StaticConstraint {
    @Override
    public boolean conforms(Call call, PositionState ps) {
        // Sprawdzamy czy którykolwiek z przeciwników (RHO lub LHO) 
        // zalicytował w swojej historii cokolwiek innego niż Pas.
        
        // Sprawdź RHO (Prawy przeciwnik)
        for (int i = 0; i < ps.getRHO().getCallCount(); i++) {
            if (!ps.getRHO().getBidHistory(i).equals(com.example.bridge.bidding.Tools.Call.PASS)) {
                return true;
            }
        }

        // Sprawdź LHO (Lewy przeciwnik)
        for (int i = 0; i < ps.getLHO().getCallCount(); i++) {
            if (!ps.getLHO().getBidHistory(i).equals(com.example.bridge.bidding.Tools.Call.PASS)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public String getLogDescription(Call call, PositionState ps) {
        return "opponents have entered the auction";
    }
}
