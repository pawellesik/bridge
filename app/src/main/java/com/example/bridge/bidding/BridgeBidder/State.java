package com.example.bridge.bidding.BridgeBidder;

import java.util.HashSet;
import java.util.Set;

public abstract class State {
    public enum CombineRule {
        Show,       // If left or right is null then use other. If both non-null then use smallest min, largest max
        CommonOnly, // If either left or right is null, result is null. Otherwise smallest min, largest max
        Merge       // If either left or right is null then use other. If both non-null then use largest min, smallest max
    }

    protected static Range combineRange(Range a, Range b, CombineRule cr) {
        if (a != null && b != null) {
            if (cr == CombineRule.Merge) {
                return new Range(Math.max(a.getMin(), b.getMin()), Math.min(a.getMax(), b.getMax()));
            }
            return new Range(Math.min(a.getMin(), b.getMin()), Math.max(a.getMax(), b.getMax()));
        }
        if (cr == CombineRule.CommonOnly) {
            return null;
        }
        return (a == null) ? b : a;
    }

    protected static Boolean combineBool(Boolean b1, Boolean b2, CombineRule cr) {
        if (b1 == null) {
            return (cr == CombineRule.CommonOnly) ? null : b2;
        }
        if (b2 == null) {
            return (cr == CombineRule.CommonOnly) ? null : b1;
        }
        return b1;
    }

    protected static Integer combineInt(Integer i1, Integer i2, CombineRule cr) {
        if (i1 == null) {
            return (cr == CombineRule.CommonOnly) ? null : i2;
        }
        if (i2 == null) {
            return (cr == CombineRule.CommonOnly) ? null : i1;
        }
        return i1;
    }

    protected static Set<Integer> combineIntSet(Set<Integer> s1, Set<Integer> s2, CombineRule cr) {
        if (s1 == null) {
            return (cr == CombineRule.CommonOnly) ? null : s2;
        }
        if (s2 == null) {
            return (cr == CombineRule.CommonOnly) ? null : s1;
        }
        if (cr == CombineRule.Merge) {
            Set<Integer> result = new HashSet<>(s1);
            result.retainAll(s2);
            return result;
        }
        Set<Integer> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }
}




























































































































































































































































































































































































































































































