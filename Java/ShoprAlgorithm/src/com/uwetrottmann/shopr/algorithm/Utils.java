
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {

    /**
     * Returns a subset of the overall case base filtered by hard-limits like
     * location, availability and opening hours.
     */
    public static List<Item> getLimitedCaseBase() {
        ArrayList<Item> cases = new ArrayList<Item>();
        // dresses
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.RED))
                .type(new ClothingType(ClothingType.Value.DRESS))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.BLUE))
                .type(new ClothingType(ClothingType.Value.DRESS))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.GREEN))
                .type(new ClothingType(ClothingType.Value.DRESS))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.BLACK))
                .type(new ClothingType(ClothingType.Value.DRESS))));

        // Jeans
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.RED))
                .type(new ClothingType(ClothingType.Value.JEANS))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.BLUE))
                .type(new ClothingType(ClothingType.Value.JEANS))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.GREEN))
                .type(new ClothingType(ClothingType.Value.JEANS))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.BLACK))
                .type(new ClothingType(ClothingType.Value.JEANS))));

        // Shirts
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.RED))
                .type(new ClothingType(ClothingType.Value.SHIRT))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.BLUE))
                .type(new ClothingType(ClothingType.Value.SHIRT))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.GREEN))
                .type(new ClothingType(ClothingType.Value.SHIRT))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.BLACK))
                .type(new ClothingType(ClothingType.Value.SHIRT))));

        // Shorts
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.RED))
                .type(new ClothingType(ClothingType.Value.SHORTS))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.BLUE))
                .type(new ClothingType(ClothingType.Value.SHORTS))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.GREEN))
                .type(new ClothingType(ClothingType.Value.SHORTS))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.BLACK))
                .type(new ClothingType(ClothingType.Value.SHORTS))));

        // Trousers
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.RED))
                .type(new ClothingType(ClothingType.Value.TROUSER))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.BLUE))
                .type(new ClothingType(ClothingType.Value.TROUSER))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.GREEN))
                .type(new ClothingType(ClothingType.Value.TROUSER))));
        cases.add(new Item().attributes(new Attributes()
                .color(new Color(Color.Value.BLACK))
                .type(new ClothingType(ClothingType.Value.TROUSER))));

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
            System.out.println("Query " + query.attributes().color() + " "
                    + query.attributes().type());
        } else {
            System.out.println("Query EMPTY");
        }
        for (int i = 0; i < cases.size(); i++) {
            Item item = cases.get(i);
            System.out
                    .println("[" + i + "] Item "
                            + item.attributes().color() + " "
                            + item.attributes().type()
                            + " sim " + item.querySimilarity()
                            + " qual " + item.quality());
        }
    }

}
