
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Item;

import java.util.ArrayList;
import java.util.List;

public class AdaptiveSelection {

    private static final int NUM_RECOMMENDATIONS = 5;

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

        while (!isAbort) {
            List<Item> recommendations = itemRecommend(query, NUM_RECOMMENDATIONS, critique);
            critique = userReview(recommendations, query);
            queryRevise(query, critique);
        }
    }

    /**
     * Takes the current query, number of recommended items to return, the last
     * critique. Returns a list of recommended items based on the case-base.
     */
    private static List<Item> itemRecommend(Query query, int numItems, Critique lastCritique) {
        /*
         * The caseBase will later be stored in a database (due to its size).
         * Think about optimizations which could be applied.
         */
        // Filter case-base to match hard-limits (location, opening hours)
        List<Item> caseBase = Utils.getLimitedCaseBase();

        List<Item> recommendations;

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
            recommendations = new ArrayList<Item>(caseBase.subList(0, numItems - 1));
        } else {
            /*
             * Negative progress: user disliked one or more of the features of
             * one recommended item. Or: first run.
             */
            // REFOCUS: show diverse recommendations
            int bound = 10;
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
     * 
     * @param recommendations
     * @return
     */
    private static Critique userReview(List<Item> recommendations, Query query) {
        Utils.dumpToConsole(recommendations, query);

        int selection = Integer.valueOf(System.console().readLine(
                "Enter the number of item to critique: "));

        boolean isPositiveCritique = Integer.valueOf(System.console().readLine(
                "Like (1) or Dislike(0)? ")) == 1;

        // TODO: allow multiple attributes
        int attribute = Integer.valueOf(System.console().readLine(
                "Color (0) or type (1)?: "));

        Critique critique = new Critique();
        critique.item(recommendations.get(selection));
        critique.feedback(new Feedback().isPositiveFeedback(isPositiveCritique)
                .attribute(attribute));

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
            case 0:
                query.attributes().color(critique.item().attributes().color());
                break;
            case 1:
                query.attributes().type(critique.item().attributes().type());
                break;
        }
    }

}
