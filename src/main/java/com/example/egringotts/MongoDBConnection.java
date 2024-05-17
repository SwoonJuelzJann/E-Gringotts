package com.example.egringotts;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.Closeable;

public class MongoDBConnection implements AutoCloseable, Closeable {
    private static final String connectionString = "mongodb+srv://ProjekDS:ProjekDSsem2@projekds.ic5ixyj.mongodb.net/?retryWrites=true&w=majority&appName=ProjekDS";
    private static MongoClient client;
    private static MongoDatabase database;
    public static MongoCollection accountsCollection,transactionsCollection;

    public static void connection(){
        MongoClientSettings settings = MongoClientSettings.builder().applyToSslSettings(builder -> builder.enabled(false)).build();
        client = MongoClients.create(connectionString);
        database = client.getDatabase("E-Gringotts");
        accountsCollection = database.getCollection("Accounts");
        transactionsCollection = database.getCollection("Transactions");
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }

    public Document findByUsername(MongoCollection collectionName, String username){
        return (Document) collectionName.find(new Document("username",username)).first();
    }
}
