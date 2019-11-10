#!/bin/bash

RUN_ID=$(date +"%s%3N")
JOB_PARAMS+=("run.id(long)=${RUN_ID}")

function help {
  cat <<EOM
usage: $(basename "${0}") [-h] [-o <options>] [-d <month>] [-y <year>]
  where command is one of the following:
    -s <name>       Enter a name to skip (e.G. Meier)
    -f <name>       Enter a name to filter (e.G. Schmitz)
    -d <month>      Enter a month as Job param
    -y <year>       Enter a year as Job param
EOM
  exit 1
}

while getopts "hs:f:d:y:" opt; do
  case "${opt}" in
  h) help
    ;;
  s) JOB_PARAMS+=("skip=${OPTARG}")
    ;;
  f) JOB_PARAMS+=("filter=${OPTARG}")
    ;;
  d) JOB_PARAMS+=("month=${OPTARG}")
    ;;
  y) JOB_PARAMS+=("year=${OPTARG}")
    ;;
  *) help
    ;;
  esac
done

echo "runId: ${RUN_ID}"
echo "jobParams: ${JOB_PARAMS[*]}"

docker run -it --rm guideline-springboot-batch --spring.batch.job.names="userImportJob" "${JOB_PARAMS[@]}"

JOB_RET=$?
echo "Job returns exit code ${JOB_RET}"
exit ${JOB_RET}
