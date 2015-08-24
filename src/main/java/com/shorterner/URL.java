package com.shorterner;

import com.mongodb.BasicDBObject;

import java.util.ArrayList;

/**
 * Created by Vincenzo on 20/08/2015.
 */
public class URL {
private String longURL;
    private String shortURL;
    private ArrayList<String> customURL;

    public URL(BasicDBObject dbObject) {
        this.longURL =  dbObject.getString("_id");
        this.shortURL = dbObject.getString("shortURL");
        this.customURL= (ArrayList<String>) dbObject.get("customURL");
    }

    public URL(String longURL, String shortURL) {
        this.longURL = longURL;
        this.shortURL = shortURL;
        this.customURL=new ArrayList<>();
        this.customURL.add((shortURL));

    }

    public ArrayList<String> getCustomURL() {
        return customURL;
    }

    public void setCustomURL(ArrayList<String> customURL) {
        this.customURL = customURL;
    }
    public void addCustomURL(String customURL){
        this.customURL.add(customURL);
    }
    public String getLongURL() {
        return longURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public  BasicDBObject getBasicDBObjectClass(){
        BasicDBObject document=new BasicDBObject("_id", this.longURL)
                .append("shortURL", this.shortURL)
                .append("customURL",this.customURL);
        return document;
    }
}
