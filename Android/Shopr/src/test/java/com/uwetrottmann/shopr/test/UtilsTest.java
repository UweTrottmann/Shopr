
package com.uwetrottmann.shopr.test;

import static org.fest.assertions.api.Assertions.assertThat;

import com.uwetrottmann.shopr.utils.Utils;

import org.junit.Test;

public class UtilsTest {

    @Test
    public void testExtractFirstUrl() {
        // make sure those stupid ebay urls with , are split correctly
        String actual = Utils
                .extractFirstUrl("[http://i.ebayimg.com/00/s/NjUwWDY1MA==/z/ZJwAAMXQHPFRg9sU/%24T2eC16N,%21%29QE9s3HD%29Y%28BRg9sT2H-w%7E%7E60_1.JPG?set_id=8800005007,http://kleiderkabine.com/Bilder/kfdlkfoe.jpg]");
        String expected = "http://i.ebayimg.com/00/s/NjUwWDY1MA==/z/ZJwAAMXQHPFRg9sU/%24T2eC16N,%21%29QE9s3HD%29Y%28BRg9sT2H-w%7E%7E60_1.JPG?set_id=8800005007";
        assertThat(actual).isEqualTo(expected);

        // only one
        actual = Utils
                .extractFirstUrl("[http://www.arts-outdoors.de/Shop/images/product_images/original_images/2828_0.jpg]");
        expected = "http://www.arts-outdoors.de/Shop/images/product_images/original_images/2828_0.jpg";
        assertThat(actual).isEqualTo(expected);

        // multiples, no commas
        actual = Utils
                .extractFirstUrl("[http://img4.guna.de/img/artikel/img.d/kat.d/zoom.d/34471-CHg.jpg,http://img4.guna.de/img/artikel/img.d/kat.d/zoom.d/34471-CHd2.jpg]");
        expected = "http://img4.guna.de/img/artikel/img.d/kat.d/zoom.d/34471-CHg.jpg";
        assertThat(actual).isEqualTo(expected);
    }

}
