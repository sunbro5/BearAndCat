package cz.mares.game;

import com.badlogic.gdx.utils.Array;
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

    private transient LevelData currentLeveData;
    private final AtomicBoolean renderDebug = new AtomicBoolean(false);
    private Array<LevelScore> levelScores = new Array<>();

    public void resetGameLevel() {
        gameLevel = 0;
        levelScores = new Array<>();
    }

    public void setScore(LevelScore score){
        levelScores.add(score);
    }

}
