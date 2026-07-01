package com.example.bridge.bridgit;

import java.util.*;

public abstract class State {
    public enum CombineRule {
        Show, CommonOnly, Merge
    }

    protected static Range combineRange(Range a, Range b, CombineRule cr) {
        if (a != null && b != null) {
            if (cr == CombineRule.Merge) {
                return new Range(Math.max(a.min, b.min), Math.min(a.max, b.max));
            }
            return new Range(Math.min(a.min, b.min), Math.max(a.max, b.max));
        }
        if (cr == CombineRule.CommonOnly) return null;
        return (a == null) ? b : a;
    }

    protected static Boolean combineBool(Boolean b1, Boolean b2, CombineRule cr) {
        if (b1 == null) return (cr == CombineRule.CommonOnly) ? null : b2;
        if (b2 == null) return (cr == CombineRule.CommonOnly) ? null : b1;
        return b1;
    }

    protected static Integer combineInt(Integer i1, Integer i2, CombineRule cr) {
        if (i1 == null) return (cr == CombineRule.CommonOnly) ? null : i2;
        if (i2 == null) return (cr == CombineRule.CommonOnly) ? null : i1;
        return i1;
    }

    protected static Set<Integer> combineIntSet(Set<Integer> s1, Set<Integer> s2, CombineRule cr) {
        if (s1 == null) return (cr == CombineRule.CommonOnly) ? null : s2;
        if (s2 == null) return (cr == CombineRule.CommonOnly) ? null : s1;
        
        Set<Integer> result = new HashSet<>(s1);
        if (cr == CombineRule.Merge) {
            result.retainAll(s2);
        } else {
            result.addAll(s2);
        }
        return result;
    }
}
