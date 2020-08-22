# TODO List
## Description
TODO List is a simple RESTful Web Service, which provides user's tasks management (create, update, change status, review, paging and delete tasks). App uses AWS Cognito for user authentication. Also there is group of users (admin), which members are able to review tasks of others user.

## Instruction of run

### Requirements
- [OpenJDK 14](https://jdk.java.net/14/)
- [Maven](https://maven.apache.org/)
- [Docker](https://docs.docker.com/docker-for-windows/install/)
- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)
- [Terraform](https://www.terraform.io/downloads.html)

1. Open system terminal.
2. Clone this repository and go into project's root directory.
3. Add executable permission for files `build.sh` and `destroy.sh`. Eg. `sudo chmod 777 build.sh destroy.sh`
4. Run script `./build`. This script:
    - builds project and create jar file
    - creates AWS Cognito on AWS Cloud using terraform
    - runs Postgres database and this app in docker containers
5. For stopping app, hit Ctrl + C and execute script `./destroy.sh`
* Author has provided launching script only for Unix OS.

## API Doc
For retrieving API Doc go to: `http://localhost:8080/swagger-ui.html`, when app is running.

# Technology stack
- Spring Boot
- Spring Data JPA
- Spring Security
- Postgres
- Liquibase
- Swagger
- AWS Cognito
- Terraform
- Docker
