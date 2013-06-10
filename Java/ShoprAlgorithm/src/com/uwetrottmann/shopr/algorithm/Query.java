
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Attributes;

/**
 * Holds the current features (one vector each, distribution of weight over all
 * values) which have been critiqued. Also some pre-defined static ones like for
 * distance and opening hours which are system-imposed.
 */
public class Query {

    private Attributes attrs;

    public Query() {
        attrs = new Attributes();
    }

    public Attributes attributes() {
        return attrs;
    }
}
