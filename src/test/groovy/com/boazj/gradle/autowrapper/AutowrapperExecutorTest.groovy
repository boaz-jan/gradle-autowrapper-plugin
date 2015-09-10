package com.boazj.gradle.autowrapper

import com.boazj.gradle.utils.OutputFactory
import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

class AutowrapperExecutorTest extends IntegrationSpec {

    def setup() {
        this.fork = true
        def props = new File("${getProjectDir().absolutePath}/gradle/wrapper/gradle-wrapper.properties")
        if (props.exists()) {
            props.delete()
        }
    }

    def cleanup(){
        def props = new File("${getProjectDir().absolutePath}/gradle/")
        if (props.exists()) {
            props.delete()
        }
    }

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

    def boolean isWrapperPropsExists() {
        def props = new File("${getProjectDir().absolutePath}/gradle/wrapper/gradle-wrapper.properties")
        if (props.exists()) {
            return props.text.contains('services.gradle.org/distributions/gradle-2.5-bin.zip')
        }
        return false
    }

    def 'run with matching version'() {
        given:
            this.gradleVersion = '2.5'
            buildFile << generateBuildScript('2.5', false, true, false, true)
        and:
            System.setProperty(OutputFactory.SIMPLE_OUTPUT_PROPERTY, "${!advanceOutputAvailable}")
        when:
            ExecutionResult result = runTasksSuccessfully('tasks')
        then:
            result.standardOutput.contains('Checking Gradle version ... Gradle 2.5 √')
        where:
            advanceOutputAvailable << [true, false]
    }

    def 'run with old gradle version'() {
        given:
            this.gradleVersion = '2.4'
            buildFile << generateBuildScript('2.5', false, true, false, true)
        and:
            System.setProperty(OutputFactory.SIMPLE_OUTPUT_PROPERTY, "${!advanceOutputAvailable}")
        when:
            ExecutionResult result = runTasksWithFailure('tasks')
        then:
            result.standardOutput.contains('Checking Gradle version ... The build script requires Gradle 2.5. Currently executing Gradle 2.4.')
            result.standardOutput.contains('Generating Gradle Wrapper for version 2.5.')
            result.standardOutput.contains('Failing build, please execute this script again with the Gradle Wrapper')
            result.standardError.contains('Gradle version is not as required')
            isWrapperPropsExists()
        where:
            advanceOutputAvailable << [true, false]
    }

    def 'run with old gradle version - non autoGen mode'() {
        given:
            this.gradleVersion = '2.4'
            buildFile << generateBuildScript('2.5', false, true, false, false)
        and:
            System.setProperty(OutputFactory.SIMPLE_OUTPUT_PROPERTY, "${!advanceOutputAvailable}")
        when:
            ExecutionResult result = runTasksWithFailure('tasks')
        then:
            result.standardOutput.contains('Checking Gradle version ... The build script requires Gradle 2.5. Currently executing Gradle 2.4.')
            result.standardOutput.contains('Failing build')
            result.standardError.contains('Gradle version is not as required')
            !isWrapperPropsExists()
        where:
            advanceOutputAvailable << [true, false]
    }

    def 'run with newer version - non strict mode'() {
        given:
            this.gradleVersion = '2.6'
            buildFile << generateBuildScript('2.5', false, true, false, true)
        and:
            System.setProperty(OutputFactory.SIMPLE_OUTPUT_PROPERTY, "${!advanceOutputAvailable}")
        when:
            ExecutionResult result = runTasksSuccessfully('tasks')
        then:
            result.standardOutput.contains('Gradle 2.6 √')
        where:
            advanceOutputAvailable << [true, false]
    }

    def 'run with newer version - strict mode'() {
        given:
            this.gradleVersion = '2.6'
            buildFile << generateBuildScript('2.5', true, true, false, true)
        and:
            System.setProperty(OutputFactory.SIMPLE_OUTPUT_PROPERTY, "${!advanceOutputAvailable}")
        when:
            ExecutionResult result = runTasksWithFailure('tasks')
        then:
            result.standardOutput.contains('Checking Gradle version ... The build script requires Gradle 2.5. Currently executing Gradle 2.6.')
            result.standardOutput.contains('Generating Gradle Wrapper for version 2.5.')
            result.standardOutput.contains('Failing build, please execute this script again with the Gradle Wrapper')
            result.standardError.contains('Gradle version is not as required')
            isWrapperPropsExists()
        where:
            advanceOutputAvailable << [true, false]
    }

    def 'run quietly with newer version - strict mode'() {
        given:
            this.gradleVersion = '2.6'
            buildFile << generateBuildScript('2.5', true, true, true, true)
        and:
            System.setProperty(OutputFactory.SIMPLE_OUTPUT_PROPERTY, "${!advanceOutputAvailable}")
        when:
            ExecutionResult result = runTasksWithFailure('tasks')
        then:
            !result.standardOutput.contains('Checking Gradle version ... The build script requires Gradle 2.5. Currently executing Gradle 2.6.')
            !result.standardOutput.contains('Generating Gradle Wrapper for version 2.5.')
            !result.standardOutput.contains('Failing build, please execute this script again with the Gradle Wrapper')
            result.standardError.contains('Gradle version is not as required')
            isWrapperPropsExists()
        where:
            advanceOutputAvailable << [true, false]
    }
}
