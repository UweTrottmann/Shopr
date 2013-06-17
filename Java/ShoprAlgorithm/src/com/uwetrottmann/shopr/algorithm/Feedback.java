
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

import java.util.Arrays;
import java.util.List;

public class Feedback {

    private boolean mIsPositiveFeedback;

    private List<Attribute> mAttributes;

    public boolean isPositiveFeedback() {
        return mIsPositiveFeedback;
    }

    public Feedback isPositiveFeedback(boolean isPositiveFeedback) {
        mIsPositiveFeedback = isPositiveFeedback;
        return this;
    }

    public List<Attribute> attributes() {
        return mAttributes;
    }

    public Feedback attributes(Attribute... attributes) {
        mAttributes = Arrays.asList(attributes);
        return this;
    }

}
