
package com.Poojan.News;


/**
 * An {@link NewsArticle} object contains information related to a single article.
 */
public class NewsArticle {

    private String distance;
    private String latitude;
    private String  longitude;
    private String level;
    private String voltage;

    public String getDistance() {
        return distance;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLevel() {
        return level;
    }

    public String getVoltage() {
        return voltage;
    }

    public NewsArticle(String distance, String latitude, String longitude, String level, String voltage) {
        this.distance = distance;

        this.latitude = latitude;
        this.longitude = longitude;
        this.level = level;
        this.voltage = voltage;
    }
}