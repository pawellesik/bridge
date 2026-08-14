package com.example.bridge.bidding.Constraints;

import com.example.bridge.bidding.Tools.Call;
import com.example.bridge.bidding.Tools.Constraint;
import com.example.bridge.bidding.Tools.HandConstraint;
import com.example.bridge.bidding.Tools.IDescribeConstraint;
import com.example.bridge.bidding.Tools.PositionState;
import com.example.bridge.bidding.Tools.StaticConstraint;

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
    private final Constraint constraint; // Warunek, który ma zostać sprawdzony dla wskazanej pozycji

    public PositionProxy(RelativePosition relativePosition, Constraint constraint) {
        this.relativePosition = relativePosition;
        this.constraint = constraint;
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
        PositionState targetPs = getPosition(ps);
        if (targetPs == null) return false;

        if (constraint instanceof StaticConstraint) {
            return ((StaticConstraint) constraint).conforms(call, targetPs);
        } else if (constraint instanceof HandConstraint) {
            // Gdy sprawdzamy HandConstraint dla innej pozycji (partner/przeciwnik),
            // musimy bazować na ich wiedzy publicznej (PublicHandSummary),
            // ponieważ nie widzą oni nawzajem swoich prywatnych kart.
            return ((HandConstraint) constraint).conforms(call, targetPs, targetPs.getPublicHandSummary());
        }
        return false;
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




























































































































































































































































































































































































































































































