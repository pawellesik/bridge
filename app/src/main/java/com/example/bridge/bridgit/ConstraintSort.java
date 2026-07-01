package com.example.bridge.bridgit;

import com.example.bridge.bridgit.constraints.*;
import java.util.*;

public class ConstraintSort {
    private static final List<Class<?>> descriptionOrder = new ArrayList<>();
    static {
        descriptionOrder.add(ShowsPoints.class);
        // descriptionOrder.add(PairShowsPoints.class); // To be added when ported
        descriptionOrder.add(ShowsShape.class);
        // descriptionOrder.add(PairShowsMinShape.class); // To be added when ported
    }

    public static int forDescription(Constraint constraint) {
        int index = descriptionOrder.indexOf(constraint.getClass());
        return index == -1 ? descriptionOrder.size() : index;
    }
}
