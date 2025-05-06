package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@SpringBootApplication
@Configuration
@EntityScan("sk.tuke.gamestudio.entity")
@ComponentScan({"sk.tuke.gamestudio.server", "sk.tuke.gamestudio.service.jpa"})
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GameStudioServer.class);
        application.setDefaultProperties(Collections.singletonMap("server.port", "9090"));
        application.run(args);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
