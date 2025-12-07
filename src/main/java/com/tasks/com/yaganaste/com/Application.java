package com.tasks.com.yaganaste.com;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        // Cargar archivo .env SOLO si existe (para ambiente local)
        Dotenv dotenv = Dotenv.configure()
                .directory("./")      
                .filename(".env")     
                .ignoreIfMissing()    
                .load();

        // Registrar variables para Spring Boot
        setEnvProperty("DB_URL", dotenv);
        setEnvProperty("DB_USERNAME", dotenv);
        setEnvProperty("DB_PASSWORD", dotenv);

        // Ejecutar Spring Boot
        SpringApplication.run(Application.class, args);
    }

    private static void setEnvProperty(String key, Dotenv dotenv) {
        String value = dotenv.get(key);
        if (value != null) {
            System.setProperty(key, value);
        }
    }
}
