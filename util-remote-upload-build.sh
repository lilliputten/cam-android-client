#!/bin/sh
# @desc Upload build to remote server
# @changed 2020.11.08, 02:27
# NOTE: Steps to produce actual build:
# - Update version/datetags (`util-update-build-variables.sh`)
# - Build APK in Android Studio
# - Generate signed bundle in Android Studio (usign created certificate, see `certificate-make.sh`, `certificate-params.txt`)
# - Locate release in `app/release`

# If no required arguments specified...
if [ $# -ne 1 ]; then
  echo "Usage: $0 <server>"
  exit 1
fi

# Server to upload
SERVER=$1
shift

# Config import
. ./util-config.sh

SRC_APK_FILE="$SRC/app-release.apk"
SRC_METADATA_FILE="$SRC/output-metadata.json"

if [ ! -f "$SRC_APK_FILE" ]; then
  echo "APK file doesn't exist!"
  exit 1
fi

if [ ! -f "$SRC_METADATA_FILE" ]; then
  echo "Metadata file doesn't exist!"
  exit 1
fi

RELEASE_NAME="$PROJECT_NAME-v.$BUILD_TAG"
TARGET_APK_FILE="$REMOTE_DIR/$RELEASE_NAME.apk"
TARGET_METADATA_FILE="$REMOTE_DIR/$RELEASE_NAME-metadata.json"

echo "Uploading on remote server: $SERVER" \
  && echo "Creating remote folder ($REMOTE_DIR)..." \
  && $PLINK_CMD $SERVER "mkdir -p -m 0777 $REMOTE_TARGET_PATH/$REMOTE_DIR" \
  && echo "Copying apk ($SRC_APK_FILE -> $TARGET_APK_FILE)..." \
  && $CP_CMD "$SRC_APK_FILE" "$SERVER:$REMOTE_TARGET_PATH/$TARGET_APK_FILE" \
  && echo "Copying metadata ($SRC_METADATA_FILE -> $TARGET_METADATA_FILE)..." \
  && $CP_CMD "$SRC_METADATA_FILE" "$SERVER:$REMOTE_TARGET_PATH/$TARGET_METADATA_FILE" \
  && echo "Uploaded $BUILD_TAG"
