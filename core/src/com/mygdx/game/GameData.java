package com.mygdx.game;

import com.mygdx.game.level.LevelData;

import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Data;

@Data
public class GameData {

    private int gameLevel;

    private boolean musicMute;
    private boolean soundMute;

    private int finalScore;
    private int maxFinalScore;
    private LevelData currentLeveData;
    private final AtomicBoolean renderDebug = new AtomicBoolean(false);

    public void resetGameLevel() {
        gameLevel = 0;
        finalScore = 0;
        maxFinalScore = 0;

    }

}
