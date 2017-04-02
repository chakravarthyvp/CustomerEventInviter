package io.varaga.apps.utils.geo;

import ch.hsr.geohash.GeoHash;

/**
 * Utils class that has functionalities as listed below# 1. Generates a GeoHash for a given co-ordinate at a given precision. GeoHash is a technique
 * used to encode a lat/long position at different precisions levels. Basically, a lat/long can be expressed as 64 bit with base32 encoding. For ex.,
 * a GeoHash at 100KM for Dublin is gc7, while at 1000KM is gc, grouping gps co-ordinates can be used for routing/distribution and subset searches
 *
 * @TODO - This class can be refactored to honour mutiple precision levels for grid key generation. This is hardcode to 100KMs for now.
 */
public final class GeoUtils {

    //earth radius in kms
    private static final double earthRadius = 6371.01;

    /**
     * Get GridKey/GeoHash for a location, lat/long, with 3(~100KM bounding co-ordinates where this location is nearly the center) character encoding
     *
     * @param latitude
     *            - latitude in GPS co-ordinate
     * @param longitude
     *            - longitude in GPS co-ordinate
     * @return - encoded hash key
     */
    public static String getCellHashGroupFor100KMs(final double latitude, final double longitude) {
        /**
         * roughly gets a bound for the co-ordinates within 100 km for the given lat/long the character precision determines the bounding box. 7 -
         * roughly upto 110 meters - 35 bits 6 - roughly upto 1.2 km - 30 bits 5 - roughly upto 7 kms - 25 bits 3 - roughly upto 220 kms 2 - roughly
         * 1000 km
         */

        return GeoHash.geoHashStringWithCharacterPrecision(latitude, longitude, 3);
    }

    /**
     * Calculates the Great Circle distance between 2 points.
     *
     * @param fromLatitude
     *            - from lat
     * @param fromLongitude
     *            - from longitude
     * @param toLatitude
     *            - to lat
     * @param toLongitude
     *            - to longitude
     * @return - distance as double, in KM, between 2 points
     */
    public static double getDistanceInKMBetween(final double fromLatitude, final double fromLongitude, final double toLatitude,
                                                final double toLongitude) {
        //gets the degress converted to radians and composes the location
        final GeoLocation location1 = GeoLocation.fromDegrees(fromLatitude, fromLongitude);
        final GeoLocation location2 = GeoLocation.fromDegrees(toLatitude, toLongitude);
        //great circle distance
        return location1.distanceTo(location2, earthRadius);
    }

}
