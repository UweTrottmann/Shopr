
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Attributes.Attribute;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.algorithm.model.Label;
import com.uwetrottmann.shopr.algorithm.model.Price;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AdaptiveSelection {

    private static final int NUM_RECOMMENDATIONS_DEFAULT = 8;
    private static final int BOUND_DEFAULT = 10;
    private static final boolean DUMP_INVENTORY = false;
    private static final boolean IS_USING_DIVERSITY_DEFAULT = true;

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
    private Critique mCurrentCritique;
    private int mNumRecommendations;
    private List<Item> mCurrentRecommendations;
    private boolean mIsUsingDiversity;
    private LocalizationModule mLocalizer;

    private AdaptiveSelection() {
        mCaseBase = new ArrayList<Item>();
        mQuery = new Query();
        mNumRecommendations = NUM_RECOMMENDATIONS_DEFAULT;
        mIsUsingDiversity = IS_USING_DIVERSITY_DEFAULT;
        mCurrentRecommendations = new ArrayList<Item>();
    }

    public void setLocalizationModule(LocalizationModule localizer) {
        mLocalizer = localizer;
    }

    public LocalizationModule getLocalizationModule() {
        return mLocalizer;
    }

    /**
     * Call before {@link #getRecommendations(Critique)} to set the initial data
     * set. Resets the query.
     */
    public void setInitialCaseBase(List<Item> caseBase, boolean isUsingDiversity) {
        mQuery = new Query();
        mCurrentCritique = null;
        mCaseBase = caseBase;
        mIsUsingDiversity = isUsingDiversity;
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
    public List<Item> getRecommendations() {
        // build a new set of recommendations
        List<Item> recommendations;
        if (mIsUsingDiversity) {
            // using adaptive selection (diversity on negative progress)
            recommendations = itemRecommend(mCaseBase, mQuery, mNumRecommendations,
                    BOUND_DEFAULT, mIsUsingDiversity, mCurrentCritique);
        } else {
            // using similarity based recommendations
            recommendations = itemRecommendSimOnly(mCaseBase, mQuery, mNumRecommendations,
                    BOUND_DEFAULT, mCurrentCritique);
        }

        mCurrentRecommendations = recommendations;

        return recommendations;
    }

    public List<Item> getCurrentRecommendations() {
        return mCurrentRecommendations;
    }

    /**
     * Updates the item query, will influence the next set of recommendations.
     * Call {@link #getRecommendations()} afterwards.
     */
    public void submitCritique(Critique critique) {
        // Update the current query with the new critique
        if (critique != null) {
            queryRevise(mQuery, critique);
            mCurrentCritique = critique;
        }
    }

    /**
     * Returns the last critiqued item. May be {@code null}.
     */
    public Item getLastCritiquedItem() {
        if (mCurrentCritique != null) {
            return mCurrentCritique.item();
        }
        return null;
    }

    public Query getCurrentQuery() {
        return mQuery;
    }

    /**
     * Toggles usage of diverse recommendations when the last critique indicated
     * negative progress (adaptive selection). If disabled always uses
     * similarity based recommendations.
     */
    public void setIsUsingDiversity(boolean isUsingDiversity) {
        mIsUsingDiversity = isUsingDiversity;
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
                    NUM_RECOMMENDATIONS_DEFAULT, BOUND_DEFAULT, IS_USING_DIVERSITY_DEFAULT,
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
            int bound, boolean isUsingDiversity, Critique lastCritique) {

        List<Item> recommendations = new ArrayList<Item>();

        if ((lastCritique != null && !isUsingDiversity) ||
                lastCritique != null && lastCritique.item() != null
                && lastCritique.feedback().isPositiveFeedback()) {
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
                recommendations.add(caseBase.get(i));
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
        if (lastCritique != null && lastCritique.item() != null) {
            // check if it is already in the list
            boolean isAlreadyPresent = false;
            for (Item item : recommendations) {
                if (item.id() == lastCritique.item().id()) {
                    isAlreadyPresent = true;
                    break;
                }
            }
            // if not: replace the last one with it
            if (!isAlreadyPresent) {
                recommendations.remove(recommendations.size() - 1);
                recommendations.add(lastCritique.item());
            }
        }

        return recommendations;
    }

    /**
     * Takes the current query, number of recommended items to return, the last
     * critique. Returns a list of recommended items based on the case-base.
     */
    private static List<Item> itemRecommendSimOnly(List<Item> caseBase, Query query, int numItems,
            int bound, Critique lastCritique) {
        List<Item> recommendations = new ArrayList<Item>();

        if (lastCritique != null) {
            /*
             * REFINE: Show similar recommendations by sorting the case-base in
             * decreasing similarity to current query. Return top k items.
             */
            Utils.sortBySimilarityToQuery(query, caseBase);
            for (int i = 0; i < numItems; i++) {
                // Remove previous recs to avoid same items
                recommendations.add(caseBase.remove(0));
            }
        } else {
            /*
             * First run: show diverse results.
             */
            // REFOCUS: show diverse recommendations
            recommendations = BoundedGreedySelection
                    .boundedGreedySelection(query, caseBase, numItems, bound);
        }

        // Carry the critiqued so the user may critique it further.
        if (lastCritique != null && lastCritique.item() != null) {
            // check if it is already in the list
            boolean isAlreadyPresent = false;
            for (Item item : recommendations) {
                if (item.id() == lastCritique.item().id()) {
                    isAlreadyPresent = true;
                    break;
                }
            }
            // if not: replace the last one with it
            if (!isAlreadyPresent) {
                // Remove previous recs to avoid same items
                caseBase.add(recommendations.remove(recommendations.size() -
                        1));
                recommendations.add(lastCritique.item());
            }
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
            System.out.print("Color (0), type (1), label(2), price(3)? Enter , separated: ");
            String input = in.readLine();
            String[] attrs = input.split(",");
            Feedback feedback = new Feedback();
            for (int i = 0; i < attrs.length; i++) {
                switch (Integer.valueOf(attrs[i])) {
                    case 0:
                        feedback.addAttributes(new Color());
                        break;
                    case 1:
                        feedback.addAttributes(new ClothingType());
                        break;
                    case 2:
                        feedback.addAttributes(new Label());
                        break;
                    case 3:
                        feedback.addAttributes(new Price());
                        break;
                }
            }

            Critique critique = new Critique();
            critique.item(recommendations.get(selection));
            critique.feedback(feedback.isPositiveFeedback(isPositiveCritique));

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
        List<Attribute> attributes = critique.feedback().attributes();
        for (Attribute attribute : attributes) {
            critique.item().attributes().getAttributeById(attribute.id())
                    .critiqueQuery(query, critique.feedback().isPositiveFeedback());
        }
    }
}
