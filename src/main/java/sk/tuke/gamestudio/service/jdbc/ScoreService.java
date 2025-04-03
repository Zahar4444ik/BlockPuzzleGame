package sk.tuke.gamestudio.service.jdbc;

import sk.tuke.gamestudio.entity.Score;

import java.util.List;

public interface ScoreService {
    void addAndSetScore(Score score) throws ScoreException;
    List<Score> getTopScores(String game) throws ScoreException;
    void reset() throws ScoreException;
}
