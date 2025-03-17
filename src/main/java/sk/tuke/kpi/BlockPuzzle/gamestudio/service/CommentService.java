package sk.tuke.kpi.BlockPuzzle.gamestudio.service;

import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment) throws CommentException;
    List<Comment> getComments(String game) throws CommentException;
    void reset() throws CommentException;
}
