import java.util.ArrayList;
import java.util.List;

public class AdaptiveSelection {

    public static void main(String[] args) {
        adaptiveSelection();
    }

    /**
     * Runs one recommendation cycle.
     */
    private static void adaptiveSelection() {
        itemRecommend(null, null, 15, null, FeedbackType.FIRST_RUN);
        userReview();
        queryRevise();
    }

    /**
     * Takes the case-base, current query, number of recommended items to
     * return, the currently critiqued on item. Returns a list of recommended
     * items.
     */
    private static List<Item> itemRecommend(List<Item> caseBase, Query query,
            int numItems, Item critiquedItem, FeedbackType critiqueType) {
        /*
         * The caseBase will later be stored in a database (due to its size).
         * Think about optimizations which could be applied.
         */
        List<Item> recommendations = new ArrayList<Item>();

        // Filter case-base to match hard-limits (location, opening hours)
        recommendations = getLimitedCaseBase();

        if (critiqueType == FeedbackType.POSITIVE_FEEDBACK) {
            /*
             * Positive progress: user liked one or more features of one of the
             * recommended items.
             */
            // show similar recommendations
            recommendations = reFine();
        } else {
            /*
             * Negative progress: user disliked one or more of the features of
             * one recommended item. Or: first run.
             */
            // show diverse recommendations
            recommendations = reFocus();
        }

        /*
         * Carry the critiqued item into the next set of recommendations so the
         * user may critique it further.
         */
        recommendations.add(critiquedItem);

        return recommendations;
    }

    /**
     * Returns a subset of the overall case base filtered by hard-limits like
     * location, availability and opening hours.
     */
    private static List<Item> getLimitedCaseBase() {
        // TODO Auto-generated method stub
        return null;
    }

    private static List<Item> reFocus() {
        // TODO Auto-generated method stub
        return null;
    }

    private static List<Item> reFine() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Takes the list of recommended items and elicits a critique on a feature
     * of one item from the user. Returns the liked/disliked item and which
     * feature value (! not just which feature !) was liked/disliked.
     */
    private static void userReview() {
        // TODO Auto-generated method stub
    }

    /**
     * Takes a liked/disliked item, which feature value was liked/disliked, the
     * current query. Returns a new query modified according to the given user
     * critique.
     */
    private static void queryRevise() {
        // TODO Auto-generated method stub

    }

}
