DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd "${DIR}"

DATABASE_URL=$(heroku config:get DATABASE_URL)
DATABASE_URL=${DATABASE_URL:8} #cut postgres

USER="$(cut -d':' -f2 <<<$DATABASE_URL)"
USER=${USER:2}

PASSWORD="$(cut -d':' -f3 <<<$DATABASE_URL)"
PASSWORD="$(cut -d'@' -f1 <<<$PASSWORD)"

DATABASE_URL="$(cut -d'@' -f2 <<<$DATABASE_URL)"

echo JDBC_DATABASE_URL=\'jdbc:postgresql://$DATABASE_URL?user=$USER\&password=$PASSWORD\&sslmode=require\' > .env
echo JAVA_OPTS=\' \' >> .env
mvn clean install
heroku local -f Procfile.windows
$SHELL
