import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.shorterner.presentation.UrlResource;
import com.shorterner.presentation.UrlService;

import static spark.Spark.*;

public class Bootstrap {
    private static final String IP = "0.0.0.0";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        setIpAddress(IP);
        setPort(PORT);
        staticFileLocation("/public");
        try {
            UrlService s = new UrlService(mongo());
            new UrlResource(s);
//            s.droppadb();
//            s.popoladb();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static DB mongo() throws Exception {
        MongoClient mongoClient = new MongoClient("locahost");
        return mongoClient.getDB("shortlink");
    }

}
