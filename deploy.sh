#!/bin/sh

echo "Ready to deploy apk"

if [ "$TRAVIS_BRANCH" == "staging" ]; then
    echo "Deploy apk to Fabric Beta"
    ./gradlew crashlyticsUploadDistributionStaging --stacktrace
elif [ "$TRAVIS_BRANCH" == "master" ]; then
    echo "Deploy apk to Google Play"
    # deploy to google play store
else
    echo "Deploy to nowhere"
fi
