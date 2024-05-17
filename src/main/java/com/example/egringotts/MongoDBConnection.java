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

    //~~~~~ALL METHODS TO RETRIEVE OBJECTS FROM MONGO DATABASE~~~~~
    //~ACCOUNTS COLLECTION

    public Object findUsername(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return foundDoc.get("username");
    }

    public Object findFirstName(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return foundDoc.get("firstName");
    }

    public Object findLastName(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return foundDoc.get("lastName");
    }

    public Object findPhoneNo(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return foundDoc.get("phoneNo");
    }

    public Object findUserAddress(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return foundDoc.get("userAddress");
    }

    public Object findPostcode(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return foundDoc.get("postcode");
    }

    public Object findUserType(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return foundDoc.get("userType");
    }

    public Object findBalance_K(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return foundDoc.get("balance_K");
    }

    public Object findBalance_S(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return foundDoc.get("balance_S");
    }

    public Object findBalance_G(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return foundDoc.get("balance_G");
    }

    public Object findGoblinStatus(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return foundDoc.get("goblinStatus");
    }

    public Object findAvatar(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return foundDoc.get("avatar");
    }

    //~TRANSACTIONS COLLECTION

    public Object findTransactionID(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return foundDoc.get("_id");
    }

    public Object findSenderUsername(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return foundDoc.get("username");
    }

    public Object findReceiverUsername(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return foundDoc.get("receiverUsername");
    }

    public Object findAmount(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return foundDoc.get("amount");
    }

    public Object findCurrency(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return foundDoc.get("currency");
    }

    public Object findBalanceAfter(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return foundDoc.get("balanceAfter");
    }

    public Object findDateTransaction(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return foundDoc.get("date");
    }

    public Object findCategory(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return foundDoc.get("category");
    }
}
