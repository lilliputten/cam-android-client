#!/bin/sh
# @desc Increment version number
# @changed 2020.11.08, 01:41

VERSION_FILE="build-version.txt"
VERSIONCODE_FILE="build-version-code.txt"
# BACKUP="$VERSION_FILE.bak"

test -f "$VERSION_FILE" || echo "0.0.0" > "$VERSION_FILE"
test -f "$VERSIONCODE_FILE" || echo "0" > "$VERSIONCODE_FILE"

VERSION=`cat $VERSION_FILE`
CODE=`cat $VERSIONCODE_FILE`

echo "Current version: $VERSION (code: $CODE)"

# Extract patch number
PATCH_NUMBER=`echo $VERSION | sed "s/^\(.*\)\.\([0-9]\+\)$/\2/"`

if [ "$PATCH_NUMBER" == "" ]; then
  echo "No patch number found!"
  exit 1
fi

# Extract code number
CODE_NUMBER=$CODE

if [ "$CODE_NUMBER" == "" ]; then
  echo "No code number found!"
  exit 1
fi

# Increment patch number & code
NEXT_PATCH_NUMBER=`expr $PATCH_NUMBER + 1`
NEXT_CODE_NUMBER=`expr $CODE_NUMBER + 1`

echo "Increment patch ($PATCH_NUMBER -> $NEXT_PATCH_NUMBER) & code ($CODE_NUMBER -> $NEXT_CODE_NUMBER) numbers" \
  && cp "$VERSION_FILE" "$VERSION_FILE.bak" \
  && cat "$VERSION_FILE.bak" \
    | sed "s/^\(.*\)\.\([0-9]\+\)$/\1.$NEXT_PATCH_NUMBER/" \
    > "$VERSION_FILE" \
  && rm "$VERSION_FILE.bak" \
  && echo $NEXT_CODE_NUMBER > "$VERSIONCODE_FILE" \
  && echo "Updated version: `cat $VERSION_FILE` ($NEXT_CODE_NUMBER)" \
  && sh "./util-update-build-variables.sh"
