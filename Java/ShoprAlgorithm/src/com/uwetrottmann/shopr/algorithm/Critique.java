
package com.uwetrottmann.shopr.algorithm;

import com.uwetrottmann.shopr.algorithm.model.Item;

public class Critique {

    private Item item;
    private Feedback feedback;

    public Item item() {
        return item;
    }

    public Critique item(Item item) {
        this.item = item;
        return this;
    }

    public Feedback feedback() {
        return feedback;
    }

    public Critique feedback(Feedback feedback) {
        this.feedback = feedback;
        return this;
    }

}
