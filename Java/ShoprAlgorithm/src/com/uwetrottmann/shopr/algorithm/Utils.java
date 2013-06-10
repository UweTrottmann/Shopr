
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Item;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {

    /**
     * Returns a subset of the overall case base filtered by hard-limits like
     * location, availability and opening hours.
     */
    public static List<Item> getLimitedCaseBase() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Sorts items by similarity to query using the Sim function: sim(query,
     * item of caseBase).
     */
    public static void sortBySimilarityToQuery(Query query, List<Item> caseBase) {
        // calculate similarity value for each item
        for (Item item : caseBase) {
            item.querySimilarity(Similarity.similarity(query.attributes(), item.attributes()));
        }

        // sort highest similarity first
        Collections.sort(caseBase, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                // is o2 smaller?
                if (o1.querySimilarity() > o2.querySimilarity()) {
                    return -1;
                }
                // is o2 bigger?
                if (o1.querySimilarity() < o2.querySimilarity()) {
                    return 1;
                }
                // they are equal!
                return 0;
            }
        });
    }

    public static void dumpToConsole(List<Item> cases, Query query) {
        System.out.println("Query " + query.attributes().color());
        for (Item item : cases) {
            System.out.println("Item " + item.attributes().color() + " " + item.attributes().type()
                    + " querySimilarity: " + item.querySimilarity());
        }
    }

}
