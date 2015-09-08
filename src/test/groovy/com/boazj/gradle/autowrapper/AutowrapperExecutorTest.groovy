package com.boazj.gradle.autowrapper

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

class AutowrapperExecutorTest extends IntegrationSpec {

    def 'run with matching version'() {
        this.gradleVersion = '2.5'
        buildFile << '''
            apply plugin: 'com.boazj.autowrapper'

            task nothing << {
                println 'Done.'
            }

            autowrapper {
                gradleVersion = '2.5'
                strict = false
                failFast = true
                quiet = false
                autoGen = true
            }
        '''.stripIndent()

        when:
        ExecutionResult result = runTasksSuccessfully('nothing')

        then:
        result.standardOutput.contains('Gradle 2.5 √')
    }

    def 'run with old gradle version'() {
        this.gradleVersion = '2.4'
        buildFile << '''
            apply plugin: 'com.boazj.autowrapper'

            task nothing << {
                println 'Done.'
            }

            autowrapper {
                gradleVersion = '2.5'
                strict = false
                failFast = true
                quiet = false
                autoGen = true
            }
        '''.stripIndent()

        when:
        ExecutionResult result = runTasksWithFailure('autowrapper')

        then:
        result.standardOutput.contains('Checking Gradle version ... The build script requires Gradle 2.5. Currently executing Gradle 2.4.')
        result.standardOutput.contains('Generating Gradle Wrapper for version 2.5.')
        result.standardOutput.contains('Failing build, please execute this script again with the Gradle Wrapper')
        result.standardError.contains('Gradle version is not as required')
        new File("${getProjectDir().absolutePath}/gradle/wrapper/gradle-wrapper.properties").text.contains('services.gradle.org/distributions/gradle-2.5-bin.zip')
    }

    def 'run with old gradle version - non autoGen mode'() {
        def props = new File("${getProjectDir().absolutePath}/gradle/wrapper/gradle-wrapper.properties")
        if (props.exists()) {
            props.delete()
        }
        this.gradleVersion = '2.4'
        buildFile << '''
            apply plugin: 'com.boazj.autowrapper'

            task nothing << {
                println 'Done.'
            }

            autowrapper {
                gradleVersion = '2.5'
                strict = false
                failFast = true
                quiet = false
                autoGen = false
            }
        '''.stripIndent()

        when:
        ExecutionResult result = runTasksWithFailure('autowrapper')

        then:
        result.standardOutput.contains('Checking Gradle version ... The build script requires Gradle 2.5. Currently executing Gradle 2.4.')
        result.standardOutput.contains('Failing build')
        result.standardError.contains('Gradle version is not as required')
        def prop = new File("${getProjectDir().absolutePath}/gradle/wrapper/gradle-wrapper.properties");
        !prop.exists()
    }

    def 'run with newer version - non strict mode'() {
        this.gradleVersion = '2.6'
        buildFile << '''
            apply plugin: 'com.boazj.autowrapper'

            task nothing << {
                println 'Done.'
            }

            autowrapper {
                gradleVersion = '2.5'
                strict = false
                failFast = true
                quiet = false
                autoGen = true
            }
        '''.stripIndent()

        when:
        ExecutionResult result = runTasksSuccessfully('autowrapper')

        then:
        result.standardOutput.contains('Gradle 2.6 √')
    }

    def 'run with newer version - strict mode'() {
        this.gradleVersion = '2.6'
        buildFile << '''
            apply plugin: 'com.boazj.autowrapper'

            task nothing << {
                println 'Done.'
            }

            autowrapper {
                gradleVersion = '2.5'
                strict = true
                failFast = true
                quiet = false
                autoGen = true
            }
        '''.stripIndent()

        when:
        ExecutionResult result = runTasksWithFailure('autowrapper')

        then:
        result.standardOutput.contains('Checking Gradle version ... The build script requires Gradle 2.5. Currently executing Gradle 2.6.')
        result.standardOutput.contains('Generating Gradle Wrapper for version 2.5.')
        result.standardOutput.contains('Failing build, please execute this script again with the Gradle Wrapper')
        result.standardError.contains('Gradle version is not as required')
        new File("${getProjectDir().absolutePath}/gradle/wrapper/gradle-wrapper.properties").text.contains('services.gradle.org/distributions/gradle-2.5-bin.zip')
    }

    def 'run quietly with newer version - strict mode'() {
        this.gradleVersion = '2.6'
        buildFile << '''
            apply plugin: 'com.boazj.autowrapper'

            task nothing << {
                println 'Done.'
            }

            autowrapper {
                gradleVersion = '2.5'
                strict = true
                failFast = true
                quiet = true
                autoGen = true
            }
        '''.stripIndent()

        when:
        ExecutionResult result = runTasksWithFailure('autowrapper')

        then:
        !result.standardOutput.contains('Checking Gradle version ... The build script requires Gradle 2.5. Currently executing Gradle 2.6.')
        !result.standardOutput.contains('Generating Gradle Wrapper for version 2.5.')
        !result.standardOutput.contains('Failing build, please execute this script again with the Gradle Wrapper')
        result.standardError.contains('Gradle version is not as required')
        new File("${getProjectDir().absolutePath}/gradle/wrapper/gradle-wrapper.properties").text.contains('services.gradle.org/distributions/gradle-2.5-bin.zip')
    }
}
