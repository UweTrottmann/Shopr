
package com.uwetrottmann.shopr.algorithm.test;

import static org.fest.assertions.api.Assertions.assertThat;

import com.uwetrottmann.shopr.algorithm.model.ClothingType;
import com.uwetrottmann.shopr.algorithm.model.Color;
import com.uwetrottmann.shopr.algorithm.model.GenericAttribute;
import com.uwetrottmann.shopr.algorithm.model.Price;

import org.junit.Test;

public class LikeDislikeTest {

    @Test
    public void testLikeValue() {
        /*
         * Using trunks because they have no similar types.
         */
        // all equal
        double[] actual = new double[] {
                0.25, 0.25, 0.25, 0.25
        };
        double weightOthers = 0.25 - (1.0 / 3) / 3;
        double[] expected = new double[] {
                weightOthers, 0.25 + 1.0 / 3, weightOthers, weightOthers
        };
        new ClothingType().likeValue(1, actual);
        assertThat(actual).isEqualTo(expected);

        // one getting bigger than zero
        expected = new double[] {
                0.0, 1.0, 0.0, 0.0
        };
        new ClothingType().likeValue(1, actual);
        new ClothingType().likeValue(1, actual);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testLikeValueWithSimilars() {
        // all equal
        double[] actual = new double[] {
                // BLUE, RED, PINK, VIOLET
                0.25, 0.25, 0.25, 0.25
        };
        new Color().likeValue(1, actual);
        double sumActual = GenericAttribute.getSum(actual);
        assertThat(sumActual).isEqualTo(1.0);

        // ensure value sum stays 1.0
        actual = new double[] {
                // BLUE, RED, PINK, VIOLET
                1.0 / 3, 0.0, 1.0 / 3, 1.0 / 3
        };
        new Color().likeValue(1, actual);
        sumActual = GenericAttribute.getSum(actual);
        assertThat(sumActual).isEqualTo(1.0);
    }

    @Test
    public void testShiftedLikeSmall() {
        // like RED
        double[] actual = new double[] {
                // BLUE, RED, PINK, VIOLET
                0.25, 0.25, 0.25, 0.25
        };
        new Color().likeValue(1, actual);

        // RED should have biggest weight
        assertThat(actual[1]).isGreaterThan(actual[0]);
        assertThat(actual[1]).isGreaterThan(actual[2]);
        assertThat(actual[1]).isGreaterThan(actual[3]);
        // sum should be one
        double sumActual = GenericAttribute.getSum(actual);
        assertThat(sumActual).isEqualTo(1.0);

        // like PURPLE
        new Color().likeValue(3, actual);

        // PURPLE should have bigger weight than RED
        assertThat(actual[3]).isGreaterThan(actual[1]);
        // sum should be one
        sumActual = GenericAttribute.getSum(actual);
        assertThat(sumActual).isEqualTo(1.0);
    }

    @Test
    public void testShiftedLikeLarge() {
        /*
         * The sums are close to one, but not exactly. This is due to the
         * imprecision of double. The impact on the results is negligible.
         */

        // like RED
        double[] actual = new double[] {
                // BLUE, RED, PINK, VIOLET, YELLOW, ...
                1.0 / 15, 1.0 / 15, 1.0 / 15, 1.0 / 15, 1.0 / 15, 1.0 / 15, 1.0 / 15, 1.0 / 15,
                1.0 / 15, 1.0 / 15, 1.0 / 15, 1.0 / 15, 1.0 / 15, 1.0 / 15, 1.0 / 15
        };
        new Color().likeValue(1, actual);

        // RED should have biggest weight
        assertThat(actual[1]).isGreaterThan(actual[0]);
        assertThat(actual[1]).isGreaterThan(actual[2]);
        assertThat(actual[1]).isGreaterThan(actual[3]);

        // like PURPLE
        new Color().likeValue(3, actual);

        // PURPLE should have bigger weight than RED
        assertThat(actual[3]).isGreaterThan(actual[1]);
    }

    @Test
    public void testdislikeValue() {
        // all equal
        double[] actual = new double[] {
                0.25, 0.25, 0.25, 0.25
        };
        double[] expected = new double[] {
                0.25 + 0.25 / 3, 0.0, 0.25 + 0.25 / 3, 0.25 + 0.25 / 3
        };
        new ClothingType().dislikeValue(1, actual);
        assertThat(actual).isEqualTo(expected);

        // two zero, keep them zero
        actual = new double[] {
                0.0, 0.5, 0.0, 0.5
        };
        expected = new double[] {
                0.0, 0.0, 0.0, 1.0
        };
        new ClothingType().dislikeValue(1, actual);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testDislikeLikeValue() {
        /*
         * Using trunks because they have no similar types.
         */
        // first dislike
        double[] actual = new double[] {
                0.25, 0.25, 0.25, 0.25
        };
        double[] expected = new double[] {
                0.25 + 0.25 / 3, 0.0, 0.25 + 0.25 / 3, 0.25 + 0.25 / 3
        };
        new ClothingType().dislikeValue(1, actual);
        assertThat(actual).isEqualTo(expected);

        // then like again
        // liked value should have highest weight now
        double weightOthers = 0.25 + 0.25 / 3 - 1.0 / (3 * 3);
        expected = new double[] {
                weightOthers, 1.0 / 3, weightOthers, weightOthers
        };
        new ClothingType().likeValue(1, actual);
        assertThat(actual).isEqualTo(expected);

        // liking with similars, make one non-similar zero
        actual = new double[] {
                1.0 / 7, 1.0 / 7, 1.0 / 7, 1.0 / 7, 1.0 / 7, 1.0 / 7, 1.0 / 7
        };
        double nonSimilarVal = 1.0 / 6 - ((1.0 / 7) / 4);
        expected = new double[] {
                1.0 / 6 + 1.0 / 7, 1.0 / 6, nonSimilarVal, nonSimilarVal,
                nonSimilarVal,
                nonSimilarVal, 0.0
        };
        new Price().dislikeValue(6, actual);
        new Price().likeValue(0, actual);
        assertThat(actual).isEqualTo(expected);
    }

}
