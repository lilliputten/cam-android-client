#!/bin/sh
# @desc Execute custom shell command on remote server
# @since 2020.11.08, 00:59
# @changed 2020.11.08, 00:59

# If no arguments specified...
if [ $# -lt 2 ]; then
  echo "Usage: $0 <server> <cmd...>"
  exit 1
fi

# Remote server
SERVER=$1
shift

echo $SERVER

# Config import
. ./util-config.sh

echo "Running commands ($*) on $SERVER..." \
  && $PLINK_CMD $SERVER "cd $REMOTE_TARGET_PATH; $*"
