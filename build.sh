#!/bin/sh

echo "Ready to build apk"

if [ "$TRAVIS_BRANCH" == "staging" ]; then
    echo "Build staging apk"
    ./gradlew staging --stacktrace
elif [ "$TRAVIS_BRANCH" == "mstager" ]; then
    echo "Build release apk"
    ./gradlew release --stacktrace
else
    echo "Build debug apk"
    ./gradlew debug --stacktrace
fi