import java.util.ArrayList;
import java.util.List;

public class BoundedGreedySelection {

    public static final double ALPHA = 0.5;

    /**
     * Chooses <code>bound*limit</code> items most similar to current query.
     * Returns <code>limit</code> items most similar to query and most
     * dissimilar to already selected items out of those.
     */
    public static List<Item> boundedGreedySelection(Query query, List<Item> caseBase, int limit,
            int bound) {
        // FIXME: Similarity to the query has to be only calculated once per
        // cycle!
        Utils.sortBySimilarityToQuery(query, caseBase);

        // TODO: get first b*k items

        List<Item> recommendations = new ArrayList<Item>();

        // add recommendations
        for (int i = 0; i < limit; i++) {
            sortByQuality(caseBase, recommendations, query);

            // get top item, remove it from remaining cases
            recommendations.add(caseBase.remove(0));
        }

        return recommendations;
    }

    /**
     * Calculates the quality for each item, sorts the case base with highest
     * quality first.
     */
    private static void sortByQuality(List<Item> caseBase, List<Item> recommendations, Query query) {
        // TODO Sort by quality
        for (Item item : caseBase) {
            // FIXME: Similarity to the query has to be only calculated once per
            // cycle!
            double quality = ALPHA * Similarity.similarity(query.attrs, item.attrs) + (1 - ALPHA)
                    * relativeDiversity(item, recommendations);
        }
    }

    /**
     * Calculates the relative diversity of an item to the current list of
     * recommendations.
     */
    private static double relativeDiversity(Item item, List<Item> recommendations) {
        if (recommendations.size() == 0) {
            // default to 1 for R={}
            return 1;
        }

        double similarity = 0;
        for (Item recommendation : recommendations) {
            similarity += 1 - Similarity.similarity(item.attrs, recommendation.attrs);
        }
        similarity /= recommendations.size();

        return similarity;
    }

}
