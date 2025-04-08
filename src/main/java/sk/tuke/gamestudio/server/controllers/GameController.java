package sk.tuke.gamestudio.server.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllGames() {
        return new ResponseEntity<>("List of all games", HttpStatus.OK);
    }
}