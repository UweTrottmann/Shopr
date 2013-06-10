
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AdaptiveSelection {

    private static final int NUM_RECOMMENDATIONS = 5;
    private static final int BOUND = 5;

    public static void main(String[] args) {
        adaptiveSelection();
    }

    private static void adaptiveSelection() {
        /*
         * TODO: Build an actual cycle with item output and user input.
         */
        Query query = new Query();
        Critique critique = new Critique();
        critique.item(null);
        boolean isAbort = false;

        /*
         * The caseBase will later be stored in a database (due to its size).
         * Think about optimizations which could be applied.
         */
        // Filter case-base to match hard-limits (location, opening hours)
        List<Item> caseBase = Utils.getLimitedCaseBase();

        while (!isAbort) {
            List<Item> recommendations = itemRecommend(caseBase, query, NUM_RECOMMENDATIONS, BOUND,
                    critique);
            critique = userReview(recommendations, query);
            queryRevise(query, critique);
        }
    }

    /**
     * Takes the current query, number of recommended items to return, the last
     * critique. Returns a list of recommended items based on the case-base.
     */
    private static List<Item> itemRecommend(List<Item> caseBase, Query query, int numItems,
            int bound, Critique lastCritique) {

        List<Item> recommendations = new ArrayList<Item>();

        if (lastCritique.item() != null && lastCritique.feedback().isPositiveFeedback()) {
            /*
             * Positive progress: user liked one or more features of one of the
             * recommended items.
             */
            /*
             * REFINE: Show similar recommendations by sorting the case-base in
             * decreasing similarity to current query. Return top k items.
             */
            Utils.sortBySimilarityToQuery(query, caseBase);
            for (int i = 0; i < numItems; i++) {
                recommendations.add(caseBase.remove(0));
            }
        } else {
            /*
             * Negative progress: user disliked one or more of the features of
             * one recommended item. Or: first run.
             */
            // REFOCUS: show diverse recommendations
            recommendations = BoundedGreedySelection
                    .boundedGreedySelection(query, caseBase, numItems, bound);
        }

        // Carry the critiqued so the user may critique it further.
        if (lastCritique.item() != null) {
            recommendations.add(lastCritique.item());
        }

        return recommendations;
    }

    /**
     * Takes the list of recommended items and elicits a critique on a feature
     * of one item from the user. Returns the liked/disliked item and which
     * feature value (! not just which feature !) was liked/disliked.
     */
    private static Critique userReview(List<Item> recommendations, Query query) {
        Utils.dumpToConsole(recommendations, query);

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter the number of item to critique: ");
        int selection;
        try {
            selection = Integer.valueOf(in.readLine());

            System.out.print("Like (1) or Dislike(0)? ");
            boolean isPositiveCritique = Integer.valueOf(in.readLine()) == 1;

            // TODO: allow multiple attributes
            System.out.print("Color (0) or type (1)?: ");
            int attribute = Integer.valueOf(in.readLine());

            Critique critique = new Critique();
            critique.item(recommendations.get(selection));
            critique.feedback(new Feedback().isPositiveFeedback(isPositiveCritique)
                    .attribute(attribute));

            return critique;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Takes a liked/disliked item, which feature value was liked/disliked, the
     * current query. Returns a new query modified according to the given user
     * critique.
     */
    private static void queryRevise(Query query, Critique critique) {
        // TODO implement weighting, right now only replacing with item
        // attributes
        switch (critique.feedback().attribute()) {
            case 0: {
                if (query.attributes().color() == null) {
                    // initialize with evenly weighted values
                    query.attributes().color(new Color());
                }
                if (critique.feedback().isPositiveFeedback()) {

                } else {
                    int valueIndex = critique.item().attributes().color().currentValue().ordinal();
                    double[] weights = query.attributes().color().getValueWeights();

                    dislikeValue(valueIndex, weights);
                }
                break;
            }
            case 1: {
                if (query.attributes().type() == null) {
                    // initialize with evenly weighted values
                    query.attributes().type(new ClothingType());
                }
                if (critique.feedback().isPositiveFeedback()) {

                } else {
                    int valueIndex = critique.item().attributes().type().currentValue().ordinal();
                    double[] weights = query.attributes().type().getValueWeights();

                    dislikeValue(valueIndex, weights);
                }
                break;
            }
        }
    }

    private static void dislikeValue(int valueIndex, double[] weights) {
        double dislikedValue = weights[valueIndex];

        weights[valueIndex] = 0.0;

        int nonZeroCount = 0;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] != 0) {
                nonZeroCount++;
            }
        }

        double redistributed = dislikedValue / nonZeroCount;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] != 0) {
                weights[i] += redistributed;
            }
        }
    }

}
