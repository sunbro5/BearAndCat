package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.level.LevelLoader;
import com.mygdx.game.screens.MainMenuScreen;

import lombok.Getter;

public class MyGdxGame extends Game {

    @Getter
    private int gameLevel = 0;

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

    public boolean incrementGameLevel() {
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
