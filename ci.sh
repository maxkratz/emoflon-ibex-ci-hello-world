#!/bin/bash

# hello world CI

#
# Config
#

ECLIPSE_ARCHIVE=eclipse-emoflon-linux-user-ci

set -e
START_PWD=$PWD
TEST_PROJECT=org.emoflon.ci.helloworld.tests
JAR_NAME=helloworld-testrunner

#
# Utils
#

# Displays the given input including "=> " on the console.
log () {
	echo "=> $1"
}

#
# Script
#

# Get eclipse
if [[ ! -f "./$ECLIPSE_ARCHIVE.zip" ]]; then
	log "Downloading latest eMoflon Eclipse archive from Github."
	curl -s https://api.github.com/repos/maxkratz/emoflon-eclipse-build/releases/latest \
        | grep "$ECLIPSE_ARCHIVE.*zip" \
        | cut -d : -f 2,3 \
        | tr -d \" \
        | wget -qi -
fi

log "Unzip new Eclipse from archive."
unzip -qq -o $ECLIPSE_ARCHIVE.zip

# Import into workspace
log "Import projects into Eclipse workspace."
cd $START_PWD
mkdir -p ./ws
cd eclipse
./eclipse -noSplash -consoleLog -data $START_PWD/ws -application com.seeq.eclipse.importprojects.headlessimport -importProject $START_PWD

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
cd $START_PWD/$TEST_PROJECT/
ant -f $JAR_NAME.xml

# Run tests
log "Running tests from JAR file."
java -jar $JAR_NAME.jar "$PWD/$JAR_NAME.jar"
