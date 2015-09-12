package com.boazj.gradle.autowrapper

import org.gradle.api.Project
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class AutowrapperExtensionSpec extends Specification {

    def 'test get extension from project'() {
        given:
            def Project p = ProjectBuilder.builder().build()
            def wrapperTask = p.tasks.create(AutowrapperPlugin.WRAPPER_TASK_NAME, Wrapper.class)
            def AutowrapperExtension ext = p.extensions.create(AutowrapperPlugin.AUTOWRAPPER_EXTENSION_NAME, AutowrapperExtension, wrapperTask)
        expect:
            AutowrapperExtension.get(p) == ext
    }

    def 'test extension present'() {
        given:
            def Project p = ProjectBuilder.builder().build()
            def wrapperTask = p.tasks.create(AutowrapperPlugin.WRAPPER_TASK_NAME, Wrapper.class)
            p.extensions.create(AutowrapperPlugin.AUTOWRAPPER_EXTENSION_NAME, AutowrapperExtension, wrapperTask)
        expect:
            AutowrapperExtension.exists(p) == true
    }

    def 'test extension not present'() {
        given:
            def Project p = ProjectBuilder.builder().build()
        expect:
            AutowrapperExtension.exists(p) == false
    }
}
