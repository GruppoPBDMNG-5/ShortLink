package com.shorterner;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Created by Vincenzo on 16/07/2015.
 */
public class UrlService {
    private final DB db;
    private final DBCollection collection;

    public UrlService(DB db) {
        this.db = db;
        this.collection = db.getCollection("url");
        this.collection.createIndex(new BasicDBObject("longURL", 1).append("unique", true));
        this.collection.createIndex(new BasicDBObject("shortURL", 1).append("unique", true));

    }

    public void createNewURL(String longURL,String shortURL){
          URL url=new URL(longURL,shortURL);
        collection.insert(new BasicDBObject("longURL", url.getLongURL()).append("shortURL", url.getShortURL()));
    }

    public URL findURL(String shortURL){
        return new URL((BasicDBObject) collection.findOne(new BasicDBObject("shortURL", shortURL.replace("http://",""))));
    }
    public URL findLongURL(String longURL){
        return new URL((BasicDBObject) collection.findOne(new BasicDBObject("longURL", longURL.replace("http://",""))));
    }
}
