
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Feedback {

    private boolean mIsPositiveFeedback;

    private List<Attribute> mAttributes = new ArrayList<Attribute>();

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

    public Feedback addAttributes(Attribute... attributes) {
        mAttributes.addAll(Arrays.asList(attributes));
        return this;
    }

}
