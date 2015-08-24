package com.shorterner;

import com.mongodb.BasicDBObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vincenzo on 20/08/2015.
 */
public class URL {
private String longURL;
    private String shortURL;
    private ArrayList<String> customURL;
    private int click;
    private HashMap<String,Integer> statistichePaesi;
    private HashMap<String,Integer> statisticheBrowser;
    private HashMap<String,Integer> statisticheOS;



    public URL(BasicDBObject dbObject) {
        this.longURL =  dbObject.getString("_id");
        this.shortURL = dbObject.getString("shortURL");
        this.customURL= (ArrayList<String>) dbObject.get("customURL");
        this.click=dbObject.getInt("click");
        this.statistichePaesi = ((HashMap<String, HashMap<String,Integer>>) dbObject.get("statistiche")).get("paesi");
       this.statisticheBrowser = ((HashMap<String, HashMap<String,Integer>>) dbObject.get("statistiche")).get("browser");
       this.statisticheOS = ((HashMap<String, HashMap<String,Integer>>) dbObject.get("statistiche")).get("os");

    }

    public URL(String longURL, String shortURL) {
        this.longURL = longURL;
        this.shortURL = shortURL;
        this.customURL=new ArrayList<>();
        this.customURL.add((shortURL));
        this.click=0;
        this.statistichePaesi =new HashMap<>();
        this.statisticheOS =new HashMap<>();
        this.statisticheBrowser =new HashMap<>();

    }

    public int getClick() {
        return click;
    }
public void addClick(){
    this.click++;
}
    public void addClickCountry(String country){
        this.statistichePaesi.put(country,this.statistichePaesi.getOrDefault(country,0)+1);
    }
    public void addClickBrowser(String browser){

        this.statisticheBrowser.put(browser,this.statisticheBrowser.getOrDefault(browser,0)+1);
    }
    public void addClickOS(String os){
        this.statisticheOS.put(os,this.statisticheOS.getOrDefault(os,0)+1);
    }
    public HashMap<String, Integer> getStatistichePaesi() {
        return statistichePaesi;
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
        BasicDBObject statistiche=new BasicDBObject("paesi", this.statistichePaesi)
                .append("browser",this.statisticheBrowser)
                .append("os",this.statisticheOS);

        BasicDBObject document=new BasicDBObject("_id", this.longURL)
                .append("shortURL", this.shortURL)
                .append("customURL", this.customURL)
                .append("click", this.click)
                .append("statistiche", statistiche);



        return document;
    }
}
