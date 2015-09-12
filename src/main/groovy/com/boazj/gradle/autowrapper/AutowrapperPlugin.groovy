package com.boazj.gradle.autowrapper

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginInstantiationException
import org.gradle.api.tasks.wrapper.Wrapper

class AutowrapperPlugin implements Plugin<Project> {
    public static final String AUTOWRAPPER_EXTENSION_NAME = 'autowrapper'
    public static final String WRAPPER_TASK_NAME = 'autowrapper'

    @Override
    void apply(Project project) {
        def potentialHijackedTask = project.tasks.findByName(WRAPPER_TASK_NAME)
        if (AutowrapperPlugin.get(project) != null &&
                AutowrapperExtension.exists(project) &&
                potentialHijackedTask != null &&
                potentialHijackedTask instanceof Wrapper) {
            //This means this plugin and it's components are already fully applied
            return
        }

        if (project.extensions.findByName(AUTOWRAPPER_EXTENSION_NAME) != null) {
            throw new PluginInstantiationException("Cannot instantiate plugin Autowrapper due to hijacked extension name (${AUTOWRAPPER_EXTENSION_NAME})")
        }

        if (potentialHijackedTask != null) {
            throw new PluginInstantiationException("Cannot instantiate plugin Autowrapper due to hijacked task name (${WRAPPER_TASK_NAME})")
        }

        def wrapperTask = project.tasks.create(WRAPPER_TASK_NAME, Wrapper.class)
        AutowrapperExtension ext = project.extensions.create(AUTOWRAPPER_EXTENSION_NAME, AutowrapperExtension, wrapperTask)
        project.afterEvaluate(new AutowrapperExecutor(ext, wrapperTask))
    }

    def static Plugin get(Project p){
        return p.plugins.findPlugin(AutowrapperPlugin.class)
    }
}


