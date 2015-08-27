package com.shorterner;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import static spark.Spark.*;

/**
 * Created by Vincenzo on 16/07/2015.
 */
public class Bootstrap {
    private static final String IP="localhost";
    private static final int PORT=80;
    public static void main(String[] args){
        setIpAddress(IP);
        setPort(PORT);
        staticFileLocation("/public");
        try {
            new UrlResource(new UrlService(mongo()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static DB mongo() throws Exception {
        MongoClient mongoClient = new MongoClient("localhost");
        return mongoClient.getDB("shortlink");

    }
}
