package com.boazj.gradle.utils

class DefaultOutput implements Output {

    private def boolean quiet
    private def OutputListener listener

    DefaultOutput(boolean quiet = false, OutputListener listener = null) {
        this.quiet = quiet
        this.listener = listener
    }

    def void say(String s, Color color = Color.White) {
        if (!this.quiet) {
            listener?.onOutput(s, color)
            print(s)
        }
    }

    def void sayln(String s, Color color = Color.White) {
        if (!this.quiet) {
            listener?.onOutput(s, color)
            println(s)
        }
    }
}
