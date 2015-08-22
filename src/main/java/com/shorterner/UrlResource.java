package com.shorterner;

import com.shorterner.utility.JsonTransformer;
import com.shorterner.utility.URLShortener;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.setIpAddress;

/**
 * Created by Vincenzo on 16/07/2015.
 */
public class UrlResource {
    private static final String API_CONTEXT = "/api/v1";
    private UrlService urlService;
    public UrlResource(UrlService urlService){
        this.urlService=urlService;
        setupEndpoints();
    }

    private void setupEndpoints() {

        post(API_CONTEXT + "/shortCustom", "application/json", (request, response) -> {
            response.status(201);
           return "localhost/" + request.body();
        },new JsonTransformer());

        post(API_CONTEXT + "/short", "application/json", (request, response) -> {
            String url;
             try{
             url=urlService.findLongURL(request.body()).getShortURL();}
             catch (NullPointerException e){
                 url=null;

             }



           if(url!=null){
               response.status(201);

               return url;}
            else{
         String  URLSHORT=URLShortener.shortenURL(request.body());
            urlService.createNewURL(request.body(),URLSHORT);
               response.status(201);

               return URLSHORT;
           }
        },new JsonTransformer());



        post(API_CONTEXT+"/risultato","application/json",(request, response) ->
        {
            response.status(201);
                return urlService.findURL(request.body()).getLongURL();}, new JsonTransformer());
    }

    }
