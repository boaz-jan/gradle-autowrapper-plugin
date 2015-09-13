package com.boazj.gradle.autowrapper

import org.gradle.api.Action
import org.gradle.api.AntBuilder
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger
import org.gradle.api.logging.LoggingManager
import org.gradle.api.plugins.Convention
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.TaskDependency
import org.gradle.api.tasks.TaskInputs
import org.gradle.api.tasks.TaskOutputs
import org.gradle.api.tasks.TaskState
import spock.lang.Specification

class DefaultTaskRunnerSpec extends Specification {

    def 'test task runner with no execute method'() {
        given:
            Task t = new Task(){

                @Override
                String getName() {
                    return null
                }

                @Override
                Project getProject() {
                    return null
                }

                @Override
                List<Action<? super Task>> getActions() {
                    return null
                }

                @Override
                void setActions(List<Action<? super Task>> actions) {

                }

                @Override
                TaskDependency getTaskDependencies() {
                    return null
                }

                @Override
                Set<Object> getDependsOn() {
                    return null
                }

                @Override
                void setDependsOn(Iterable<?> dependsOnTasks) {

                }

                @Override
                Task dependsOn(Object... paths) {
                    return null
                }

                @Override
                void onlyIf(Closure onlyIfClosure) {

                }

                @Override
                void onlyIf(Spec<? super Task> onlyIfSpec) {

                }

                @Override
                void setOnlyIf(Closure onlyIfClosure) {

                }

                @Override
                void setOnlyIf(Spec<? super Task> onlyIfSpec) {

                }

                @Override
                TaskState getState() {
                    return null
                }

                @Override
                void setDidWork(boolean didWork) {

                }

                @Override
                boolean getDidWork() {
                    return false
                }

                @Override
                String getPath() {
                    return null
                }

                @Override
                Task doFirst(Action<? super Task> action) {
                    return null
                }

                @Override
                Task doFirst(Closure action) {
                    return null
                }

                @Override
                Task doLast(Action<? super Task> action) {
                    return null
                }

                @Override
                Task doLast(Closure action) {
                    return null
                }

                @Override
                Task leftShift(Closure action) {
                    return null
                }

                @Override
                Task deleteAllActions() {
                    return null
                }

                @Override
                boolean getEnabled() {
                    return false
                }

                @Override
                void setEnabled(boolean enabled) {

                }

                @Override
                Task configure(Closure configureClosure) {
                    return null
                }

                @Override
                AntBuilder getAnt() {
                    return null
                }

                @Override
                Logger getLogger() {
                    return null
                }

                @Override
                LoggingManager getLogging() {
                    return null
                }

                @Override
                Object property(String propertyName) throws MissingPropertyException {
                    return null
                }

                @Override
                boolean hasProperty(String propertyName) {
                    return false
                }

                @Override
                Convention getConvention() {
                    return null
                }

                @Override
                String getDescription() {
                    return null
                }

                @Override
                void setDescription(String description) {

                }

                @Override
                String getGroup() {
                    return null
                }

                @Override
                void setGroup(String group) {

                }

                @Override
                boolean dependsOnTaskDidWork() {
                    return false
                }

                @Override
                TaskInputs getInputs() {
                    return null
                }

                @Override
                TaskOutputs getOutputs() {
                    return null
                }

                @Override
                File getTemporaryDir() {
                    return null
                }

                @Override
                Task mustRunAfter(Object... paths) {
                    return null
                }

                @Override
                void setMustRunAfter(Iterable<?> mustRunAfter) {

                }

                @Override
                TaskDependency getMustRunAfter() {
                    return null
                }

                @Override
                Task finalizedBy(Object... paths) {
                    return null
                }

                @Override
                void setFinalizedBy(Iterable<?> finalizedBy) {

                }

                @Override
                TaskDependency getFinalizedBy() {
                    return null
                }

                @Override
                TaskDependency shouldRunAfter(Object... paths) {
                    return null
                }

                @Override
                void setShouldRunAfter(Iterable<?> shouldRunAfter) {

                }

                @Override
                TaskDependency getShouldRunAfter() {
                    return null
                }

                @Override
                int compareTo(Task o) {
                    return 0
                }

                @Override
                ExtensionContainer getExtensions() {
                    return null
                }
            }
            TaskRunner runner = new DefaultTaskRunner()
        when:
            runner.run(t)
        then:
            noExceptionThrown()
    }

    def 'test task runner with execute method'() {
        given:
            Task t = new Task(){

                void execute(){
                    throw new RuntimeException()
                }

                @Override
                String getName() {
                    return null
                }

                @Override
                Project getProject() {
                    return null
                }

                @Override
                List<Action<? super Task>> getActions() {
                    return null
                }

                @Override
                void setActions(List<Action<? super Task>> actions) {

                }

                @Override
                TaskDependency getTaskDependencies() {
                    return null
                }

                @Override
                Set<Object> getDependsOn() {
                    return null
                }

                @Override
                void setDependsOn(Iterable<?> dependsOnTasks) {

                }

                @Override
                Task dependsOn(Object... paths) {
                    return null
                }

                @Override
                void onlyIf(Closure onlyIfClosure) {

                }

                @Override
                void onlyIf(Spec<? super Task> onlyIfSpec) {

                }

                @Override
                void setOnlyIf(Closure onlyIfClosure) {

                }

                @Override
                void setOnlyIf(Spec<? super Task> onlyIfSpec) {

                }

                @Override
                TaskState getState() {
                    return null
                }

                @Override
                void setDidWork(boolean didWork) {

                }

                @Override
                boolean getDidWork() {
                    return false
                }

                @Override
                String getPath() {
                    return null
                }

                @Override
                Task doFirst(Action<? super Task> action) {
                    return null
                }

                @Override
                Task doFirst(Closure action) {
                    return null
                }

                @Override
                Task doLast(Action<? super Task> action) {
                    return null
                }

                @Override
                Task doLast(Closure action) {
                    return null
                }

                @Override
                Task leftShift(Closure action) {
                    return null
                }

                @Override
                Task deleteAllActions() {
                    return null
                }

                @Override
                boolean getEnabled() {
                    return false
                }

                @Override
                void setEnabled(boolean enabled) {

                }

                @Override
                Task configure(Closure configureClosure) {
                    return null
                }

                @Override
                AntBuilder getAnt() {
                    return null
                }

                @Override
                Logger getLogger() {
                    return null
                }

                @Override
                LoggingManager getLogging() {
                    return null
                }

                @Override
                Object property(String propertyName) throws MissingPropertyException {
                    return null
                }

                @Override
                boolean hasProperty(String propertyName) {
                    return false
                }

                @Override
                Convention getConvention() {
                    return null
                }

                @Override
                String getDescription() {
                    return null
                }

                @Override
                void setDescription(String description) {

                }

                @Override
                String getGroup() {
                    return null
                }

                @Override
                void setGroup(String group) {

                }

                @Override
                boolean dependsOnTaskDidWork() {
                    return false
                }

                @Override
                TaskInputs getInputs() {
                    return null
                }

                @Override
                TaskOutputs getOutputs() {
                    return null
                }

                @Override
                File getTemporaryDir() {
                    return null
                }

                @Override
                Task mustRunAfter(Object... paths) {
                    return null
                }

                @Override
                void setMustRunAfter(Iterable<?> mustRunAfter) {

                }

                @Override
                TaskDependency getMustRunAfter() {
                    return null
                }

                @Override
                Task finalizedBy(Object... paths) {
                    return null
                }

                @Override
                void setFinalizedBy(Iterable<?> finalizedBy) {

                }

                @Override
                TaskDependency getFinalizedBy() {
                    return null
                }

                @Override
                TaskDependency shouldRunAfter(Object... paths) {
                    return null
                }

                @Override
                void setShouldRunAfter(Iterable<?> shouldRunAfter) {

                }

                @Override
                TaskDependency getShouldRunAfter() {
                    return null
                }

                @Override
                int compareTo(Task o) {
                    return 0
                }

                @Override
                ExtensionContainer getExtensions() {
                    return null
                }
            }
            TaskRunner runner = new DefaultTaskRunner()
        when:
            runner.run(t)
        then:
            thrown(RuntimeException)
    }
}
