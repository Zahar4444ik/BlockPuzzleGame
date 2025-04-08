package sk.tuke.gamestudio.server.webservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.DTO.CommentDTO;
import sk.tuke.gamestudio.DTO.PlayerDTO;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.PlayerService;

import java.util.List;

@RestController
@RequestMapping("api/comment")
public class CommentServiceRest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PlayerService playerService;

    @GetMapping("/{game}")
    public List<Comment> getComments(@PathVariable String game) {
        return commentService.getComments(game);
    }

    @PostMapping
    public void addComment(@RequestBody CommentDTO commentDTO) {
        PlayerDTO playerDTO = commentDTO.getPlayerDTO();

        if(playerDTO != null && this.playerService.playerExists(playerDTO.getNickname())) {
            var player = new Player(playerDTO.getNickname(), playerDTO.getPassword());
            player.setLastPlayed(playerDTO.getLastPlayed());
            player.setScore(playerDTO.getScore());
            player.setGamesPlayed(playerDTO.getGamesPlayed());
            var comment = new Comment(commentDTO.getGame(), player, commentDTO.getComment(), commentDTO.getCommentedOn());
            commentService.addComment(comment);
        }
        else {
            throw new IllegalArgumentException("Player does not exist");
        }
    }

}
