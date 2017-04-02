package io.varaga.apps.utils.geo;

/**
 * <p>
 * Represents a point on the surface of a sphere. (The Earth is almost spherical.)
 * </p>
 *
 * <p>
 * To create an instance, call one of the static methods fromDegrees() or fromRadians().
 * </p>
 *
 * <p>
 * This code was originally published at
 * <a href="http://JanMatuschek.de/LatitudeLongitudeBoundingCoordinates#Java"> http://JanMatuschek.de/LatitudeLongitudeBoundingCoordinates#Java</a>.
 * </p>
 *
 * @author Jan Philip Matuschek
 * @version 22 September 2010
 */
public class GeoLocation {

    private double radLat; // latitude in radians
    private double radLon; // longitude in radians

    private double degLat; // latitude in degrees
    private double degLon; // longitude in degrees

    private static final double MIN_LAT = Math.toRadians(-90d); // -PI/2
    private static final double MAX_LAT = Math.toRadians(90d); //  PI/2
    private static final double MIN_LON = Math.toRadians(-180d); // -PI
    private static final double MAX_LON = Math.toRadians(180d); //  PI

    private GeoLocation() {
    }

    /**
     * @param latitude
     *            the latitude, in degrees.
     * @param longitude
     *            the longitude, in degrees.
     */
    public static GeoLocation fromDegrees(final double latitude, final double longitude) {
        final GeoLocation result = new GeoLocation();
        result.radLat = Math.toRadians(latitude);
        result.radLon = Math.toRadians(longitude);
        result.degLat = latitude;
        result.degLon = longitude;
        result.checkBounds();
        return result;
    }

    private void checkBounds() {
        if (radLat < MIN_LAT || radLat > MAX_LAT || radLon < MIN_LON || radLon > MAX_LON) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return the latitude, in degrees.
     */
    public double getLatitudeInDegrees() {
        return degLat;
    }

    /**
     * @return the longitude, in degrees.
     */
    public double getLongitudeInDegrees() {
        return degLon;
    }

    /**
     * @return the latitude, in radians.
     */
    public double getLatitudeInRadians() {
        return radLat;
    }

    /**
     * @return the longitude, in radians.
     */
    public double getLongitudeInRadians() {
        return radLon;
    }

    @Override
    public String toString() {
        return "(" + degLat + "\u00B0, " + degLon + "\u00B0) = (" + radLat + " rad, " + radLon + " rad)";
    }

    /**
     * Computes the great circle distance between this GeoLocation instance and the location argument.
     * 
     * @param radius
     *            the radius of the sphere, e.g. the average radius for a spherical approximation of the figure of the Earth is approximately 6371.01
     *            kilometers.
     * @return the distance, measured in the same unit as the radius argument.
     */
    public double distanceTo(final GeoLocation location, final double radius) {
        return Math.acos(
                Math.sin(radLat) * Math.sin(location.radLat) + Math.cos(radLat) * Math.cos(location.radLat) * Math.cos(radLon - location.radLon))
                * radius;
    }

}
