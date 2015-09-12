package com.boazj.gradle.utils

import groovy.ui.SystemOutputInterceptor
import spock.lang.Specification

class DefaultOutputSpec extends Specification {

    def 'test default output say'() {
        given:
            def DefaultOutput out = new DefaultOutput()
            def expected = 'test text'
            def actual ='';
            def interceptor = new SystemOutputInterceptor({ actual += it; false})
        when:
            interceptor.start()
            out.say(expected)
            interceptor.stop()
        then:
            actual == expected
    }

    def 'test default output sayln'() {
        given:
            def DefaultOutput out = new DefaultOutput()
            def expected = 'test text'
            def actual ='';
            def interceptor = new SystemOutputInterceptor({ actual += it; false})
        when:
            interceptor.start()
            out.sayln(expected)
            interceptor.stop()
        then:
            actual == expected + OutputFactory.lineSeparator()
    }

    def 'test default output say when quiet'() {
        given:
            def DefaultOutput out = new DefaultOutput(true)
            def actual ='';
            def interceptor = new SystemOutputInterceptor({ actual += it; false})
        when:
            interceptor.start()
            out.say('test text')
            interceptor.stop()
        then:
            actual == ''
    }

    def 'test default output sayln when quiet'() {
        given:
            def DefaultOutput out = new DefaultOutput(true)
            def actual ='';
            def interceptor = new SystemOutputInterceptor({ actual += it; false})
        when:
            interceptor.start()
            out.sayln('test text')
            interceptor.stop()
        then:
            actual == ''
    }
}
