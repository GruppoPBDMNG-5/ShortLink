package com.shorterner;

import com.mongodb.*;
import com.shorterner.entity.Statistiche;
import com.shorterner.entity.URL;
import com.shorterner.entity.UrlCustom;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    }

    public String findURLByShortUrl(String shortURL) {
        return ((BasicDBObject) collection.findOne(new BasicDBObject("customURL.URL", shortURL))).getString("_id");
    }

    public URL findUrlByLongURL(String longURL) {
        return new URL((BasicDBObject) collection.findOne(new BasicDBObject("_id", longURL)));
    }
     public void aumentaClick(String url){
         BasicDBObject query=new BasicDBObject().append("_id",url);
         BasicDBObject newDocument =
                 new BasicDBObject().append("$inc",
                         new BasicDBObject().append("click", 1));
         collection.update(query,newDocument);

     }


    public URL findUrlByCustomUrl(String url) {
        return new URL((BasicDBObject) collection.findOne(new BasicDBObject("customURL.URL", url)));
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
public void aggiornaListaCustom(String longURL,String customURL){
    BasicDBObject query=new BasicDBObject().append("_id",longURL);


    BasicDBObject var = new BasicDBObject("URL",customURL).append("statistiche",new Statistiche().getBasicDBObjectClass());
    BasicDBObject updateDocument = new BasicDBObject();
    ArrayList<BasicDBObject>arrayList=new ArrayList<>();
    arrayList.addAll((Collection<? extends BasicDBObject>) collection.findOne(query).get("customURL"));
    arrayList.add(var);
    updateDocument.append("$set", new BasicDBObject("customURL",arrayList));
        collection.update(query, updateDocument);


}
    public  void aggiornaStatisticheCustomURL(UrlCustom urlCustom){
        BasicDBObject query=new BasicDBObject("_id",urlCustom.getLongURL());
        ArrayList<BasicDBObject>arrayList=new ArrayList<>();
        arrayList.addAll((Collection<? extends BasicDBObject>) collection.findOne(query).get("customURL"));
        for (int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).getString("URL").equals(urlCustom.getCustomURL()))
                arrayList.remove(i);
        }
        BasicDBObject var = new BasicDBObject("URL",urlCustom.getCustomURL()).append("statistiche",urlCustom.getStatistiche().getBasicDBObjectClass());
        arrayList.add(var);
        BasicDBObject updateDocument = new BasicDBObject();
        updateDocument.append("$set", new BasicDBObject("customURL",arrayList));
        collection.update(query,updateDocument);
    }
    public void aggiornaStatistiche(Statistiche statistiche) {

        collectionStatistiche.update(new BasicDBObject().append("_id", new ObjectId(statistiche.getId())), statistiche.getBasicDBObjectClass());
    }

    public BasicDBObject prendiStatisticheShortURL(String shortUrl){
        BasicDBList dbList= (BasicDBList) (collection.findOne(new BasicDBObject("customURL.URL", shortUrl))).get("customURL");
         for(int i=0;i<dbList.size();i++){
             BasicDBObject basicDBObject= (BasicDBObject) dbList.get(i);
             if(basicDBObject.getString("URL").equals(shortUrl))
                 return new BasicDBObject((Map) basicDBObject.get("statistiche"));
         }
        return new BasicDBObject();
    }
}
