package com.example.bridge.bridgit;

import java.util.*;

public class CallFeatureGroup extends CallFeature {
    private final List<CallFeature> features = new ArrayList<>();

    public CallFeatureGroup() {
        super(null);
    }

    public List<CallFeature> getFeatures() {
        return features;
    }

    public void addFeature(CallFeature feature) {
        features.add(feature);
    }
}
