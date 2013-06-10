
package com.uwetrottmann.shopr.algorithm.model;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

import java.util.Arrays;

public class Label extends GenericAttribute<Label.Value> implements Attribute {
    public enum Value {
        ARMANI,
        HUGO_BOSS,
        CHANEL,
        DOLCE_AND_GABBANA,
        KARL_LAGERFELD
    }

    public Label() {
        int numValues = Value.values().length;
        mValueWeights = new double[numValues];
        Arrays.fill(mValueWeights, 1.0 / numValues);
    }

    public Label(Value value) {
        mValueWeights = new double[Value.values().length];
        Arrays.fill(mValueWeights, 0.0);
        mValueWeights[value.ordinal()] = 1.0;
        currentValue(value);
    }

    @Override
    public Value[] getValueSymbols() {
        return Value.values();
    }
}
