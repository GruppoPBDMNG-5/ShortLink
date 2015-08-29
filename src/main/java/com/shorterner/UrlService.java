package com.shorterner;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.shorterner.entity.Statistiche;
import com.shorterner.entity.URL;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Vincenzo on 16/07/2015.
 */
public class UrlService {
    private final DB db;
    private final DBCollection collection;
    private final DBCollection collectionStatistiche;

    public UrlService(DB db) {
        this.db = db;
        this.collection = db.getCollection("url");
        this.collectionStatistiche = db.getCollection("statistiche");
        this.collection.createIndex(new BasicDBObject("customURL", 1), "customURL", true);
        this.collection.createIndex(new BasicDBObject("click", -1));
    }

    public void createNewURL(String longURL, String shortURL) {
        URL url = new URL(longURL, shortURL);
        collection.insert(url.getBasicDBObjectClass());
        findTopTen();
    }

    public URL findURLByShortUrl(String shortURL) {
        shortURL = shortURL.replace("http://", "");
        shortURL = shortURL.replace("https://", "");
        return new URL((BasicDBObject) collection.findOne(new BasicDBObject("customURL", shortURL)));
    }

    public URL findUrlByLongURL(String longURL) {
        return new URL((BasicDBObject) collection.findOne(new BasicDBObject("_id", longURL)));
    }

    public void aggiornaUrl(URL url) {
        collection.update(new BasicDBObject().append("_id", url.getLongURL()), url.getBasicDBObjectClass());
    }

    public URL findUrlByCustomUrl(String url) {
        return new URL((BasicDBObject) collection.findOne(new BasicDBObject("customURL", url)));
    }

    public List<DBObject> findTopTen() {
        return collection.find().limit(10).sort(new BasicDBObject("click", -1)).toArray();
    }

    public Statistiche prendiStatistiche() {
        try {
            return new Statistiche(findTopTen(), (BasicDBObject) collectionStatistiche.findOne());

        } catch (NullPointerException e) {
            Statistiche statistiche = new Statistiche();
            collectionStatistiche.insert(statistiche.getBasicDBObjectClass());
            return new Statistiche(findTopTen(), (BasicDBObject) collectionStatistiche.findOne());

        }
    }

    public void aggiornaStatistiche(Statistiche statistiche) {
        collectionStatistiche.update(new BasicDBObject().append("_id", new ObjectId(statistiche.getId())), statistiche.getBasicDBObjectClass());
    }
}
