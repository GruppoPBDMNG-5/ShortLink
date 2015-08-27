package com.shorterner;

import com.google.gson.Gson;
import com.mongodb.*;
import com.shorterner.utility.UrlCustom;
import org.bson.types.ObjectId;

import java.util.ArrayList;
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
        this.collectionStatistiche=db.getCollection("statistiche");
        this.collection.createIndex(new BasicDBObject("customURL", 1), "customURL", true);
        this.collection.createIndex(new BasicDBObject("click", -1));


    }
public void popola(){
            for(int i=0;i<100;i++){
            URL url1=new URL("www.capocchi."+i,"localostcapocchi"+i);
            url1.setClick(i);
            collection.insert(url1.getBasicDBObjectClass());
        }
}
    public void createNewURL(String longURL, String shortURL) {
        URL url = new URL(longURL, shortURL);


        collection.insert(url.getBasicDBObjectClass());
        findTopTen();
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
    public List<DBObject> findTopTen(){
        return collection.find().limit(10).sort(new BasicDBObject("click",-1)).toArray();

    }
    public Statistiche prendiStatistiche(){
        try {
            return new Statistiche(findTopTen(),(BasicDBObject)collectionStatistiche.findOne());

        }catch (NullPointerException e){
            Statistiche statistiche=new Statistiche();
            collectionStatistiche.insert(statistiche.getBasicDBObjectClass());
            return new Statistiche(findTopTen(),(BasicDBObject)collectionStatistiche.findOne());

        }
    }
    public void aggiornaStatistiche(Statistiche statistiche){
        collectionStatistiche.update(new BasicDBObject().append("_id",new ObjectId(statistiche.getId())),statistiche.getBasicDBObjectClass());
    }
}
