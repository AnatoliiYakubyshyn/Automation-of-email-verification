package com.solvd;

import jakarta.mail.*;
import jakarta.mail.search.SearchTerm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CheckMailService {
    public static int cntOfTestMailsInbox(final String topic) throws MessagingException {

        Properties properties = new Properties();
        String host = "imap.ukr.net";
        String username = "";
        String appPassword = "";
        try {
            properties.load(new FileInputStream("src/main/resources/data.properties"));
            username = properties.getProperty("email");
            appPassword = properties.getProperty("pass");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Gmail configuration


        // Set mail properties
        properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", host);
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");
        Store store = null;
        Folder inbox = null;
        try {
            Session session = Session.getDefaultInstance(properties, null);

            store = session.getStore("imaps");
            store.connect(username, appPassword);

            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            SearchTerm searchTerm = new SearchTerm() {
                @Override
                public boolean match(Message message) {
                    try {
                        if (message.getSubject().contains(topic)) {
                            return true;
                        }
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            };

            Message[] messages = inbox.search(searchTerm);

            // Close the folder and store

            return messages.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            inbox.close(false);
            store.close();
        }
        return 0;
    }
}
