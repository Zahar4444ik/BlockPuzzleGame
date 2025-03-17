package sk.tuke.kpi.BlockPuzzle.gamestudio.service;

import sk.tuke.kpi.BlockPuzzle.gamestudio.entity.Score;

import java.util.List;

public interface ScoreService {
    void addScore(Score score) throws ScoreException;
    List<Score> getTopScores(String game) throws ScoreException;
    void reset() throws ScoreException;
}
