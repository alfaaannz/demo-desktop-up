package org.example.demodesktop.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class SessionManager {

    private static final Logger log = Logger.getLogger(SessionManager.class.getName());
    private static final String SESSION_FILE = "your-path";
    private static final Properties sessionProperties = new Properties();

    static {
        try {
            File file = new File(SESSION_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                sessionProperties.load(fis);
                fis.close();
            }
        } catch (IOException e) {
            log.warning("Warning: "+ e.getMessage());
        }
    }

    public static boolean isLoggedIn() {
        return sessionProperties.getProperty("isLoggedIn", "false").equals("true");
    }

    public static void setLoggedIn(boolean loggedIn) {
        sessionProperties.setProperty("isLoggedIn", loggedIn ? "true" : "false");
        saveSession();
    }

    public static void clearSession() {
        sessionProperties.clear();
        saveSession();
    }

    private static void saveSession() {
        try {
            FileOutputStream fos = new FileOutputStream(SESSION_FILE);
            sessionProperties.store(fos, null);
            fos.close();
        } catch (IOException e) {
            log.warning("Warning: "+ e.getMessage());
        }
    }

    public static void deleteSessionFile() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}