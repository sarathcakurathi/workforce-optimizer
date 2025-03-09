package com.xorg.wo.proc;

import com.xorg.wo.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigLoader {
    private static final Properties properties = new Properties();
    private static final Logger logger = Logger.getLogger( ConfigLoader.class.getName() );

    static {
        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(Constants.CONFIG_FILE)) {
            properties.load(inputStream);
        } catch (IOException | NullPointerException e) {
            logger.severe(String.format(Constants.ERROR_FILE_NOT_FOUND, Constants.CONFIG_FILE, e.getLocalizedMessage()));
            System.exit(Constants.EXIT_CODE_FILE_NOT_FOUND);
        }
    }

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
                logger.warning(String.format(Constants.ERROR_PROCESSING_EMPLOYEE_RECORD_INTEGER_DATA, key));
            }
        }
        return defaultValue;
    }

    public static double getDouble(String key, double defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                logger.warning(String.format(Constants.ERROR_PROCESSING_EMPLOYEE_RECORD_DOUBLE_DATA, key));
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