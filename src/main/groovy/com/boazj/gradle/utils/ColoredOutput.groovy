package com.boazj.gradle.utils

import org.gradle.api.internal.GradleInternal
import org.gradle.api.invocation.Gradle
import org.gradle.logging.StyledTextOutput
import org.gradle.logging.StyledTextOutputFactory

class ColoredOutput implements Output {
    private def boolean quiet
    private def StyledTextOutput out
    private def OutputListener listener

    ColoredOutput(Gradle gradle, String name, boolean quiet = false, OutputListener listener = null) {
        this.quiet = quiet
        this.listener = listener
        def internalGradle = (GradleInternal) gradle
        out = internalGradle.getServices().get(StyledTextOutputFactory).create(name)
        this.out.style(translateColor(Color.White))
    }

    def translateColor(Color color) {
        switch (color) {
            case Color.White:
                return StyledTextOutput.Style.Normal
            case Color.Red:
                return StyledTextOutput.Style.Failure
            case Color.Yellow:
                return StyledTextOutput.Style.ProgressStatus
            case Color.Green:
                return StyledTextOutput.Style.Identifier
        }
    }

    def void say(String s, Color color = Color.White) {
        if (!quiet) {
            listener?.onOutput(s, color)
            out.withStyle(translateColor(color)).append(s)
        }
    }

    def void sayln(String s, Color color = Color.White) {
        say("${s}${OutputFactory.lineSeparator()}", color)
    }
}