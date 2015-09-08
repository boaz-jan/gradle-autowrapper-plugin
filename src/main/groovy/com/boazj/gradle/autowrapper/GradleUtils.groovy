package com.boazj.gradle.autowrapper

import org.gradle.api.Project
import org.gradle.util.GradleVersion

class GradleUtils {

    static boolean isRootProject(Project project) {
        return project.rootProject == project
    }

    static boolean isExtensionPresent(Project project, String extensionName) {
        return project.extensions.findByName(extensionName) != null
    }
}
