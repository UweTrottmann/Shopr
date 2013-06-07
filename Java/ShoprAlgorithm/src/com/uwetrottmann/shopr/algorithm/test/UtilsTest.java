
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

        // top two items are red?
        dumpToConsole(unsortedCases, query);
        assertThat(
                unsortedCases.get(0).attributes().color().getValueWeights()[Color.Value.RED
                        .ordinal()]).isEqualTo(1.0);
        assertThat(
                unsortedCases.get(1).attributes().color().getValueWeights()[Color.Value.RED
                        .ordinal()]).isEqualTo(1.0);

        // equal weight on all colors
        query.attributes().color(new Color());

        Utils.sortBySimilarityToQuery(query, unsortedCases);

        // nothing should have changed
        dumpToConsole(unsortedCases, query);
        assertThat(
                unsortedCases.get(0).attributes().color().getValueWeights()[Color.Value.RED
                        .ordinal()]).isEqualTo(1.0);
        assertThat(
                unsortedCases.get(1).attributes().color().getValueWeights()[Color.Value.RED
                        .ordinal()]).isEqualTo(1.0);
    }

    private void dumpToConsole(ArrayList<Item> unsortedCases, Query query) {
        System.out.println("Query " + query.attributes().color());
        for (Item item : unsortedCases) {
            System.out.println("Item " + item.attributes().color()
                    + " querySimilarity: " + item.querySimilarity());
        }
    }

}
