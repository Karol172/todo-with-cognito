package com.kcymerys.java.todowithcognito;

import com.kcymerys.java.todowithcognito.utils.CognitoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({CognitoProperties.class})
public class TodoWithCognitoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoWithCognitoApplication.class, args);
    }

}
