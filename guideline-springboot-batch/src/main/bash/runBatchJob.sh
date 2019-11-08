#!/bin/bash

RUN_ID=$(date +"%Y-%m-%d %H:%M:%S")
JOB_PARAMS+=("run.id=\"${RUN_ID}\"")

function help {
  cat <<EOM
usage: $(basename "${0}") [-h] [-o <options>] [-d <month>] [-y <year>]
  where command is one of the following:
    -o <options>    Options (such as autoLogin|autoLogOut)
    -d <month>      Enter a month as Job param
    -y <year>       Enter a year as Job param
EOM
  exit 1
}

while getopts "ho:d:y:" opt; do
  case "${opt}" in
  h) help
    ;;
  o) JOB_PARAMS+=("options=\"${OPTARG}\"")
    ;;
  d) JOB_PARAMS+=("month=\"${OPTARG}\"")
    ;;
  y) JOB_PARAMS+=("year=\"${OPTARG}\"")
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
