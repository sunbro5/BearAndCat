package cz.mares.game;

import com.badlogic.gdx.utils.ObjectMap;

import cz.mares.game.level.Level;
import cz.mares.game.level.LevelData;

import java.util.concurrent.atomic.AtomicBoolean;

import cz.mares.game.level.LevelScore;
import lombok.Data;

@Data
public class GameData {

    private int gameLevel;

    private float musicVolume = 1;
    boolean soundMute;

    private int finalScore;
    private int maxFinalScore;
    private transient LevelData currentLeveData;
    private final AtomicBoolean renderDebug = new AtomicBoolean(false);
    private ObjectMap<Integer, LevelScore> levelScores = new ObjectMap<>(Level.values().length);

    public void resetGameLevel() {
        gameLevel = 0;
        finalScore = 0;
        maxFinalScore = 0;
        levelScores = new ObjectMap<>(Level.values().length);
    }

}
