package com.boazj.gradle.autowrapper

import com.boazj.gradle.utils.Color
import com.boazj.gradle.utils.Output
import com.boazj.gradle.utils.OutputFactory
import com.boazj.gradle.utils.OutputListener
import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.util.GradleVersion

class AutowrapperExecutor extends Closure<Void> {

    def Output out
    def AutowrapperExtension ext
    def GradleVersion executionGradleVersion = GradleVersion.current()
    def Wrapper wrapperTask
    def TaskRunner runner
    def OutputListener listener

    AutowrapperExecutor(AutowrapperExtension ext, Wrapper wrapperTask, TaskRunner runner = null, OutputListener listener = null) {
        super(null)
        this.ext = ext
        this.wrapperTask = wrapperTask
        this.runner = runner
        if (runner == null) {
            this.runner = new DefaultTaskRunner();
        }
        this.listener = listener
    }

    void doCall(Project project) {
        out = OutputFactory.create(listener, project, "autowrapper-output", ext.quiet)
        out.say('Checking Gradle version ... ')
        def expectedGradleVersion = GradleVersion.version(ext.gradleVersion)
        if (expectedGradleVersion > executionGradleVersion || (expectedGradleVersion < executionGradleVersion && ext.strict)) {
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
        wrapperTask.setGradleVersion(ext.gradleVersion)
        wrapperTask.setArchiveBase(ext.archiveBase)
        wrapperTask.setArchivePath(ext.archivePath)
        wrapperTask.setDistributionBase(ext.distributionBase)
        wrapperTask.setDistributionPath(ext.distributionPath)
        if (ext.distributionLocator == null) {
            wrapperTask.setDistributionUrl(ext.distributionUrl)
        } else {
            wrapperTask.setDistributionUrl(ext.distributionLocator.getDistributionFor(GradleVersion.version(ext.gradleVersion)))
        }
        wrapperTask.setJarFile(ext.jarFile)
        wrapperTask.setScriptFile(ext.scriptFile)
        runner.run(wrapperTask)
    }

    void noMatch() {
        out.sayln("The build script requires Gradle ${ext.gradleVersion}. Currently executing ${executionGradleVersion}.", Color.Red)
        regenWrapper()
        if (ext.failFast) {
            fail()
        } else {
            out.sayln('This might effect the build process.', Color.Yellow)
        }
    }

    void fail() {
        def failMsg = 'Failing build'
        if (ext.autoGen) {
            failMsg += ', please execute this script again with the Gradle Wrapper'
        }
        out.sayln(failMsg, Color.Red)
        throw new StopExecutionException('Gradle version is not as required')
    }
}
