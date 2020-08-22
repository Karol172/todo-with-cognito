./mvnw clean package

cd ./terraform/main/ && terraform init && terraform apply -auto-approve

COGNITO_DOMAIN=$(terraform output | grep urlRoot | cut -d"=" -f2 | cut -d" " -f2)
CLIENT_ID=$(terraform output | grep clientId | cut -d"=" -f2 | cut -d" " -f2)
CLIENT_SECRET=$(terraform output | grep clientSecret | cut -d"=" -f2 | cut -d" " -f2)
AWS_REGION=$(terraform output | grep region | cut -d"=" -f2 | cut -d" " -f2)
POOL_ID=$(terraform output | grep poolId | cut -d"=" -f2 | cut -d" " -f2)

cd ../../
touch spring-variables.env

echo "COGNITO_DOMAIN=$COGNITO_DOMAIN" >> spring-variables.env
echo "COGNITO_CLIENT_ID=$CLIENT_ID" >> spring-variables.env
echo "COGNITO_CLIENT_SECRET=$CLIENT_SECRET" >> spring-variables.env
echo "AWS_REGION=$AWS_REGION" >> spring-variables.env
echo "COGNITO_POOL_ID=$POOL_ID" >> spring-variables.env

docker-compose up --build
