package com.boazj.gradle.autowrapper

import org.gradle.util.GradleVersion

interface DistributionLocator {
    def String getDistributionFor(GradleVersion version)
}