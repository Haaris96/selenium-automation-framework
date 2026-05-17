package com.framework.config;

import com.framework.constants.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Thread-safe singleton ConfigReader.
 * Reads config/config.properties and resolves environment-specific URLs.
 */
public final class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private final Properties properties;

    private ConfigReader() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(Constants.CONFIG_FILE_PATH)) {
            properties.load(fis);
            log.info("Configuration loaded from: {}", Constants.CONFIG_FILE_PATH);
        } catch (IOException e) {
            log.error("Failed to load config file: {}", Constants.CONFIG_FILE_PATH, e);
            throw new RuntimeException("Unable to load configuration file", e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String get(String key) {
        String value = System.getProperty(key);
        if (value != null && !value.isBlank()) return value.trim();
        value = properties.getProperty(key);
        if (value == null) {
            log.warn("Property '{}' not found in config", key);
            return "";
        }
        return value.trim();
    }

    public String getBaseUrl() {
        String env = getActiveEnv();
        return get(env + ".url");
    }

    public String getActiveEnv() {
        return get("env").isEmpty() ? "qa" : get("env");
    }

    public String getBrowser() {
        return get("browser").isEmpty() ? "chrome" : get("browser");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public int getImplicitWait() {
        try { return Integer.parseInt(get("implicitly.wait")); }
        catch (NumberFormatException e) { return Constants.IMPLICIT_WAIT; }
    }

    public int getExplicitWait() {
        try { return Integer.parseInt(get("explicit.wait")); }
        catch (NumberFormatException e) { return Constants.EXPLICIT_WAIT; }
    }

    public int getPageLoadTimeout() {
        try { return Integer.parseInt(get("page.load.timeout")); }
        catch (NumberFormatException e) { return Constants.PAGE_LOAD_TIMEOUT; }
    }

    public String getDbUrl()       { return get("db.url"); }
    public String getDbUsername()  { return get("db.username"); }
    public String getDbPassword()  { return get("db.password"); }
    public String getDbDriver()    { return get("db.driver"); }

    public String getUsername()    { return get("username"); }
    public String getPassword()    { return get("password"); }

    public String getExcelPath()   { return Constants.TESTDATA_PATH + "testdata.xlsx"; }
}
