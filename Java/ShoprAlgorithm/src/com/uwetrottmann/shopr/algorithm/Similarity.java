
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

public class Similarity {

    public static double similarity(Attributes attrsFirst, Attributes attrsSecond) {
        // TODO: calculates similarity over all attributes

        // for each feature in attrsFirst/attrsSecond
        // calc similarity(featureFirst, featureSecond)
        // sum up, divide by number of compared features


        return 1;
    }

    /**
     * Returns a similarity value between 0 and 1. The higher the more similar
     * are the weights of the attribute values.
     */
    public static double attributeSimilarity(Attribute first, Attribute second) {
        double similarity = 0;
        double[] valueWeightsFirst = first.getValueWeights();
        double[] valueWeightsSecond = second.getValueWeights();

        if (valueWeightsFirst.length != valueWeightsSecond.length) {
            throw new IllegalArgumentException(
                    "Attribute value weight vectors must be of same size.");
        }

        // sum up deltas
        for (int i = 0; i < valueWeightsFirst.length; i++) {
            similarity += 1 - Math.abs(valueWeightsFirst[i] - valueWeightsSecond[i]);
        }
        // average
        similarity /= valueWeightsFirst.length;

        return similarity;
    }

}
