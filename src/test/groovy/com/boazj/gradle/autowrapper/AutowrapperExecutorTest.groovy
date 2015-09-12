package com.boazj.gradle.autowrapper

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

class AutowrapperExecutorTest extends IntegrationSpec {

    def String generateBuildScript(String gradleVersion, boolean strict, boolean failFast, boolean quiet, boolean autoGen){
        return """
                apply plugin: 'com.boazj.autowrapper'
                autowrapper {
                    gradleVersion = '${gradleVersion}'
                    strict = ${strict}
                    failFast = ${failFast}
                    quiet = ${quiet}
                    autoGen = ${autoGen}
                }
            """.stripIndent()
    }

    def File wrapperProperties() {
        def props = new File("${getProjectDir().absolutePath}/gradle/wrapper/gradle-wrapper.properties")
        if (props.exists()) {
            return props
        }
        return null
    }

    def 'test execution with matching versions'() {
        given:
            this.gradleVersion = '2.5'
            buildFile << generateBuildScript('2.5', false, true, false, true)
        when:
            ExecutionResult result = runTasksSuccessfully('tasks')
        then:
            result.standardOutput.contains('Checking Gradle version ... Gradle 2.5 √')
    }

    def 'test execution with old version'() {
        given:
            this.gradleVersion = '2.4'
            buildFile << generateBuildScript('2.5', false, true, false, true)
        when:
            ExecutionResult result = runTasksWithFailure('tasks')
        then:
            result.standardOutput.contains('Checking Gradle version ... The build script requires Gradle 2.5. Currently executing Gradle 2.4.')
            result.standardOutput.contains('Generating Gradle Wrapper for version 2.5.')
            result.standardOutput.contains('Failing build, please execute this script again with the Gradle Wrapper')
            result.standardError.contains('Gradle version is not as required')
            wrapperProperties()?.text.contains('services.gradle.org/distributions/gradle-2.5-bin.zip')
    }

    def 'test execution with old version without autogeneration'() {
        given:
            this.gradleVersion = '2.4'
            buildFile << generateBuildScript('2.5', false, true, false, false)
        when:
            ExecutionResult result = runTasksWithFailure('tasks')
        then:
            result.standardOutput.contains('Checking Gradle version ... The build script requires Gradle 2.5. Currently executing Gradle 2.4.')
            result.standardOutput.contains('Failing build')
            result.standardError.contains('Gradle version is not as required')
            wrapperProperties() == null
    }

    def 'test execution with newer version'() {
        given:
            this.gradleVersion = '2.6'
            buildFile << generateBuildScript('2.5', false, true, false, true)
        when:
            ExecutionResult result = runTasksSuccessfully('tasks')
        then:
            result.standardOutput.contains('Gradle 2.6 √')
    }

    def 'test execution with newer version in strict mode'() {
        given:
            this.gradleVersion = '2.6'
            buildFile << generateBuildScript('2.5', true, true, false, true)
        when:
            ExecutionResult result = runTasksWithFailure('tasks')
        then:
            result.standardOutput.contains('Checking Gradle version ... The build script requires Gradle 2.5. Currently executing Gradle 2.6.')
            result.standardOutput.contains('Generating Gradle Wrapper for version 2.5.')
            result.standardOutput.contains('Failing build, please execute this script again with the Gradle Wrapper')
            result.standardError.contains('Gradle version is not as required')
            wrapperProperties()?.text.contains('services.gradle.org/distributions/gradle-2.5-bin.zip')
    }

    def 'test execution with newer version in strict quiet mode'() {
        given:
            this.gradleVersion = '2.6'
            buildFile << generateBuildScript('2.5', true, true, true, true)
        when:
            ExecutionResult result = runTasksWithFailure('tasks')
        then:
            !result.standardOutput.contains('Checking Gradle version ... The build script requires Gradle 2.5. Currently executing Gradle 2.6.')
            !result.standardOutput.contains('Generating Gradle Wrapper for version 2.5.')
            !result.standardOutput.contains('Failing build, please execute this script again with the Gradle Wrapper')
            result.standardError.contains('Gradle version is not as required')
            wrapperProperties()?.text.contains('services.gradle.org/distributions/gradle-2.5-bin.zip')
    }
}
