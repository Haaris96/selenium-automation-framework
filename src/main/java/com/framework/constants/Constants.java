package com.framework.constants;

public final class Constants {

    private Constants() {}

    // Timeouts (seconds)
    public static final int IMPLICIT_WAIT        = 10;
    public static final int EXPLICIT_WAIT        = 20;
    public static final int PAGE_LOAD_TIMEOUT    = 30;
    public static final int SCRIPT_TIMEOUT       = 15;
    public static final int FLUENT_WAIT_TIMEOUT  = 30;
    public static final int FLUENT_WAIT_POLLING  = 500;

    // Paths
    public static final String CONFIG_FILE_PATH      = System.getProperty("user.dir") + "/config/config.properties";
    public static final String REPORTS_PATH          = System.getProperty("user.dir") + "/reports/";
    public static final String SCREENSHOTS_PATH      = System.getProperty("user.dir") + "/screenshots/";
    public static final String LOGS_PATH             = System.getProperty("user.dir") + "/logs/";
    public static final String TESTDATA_PATH         = System.getProperty("user.dir") + "/testdata/";
    public static final String EXTENT_REPORT_PATH    = REPORTS_PATH + "ExtentReport.html";

    // Report
    public static final String REPORT_NAME           = "Test Execution Report";
    public static final String REPORT_TITLE          = "Selenium Automation Framework";
    public static final String SYSTEM_INFO_OS        = System.getProperty("os.name");
    public static final String SYSTEM_INFO_JAVA      = System.getProperty("java.version");

    // Date/Time formats
    public static final String DATE_FORMAT           = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT       = "yyyy-MM-dd_HH-mm-ss";
    public static final String SCREENSHOT_DATE_FORMAT = "yyyyMMdd_HHmmss";

    // Excel sheet names
    public static final String SHEET_LOGIN           = "LoginData";
    public static final String SHEET_PRODUCTS        = "ProductData";

    // Retry
    public static final int MAX_RETRY_COUNT          = 1;

    // Parallel threads
    public static final int THREAD_COUNT             = 4;
}
