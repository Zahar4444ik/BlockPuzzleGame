package sk.tuke.gamestudio.service.restclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.DTO.CommentDTO;
import sk.tuke.gamestudio.DTO.PlayerDTO;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.exceptions.CommentException;

import java.util.Arrays;
import java.util.List;

@Service
public class CommentServiceRestClient implements CommentService {
    private final String url = "http://localhost:9090/api/comment";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addComment(Comment comment) throws CommentException {
        var player = comment.getPlayer();
        var playerDTO = new PlayerDTO(player.getNickname(), player.getScore(), player.getGamesPlayed(), player.getLastPlayed(), player.getPassword());
        var commentDTO = new CommentDTO(comment.getGame(), playerDTO, comment.getComment(), comment.getCommentedOn());
        this.restTemplate.postForEntity(this.url, commentDTO, Comment.class);
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        final var comments = this.restTemplate.getForEntity(this.url + "/" + game, Comment[].class, game).getBody();
        return Arrays.asList(comments != null ? comments : new Comment[0]);
    }

    @Override
    public void reset() throws CommentException {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
