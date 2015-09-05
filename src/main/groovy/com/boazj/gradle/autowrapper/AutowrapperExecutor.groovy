package com.boazj.gradle.autowrapper

import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.util.GradleVersion

import static com.boazj.gradle.autowrapper.Output.Color

class AutowrapperExecutor extends Closure<Void> {

    def Output out
    def AutowrapperExtension ext
    def GradleVersion gradleVersion

    AutowrapperExecutor() {
        super(null)
    }

    void doCall(Project project) {
        ext = project.extensions."${AutowrapperPlugin.AUTOWRAPPER_EXTENSION_NAME}"
        out = new Output(project, "autowrapper-output")
        out.say('Checking Gradle version ... ')
        gradleVersion = GradleVersion.current()
        def versionCompare = GradleUtils.compare(GradleVersion.version(ext.version), gradleVersion)

        if (versionCompare == VersionCompare.Newer || (versionCompare == VersionCompare.Older && ext.strict)) {
            noMatch()
        } else {
            out.sayln("${gradleVersion} √", Color.Green)
        }
    }

    void regenWrapper() {
        out.sayln("Generating Gradle Wrapper for version ${ext.version}.", Color.Yellow)
        Wrapper wrapperTask = ext.wrapperTask
        wrapperTask.gradleVersion = ext.version
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
        out.sayln("The build script requieres Gradle ${ext.version}. Currently executing ${gradleVersion}.", Color.Red)
        regenWrapper()
        if (ext.failFast) {
            fail()
        } else {
            out.sayln("This might effect the build process.", Color.Red)
        }
    }

    void fail() {
        out.sayln("Failing build, please execute this script again with the Gradle Wrapper", Color.Red)
        out.emptyln()
        throw new StopExecutionException('Gradle version is not as required')
    }
}
