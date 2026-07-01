package com.example.bridge.bridgit;

public class CallAnnotation extends CallFeature {
    public enum AnnotationType { Alert, Announce, Convention }
    
    private final String text;
    private final AnnotationType type;

    public CallAnnotation(Call call, AnnotationType type, String text, Constraint.StaticConstraint... constraints) {
        super(call, constraints);
        this.type = type;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public AnnotationType getType() {
        return type;
    }
}
