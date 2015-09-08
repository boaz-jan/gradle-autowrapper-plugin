package com.boazj.gradle.autowrapper

import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.util.GradleVersion

import static com.boazj.gradle.autowrapper.Output.Color

class AutowrapperExecutor extends Closure<Void> {

    def Output out
    def AutowrapperExtension ext
    def GradleVersion executionGradleVersion = GradleVersion.current()
    def Wrapper wrapperTask

    AutowrapperExecutor(AutowrapperExtension ext, Wrapper wrapperTask) {
        super(null)
        this.ext = ext
        this.wrapperTask = wrapperTask
    }

    void doCall(Project project) {
        out = new Output(project, "autowrapper-output", ext.quiet)
        out.say('Checking Gradle version ... ')
        def expectedGradleVersion = GradleVersion.version(ext.gradleVersion)
        if (expectedGradleVersion > executionGradleVersion|| (expectedGradleVersion < executionGradleVersion && ext.strict)) {
            noMatch()
        } else {
            out.sayln("${executionGradleVersion} √", Color.Green)
        }
    }

    void regenWrapper() {
        if (!ext.autoGen) {
            return
        }
        out.sayln("Generating Gradle Wrapper for version ${ext.gradleVersion}.", Color.Yellow)
        wrapperTask.gradleVersion = ext.gradleVersion
        if (ext.archiveBase != null) {
            wrapperTask.setArchiveBase(ext.archiveBase)
        }
        if (ext.archivePath != null) {
            wrapperTask.setArchivePath(ext.archivePath)
        }
        if (ext.distributionBase != null) {
            wrapperTask.setDistributionBase(ext.distributionBase)
        }
        if (ext.distributionPath != null) {
            wrapperTask.setDistributionPath(ext.distributionPath)
        }
        if (ext.distributionUrl != null) {
            wrapperTask.setDistributionUrl(ext.distributionUrl)
        }
        if (ext.jarFile != null) {
            wrapperTask.setJarFile(ext.jarFile)
        }
        if (ext.scriptFile != null) {
            wrapperTask.setScriptFile(ext.scriptFile)
        }
        wrapperTask.execute()
    }

    void noMatch() {
        out.sayln("The build script requires Gradle ${ext.gradleVersion}. Currently executing ${executionGradleVersion}.", Color.Red)
        regenWrapper()
        if (ext.failFast) {
            fail()
        } else {
            out.sayln("This might effect the build process.", Color.Yellow)
        }
    }

    void fail() {
        def failMsg = 'Failing build'
        if (ext.autoGen) {
            failMsg  += ', please execute this script again with the Gradle Wrapper'
        }
        out.say(failMsg, Color.Red)
        out.emptyln()
        throw new StopExecutionException('Gradle version is not as required')
    }
}
