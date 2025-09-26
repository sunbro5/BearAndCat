package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.level.LevelLoader;
import com.mygdx.game.screens.BeforeLevelScreen;
import com.mygdx.game.screens.IntroScreen;
import com.mygdx.game.screens.LevelScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.ScreenType;
import com.mygdx.game.screens.SettingsScreen;
import com.mygdx.game.screens.TypedScreen;
import com.mygdx.game.screens.WinnerScreen;
import com.mygdx.game.sound.MusicPlayer;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class MyGdxGame extends Game {

    @Getter
    private GameData gameData;

    private LevelLoader levelLoader;

    @Getter
    private AssetsLoader assetsLoader;

    @Getter
    private MusicPlayer musicPlayer;

    private Map<ScreenType, TypedScreen> screens = new HashMap<>();

    @Override
    public void render() {
        super.render();
    }

    public void setScreenSafe(ScreenType screenType) {
        setScreen(screens.get(screenType));
    }

    public void create() {
        this.gameData = new GameData();
        this.assetsLoader = new AssetsLoader(gameData);
        this.levelLoader = new LevelLoader(assetsLoader);
        this.musicPlayer = new MusicPlayer(gameData);
        screens.put(ScreenType.MAIN_MENU, new MainMenuScreen(this));
        screens.put(ScreenType.SETTINGS, new SettingsScreen(this));
        screens.put(ScreenType.INTRO, new IntroScreen(this));
        screens.put(ScreenType.BEFORE_LEVEL, new BeforeLevelScreen(this));
        screens.put(ScreenType.LEVEL, new LevelScreen(this));
        screens.put(ScreenType.WIN, new WinnerScreen(this));

        setScreenSafe(ScreenType.MAIN_MENU);
    }

    public LevelData loadLevelData() {
        LevelData levelData = levelLoader.getLevel(gameData.getGameLevel());
        gameData.setCurrentLeveData(levelData);
        return levelData;
    }

    public boolean levelFinished(int score, int starsCount) {
        gameData.setFinalScore(gameData.getFinalScore() + score);
        gameData.setMaxFinalScore(gameData.getMaxFinalScore() + starsCount);
        if (gameData.getGameLevel() + 1 > levelLoader.getLevelSize() - 1) {
            return false;
        }
        gameData.setGameLevel(gameData.getGameLevel() + 1);
        return true;
    }

    public void toggleMuteMusic(){
        if(gameData.isMusicMute()){
            unMuteMusic();
        } else {
            muteMusic();
        }
    }

    public void muteMusic(){
        gameData.setMusicMute(true);
        musicPlayer.mute();
    }

    public void unMuteMusic(){
        gameData.setMusicMute(false);
        musicPlayer.unMute();
    }

    public void toggleMuteSound(){
        if(gameData.isSoundMute()){
            unMuteSound();
        } else {
            muteSound();
        }
    }

    public void muteSound(){
        gameData.setSoundMute(true);
    } 

    public void unMuteSound(){
        gameData.setSoundMute(false);
    }

    public void resetGameLevel() {
        gameData.resetGameLevel();
    }

    public void dispose() {
        assetsLoader.dispose();
        for (TypedScreen screen: screens.values()) {
            screen.dispose();
        }
    }

}
