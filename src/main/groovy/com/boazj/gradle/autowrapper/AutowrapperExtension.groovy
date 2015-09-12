package com.boazj.gradle.autowrapper

import org.gradle.api.Project
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.util.GradleVersion

class AutowrapperExtension {
    def boolean strict = false
    def boolean failFast = true
    def String gradleVersion = GradleVersion.current().getVersion()
    def boolean quiet = false
    def boolean autoGen = true

    def Wrapper.PathBase archiveBase
    def String archivePath
    def Wrapper.PathBase distributionBase
    def String distributionPath
    def String distributionUrl
    def File jarFile
    def File scriptFile

    AutowrapperExtension(Wrapper wrapper) {
        this.archiveBase = wrapper.archiveBase
        this.archivePath = wrapper.archivePath
        this.distributionBase = wrapper.distributionBase
        this.distributionPath = wrapper.distributionPath
        this.jarFile = wrapper.jarFile
        this.scriptFile = wrapper.scriptFile
    }

    def static AutowrapperExtension get(Project project) {
        return project.extensions.getByType(AutowrapperExtension)
    }

    def static boolean exists(Project project) {
        return project.extensions.findByType(AutowrapperExtension) != null
    }
}