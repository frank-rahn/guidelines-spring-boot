#!/bin/bash

RUN_ID=$(date +"%Y-%m-%d %H:%M:%S")
ARGS+=("--run.id=${RUN_ID}")

function help {
  cat <<EOM
usage: $(basename "${0}") [-h] [-g] -- start app
  where:
    -h    show this help text
    -g    app get an error
EOM
  exit 1
}

while getopts "hg" opt; do
  case "${opt}" in
  h) help
    ;;
  g) ARGS+=("--go-wrong")
    ;;
  *) help
    ;;
  esac
done

echo "runId: ${RUN_ID}"
echo "args: ${ARGS[*]}"

docker run -it --rm guideline-springboot-app-core "${ARGS[@]}"

APP_RET=$?
echo "App returns exit code ${APP_RET}"
exit ${APP_RET}
