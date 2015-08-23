package com.shorterner;

import com.google.gson.Gson;
import com.shorterner.utility.URLShortener;
import com.shorterner.utility.UrlCustom;

/**
 * Created by Vincenzo on 22/08/2015.
 */
public class DAO {
    private UrlService urlService;

    public DAO(UrlService urlService) {
        this.urlService = urlService;
    }

    public String creaUrlShort(String body) {
        String url;
        try {
            url = urlService.findShortUrlByLongURL(body);
        } catch (NullPointerException e) {
            url = null;

        }


        if (url != null) {
            return url;
        } else {
            String URLSHORT = URLShortener.shortenURL(body);
            urlService.createNewURL(body, URLSHORT);


            return URLSHORT;
        }
    }

    public String espandiUrl(String body) {
        try {
            return urlService.findURLByShortUrl(body, false);
        } catch (NullPointerException e) {
            try {

                return urlService.findURLByShortUrl(body, true);
            } catch (NullPointerException ee) {
                return "nessuno";
            }
        }

    }

    public String generaUrlCustom(String body) {
        UrlCustom urlCustom = new Gson().fromJson(body, UrlCustom.class);
        urlCustom.setCustomURL("localhost/#/" + urlCustom.getCustomURL());
        try {
            String url = urlService.findUrlCustomByCustomurl(urlCustom.getCustomURL()).getLongURL();
            if (url.equalsIgnoreCase(urlCustom.getLongURL()))
                return urlCustom.getCustomURL();
        } catch (NullPointerException e) {
            urlService.createUrlCustom(urlCustom);
            this.creaUrlShort(urlCustom.getLongURL());
            return urlCustom.getCustomURL();
        }

        return "fallito";

    }
}
