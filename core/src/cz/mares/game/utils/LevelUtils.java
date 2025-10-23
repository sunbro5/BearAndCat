package cz.mares.game.utils;

import cz.mares.game.MyGdxGame;
import cz.mares.game.level.Level;
import cz.mares.game.level.LevelData;
import cz.mares.game.screens.ScreenType;

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
