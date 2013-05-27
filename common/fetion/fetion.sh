#!/bin/sh

if [ $# -eq 0 ] ; then
  cat << EOF >&2
Usage: $0 mobile passwd to msgcontent
EOF
  exit 1
fi

FETION_MOBILE=$1
FETION_PWD=$2
FETION_TO=$3
MSG="$4"
BASE=`dirname $0`

export LD_LIBRARY_PATH=$BASE/lib:$LD_LIBRARY_PATH
$BASE/fetion --mobile=$FETION_MOBILE --pwd=$FETION_PWD --to=$FETION_TO --msg-utf8="$MSG" --msg-type=1 --exit-on-verifycode
