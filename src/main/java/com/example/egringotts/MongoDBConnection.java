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
    public static MongoCollection accountsCollection,transactionsCollection;

    public static void connection(){
        MongoClientSettings settings = MongoClientSettings.builder().applyToSslSettings(builder -> builder.enabled(false)).build();
        client = MongoClients.create(connectionString);
        MongoDatabase database = client.getDatabase("E-Gringotts");
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

    public String findUsername(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (String) foundDoc.get("username");
    }

    public String findFirstName(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (String) foundDoc.get("firstName");
    }

    public String findLastName(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (String) foundDoc.get("lastName");
    }

    public String findPhoneNo(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (String) foundDoc.get("phoneNo");
    }

    public String findUserAddress(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (String) foundDoc.get("userAddress");
    }

    public Integer findPostcode(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (Integer) foundDoc.get("postcode");
    }

    public String findUserType(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (String) foundDoc.get("userType");
    }

    public Double findBalance_K(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (Double) foundDoc.get("balance_K");
    }

    public Double findBalance_S(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (Double) foundDoc.get("balance_S");
    }

    public Double findBalance_G(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (Double) foundDoc.get("balance_G");
    }

    public Integer findGoblinStatus(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (Integer) foundDoc.get("goblinStatus");
    }

    public String findAvatar(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (String) foundDoc.get("avatar");
    }

    //~TRANSACTIONS COLLECTION

    public String findTransactionID(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return (String) foundDoc.get("_id");
    }

    public String findSenderUsername(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return (String) foundDoc.get("username");
    }

    public String findReceiverUsername(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return (String) foundDoc.get("receiverUsername");
    }

    public Double findAmount(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return (Double) foundDoc.get("amount");
    }

    public String findCurrency(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return (String) foundDoc.get("currency");
    }

    public Double findBalanceAfter(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return (Double) foundDoc.get("balanceAfter");
    }
        //---TODO implement date later on
//    public Object findDateTransaction(String username){
//        Document foundDoc = findByUsername(transactionsCollection, username);
//        return foundDoc.get("date");
//    }

    public String findCategory(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return (String) foundDoc.get("category");
    }
}
