#!/bin/bash

function help {
  cat <<EOM
usage: $(basename "${0}") -h
EOM
  exit 1
}

while getopts "ho:d:y:" opt; do
  case "${opt}" in
  h) help
    ;;
  *) help
    ;;
  esac
done

docker run -it --rm guideline-springboot-app-core

APP_RET=$?
echo "App returns exit code ${APP_RET}"
exit ${APP_RET}
