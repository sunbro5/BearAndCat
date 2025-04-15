package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.level.LevelLoader;
import com.mygdx.game.screens.BeforeLevelScreen;
import com.mygdx.game.screens.LevelScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.sound.MusicPlayer;
import com.sun.tools.javac.util.Pair;

import java.util.List;

import lombok.Getter;

public class MyGdxGame extends Game {

    @Getter
    private int gameLevel = 0;

    @Getter
    private int finalScore;

    @Getter
    private int maxFinalScore;

    private LevelLoader levelLoader;

    @Getter
    private AssetsLoader assetsLoader;

    @Getter
    private MusicPlayer musicPlayer;

    public void create() {
        this.assetsLoader = new AssetsLoader();
        this.levelLoader = new LevelLoader(assetsLoader);
        this.setScreen(new MainMenuScreen(this));
        this.musicPlayer = new MusicPlayer();
        //musicPlayer.play();
    }

    public LevelData getGameLevelData() {
        return levelLoader.getLevel(gameLevel);
    }

    public boolean levelFinished(int score, int starsCount) {
        finalScore += score;
        maxFinalScore += starsCount;
        if (gameLevel + 1 > levelLoader.getLevelSize() - 1) {
            return false;
        }
        gameLevel++;
        return true;
    }

    public void resetGameLevel() {
        if(gameLevel != 0){
            gameLevel = 1; // no intro
            finalScore = 0;
            maxFinalScore = 0;
        }
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        assetsLoader.dispose();
    }

}
