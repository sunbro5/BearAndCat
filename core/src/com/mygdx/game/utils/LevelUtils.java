package com.mygdx.game.utils;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.level.Level;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.screens.BeforeLevelScreen;
import com.mygdx.game.screens.IntroScreen;
import com.mygdx.game.screens.LevelScreen;
import com.mygdx.game.screens.ScreenType;

public class LevelUtils {

    public static void setLevelScreen(MyGdxGame game) {
        LevelData data = game.loadLevelData();
        if (data.getMetadata() == Level.INTRO) {
            game.setScreenSafe(ScreenType.INTRO);
        } else if (data.getMetadata().isBeforeLevelScreen()) {
            game.setScreenSafe(ScreenType.BEFORE_LEVEL);
        } else {
            game.setScreenSafe(ScreenType.LEVEL);
        }
    }

    public static void continueLevelScreen(MyGdxGame game) {
        LevelData levelData = game.getGameData().getCurrentLeveData();
        if (levelData != null) {
            game.setScreenSafe(ScreenType.LEVEL);
        } else {
            setLevelScreen(game);
        }
    }
}
