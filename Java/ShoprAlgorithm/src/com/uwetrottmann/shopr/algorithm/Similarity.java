
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

public class Similarity {

    public static double similarity(Attributes first, Attributes second) {
        Attribute[] attrsFirst = first.getAllAttributes();
        Attribute[] attrsSecond = second.getAllAttributes();

        int count = 0;
        double similarity = 0;

        // sum up similarity values for all attributes
        for (int i = 0; i < attrsFirst.length; i++) {
            /*
             * The query does only store new vectors for a feature once it has
             * been critiqued (others remain null). This speeds up processing by
             * avoiding useless comparisons (calculating similarity for
             * un-critiqued features).
             */
            if (attrsFirst[i] != null && attrsSecond[i] != null) {
                count++;
                similarity += attributeSimilarity(attrsFirst[i], attrsSecond[i]);
            }
        }

        if (count == 0) {
            throw new IllegalArgumentException(
                    "Could not find one match of attribute to compare.");
        }

        // average
        similarity /= count;

        return similarity;
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
        int count = 0;
        for (int i = 0; i < valueWeightsFirst.length; i++) {
            if (valueWeightsFirst[i] == 0 && valueWeightsSecond[i] == 0) {
                // skip if both weights are 0
                continue;
            }
            count++;
            similarity += 1 - Math.abs(valueWeightsFirst[i] - valueWeightsSecond[i]);
        }

        // average
        similarity /= count;

        return similarity;
    }

}
