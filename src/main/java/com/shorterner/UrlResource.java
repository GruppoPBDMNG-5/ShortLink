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
    private DAO dao;

    public UrlResource(UrlService urlService) {
        this.dao = new DAO(urlService);
        setupEndpoints();
    }

    private void setupEndpoints() {

        post(API_CONTEXT + "/shortCustom", "application/json", (request, response) -> dao.generaUrlCustom(request.body()), new JsonTransformer());

        post(API_CONTEXT + "/short", "application/json", (request, response) -> dao.creaUrlShort(request.body()), new JsonTransformer());

        post(API_CONTEXT + "/risultato", "application/json", (request, response) -> dao.espandiUrl(request), new JsonTransformer());


    }



}
