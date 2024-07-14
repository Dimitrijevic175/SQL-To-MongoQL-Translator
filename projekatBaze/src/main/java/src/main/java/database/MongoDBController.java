package src.main.java.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.util.Arrays;

public class MongoDBController {

    private static String user = "writer";
    private static String database = "bp_tim55";
    private static String password = "hQrzE0TIKpN5S008";

    public static MongoClient getConnection(){

        MongoCredential credential = MongoCredential.createCredential(user, database, password.toCharArray());
        MongoClient mongoClient = new MongoClient(new ServerAddress("134.209.239.154", 27017), Arrays.asList(credential));


        return mongoClient;

    }




}
