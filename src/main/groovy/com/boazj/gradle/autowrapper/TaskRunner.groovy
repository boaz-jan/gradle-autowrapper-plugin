package com.boazj.gradle.autowrapper

import org.gradle.api.Task

interface TaskRunner {
    def void run(Task task)
}
