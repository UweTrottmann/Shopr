
package com.uwetrottmann.shopr.algorithm.test;

import static org.fest.assertions.api.Assertions.assertThat;

import com.uwetrottmann.shopr.algorithm.Query;
import com.uwetrottmann.shopr.algorithm.Utils;
import com.uwetrottmann.shopr.algorithm.model.Attributes;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.Item;

import org.junit.Test;

import java.util.ArrayList;

public class UtilsTest {

    @Test
    public void testSortBySimilarityToQuery() {
        // unsorted list of items
        ArrayList<Item> unsortedCases = new ArrayList<Item>();
        unsortedCases.
                add(new Item().attributes(new Attributes().color(new Color(Color.Value.RED))));
        unsortedCases.
                add(new Item().attributes(new Attributes().color(new Color(Color.Value.BLACK))));
        unsortedCases
                .add(new Item().attributes(new Attributes().color(new Color(Color.Value.RED))));
        unsortedCases
                .add(new Item().attributes(new Attributes().color(new Color(Color.Value.GREEN))));
        unsortedCases
                .add(new Item().attributes(new Attributes().color(new Color(Color.Value.BLUE))));

        // sample query
        Query query = new Query().attributes(new Attributes().color(new Color(Color.Value.RED)));

        Utils.sortBySimilarityToQuery(query, unsortedCases);

        for (Item item : unsortedCases) {
            System.out.println("Item " + item.attributes().color().toString()
                    + " querySimilarity: " + item.querySimilarity());
        }

        // top item is red?
        assertThat(
                unsortedCases.get(0).attributes().color().getValueWeights()[Color.Value.RED
                        .ordinal()]).isEqualTo(1.0);
    }

}
