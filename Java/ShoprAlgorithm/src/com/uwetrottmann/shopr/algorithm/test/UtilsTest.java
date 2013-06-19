
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
        unsortedCases
                .
                add(new Item().attributes(new Attributes().putAttribute(new Color(Color.Value.RED))));
        unsortedCases.
                add(new Item().attributes(new Attributes()
                        .putAttribute(new Color(Color.Value.BLACK))));
        unsortedCases
                .add(new Item().attributes(new Attributes()
                        .putAttribute(new Color(Color.Value.RED))));
        unsortedCases
                .add(new Item().attributes(new Attributes().putAttribute(new Color(
                        Color.Value.GREEN))));
        unsortedCases
                .add(new Item().attributes(new Attributes()
                        .putAttribute(new Color(Color.Value.BLUE))));

        // sample query
        Query query = new Query();
        query.attributes().putAttribute(new Color(Color.Value.RED));

        Utils.sortBySimilarityToQuery(query, unsortedCases);

        // top two items are red?
        Utils.dumpToConsole(unsortedCases, query);
        assertThat(
                unsortedCases.get(0).attributes().getAttributeById(Color.ID).getValueWeights()[Color.Value.RED
                        .ordinal()]).isEqualTo(1.0);
        assertThat(
                unsortedCases.get(1).attributes().getAttributeById(Color.ID).getValueWeights()[Color.Value.RED
                        .ordinal()]).isEqualTo(1.0);

        // equal weight on all colors
        query.attributes().putAttribute(new Color());

        Utils.sortBySimilarityToQuery(query, unsortedCases);

        // nothing should have changed
        Utils.dumpToConsole(unsortedCases, query);
        assertThat(
                unsortedCases.get(0).attributes().getAttributeById(Color.ID).getValueWeights()[Color.Value.RED
                        .ordinal()]).isEqualTo(1.0);
        assertThat(
                unsortedCases.get(1).attributes().getAttributeById(Color.ID).getValueWeights()[Color.Value.RED
                        .ordinal()]).isEqualTo(1.0);
    }

}
