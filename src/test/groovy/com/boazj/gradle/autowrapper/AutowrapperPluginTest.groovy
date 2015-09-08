package com.boazj.gradle.autowrapper

import nebula.test.PluginProjectSpec

class AutowrapperPluginTest extends PluginProjectSpec {

    @Override
    String getPluginName() { return 'com.boazj.autowrapper' }

    def 'check autowrapper task when applying plugin via full name'() {
        when:
        this.project.apply plugin: 'com.boazj.autowrapper'

        then:
        this.project.tasks.getByName(AutowrapperPlugin.WRAPPER_TASK_NAME)
    }

    def 'check autowrapper task when applying plugin via short name'() {
        when:
        this.project.apply plugin: 'autowrapper'

        then:
        this.project.tasks.getByName(AutowrapperPlugin.WRAPPER_TASK_NAME)
    }
}