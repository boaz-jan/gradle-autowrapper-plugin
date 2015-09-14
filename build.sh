#!/bin/bash
./gradlew build -S
echo $TRAVIS_JDK_VERSION
echo $TRAVIS_PULL_REQUEST
echo $TRAVIS_TAG
if [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_TAG" != "" ]; then
   ./gradlew release
fi