docker-compose down
rm spring-variables.env
cd ./terraform/main/ && terraform init && terraform destroy -auto-approve
