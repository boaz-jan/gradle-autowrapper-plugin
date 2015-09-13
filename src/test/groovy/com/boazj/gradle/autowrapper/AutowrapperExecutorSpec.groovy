package com.boazj.gradle.autowrapper

import com.boazj.gradle.utils.AccumulatingOutputListener
import com.boazj.gradle.utils.OutputListener
import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.util.GradleVersion
import spock.lang.Specification

class AutowrapperExecutorSpec extends Specification {
    def Project projectProto = ProjectBuilder.builder().build()
    def Wrapper proto = projectProto.tasks.create('proto_wrapper', Wrapper.class)
    def GradleVersion executionVersion = GradleVersion.current()
    def String nextVersion = GradleVersion.current().getNextMajor().getVersion()

    def createDefaultWrapperMock() {
        def Wrapper wrapperTask = Mock()
        wrapperTask.archiveBase >> proto.archiveBase
        wrapperTask.archivePath >> proto.archivePath
        wrapperTask.distributionBase >> proto.distributionBase
        wrapperTask.distributionPath >> proto.distributionPath
        wrapperTask.distributionUrl >> proto.distributionUrl
        wrapperTask.jarFile >> proto.jarFile
        wrapperTask.scriptFile >> proto.scriptFile
        return wrapperTask
    }

    def 'test newer version in non strict mode'() {
        given:
            def OutputListener listener = new AccumulatingOutputListener()
            def Wrapper wrapperMock = createDefaultWrapperMock()
            def AutowrapperExtension ext = new AutowrapperExtension(wrapperMock)
            ext.gradleVersion = '1.0'
            def AutowrapperExecutor exec = new AutowrapperExecutor(ext, wrapperMock, null, listener)
        when:
            exec.call(Mock(Project))
        then:
            listener.contains('Checking Gradle version ... ')
            listener.contains("${executionVersion} √")
        then:
            noExceptionThrown()
    }

    def 'test newer version in strict mode'() {
        given:
            def OutputListener listener = new AccumulatingOutputListener()
            def TaskRunner runner = Mock()
            def Wrapper wrapperMock = createDefaultWrapperMock()
            def AutowrapperExtension ext = new AutowrapperExtension(wrapperMock)
            ext.gradleVersion = '1.0'
            ext.strict = true
            def AutowrapperExecutor exec = new AutowrapperExecutor(ext, wrapperMock, runner, listener)
        when:
            exec.call(Mock(Project))
        then:
            listener.contains('Checking Gradle version ... ')
            listener.contains("The build script requires Gradle ${ext.gradleVersion}. Currently executing ${executionVersion}.")
            listener.contains("Generating Gradle Wrapper for version ${ext.gradleVersion}.")
        then:
            1 * runner.run(wrapperMock)
        then:
            listener.contains('Failing build, please execute this script again with the Gradle Wrapper')
        then:
            def e = thrown(StopExecutionException.class)
            e.message == 'Gradle version is not as required'
    }

    def 'test older version'() {
        given:
            def OutputListener listener = new AccumulatingOutputListener()
            def TaskRunner runner = Mock()
            def Wrapper wrapperMock = createDefaultWrapperMock()
            def AutowrapperExtension ext = new AutowrapperExtension(wrapperMock)
            ext.gradleVersion = nextVersion
            def AutowrapperExecutor exec = new AutowrapperExecutor(ext, wrapperMock, runner, listener)
        when:
            exec.call(Mock(Project))
        then:
            listener.contains('Checking Gradle version ... ')
            listener.contains("The build script requires Gradle ${ext.gradleVersion}. Currently executing ${executionVersion}.")
            listener.contains("Generating Gradle Wrapper for version ${ext.gradleVersion}.")
        then:
            1 * runner.run(wrapperMock)
        then:
            listener.contains('Failing build, please execute this script again with the Gradle Wrapper')
        then:
            def e = thrown(StopExecutionException.class)
            e.message == 'Gradle version is not as required'
    }

    def 'test older version with a custom distribution locator'() {
        given:
            def DistributionLocator locator = { 'http://fake.com' } as DistributionLocator
            def OutputListener listener = new AccumulatingOutputListener()
            def TaskRunner runner = Mock()
            def Wrapper wrapperMock = createDefaultWrapperMock()
            def AutowrapperExtension ext = new AutowrapperExtension(wrapperMock)
            ext.gradleVersion = nextVersion
            ext.distributionLocator = locator
            def AutowrapperExecutor exec = new AutowrapperExecutor(ext, wrapperMock, runner, listener)
        when:
            exec.call(Mock(Project))
        then:
            listener.contains('Checking Gradle version ... ')
            listener.contains("The build script requires Gradle ${ext.gradleVersion}. Currently executing ${executionVersion}.")
            listener.contains("Generating Gradle Wrapper for version ${ext.gradleVersion}.")
        then:
            1 * wrapperMock.setDistributionUrl('http://fake.com')
        then:
            1 * runner.run(wrapperMock)
        then:
            listener.contains('Failing build, please execute this script again with the Gradle Wrapper')
        then:
            def e = thrown(StopExecutionException.class)
            e.message == 'Gradle version is not as required'
    }


    def 'test older version without auto generation'() {
        given:
            def OutputListener listener = new AccumulatingOutputListener()
            def Wrapper wrapperMock = createDefaultWrapperMock()
            def AutowrapperExtension ext = new AutowrapperExtension(wrapperMock)
            ext.gradleVersion = nextVersion
            ext.autoGen = false
            def AutowrapperExecutor exec = new AutowrapperExecutor(ext, wrapperMock, null, listener)
        when:
            exec.call(Mock(Project))
        then:
            listener.contains('Checking Gradle version ... ')
            listener.contains("The build script requires Gradle ${ext.gradleVersion}. Currently executing ${executionVersion}.")
            listener.contains('Failing build')
        then:
            def e = thrown(StopExecutionException.class)
            e.message == 'Gradle version is not as required'
    }

    def 'test older version without fail fast'() {
        given:
            def OutputListener listener = new AccumulatingOutputListener()
            def TaskRunner runner = Mock()
            def Wrapper wrapperMock = createDefaultWrapperMock()
            def AutowrapperExtension ext = new AutowrapperExtension(wrapperMock)
            ext.gradleVersion = nextVersion
            ext.failFast = false
            def AutowrapperExecutor exec = new AutowrapperExecutor(ext, wrapperMock, runner, listener)
        when:
            exec.call(Mock(Project))
        then:
            listener.contains('Checking Gradle version ... ')
            listener.contains("The build script requires Gradle ${ext.gradleVersion}. Currently executing ${executionVersion}.")
            listener.contains("Generating Gradle Wrapper for version ${ext.gradleVersion}.")
        then:
            1 * runner.run(wrapperMock)
        then:
            listener.contains('This might effect the build process.')
            noExceptionThrown()
    }
}
