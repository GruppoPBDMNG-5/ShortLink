package com.shorterner;

import com.mongodb.BasicDBObject;

/**
 * Created by Vincenzo on 20/08/2015.
 */
public class URL {
private String longURL;
    private String shortURL;

    public URL(BasicDBObject dbObject) {
        this.longURL =  dbObject.getString("longURL");
        this.shortURL = dbObject.getString("shortURL");
    }

    public URL(String longURL, String shortURL) {
        this.longURL = longURL;
        this.shortURL = shortURL;
    }

    public String getLongURL() {
        return longURL;
    }

    public String getShortURL() {
        return shortURL;
    }
}
