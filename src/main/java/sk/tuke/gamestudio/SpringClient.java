package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.game.BlockPuzzle.consoleui.BlockPuzzleConsole;

@SpringBootApplication
@Configuration
public class SpringClient {
    public static void main(String[] args) {
        SpringApplication.run(SpringClient.class, args);
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
}