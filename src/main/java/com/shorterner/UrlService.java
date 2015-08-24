package com.shorterner;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.shorterner.utility.UrlCustom;

import java.util.ArrayList;

/**
 * Created by Vincenzo on 16/07/2015.
 */
public class UrlService {
    private final DB db;
    private final DBCollection collection;

    public UrlService(DB db) {
        this.db = db;
        this.collection = db.getCollection("url");
        this.collection.createIndex(new BasicDBObject("customURL", 1).append("unique", true));


    }

    public void createNewURL(String longURL, String shortURL) {
        URL url = new URL(longURL, shortURL);

        collection.insert(url.getBasicDBObjectClass());
    }

    public URL findURLByShortUrl(String shortURL) {
            return new URL((BasicDBObject) collection.findOne(new BasicDBObject("customURL", shortURL.replace("http://", ""))));
    }

    public URL findUrlByLongURL(String longURL) {
        return new URL((BasicDBObject) collection.findOne(new BasicDBObject("_id", longURL.replace("http://", ""))));
    }

    public void aggiornaUrl(URL url) {
        collection.update( new BasicDBObject().append("_id", url.getLongURL()),url.getBasicDBObjectClass());
    }

    public URL findUrlByCustomUrl(String url) {
        return new URL((BasicDBObject) collection.findOne(new BasicDBObject("customURL", url)));
    }
}
