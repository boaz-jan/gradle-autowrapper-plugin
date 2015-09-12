package com.boazj.gradle.utils

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class ColoredOutputSpec extends Specification {
    private def OutputListener listener = Mock()
    private Project p = ProjectBuilder.builder().build()

    def 'test colored output say'() {
        given:
            def ColoredOutput out = new ColoredOutput(p.getGradle(), 'name', false, listener)
            def expected = 'test text'
        when:
            out.say(expected)
        then:
            1 * listener.onOutput(expected, _)
    }

    def 'test colored output sayln'() {
        given:
            def ColoredOutput out = new ColoredOutput(p.getGradle(), 'name', false, listener)
            def expected = 'test text'
        when:
            out.sayln(expected)
        then:
            1 * listener.onOutput(expected + OutputFactory.lineSeparator(), _)
    }

    def 'test colored output say when quiet'() {
        given:
            def ColoredOutput out = new ColoredOutput(p.getGradle(), 'name', true, listener)
            def expected = 'test text'
        when:
            out.say(expected)
        then:
            0 * listener.onOutput(_, _)
    }

    def 'test colored output sayln when quiet'() {
        given:
            def ColoredOutput out = new ColoredOutput(p.getGradle(), 'name', true, listener)
            def expected = 'test text'
        when:
            out.sayln(expected)
        then:
            0 * listener.onOutput(_, _)
    }
}
