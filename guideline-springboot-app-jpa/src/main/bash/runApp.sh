#!/bin/bash

RUN_ID=$(date +"%Y-%m-%d %H:%M:%S")
ARGS+=("--run.id=\"${RUN_ID}\"")

function help {
  cat <<EOM
usage: $(basename "${0}") [-h] -- start app
  where:
    -h    show this help text
EOM
  exit 1
}

while getopts "h" opt; do
  case "${opt}" in
  h) help
    ;;
  *) help
    ;;
  esac
done

echo "runId: ${RUN_ID}"
echo "args: ${ARGS[*]}"

docker run -it --rm guideline-springboot-app-jpa "${ARGS[@]}"

APP_RET=$?
echo "App returns exit code ${APP_RET}"
exit ${APP_RET}
