package com.boazj.gradle.autowrapper

import org.gradle.api.Task

class DefaultTaskRunner implements TaskRunner {

    def void run(Task task) {
        if (task.metaClass.respondsTo(task, 'execute')) {
            task.execute()
        }
    }
}
