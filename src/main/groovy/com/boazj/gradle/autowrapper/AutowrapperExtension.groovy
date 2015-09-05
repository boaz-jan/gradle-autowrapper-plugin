package com.boazj.gradle.autowrapper

import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.util.GradleVersion

class AutowrapperExtension {
    def boolean strict = false
    def boolean failFast = true
    def String gradleVersion = GradleVersion.current().getVersion()
    def boolean quiet = false
    def boolean autoGen = true

    //wrapper props
    def Wrapper.PathBase archiveBase
    def String archivePath
    def Wrapper.PathBase distributionBase
    def String distributionPath
    def String distributionUrl
    def File jarFile
    def File scriptFile

    //internal state
    def Wrapper wrapperTask
}