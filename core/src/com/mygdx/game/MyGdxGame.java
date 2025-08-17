package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.level.LevelLoader;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.sound.MusicPlayer;

import lombok.Getter;

public class MyGdxGame extends Game {

    @Getter
    private GameData gameData;

    private LevelLoader levelLoader;

    @Getter
    private AssetsLoader assetsLoader;

    @Getter
    private MusicPlayer musicPlayer;

    public void create() {
        this.gameData = new GameData();
        this.assetsLoader = new AssetsLoader(gameData);
        this.levelLoader = new LevelLoader(assetsLoader);
        this.musicPlayer = new MusicPlayer(gameData);
        this.setScreen(new MainMenuScreen(this));
    }

    public LevelData getGameLevelData() {
        return levelLoader.getLevel(gameData.getGameLevel());
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

    public void render() {
        super.render();
    }

    public void dispose() {
        assetsLoader.dispose();
    }

}
