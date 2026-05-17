package com.framework.retry;

import com.framework.constants.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Automatically retries failed tests up to MAX_RETRY_COUNT times.
 * Wire this in via @Test(retryAnalyzer = RetryAnalyzer.class) or the
 * RetryAnnotationTransformer (registered in testng.xml listener).
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LogManager.getLogger(RetryAnalyzer.class);

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < Constants.MAX_RETRY_COUNT) {
            retryCount++;
            log.warn("Retrying failed test '{}' — attempt {}/{}",
                result.getName(), retryCount, Constants.MAX_RETRY_COUNT);
            return true;
        }
        return false;
    }
}
