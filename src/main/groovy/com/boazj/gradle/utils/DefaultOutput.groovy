package com.boazj.gradle.utils

import org.gradle.api.Project

class DefaultOutput implements Output {

    private def boolean quiet

    DefaultOutput(Project project, String name, boolean quiet = false) {
        this.quiet = quiet
    }

    def void say(String s, Color color = Color.White) {
        if (!this.quiet) {
            print(s)
        }
    }

    def void sayln(String s, Color color = Color.White) {
        if (!this.quiet) {
            println(s)
        }
    }
}
