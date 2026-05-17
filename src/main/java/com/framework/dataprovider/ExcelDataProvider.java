package com.framework.dataprovider;

import com.framework.constants.Constants;
import com.framework.utils.ExcelUtils;
import org.testng.annotations.DataProvider;

import java.util.Map;

/**
 * Central TestNG DataProvider using Excel sheets.
 * Tests reference methods here via @Test(dataProviderClass = ExcelDataProvider.class, dataProvider = "...")
 */
public class ExcelDataProvider {

    @DataProvider(name = "loginData", parallel = false)
    public static Object[][] loginData() {
        return ExcelUtils.getDataProviderArray(Constants.SHEET_LOGIN);
    }

    @DataProvider(name = "loginDataParallel", parallel = true)
    public static Object[][] loginDataParallel() {
        return ExcelUtils.getDataProviderArray(Constants.SHEET_LOGIN);
    }

    @DataProvider(name = "productData", parallel = false)
    public static Object[][] productData() {
        return ExcelUtils.getDataProviderArray(Constants.SHEET_PRODUCTS);
    }

    /**
     * Generic provider: specify sheet name as the test's first parameter.
     * Not used directly — use named providers above for clarity.
     */
    public static Object[][] fromSheet(String sheetName) {
        return ExcelUtils.getDataProviderArray(sheetName);
    }
}
