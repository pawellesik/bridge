package com.example.bridge.bidding.BridgeBidder.Constraints;

import com.example.bridge.bidding.BridgeBidder.Tools.Call;
import com.example.bridge.bidding.BridgeBidder.Tools.Constraint;
import com.example.bridge.bidding.BridgeBidder.Tools.IDescribeConstraint;
import com.example.bridge.bidding.BridgeBidder.Tools.PositionState;
import com.example.bridge.bidding.BridgeBidder.Tools.StaticConstraint;

/**
 * Techniczna klasa pozwalająca na delegowanie sprawdzenia warunku na inną pozycję przy stole.
 * Pozwala np. sprawdzić czy PARTNER zalicytował już dany kolor lub czy PRZECIWNIK ma określoną siłę.
 */
public class PositionProxy extends StaticConstraint implements IDescribeConstraint {
    /**
     * Definiuje pozycję względem aktualnego gracza.
     */
    public enum RelativePosition { 
        Partner, // Gracz po przeciwnej stronie stołu
        LHO,     // Lewy przeciwnik
        RHO      // Prawy przeciwnik
    }

    private final RelativePosition relativePosition;
    private final StaticConstraint constraint; // Warunek, który ma zostać sprawdzony dla wskazanej pozycji

    public PositionProxy(RelativePosition relativePosition, Constraint constraint) {
        this.relativePosition = relativePosition;
        this.constraint = (StaticConstraint) constraint;
    }

    private PositionState getPosition(PositionState positionState) {
        switch (relativePosition) {
            case Partner: return positionState.getPartner();
            case LHO: return positionState.getLHO();
            case RHO: return positionState.getRHO();
            default: return null;
        }
    }

    @Override
    public boolean conforms(Call call, PositionState ps) {
        return constraint.conforms(call, getPosition(ps));
    }

    private String getPositionName() {
        switch (relativePosition) {
            case Partner: return "partner";
            case LHO: return "LHO";
            case RHO: return "RHO";
            default: return "";
        }
    }

    @Override
    public String describe(Call call, PositionState ps) {
        if (constraint instanceof IDescribeConstraint) {
            return getPositionName() + " " + ((IDescribeConstraint) constraint).describe(call, getPosition(ps));
        }
        return null;
    }

    @Override
    public String getLogDescription(Call call, PositionState ps) {
        String desc = describe(call, ps);
        return desc == null ? getPositionName() + " " + constraint.getLogDescription(call, getPosition(ps)) : desc;
    }
}




























































































































































































































































































































































































































































































