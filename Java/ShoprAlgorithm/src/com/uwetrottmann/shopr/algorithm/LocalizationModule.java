
package com.uwetrottmann.shopr.algorithm;

/**
 * Can be passed to {@link AdaptiveSelection} to localize attribute descriptors.
 */
public interface LocalizationModule {

    /**
     * Return a localized version for the given {@link String}.
     */
    public String getLocalizedValueDescriptor(String value);

    public String getOnlyString();

    public String getAvoidString();

    public String getPreferablyString();
}
