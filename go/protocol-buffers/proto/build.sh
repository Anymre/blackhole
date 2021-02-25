#!/usr/bin/env sh
DIR=$(cd "$(dirname "$0")" && pwd)
echo "$DIR"

PLUGIN_DIR=${DIR}/proto
OUT_DIR=.

for f in "${PLUGIN_DIR}"/*.proto
do
  echo "$f"
  protoc "${PLUGIN_DIR}${f}" --go_out=${OUT_DIR}
done
