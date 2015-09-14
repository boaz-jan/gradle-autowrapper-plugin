package com.boazj.gradle.autowrapper

import nebula.test.PluginProjectSpec
import org.gradle.api.GradleException
import org.gradle.api.tasks.wrapper.Wrapper

class AutowrapperPluginSpec extends PluginProjectSpec {

    @Override
    String getPluginName() { return 'com.boazj.autowrapper' }

    def 'test autowrapper task when applying plugin via full name'() {
        when:
            this.project.apply plugin: 'com.boazj.autowrapper'

        then:
            this.project.tasks.getByName(AutowrapperPlugin.WRAPPER_TASK_NAME)
    }

    def 'test hijacked task'() {
        given:
            this.project.tasks.create(AutowrapperPlugin.WRAPPER_TASK_NAME, Wrapper.class)

        when:
            this.project.apply plugin: 'com.boazj.autowrapper'

        then:
            def e = thrown(GradleException.class)
            e.cause.message == "Cannot instantiate plugin Autowrapper due to hijacked task name (${AutowrapperPlugin.WRAPPER_TASK_NAME})"
    }

    def 'test hijacked extension'() {
        given:
            def wrapperTask = this.project.tasks.create(AutowrapperPlugin.WRAPPER_TASK_NAME, Wrapper.class)
            this.project.extensions.create(AutowrapperPlugin.AUTOWRAPPER_EXTENSION_NAME, AutowrapperExtension, wrapperTask)
        when:
            this.project.apply plugin: 'com.boazj.autowrapper'

        then:
            def e = thrown(GradleException.class)
            e.cause.message == "Cannot instantiate plugin Autowrapper due to hijacked extension name (${AutowrapperPlugin.AUTOWRAPPER_EXTENSION_NAME})"
    }

    def 'test apply plugin on a project with that plugin already applied'() {
        given:
            this.project.apply plugin: 'com.boazj.autowrapper'
        when:
            this.project.apply plugin: 'com.boazj.autowrapper'

        then:
            noExceptionThrown()
    }
}