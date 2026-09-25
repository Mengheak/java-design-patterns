package com.pisethjava.factory.lesson06_reflection;

import com.pisethjava.factory.common.Report;

public class ReflectionReportFactory {
    public Report create(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object object = clazz.getDeclaredConstructor().newInstance();
            return (Report) object;
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create report from class: " + className, e);
        }
    }
}
