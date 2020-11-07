#!/bin/sh
# @desc Remote utils configuration
# @since 2020.11.08, 00:59
# @changed 2020.11.08, 00:59

# export DATE=`date "+%Y.%m.%d %H:%M:%S"`
# export DATETAG=`date "+%y%m%d-%H%M"`

export PWD=`pwd`
# export PROJECT_NAME=`basename "${PWD}"`
export PROJECT_NAME="cam-client-android"

export REMOTE_TARGET_PATH="/home/g/goldenjeru/lilliputten.ru/$PROJECT_NAME" # Destination folder

# export ARCDIR="../!ARC"
# export REMOTE_ARCDIR="!ARC"

TIMESTAMP=`cat build-timestamp.txt`
TIMETAG=`cat build-timetag.txt`
VERSION=`cat build-version.txt`
VERSIONCODE=`cat build-version-code.txt`

export BUILD_TAG="$VERSION-$TIMETAG"

export REMOTE_DIR="$VERSION" # Destination folder

# Source folder (if not specified externally)
test -z "$SRC" && SRC="app/release"

# Check for creditinals presence...
export GOLDEN_PORT="2222" # Terminal connection port (may be specified in environment)
# Note: `$GOLDEN_USER` and `$GOLDEN_PASS` taken from project environment
if [ -z "$GOLDEN_USER" -o -z "$GOLDEN_PASS" -o -z "$GOLDEN_PORT" ]; then
  echo "Terminal creditinals must be specified in system environment!"
  echo "Check/set system environment variables 'GOLDEN_USER' and 'GOLDEN_PASS' (not 'TerminalServer*' -- it's old setup')."
  exit 1
fi

# Commands configuration...
export PLINK_CMD="plink -C -P $GOLDEN_PORT -l $GOLDEN_USER -pw $GOLDEN_PASS"
export CP_CMD="pscp -scp -r -C -P $GOLDEN_PORT -l $GOLDEN_USER -pw $GOLDEN_PASS"

# export ARC_CMD="tar czf"

# export ARCNAME="$PROJECT_NAME-$BUILD_TAG.tgz"
