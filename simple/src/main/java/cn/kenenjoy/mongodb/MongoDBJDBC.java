package cn.kenenjoy.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hefa on 2017/7/25.
 */
public class MongoDBJDBC {
    public static MongoDatabase db;

    public static void main(String[] args) {

        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase db = mongoClient.getDatabase("temp");
        System.out.println("Connect to " + db.getName() + " successfully");


    }

    public static void init(){
        ServerAddress serverAddress = new ServerAddress("localhost",27017);
        List<ServerAddress> addrs = new ArrayList<ServerAddress>();
        addrs.add(serverAddress);

        MongoCredential credential = MongoCredential.createScramSha1Credential("","","".toCharArray());
    }

    public static void createCollection(){

    }
}
