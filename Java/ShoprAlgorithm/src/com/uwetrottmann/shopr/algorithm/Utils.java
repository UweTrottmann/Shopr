
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.algorithm.model.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {

    /**
     * Returns a subset of the overall case base filtered by hard-limits like
     * location, availability and opening hours.<br>
     * Currently only returns sample items.
     */
    public static List<Item> getLimitedCaseBase() {
        // TODO replace with actual prefiltered data
        ArrayList<Item> cases = new ArrayList<Item>();

        for (Color.Value color : Color.Value.values()) {
            for (ClothingType.Value type : ClothingType.Value.values()) {
                for (Label.Value label : Label.Value.values()) {
                    cases.add(new Item().attributes(new Attributes()
                            .color(new Color(color))
                            .type(new ClothingType(type))
                            .label(new Label(label))));
                }
            }
        }

        return cases;
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
        if (query.attributes() != null) {
            System.out.println("Query "
                    + query.attributes().color() + " "
                    + query.attributes().type() + " "
                    + query.attributes().label());
        } else {
            System.out.println("Query EMPTY");
        }
        for (int i = 0; i < cases.size(); i++) {
            Item item = cases.get(i);
            System.out
                    .println("[" + i + "] Item "
                            + item.attributes().color() + " "
                            + item.attributes().type() + " "
                            + item.attributes().label()
                            + " sim " + item.querySimilarity()
                            + " qual " + item.quality());
        }
    }

}
