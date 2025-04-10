package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.BlockPuzzleConsole;


@SpringBootApplication
@Configuration
@ComponentScan({"sk.tuke.gamestudio.service.restclient"})
public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(BlockPuzzleConsole game) {
        return args -> {
            game.start();
        };
    }

    @Bean
    public BlockPuzzleConsole blockPuzzleConsole() {
        return new BlockPuzzleConsole();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}