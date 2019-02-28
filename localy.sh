DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd "${DIR}"
heroku config -s > .env
mvn clean install
heroku local -f Procfile.windows