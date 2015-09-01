package com.shorterner.data_access;


import com.google.gson.Gson;
import com.shorterner.UrlService;
import com.shorterner.entity.Statistiche;
import com.shorterner.entity.URL;
import com.shorterner.utility.IPGeo;
import com.shorterner.utility.URLShortener;
import com.shorterner.entity.UrlCustom;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import spark.Request;

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
        if (body.substring(0, 8).equals("https://")) {
            body = body.replace("https://", "http://");
        } else if (!body.substring(0, 7).equals("http://")) {
            body = "http://" + body;
        }
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
            String longURl=urlService.findURLByShortUrl(request.body());
           UrlCustom url = new UrlCustom(longURl,request.body(),urlService.prendiStatisticheShortURL(request.body()));
            UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
            ReadableUserAgent agent = parser.parse(request.userAgent());
           aggiornaUrl(agent, request.ip(), url);
            aggiornaStatistiche(agent, request.ip());
            urlService.aumentaClick(url.getLongURL());
            return url.getLongURL();
        } catch (NullPointerException e) {
            return "/#/404_Rendering_Error_Page_Not_Found";
        }


    }

    public URL generaUrlCustom(String body) {
        if (body.substring(12, 20).equals("https://")) {
            body = body.replace("https://", "http://");
        } else if (!body.substring(12, 19).equals("http://")) {
            body = body.substring(0, 12) + "http://" + body.substring(12);
        }
        UrlCustom urlCustom = new Gson().fromJson(body, UrlCustom.class);
        urlCustom.setCustomURL("http://localhost:8080/#/" + urlCustom.getCustomURL());
        URL url = null;
        if (isAvailable(urlCustom) == INDISPONIBILE)
            return null;
        if (isAvailable(urlCustom) == DISPONIBILE_USO) {
            url = urlService.findUrlByLongURL(urlCustom.getLongURL());
            return url;
        }
        try {
            url = urlService.findUrlByLongURL(urlCustom.getLongURL());
            urlService.aggiornaListaCustom(urlCustom.getLongURL(),urlCustom.getCustomURL());
        } catch (NullPointerException e) {
            url = new URL(urlCustom.getLongURL(), URLShortener.shortenURL(urlCustom.getLongURL()));
            urlService.createNewURL(url.getLongURL(), url.getShortURL());
            urlService.aggiornaListaCustom(urlCustom.getLongURL(), urlCustom.getCustomURL());
           aggiungiSitoStatistiche();
            return url;
        }
        return url;
    }

    public UrlCustom getUrlStatistics(String body) {
        String longURl=urlService.findURLByShortUrl(body);
        UrlCustom url = new UrlCustom(longURl,body,urlService.prendiStatisticheShortURL(body));
       return url;
    }

    public Statistiche getStatistics() {
        return urlService.prendiStatistiche();
    }

    private void aggiornaUrl(ReadableUserAgent agent, String ip, UrlCustom url) {
       url.getStatistiche().addClickOS(agent.getOperatingSystem().getFamilyName());
    url.getStatistiche().addClickBrowser(agent.getFamily().getName());
        url.getStatistiche().addNum();
        url.getStatistiche().addClickCountry(IPGeo.getCountry(ip));
        urlService.aggiornaStatisticheCustomURL(url);
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
        statistiche.addNum();
        urlService.aggiornaStatistiche(statistiche);

    }
}

