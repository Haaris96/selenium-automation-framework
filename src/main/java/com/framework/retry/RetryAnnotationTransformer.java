package com.framework.retry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Automatically attaches RetryAnalyzer to every @Test method
 * without having to specify retryAnalyzer on each annotation.
 * Register in testng.xml: <listener class-name="com.framework.retry.RetryAnnotationTransformer"/>
 */
public class RetryAnnotationTransformer implements IAnnotationTransformer {

    private static final Logger log = LogManager.getLogger(RetryAnnotationTransformer.class);

    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {
        if (annotation.getRetryAnalyzerClass() == null) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
            log.debug("RetryAnalyzer applied to: {}",
                testMethod != null ? testMethod.getName() : "unknown");
        }
    }
}
