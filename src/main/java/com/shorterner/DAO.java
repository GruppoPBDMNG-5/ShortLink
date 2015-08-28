package com.shorterner;


import com.google.gson.Gson;
import com.shorterner.utility.IPGeo;
import com.shorterner.utility.URLShortener;
import com.shorterner.utility.UrlCustom;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import spark.Request;

import java.io.IOException;

/**
 * Created by Vincenzo on 22/08/2015.
 */
public class DAO {
    private UrlService urlService;

    private final static int INDISPONIBILE = 0;
    private final static int DISPONIBILE_USO = 1;
    private final static int DISPONIBILE = 2;

    public DAO(UrlService urlService) {
        this.urlService = urlService;
    }

    public URL creaUrlShort(String body) {
        URL url;
        try {
            url = urlService.findUrlByLongURL(body);
        } catch (NullPointerException e) {
            url = null;

        }


        if (url != null) {
            return url;
        } else {
            String URLSHORT = URLShortener.shortenURL(body);
            urlService.createNewURL(body, URLSHORT);
            url = urlService.findUrlByLongURL(body);
            aggiungiSitoStatistiche();
            return url;
        }
    }

    public String espandiUrl(Request request) {
        try {
            URL url = urlService.findURLByShortUrl(request.body());
            UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
            ReadableUserAgent agent = parser.parse(request.userAgent());
            aggiornaUrl(agent, request.ip(), url);
            aggiornaStatistiche(agent, request.ip());
            return url.getLongURL();
        } catch (NullPointerException e) {
            return "nessuno";
        }


    }

    public URL generaUrlCustom(String body) {
        UrlCustom urlCustom = new Gson().fromJson(body, UrlCustom.class);
        urlCustom.setCustomURL("localhost:8080/#/" + urlCustom.getCustomURL());
        URL url = null;
        if (isAvailable(urlCustom) == INDISPONIBILE)
            return null;
        if (isAvailable(urlCustom) == DISPONIBILE_USO) {
            url = urlService.findUrlByLongURL(urlCustom.getLongURL());
            return url;
        }
        try {
            url = urlService.findUrlByLongURL(urlCustom.getLongURL());
            url.addCustomURL(urlCustom.getCustomURL());
        } catch (NullPointerException e) {
            url = new URL(urlCustom.getLongURL(), URLShortener.shortenURL(urlCustom.getLongURL()));
            urlService.createNewURL(url.getLongURL(), url.getShortURL());
            url.addCustomURL(urlCustom.getCustomURL());
        } finally {
            urlService.aggiornaUrl(url);
            aggiungiSitoStatistiche();
            return url;
        }
    }

    public URL getUrlStatistics(String body) {
        return urlService.findUrlByLongURL(body);
    }

    public Statistiche getStatistics() {
        return urlService.prendiStatistiche();
    }

    private void aggiornaUrl(ReadableUserAgent agent, String ip, URL url) {
        url.addClickOS(agent.getOperatingSystem().getFamilyName());
        url.addClickBrowser(agent.getFamily().getName());
        url.addClick();
        url.addClickCountry(IPGeo.getCountry(ip));
        urlService.aggiornaUrl(url);
    }

    private void aggiornaStatistiche(ReadableUserAgent agent, String ip) {
        Statistiche statistiche = urlService.prendiStatistiche();
        statistiche.addClickBrowser(agent.getFamily().getName());
        statistiche.addClickOS(agent.getOperatingSystem().getFamilyName());
        statistiche.addClickCountry(IPGeo.getCountry(ip));
        urlService.aggiornaStatistiche(statistiche);
    }

    private int isAvailable(UrlCustom customURL) {
        try {
            URL url = urlService.findUrlByCustomUrl(customURL.getCustomURL());
            if (url.getLongURL().equalsIgnoreCase(customURL.getLongURL()))
                return DISPONIBILE_USO;
            return INDISPONIBILE;
        } catch (NullPointerException e) {
            return DISPONIBILE;
        }
    }

    private void aggiungiSitoStatistiche() {
        Statistiche statistiche = urlService.prendiStatistiche();
        statistiche.addSite();
        urlService.aggiornaStatistiche(statistiche);

    }
}

