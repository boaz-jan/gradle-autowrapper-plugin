package com.boazj.gradle.autowrapper

import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.util.GradleVersion

class AutowrapperExtension {
    // false means a newer version is cool, true means exact version is enforce
    def boolean strict = false
    // false means lets the build continue in case incorrect version, true means fail the build
    def boolean failFast = true
    // the expected version of the gradle binary
    def String gradleVersion = GradleVersion.current().getVersion()
    // shut up about it
    def boolean quiet = false

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