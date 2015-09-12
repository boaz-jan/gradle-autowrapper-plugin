package com.boazj.gradle.autowrapper

import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.util.GradleVersion
import spock.lang.Specification

class AutowrapperExecutorSpec extends Specification {
    def Project projectProto = ProjectBuilder.builder().build()
    def Wrapper proto = projectProto.tasks.create('proto_wrapper', Wrapper.class)
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
        _ * wrapperTask./set.*/(_)
        return wrapperTask
    }

    def 'test newer version in non strict mode'() {
        given:
            def Project p = Mock()
            def Wrapper wrapperMock = createDefaultWrapperMock()
            def AutowrapperExtension ext = new AutowrapperExtension(wrapperMock)
            ext.gradleVersion = '2.5'
            def AutowrapperExecutor exec = new AutowrapperExecutor(ext, wrapperMock)
        when:
            exec.call(p)
        then:
            noExceptionThrown()
    }

    def 'test newer version in strict mode'() {
        given:
            def Project p = Mock()
            p.getGradle() >> null
            def TaskRunner runner = Mock()
            def Wrapper wrapperMock = createDefaultWrapperMock()
            def AutowrapperExtension ext = new AutowrapperExtension(wrapperMock)
            ext.gradleVersion = '2.5'
            ext.strict = true
            def AutowrapperExecutor exec = new AutowrapperExecutor(ext, wrapperMock, runner)
        when:
            exec.call(p)
        then:
            1 * runner.run(wrapperMock)
        then:
            def e = thrown(StopExecutionException.class)
            e.message == 'Gradle version is not as required'
    }

    def 'test older version'() {
        given:
            def Project p = Mock()
            p.getGradle() >> null
            def TaskRunner runner = Mock()
            def Wrapper wrapperMock = createDefaultWrapperMock()
            def AutowrapperExtension ext = new AutowrapperExtension(wrapperMock)
            ext.gradleVersion = nextVersion
            def AutowrapperExecutor exec = new AutowrapperExecutor(ext, wrapperMock, runner)
        when:
            exec.call(p)
        then:
            1 * runner.run(wrapperMock)
        then:
            def e = thrown(StopExecutionException.class)
            e.message == 'Gradle version is not as required'
    }


    def 'test older version without auto generation'() {
        given:
            def Project p = Mock()
            p.getGradle() >> null
            def Wrapper wrapperMock = createDefaultWrapperMock()
            def AutowrapperExtension ext = new AutowrapperExtension(wrapperMock)
            ext.gradleVersion = nextVersion
            ext.autoGen = false
            def AutowrapperExecutor exec = new AutowrapperExecutor(ext, wrapperMock)
        when:
            exec.call(p)
        then:
            def e = thrown(StopExecutionException.class)
            e.message == 'Gradle version is not as required'
    }

    def 'test older version without fail fast'() {
        given:
            def Project p = Mock()
            p.getGradle() >> null
            def TaskRunner runner = Mock()
            def Wrapper wrapperMock = createDefaultWrapperMock()
            def AutowrapperExtension ext = new AutowrapperExtension(wrapperMock)
            ext.gradleVersion = nextVersion
            ext.failFast = false
            def AutowrapperExecutor exec = new AutowrapperExecutor(ext, wrapperMock, runner)
        when:
            exec.call(p)
        then:
            1 * runner.run(wrapperMock)
            noExceptionThrown()
    }
}
