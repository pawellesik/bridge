package com.example.bridge.bidding.BridgeBidder.Constraints;

import com.example.licytacja.moje.BridgeBidder.*;
import java.util.*;

/**
 * Constraint sprawdzający SUMARYCZNĄ liczbę asów i króli w obu rękach pary.
 * Przydatny przy końcowym sprawdzaniu szlemika, gdy znamy siłę obu rąk.
 */
public class SumPairAcesAndKings extends HandConstraint implements IDescribeConstraint {
    private final Set<Integer> counts;
    private final String customDescription; // Opcjonalny własny opis (np. "test")
    private final Range range; // Opcjonalny zakres zamiast listy konkretnych liczb

    /**
     * @param counts Lista akceptowalnych sum (np. 5, 6 oznacza że para ma razem 5 lub 6 asów i króli).
     */
    public SumPairAcesAndKings(int... counts) {
        this(null, counts);
    }
    
    // ... reszta konstruktorów ...

    public SumPairAcesAndKings(String customDescription, int... counts) {
        this.customDescription = customDescription;
        this.counts = new HashSet<>();
        for (int c : counts) this.counts.add(c);
        this.range = null;
    }

    public SumPairAcesAndKings(Range range) {
        this(null, range);
    }

    public SumPairAcesAndKings(String customDescription, Range range) {
        this.customDescription = customDescription;
        this.range = range;
        this.counts = null;
    }

    /**
     * Główna logika sprawdzająca sumę wszystkich kombinacji możliwych asów i króli partnerów.
     */
    @Override
    public boolean conforms(Call call, PositionState ps, HandSummary hs) {
        Set<Integer> ourAces = hs.getCountAces();
        Set<Integer> ourKings = hs.getCountKings();
        Set<Integer> partnerAces = ps.getPartner().getPublicHandSummary().getCountAces();
        Set<Integer> partnerKings = ps.getPartner().getPublicHandSummary().getCountKings();

        if (ourAces == null || ourKings == null || partnerAces == null || partnerKings == null) {
            return true;
        }

        for (int myA : ourAces) {
            for (int myK : ourKings) {
                for (int pA : partnerAces) {
                    for (int pK : partnerKings) {
                        int total = myA + myK + pA + pK;
                        if (range != null) {
                            if (range.contains(total)) return true;
                        } else {
                            if (counts.contains(total)) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String describe(Call call, PositionState ps) {
        if (customDescription != null) return customDescription;
        if (range != null) return range.toString() + " total Aces and Kings in pair";
        return counts.toString() + " total Aces and Kings in pair";
    }
}
