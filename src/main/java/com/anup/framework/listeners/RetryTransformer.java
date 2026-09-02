package com.anup.framework.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/** Applies {@link RetryAnalyzer} to every @Test method without annotating each one by hand. */
public class RetryTransformer implements IAnnotationTransformer {

    @Override
    @SuppressWarnings({"rawtypes"})
    public void transform(ITestAnnotation annotation, Class testClass,
                           Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
