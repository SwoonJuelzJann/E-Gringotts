package com.example.egringotts;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;

import static com.example.egringotts.main.activeUsername;
import static com.example.egringotts.main.mongo;
import static javax.mail.Message.RecipientType.TO;

public class GMailer {

    private static final String EMAIL = mongo.findEmail(activeUsername);
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/client_secret_147660039380-al4kqdlv268htgd2kki6a56b1h76asig.apps.googleusercontent.com (1).json";
    private static final java.util.Set<String> SCOPES = Collections.singleton(GmailScopes.GMAIL_SEND);
    private final Gmail service;

    public GMailer() throws GeneralSecurityException, IOException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        service = new Gmail.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName("E-Gringotts")
                .build();
    }

    private static Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
//        Path tokensPath = Paths.get(TOKENS_DIRECTORY_PATH);
//        if (Files.exists(tokensPath)) {
//            Files.walk(tokensPath)
//                    .map(Path::toFile)
//                    .forEach(file -> {
//                        if (!file.delete()) {
//                            System.err.println("Failed to delete " + file);
//                        }
//                    });
//        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(Objects.requireNonNull(GMailer.class.getResourceAsStream(CREDENTIALS_FILE_PATH))));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get(TOKENS_DIRECTORY_PATH).toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void sendMail(String subject, String message) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("egringott.goblin@gmail.com"));
        email.addRecipient(TO, new InternetAddress(EMAIL));
        email.setSubject(subject);
        email.setText(message);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
            msg = service.users().messages().send("me", msg).execute();
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            }
            throw e;
        }
    }

}
