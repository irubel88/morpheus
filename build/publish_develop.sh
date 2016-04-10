#!/usr/bin/env bash
if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "develop" ];
then

    if [ "${TRAVIS_SCALA_VERSION}" == "2.11.7" ] && [ "${TRAVIS_JDK_VERSION}" == "oraclejdk8" ];
    then

        echo "Setting git user email to ci@outworkers.com"
        git config user.email "ci@outworkers.com"
        
        echo "Setting git user name to Travis CI"
        git config user.name "Travis CI"

        echo "The current JDK version is ${TRAVIS_JDK_VERSION}"
        echo "The current Scala version is ${TRAVIS_SCALA_VERSION}"

        echo "Creating credentials file"
        if [ -e "$HOME/.bintray/.credentials" ]; then
            echo "Bintray redentials file already exists"
        else
            mkdir -p "$HOME/.bintray/"
            touch "$HOME/.bintray/.credentials"
            echo "realm = Bintray API Realm" >> "$HOME/.bintray/.credentials"
            echo "host = api.bintray.com" >> "$HOME/.bintray/.credentials"
            echo "user = $bintray_user" >> "$HOME/.bintray/.credentials"
            echo "password = $bintray_password" >> "$HOME/.bintray/.credentials"
        fi

        if [ -e "$HOME/.bintray/.credentials" ]; then
            echo "Bintray credentials file succesfully created"
        else
            echo "Bintray credentials still not found"
        fi

        sbt version-bump-patch git-tag

        echo "Pushing tag to GitHub."

        git push --tags "https://${github_token}@${GH_REF}"

        echo "Publishing Bintray artifact"

        "Publishing a new version to Bintray"
        sbt +publish

        git add .

        git commit -m "Automatically incrementing tag version."

        echo "Printing available remotes"
        git remote -v

        git push "https://${github_token}@${GH_REF}" develop

        git push "https://${github_token}@${GH_REF}" develop:master

    else
        echo "Only publishing version for Scala 2.11.7 and Oracle JDK 8 to prevent multiple artifacts"
    fi
else
    echo "This is either a pull request or the branch is not develop, deployment not necessary"
fi
