
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.AttributeValue;

import java.math.BigDecimal;
import java.util.Arrays;

public class Price extends GenericAttribute {

    public static final String ID = "price";

    public enum Value implements AttributeValue {
        SUB_25("less than 25 €"),
        BETWEEN_25_50("25 to 50 €"),
        BETWEEN_50_75("50 to 75 €"),
        BETWEEN_75_100("75 to 100 €"),
        BETWEEN_100_150("100 to 150 €"),
        BETWEEN_150_200("150 to 200 €"),
        ABOVE_200("200 € or more");

        private String mDescriptor;

        Value(String descriptor) {
            mDescriptor = descriptor;
        }

        @Override
        public String descriptor() {
            return mDescriptor;
        }

        @Override
        public int index() {
            return ordinal();
        }

    }

    public Price() {
        int numValues = Value.values().length;
        mValueWeights = new double[numValues];
        Arrays.fill(mValueWeights, 1.0 / numValues);
    }

    public Price(BigDecimal price) {
        mValueWeights = new double[Value.values().length];
        Arrays.fill(mValueWeights, 0.0);

        // determine price range
        Value value;
        if (price.doubleValue() < 25.0) {
            value = Value.SUB_25;
        } else if (price.doubleValue() < 50.0) {
            value = Value.BETWEEN_25_50;
        } else if (price.doubleValue() < 75.0) {
            value = Value.BETWEEN_50_75;
        } else if (price.doubleValue() < 100.0) {
            value = Value.BETWEEN_75_100;
        } else if (price.doubleValue() < 150.0) {
            value = Value.BETWEEN_100_150;
        } else if (price.doubleValue() < 200.0) {
            value = Value.BETWEEN_150_200;
        } else {
            value = Value.ABOVE_200;
        }

        mValueWeights[value.ordinal()] = 1.0;
        currentValue(value);
    }

    @Override
    public String id() {
        return ID;
    }

    @Override
    public AttributeValue[] getValueSymbols() {
        return Value.values();
    }

    /**
     * Changes the behavior to also increase the weight of neighboring weights.
     * E.g. a user might like items priced in a little more expensive or a
     * little cheaper based on the price range he liked.
     */
    @Override
    public void likeValue(int valueIndex, double[] weights) {
        // TODO Auto-generated method stub
        super.likeValue(valueIndex, weights);
    }

}
