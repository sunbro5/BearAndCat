package com.mygdx.game.utils;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.level.Level;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.screens.BeforeLevelScreen;
import com.mygdx.game.screens.IntroScreen;
import com.mygdx.game.screens.LevelScreen;

public class LevelUtils {

    public static void setLevelScreen(MyGdxGame game) {
        LevelData data = game.getGameLevelData();
        if (data.getMetadata() == Level.INTRO) {
            game.setScreen(new IntroScreen(game, data));
        } else if (data.getMetadata().isBeforeLevelScreen()) {
            game.setScreen(new BeforeLevelScreen(game));
        } else {
            game.setScreen(new LevelScreen(game, data));
        }
    }
}
