package com.xorg.wo.proc;

import com.xorg.wo.utils.GlobalConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(GlobalConstants.CONFIG_FILE)) {
            properties.load(inputStream);
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(GlobalConstants.ERROR_FILE_NOT_FOUND, e); // Todo: Proper exception handling
        }
    }

    // ToDo: Add a test case to check the scenario
    public static String getString(String key, String defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            return properties.getProperty(key);
        }
        return defaultValue;
    }

    public static int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(properties.getProperty(key));
            } catch (NumberFormatException e) {
                System.err.println("Error parsing integer for key: " + key + ". Using default.");
            }
        }
        return defaultValue;
    }

    public static double getDouble(String key, double defaultValue) {
        System.out.println("Coming here");
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing double for key: " + key + ". Using default.");
            }
        }
        return defaultValue;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }
}