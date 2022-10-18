#!/bin/bash

# hello world CI

#
# Config
#

ECLIPSE_ARCHIVE=eclipse-emoflon-linux-user-ci

set -e
START_PWD=$PWD
REPO_NAME=emoflon-ci-hello-world
REPO_GROUP=maxkratz
TEST_PROJECT=org.emoflon.ci.helloworld.tests
JAR_NAME=helloworld-testrunner

#
# Utils
#

# Displays the given input including "=> " on the console.
log () {
	echo "=> $1"
}

# Cleans directories from previous runs up
cleanup () {
    rm -rf ./eclipse
    rm -rf ./repo
    rm -rf ./ws
    rm -rf ./repo/$REPO_NAME/$TEST_PROJECT/$JAR_NAME.xml
    rm -rf ./repo/$REPO_NAME/$TEST_PROJECT/$JAR_NAME.jar

    mkdir -p ./repo
    mkdir -p ./ws
}

#
# Script
#

log "Cleanup."
cleanup

# Get eclipse
if [[ ! -f "./$ECLIPSE_ARCHIVE.zip" ]]; then
	log "Downloading latest eMoflon Eclipse archive from GitHub."
	curl -s https://api.github.com/repos/eMoflon/emoflon-eclipse-build/releases/latest \
        | grep "$ECLIPSE_ARCHIVE.*zip" \
        | cut -d : -f 2,3 \
        | tr -d \" \
        | wget -qi -
fi

log "Unzip new Eclipse from archive."
unzip -qq -o $ECLIPSE_ARCHIVE.zip

# Get repo (and currently change branch)
log "Prepare repository."
cd repo
git clone git@github.com:$REPO_GROUP/$REPO_NAME.git
cd $REPO_NAME

# Import into workspace
log "Import projects into Eclipse workspace."
cd $START_PWD
cd eclipse
./eclipse -noSplash -consoleLog -data $START_PWD/ws -application com.seeq.eclipse.importprojects.headlessimport -importProject $START_PWD/repo

# Build projects (twice)
log "Build projects in Eclipse."
for i in {1..2}
do
    xvfb-run --auto-servernum --server-args="-ac" ./eclipse -noSplash -consoleLog -data $START_PWD/ws -application org.eclipse.jdt.apt.core.aptBuild
done

# Export ANT XML file
log "Exporting ANT XML file."
xvfb-run --auto-servernum --server-args="-ac" ./eclipse -noSplash -consoleLog -application org.emoflon.jar.build.JarAntBuilderApp -project $TEST_PROJECT -data $START_PWD/ws -antPath $JAR_NAME.xml -jarPath $JAR_NAME.jar -launchPath GlobalTestRunner.launch

# Export jar
log "Exporting JAR file using ANT."
cd $START_PWD/repo/$REPO_NAME/$TEST_PROJECT/
ant -f $JAR_NAME.xml

# Extract HiPE network file
log "Extract HiPE network file."
unzip -o $JAR_NAME.jar "org/emoflon/ci/helloworld/gt/hipe/engine/hipe-network.xmi"
rsync -a ./org ./bin
rm -rf ./org

# Run tests
log "Running tests from JAR file."
java -jar $JAR_NAME.jar "$PWD/$JAR_NAME.jar"
