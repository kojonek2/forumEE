DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd "${DIR}"

sh prepareEnv.sh &
PID=$!

mvn clean install
wait $PID #wait for .env file creation
heroku local -f Procfile.windows
