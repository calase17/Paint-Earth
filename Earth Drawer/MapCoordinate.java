package assignment;

public class MapCoordinate implements Comparable<MapCoordinate> {
    public final double ALTITUDE;
    public final double LATITUDE;
    public final double LONGITUDE;
    public static final double RADIUS_OF_EARTH = 6371;

    public MapCoordinate(double longitude, double latitude, double altitude) {
        this.ALTITUDE = altitude;
        this.LATITUDE = latitude;
        this.LONGITUDE = longitude;

    }

    public double distanceTo(MapCoordinate mapCoordinate) { // implementation of haversine formula
        double diffLon = Math.toRadians(LONGITUDE - mapCoordinate.LONGITUDE);
        double diffLat = Math.toRadians(LATITUDE - mapCoordinate.LATITUDE);
        double x = Math.pow(Math.sin(diffLat / 2), 2) +
                Math.cos(Math.toRadians(LATITUDE))
                        * Math.cos(Math.toRadians(mapCoordinate.LATITUDE))
                        * Math.pow(Math.sin(diffLon / 2), 2);

        double dist = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));
        return (Math.round(dist * RADIUS_OF_EARTH));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;}

        MapCoordinate that = (MapCoordinate) o;

        if (Double.compare(that.ALTITUDE, ALTITUDE) != 0) {
            return false;
        }
        if (Double.compare(that.LATITUDE, LATITUDE) != 0) {
            return false;
        }
        return Double.compare(that.LONGITUDE, LONGITUDE) == 0;
    }


    @Override
    public String toString() {
        return "Coordinates (" +
                "Longitude: " + LONGITUDE +
                ", Latitude: " + LATITUDE+
                " at Altitude: "+ALTITUDE+ ")";
    }


    public int compareTo(MapCoordinate mapCoordinate) {
        int comparison1 = (int) (this.ALTITUDE - mapCoordinate.ALTITUDE);
        int comparison2 = (int) (this.LATITUDE - mapCoordinate.LATITUDE);
        int comparison3 = (int) (this.LONGITUDE - mapCoordinate.LONGITUDE);
        int result = comparison1 + comparison2 + comparison3;

        return result; // if result is 0 the two objects are equal
    }
}