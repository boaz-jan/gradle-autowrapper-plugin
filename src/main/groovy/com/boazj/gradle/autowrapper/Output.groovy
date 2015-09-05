package com.boazj.gradle.autowrapper

import org.gradle.api.Project
import org.gradle.api.internal.GradleInternal
import org.gradle.internal.SystemProperties
import org.gradle.logging.StyledTextOutput

class Output {

    static enum Color {
        White,
        Red,
        Yellow,
        Green
    }

    def boolean quiet

    private def Closure sayMethod = { String s, Color color = Color.White ->
        if (!this.quiet) {
            print(s)
        }
    }
    private def Closure saylnMethod = { String s, Color color = Color.White ->
        if (!this.quiet) {
            println(s)
        }
    }
    private def translateColor = { Color color -> null }

    Output(Project project, String name, boolean quiet = false) {
        this.quiet = quiet
        def gradle = project.getGradle()
        if (gradle instanceof GradleInternal && gradle.metaClass.respondsTo(gradle, "getServices")) {
            org.gradle.logging.StyledTextOutput out = gradle.getServices().get(org.gradle.logging.StyledTextOutputFactory).create(name)
            out.style(translateColor(Color.White))

            translateColor = { Color color ->
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

            sayMethod = { String s, Color color = Color.White ->
                if (!quiet) {
                    out.withStyle(translateColor(color)).append(s)
                }
            }

            saylnMethod = { String s, Color color = Color.White ->
                say("${s}${SystemProperties.getInstance().getLineSeparator()}", color)
            }
        }
    }

    def say(String s, Color color = Color.White) {
        sayMethod(s, color)
    }

    def sayln(String s, Color color = Color.White) {
        saylnMethod(s, color)
    }

    def emptyln() {
        sayln('')
    }
}
