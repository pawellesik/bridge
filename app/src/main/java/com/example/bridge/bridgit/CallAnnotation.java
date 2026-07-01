package com.example.bridge.bridgit;

public class CallAnnotation extends CallFeature {
    public enum AnnotationType { Alert, Announce, Convention }

    private final AnnotationType type;
    private final String text;

    public CallAnnotation(Call call, AnnotationType type, String text, Constraint.StaticConstraint... constraints) {
        super(call, constraints);
        this.type = type;
        this.text = text;
    }

    public AnnotationType getType() { return type; }
    public String getText() { return text; }
}
