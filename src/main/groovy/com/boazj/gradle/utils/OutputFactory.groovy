package com.boazj.gradle.utils

import org.gradle.api.Project

class OutputFactory {

    def static final String SIMPLE_OUTPUT_PROPERTY = 'com.boazj.autowrapper.simpleoutput'

    def static Output create(Project project, String name, boolean quiet = false) {
        return create(null, project, name, quiet)
    }

    def static Output create(OutputListener listener, Project project, String name, boolean quiet = false) {
        if (System.hasProperty(OutputFactory.SIMPLE_OUTPUT_PROPERTY) &&
                System.getProperty(OutputFactory.SIMPLE_OUTPUT_PROPERTY)?.toBoolean()) {
            return new DefaultOutput(quiet, listener);
        }

        def boolean gradleInternalPresent = isClassPresent(project, 'org.gradle.api.internal.GradleInternal')
        def boolean styledTextOutputFactoryPresent = isClassPresent(project, 'org.gradle.logging.StyledTextOutputFactory')
        def boolean styledTextOutputPresent = isClassPresent(project, 'org.gradle.logging.StyledTextOutput')

        if (!(gradleInternalPresent && styledTextOutputFactoryPresent && styledTextOutputPresent)) {
            return new DefaultOutput(quiet, listener);
        }

        def gradle = project.getGradle()
        def boolean isGradleInternal = gradle instanceof org.gradle.api.internal.GradleInternal;
        def boolean hasGradleServices = gradle == null ? false : gradle.metaClass.respondsTo(gradle, "getServices")

        if (!(isGradleInternal && hasGradleServices)) {
            return new DefaultOutput(quiet, listener);
        }

        return new ColoredOutput(gradle, name, quiet, listener)
    }

    def static boolean isClassPresent(Object object, String s) {
        try {
            Class.forName(s, true, object.class.getClassLoader())
            return true
        } catch (ClassNotFoundException) {
            return false
        }
    }

    def static String lineSeparator() {
        return System.getProperty("line.separator")
    }
}
