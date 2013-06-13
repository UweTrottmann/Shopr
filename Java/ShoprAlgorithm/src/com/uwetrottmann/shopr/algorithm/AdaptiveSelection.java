
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.algorithm.model.Label;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdaptiveSelection {

    private static final int NUM_RECOMMENDATIONS_DEFAULT = 2;
    private static final int BOUND_DEFAULT = 5;
    private static final boolean DUMP_INVENTORY = false;

    private static AdaptiveSelection _instance;

    public static synchronized AdaptiveSelection get() {
        if (_instance == null) {
            _instance = new AdaptiveSelection();
        }
        return _instance;
    }

    /**
     * **DO NOT USE** Only for internal testing.
     */
    public static void main(String[] args) {
        adaptiveSelection();
    }

    private List<Item> mCaseBase;
    private Query mQuery;
    private int mNumRecommendations;

    private AdaptiveSelection() {
        mCaseBase = new ArrayList<Item>();
        mQuery = new Query();
        mNumRecommendations = NUM_RECOMMENDATIONS_DEFAULT;
    }

    /**
     * Call before {@link #getRecommendations(Critique)} to set the initial data
     * set.
     */
    public void setInitialCaseBase(List<Item> caseBase) {
        mCaseBase = caseBase;
    }

    /**
     * The maximum number of recommendations to return. Does not count the
     * carried item!
     */
    public void setMaxRecommendations(int limit) {
        mNumRecommendations = Math.max(1, limit);
    }

    /**
     * Returns a sorted list of recommendations based on the current query which
     * itself is shaped through passed critiques. The first time you may pass
     * {@code null} for the critique to get an initial set of diverse
     * recommendations.
     */
    public List<Item> getRecommendations(Critique critique) {
        // Update the current query with the new critique
        if (critique != null) {
            queryRevise(mQuery, critique);
        }

        // build a new set of recommendations
        List<Item> recommendations = itemRecommend(mCaseBase, mQuery, mNumRecommendations,
                BOUND_DEFAULT,
                critique);

        return recommendations;
    }

    /**
     * **DO NOT USE** Only for testing of the Adaptive Selection cycle using a
     * console program.
     */
    private static void adaptiveSelection() {
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

        // dump database
        if (DUMP_INVENTORY) {
            dumpInventory(query, caseBase);
        }

        while (!isAbort) {
            List<Item> recommendations = itemRecommend(caseBase, query,
                    NUM_RECOMMENDATIONS_DEFAULT, BOUND_DEFAULT,
                    critique);
            critique = userReview(recommendations, query);
            queryRevise(query, critique);
        }
    }

    private static void dumpInventory(Query query, List<Item> caseBase) {
        System.out.println("*** START INVENTORY DUMP ***");
        Utils.dumpToConsole(caseBase, query);
        System.out.println("*** DONE INVENTORY DUMP ***");
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

        System.out.print("Enter which item to critique: ");
        int selection;
        try {
            selection = Integer.valueOf(in.readLine());

            System.out.print("Like (1) or Dislike(0)? ");
            boolean isPositiveCritique = Integer.valueOf(in.readLine()) == 1;

            // TODO: allow multiple attributes
            System.out.print("Color (0), type (1) or label(2)?: ");
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
        int valueIndex = -1;
        double[] weights = null;

        // get value weight index, current weights
        switch (critique.feedback().attribute()) {
            case 0: {
                if (query.attributes().color() == null) {
                    // initialize with evenly weighted values
                    query.attributes().color(new Color());
                }
                valueIndex = critique.item().attributes().color().currentValue().ordinal();
                weights = query.attributes().color().getValueWeights();
                break;
            }
            case 1: {
                if (query.attributes().type() == null) {
                    // initialize with evenly weighted values
                    query.attributes().type(new ClothingType());
                }
                valueIndex = critique.item().attributes().type().currentValue().ordinal();
                weights = query.attributes().type().getValueWeights();
                break;
            }
            case 2: {
                if (query.attributes().label() == null) {
                    // initialize with evenly weighted values
                    query.attributes().label(new Label());
                }
                valueIndex = critique.item().attributes().label().currentValue().ordinal();
                weights = query.attributes().label().getValueWeights();
                break;
            }
        }

        // calculate new weights
        if (valueIndex != -1 || weights != null) {
            if (critique.feedback().isPositiveFeedback()) {
                likeValue(valueIndex, weights);
            } else {
                dislikeValue(valueIndex, weights);
            }
        }
    }

    /**
     * Doubles the weight of the liked value, clamps to one. Subtracts the
     * difference evenly from other weights, clamps them to zero if necessary.
     */
    public static void likeValue(int valueIndex, double[] weights) {
        double likedWeight = weights[valueIndex];

        weights[valueIndex] += likedWeight;

        // sum can not exceed 1.0
        if (weights[valueIndex] > 1.0) {
            Arrays.fill(weights, 0.0);
            weights[valueIndex] = 1.0;
        }

        // subtract average from other weights
        double redistributed = likedWeight / (weights.length - 1);
        for (int i = 0; i < weights.length; i++) {
            if (i != valueIndex) {
                weights[i] -= redistributed;
                // floor at 0.0
                if (weights[i] < 0) {
                    weights[i] = 0.0;
                }
            }
        }
    }

    /**
     * Sets the disliked values weight to zero, distributes its ex-weight evenly
     * to other weights.
     */
    public static void dislikeValue(int valueIndex, double[] weights) {
        double dislikedWeight = weights[valueIndex];

        weights[valueIndex] = 0.0;

        int nonZeroCount = 0;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] != 0) {
                nonZeroCount++;
            }
        }

        double redistributed = dislikedWeight / nonZeroCount;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] != 0) {
                weights[i] += redistributed;
            }
        }
    }

}
