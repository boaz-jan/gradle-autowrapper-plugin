package com.boazj.gradle.autowrapper

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.wrapper.Wrapper

class AutowrapperPlugin implements Plugin<Project> {
    public static final String AUTOWRAPPER_EXTENSION_NAME = 'autowrapper'
    public static final String WRAPPER_TASK_NAME = 'autowrapper'

    @Override
    void apply(Project project) {
        if (!GradleUtils.isRootProject(project) ||
                GradleUtils.isExtensionPresent(project, AUTOWRAPPER_EXTENSION_NAME) ||
                project.tasks.findByName(WRAPPER_TASK_NAME) != null) {
            return
        }

        AutowrapperExtension ext = project.extensions.create(AUTOWRAPPER_EXTENSION_NAME, AutowrapperExtension)
        def wrapperTask = project.tasks.create(WRAPPER_TASK_NAME, Wrapper.class)
        project.afterEvaluate(new AutowrapperExecutor(ext, wrapperTask))
    }
}


