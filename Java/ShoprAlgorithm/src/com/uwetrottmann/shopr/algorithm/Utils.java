
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;
import com.uwetrottmann.shopr.algorithm.model.Label;
import com.uwetrottmann.shopr.algorithm.model.Price;

import java.math.BigDecimal;
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

        int count = 0;
        for (Color.Value color : Color.Value.values()) {
            for (ClothingType.Value type : ClothingType.Value.values()) {
                for (Label.Value label : Label.Value.values()) {
                    // id
                    int id = count++;
                    // random price
                    double random = Math.random() * 200;
                    // named after type and label + id
                    ClothingType typeValue = new ClothingType(type);
                    Label labelValue = new Label(label);
                    String name = typeValue.currentValue().descriptor() + " "
                            + labelValue.currentValue().descriptor() + id;

                    BigDecimal price = new BigDecimal(random);
                    cases.add(new Item()
                            .id(id)
                            .name(name)
                            .price(price)
                            // use label to define shop
                            .shopId(label.ordinal())
                            .attributes(new Attributes()
                                    .putAttribute(new Color(color))
                                    .putAttribute(typeValue)
                                    .putAttribute(labelValue)
                                    .putAttribute(new Price(price))));
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
            System.out.println("Query " + query.attributes().getAllAttributesString());
        } else {
            System.out.println("Query EMPTY");
        }
        for (int i = 0; i < cases.size(); i++) {
            Item item = cases.get(i);
            System.out
                    .println("[" + i + "] Item "
                            + item.attributes().getAllAttributesString()
                            + " sim " + item.querySimilarity()
                            + " qual " + item.quality());
        }
    }

}
