
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;

import java.util.List;

public class Similarity {

    public static double similarity(Attributes first, Attributes second) {
        List<Attribute> attrsFirst = first.getAllAttributes();

        int count = 0;
        double similarity = 0;

        // sum up similarity values for all attributes
        for (Attribute attrFirst : attrsFirst) {
            /*
             * The query does only store new vectors for a feature once it has
             * been critiqued (others remain null). This speeds up processing by
             * avoiding useless comparisons (calculating similarity for
             * un-critiqued features).
             */
            Attribute attrSecond = second.getAttributeById(attrFirst.id());
            if (attrSecond != null) {
                count++;
                similarity += attributeSimilarity(attrFirst, attrSecond);
            }
        }

        // if there are no comparable attributes, there is no similarity
        if (count == 0) {
            return 0;
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
