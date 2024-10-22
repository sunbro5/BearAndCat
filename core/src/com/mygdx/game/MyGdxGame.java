package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.level.LevelLoader;
import com.mygdx.game.screens.MainMenuScreen;
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

    public void create() {
        this.assetsLoader = new AssetsLoader();
        this.levelLoader = new LevelLoader(assetsLoader);
        this.setScreen(new MainMenuScreen(this));
    }

    public LevelData getGameLevelData() {
        return levelLoader.getLevel(gameLevel);
    }

    public boolean levelFinished(int score, int starsCount) {
        finalScore += score;
        maxFinalScore += starsCount;
        if (gameLevel + 1 > levelLoader.getLevelSize() -1) {
            return false;
        }
        gameLevel++;
        return true;
    }

    public void resetGameLevel() {
        gameLevel = 0;
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        assetsLoader.dispose();
    }

}
