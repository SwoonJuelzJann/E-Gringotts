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
    public static MongoCollection accountsCollection,transactionsCollection,userTypesCollection;

    public MongoDBConnection() {
        connection();
    }

    public static void connection(){
        MongoClientSettings settings = MongoClientSettings.builder().applyToSslSettings(builder -> builder.enabled(false)).build();
        client = MongoClients.create(connectionString);
        MongoDatabase database = client.getDatabase("E-Gringotts");
        accountsCollection = database.getCollection("Accounts");
        transactionsCollection = database.getCollection("Transactions");
        userTypesCollection = database.getCollection("UserTypes");
    }

    public void close() {
        if (client != null) {
            client.close();
        }
    }

    public Document findByUsername(MongoCollection collectionName, String username){
        return (Document) collectionName.find(new Document("username",username)).first();
    }

    public void addAccountDocument(account account){
        Document doc = new Document("username",account.getUsername());
        doc.append("password",account.getPassword());
        doc.append("firstName",account.getFirstName());
        doc.append("lastName",account.getLastName());
        doc.append("email",account.getEmail());
        doc.append("phoneNo",account.getPhoneNo());
        doc.append("userAddress",account.getUserAddress());
        doc.append("postcode",account.getPostcode());
        doc.append("userType",account.getUserType());
        doc.append("balance_K",account.getBalance_K());
        doc.append("balance_S",account.getBalance_S());
        doc.append("balance_G",account.getBalance_G());
        doc.append("goblinStatus",account.getGoblinStatus());
        doc.append("avatar",account.getAvatar());
        accountsCollection.insertOne(doc);
    }

    public void addTransactionDocument(transaction transaction){
        Document doc = new Document("username",transaction.getUsername());
        doc.append("receiverUsername",transaction.getReceiverUsername());
        doc.append("amount",transaction.getAmount());
        doc.append("currency",transaction.getCurrency());
        doc.append("date",transaction.getDate());
        doc.append("category",transaction.getCategory());
        transactionsCollection.insertOne(doc);
    }


    //~~~~~ALL METHODS TO RETRIEVE OBJECTS FROM MONGO DATABASE~~~~~
    //~ACCOUNTS COLLECTION

    public String findUsername(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (String) foundDoc.get("username");
    }

    public String findPassword(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (String) foundDoc.get("password");
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

    public double findBalance(String currency, String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (Double) foundDoc.get(currency);
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

    public String findEmail(String username){
        Document foundDoc = findByUsername(accountsCollection, username);
        return (String) foundDoc.get("email");
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

    public Object findDateTransaction(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return foundDoc.get("date");
    }

    public String findCategory(String username){
        Document foundDoc = findByUsername(transactionsCollection, username);
        return (String) foundDoc.get("category");
    }

    //~USERTYPES COLLECTION

    public Document findByUserType(MongoCollection collectionName, String userType){
        return (Document) collectionName.find(new Document("name",userType)).first();
    }

    public String findMaxTransferK(String userType){
        Document foundDoc = findByUserType(userTypesCollection, userType);
        return (String) foundDoc.get("maxTransferK");
    }

    public String findMaxTransferS(String userType){
        Document foundDoc = findByUserType(userTypesCollection, userType);
        return (String) foundDoc.get("maxTransferS");
    }

    public String findMaxTransferG(String userType){
        Document foundDoc = findByUserType(userTypesCollection, userType);
        return (String) foundDoc.get("maxTransferG");
    }
}
