package com.shorterner.utility;

import com.mongodb.BasicDBObject;

/**
 * Created by Vincenzo on 22/08/2015.
 */
public class UrlCustom {
    private String longURL;
    private String customURL;

    public UrlCustom(BasicDBObject customURL) {
        this.customURL=customURL.getString("customURL");
        this.longURL =  customURL.getString("longURL");

    }

    public void setCustomURL(String customURL) {
        this.customURL = customURL;
    }

    public String getCustomURL() {
        return customURL;
    }

    public String getLongURL() {
        return longURL;
    }

    @Override
    public String toString() {
        return "UrlCustom{" +
                "longURL='" + longURL + '\'' +
                ", customURL='" + customURL + '\'' +
                '}';
    }
}
