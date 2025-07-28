package com.mygdx.game;

import lombok.Data;

@Data
public class GameData {

    private int gameLevel;

    private boolean musicMute;
    private boolean soundMute;

    private int finalScore;
    private int maxFinalScore;

    public void resetGameLevel() {
        gameLevel = 0;
        finalScore = 0;
        maxFinalScore = 0;

    }

}
