FROM openjdk:14

COPY target/todo-with-cognito.jar todo-with-cognito.jar

ENTRYPOINT ["java", "-jar", "todo-with-cognito.jar"]
