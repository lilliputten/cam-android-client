#!/bin/sh
# @desc Pack abandoned files after system crash
# @changed 2020.04.01, 21:38
# TODO:
# - 2020.04.01, 21:39 -- Automatically detect find|find_ tar|tar_ command names

# Config import
# . ./util-config.sh

# DATE=`date "+%Y.%m.%d %H:%M:%S"`
DATETAG=`date "+%y%m%d-%H%M"`

FILE_ID="crashed-session"
LISTFILE="$DATETAG-$FILE_ID.txt"
ARCFILE="$DATETAG-$FILE_ID.tgz"
# find_ . -name ".*.sw?"  -not -path "./node_modules/*" > $LISTFILE \
find_ . -name ".*.sw?" > $LISTFILE \
  && tar_ czf $ARCFILE -T $LISTFILE --remove-files \
  && echo "Created archive $ARCFILE"
