package com.shorterner.utility;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Vincenzo on 25/08/2015.
 */
public class IPGeo {

    public static String getCountry(String ip){
        try {
             if(ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1"))
                 return "Italy";
            String path=System.getProperty("user.dir") +"/geodb/GeoLite2-City.mmdb";
            File database = new File(path);

            DatabaseReader reader = new DatabaseReader.Builder(database).build();

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = reader.city(ipAddress);
           return response.getCountry().getName();


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
return "";
    }

}
