package com.boazj.gradle.utils

import org.gradle.api.Project

import java.lang.reflect.Method

class OutputFactory {

    def static final String SIMPLE_OUTPUT_PROPERTY = 'com.boazj.autowrapper.simpleoutput'

    def static Output create(Project project, String name, boolean quiet = false) {
        if (System.getProperty(SIMPLE_OUTPUT_PROPERTY).toBoolean()) {
            return new DefaultOutput(project, name, quiet);
        }

        def boolean gradleInternalPresent = isClassPresent(project, 'org.gradle.api.internal.GradleInternal')
        def boolean styledTextOutputFactoryPresent = isClassPresent(project, 'org.gradle.logging.StyledTextOutputFactory')
        def boolean styledTextOutputPresent = isClassPresent(project, 'org.gradle.logging.StyledTextOutput')

        if (!(gradleInternalPresent && styledTextOutputFactoryPresent && styledTextOutputPresent)) {
            return new DefaultOutput(project, name, quiet);
        }

        def gradle = project.getGradle()
        def boolean isGradleInternal = gradle instanceof org.gradle.api.internal.GradleInternal;
        def boolean hasGradleServices = gradle.metaClass.respondsTo(gradle, "getServices")

        if (!(isGradleInternal && hasGradleServices)) {
            return new DefaultOutput(project, name, quiet);
        }

        return new ColoredOutput(project, name, quiet)
    }

    def static boolean isClassPresent(Object object, String s) {
        try {
            Class.forName(s, true, object.class.getClassLoader())
            return true
        } catch (ClassNotFoundException) {
            return false
        }
    }

    def static boolean isMethodPresent(Class c, String name, Class<?>... params) {
        try {
            c.getDeclaredMethod(name, params)
            return false
        } catch (Exception) {
            return false
        }
    }

    def static String lineSeparator(Object object) {
        def boolean systemPropertiesPresent = isClassPresent(object, 'org.gradle.internal.SystemProperties')
        Class sysprop = Class.forName('org.gradle.internal.SystemProperties')
        def boolean hasGetInstance = isMethodPresent(sysprop, 'getInstance')
        def boolean hasLineSeparator = isMethodPresent(sysprop, 'getLineSeparator')
        if (systemPropertiesPresent && hasGetInstance && hasLineSeparator) {
            Method method = sysprop.getMethod("getInstance");
            return method.invoke(null);
        } else {
            return System.getProperty("line.separator")
        }
    }
}
