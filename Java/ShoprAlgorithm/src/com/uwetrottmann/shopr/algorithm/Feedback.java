
package com.uwetrottmann.shopr.algorithm;

public class Feedback {

    private boolean isPositiveFeedback;

    private int attribute;

    public boolean isPositiveFeedback() {
        return isPositiveFeedback;
    }

    public Feedback isPositiveFeedback(boolean isPositiveFeedback) {
        this.isPositiveFeedback = isPositiveFeedback;
        return this;
    }

    public int attribute() {
        return attribute;
    }

    public Feedback attribute(int attribute) {
        this.attribute = attribute;
        return this;
    }

}
