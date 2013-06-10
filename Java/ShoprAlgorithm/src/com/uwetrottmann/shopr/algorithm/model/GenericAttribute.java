
package com.uwetrottmann.shopr.algorithm.model;

public abstract class GenericAttribute<T> {

    private T currentValue;

    double[] mValueWeights;

    public double[] getValueWeights() {
        return mValueWeights;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        T[] values = getValueSymbols();

        builder.append("[");
        for (int i = 0; i < mValueWeights.length; i++) {
            if (mValueWeights[i] != 0) {
                builder.append(values[i]).append(":").append(mValueWeights[i]).append(" ");
            }
        }
        builder.append("]");

        return builder.toString();
    }

    public abstract T[] getValueSymbols();

    public T currentValue() {
        return currentValue;
    }

    public GenericAttribute<T> currentValue(T currentValue) {
        this.currentValue = currentValue;
        return this;
    }

}
