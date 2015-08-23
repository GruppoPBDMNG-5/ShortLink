package com.shorterner;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.shorterner.utility.UrlCustom;

/**
 * Created by Vincenzo on 16/07/2015.
 */
public class UrlService {
    private final DB db;
    private final DBCollection collection;
    private final DBCollection collectionCustom;

    public UrlService(DB db) {
        this.db = db;
        this.collection = db.getCollection("url");
        this.collectionCustom = db.getCollection("customUrl");
        this.collectionCustom.createIndex(new BasicDBObject("customURL", 1).append("unique", true));

        this.collection.createIndex(new BasicDBObject("longURL", 1).append("unique", true));
        this.collection.createIndex(new BasicDBObject("shortURL", 1).append("unique", true));

    }

    public void createNewURL(String longURL, String shortURL) {
        URL url = new URL(longURL, shortURL);
        collection.insert(new BasicDBObject("longURL", url.getLongURL()).append("shortURL", url.getShortURL()));
    }

    public String findURLByShortUrl(String shortURL, Boolean custom) {
        if (custom)
            return new URL((BasicDBObject) collectionCustom.findOne(new BasicDBObject("customURL", shortURL.replace("http://", "")))).getLongURL();
        else
            return new URL((BasicDBObject) collection.findOne(new BasicDBObject("shortURL", shortURL.replace("http://", "")))).getLongURL();
    }

    public String findShortUrlByLongURL(String longURL) {

        return new URL((BasicDBObject) collection.findOne(new BasicDBObject("longURL", longURL.replace("http://", "")))).getShortURL();
    }

    public void createUrlCustom(UrlCustom urlCustom) {
        collectionCustom.insert(new BasicDBObject("customURL", urlCustom.getCustomURL()).append("longURL", urlCustom.getLongURL()));
    }

    public UrlCustom findUrlCustomByCustomurl(String url) {
        return new UrlCustom((BasicDBObject) collectionCustom.findOne(new BasicDBObject("customURL", url)));
    }
}
