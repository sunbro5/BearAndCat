package cz.mares.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import cz.mares.game.level.LevelData;
import cz.mares.game.level.LevelLoader;
import cz.mares.game.screens.BeforeLevelScreen;
import cz.mares.game.screens.InstructionScreen;
import cz.mares.game.screens.IntroScreen;
import cz.mares.game.screens.LevelScreen;
import cz.mares.game.screens.MainMenuScreen;
import cz.mares.game.screens.ScreenType;
import cz.mares.game.screens.SettingsScreen;
import cz.mares.game.screens.TypedScreen;
import cz.mares.game.screens.WinnerScreen;
import cz.mares.game.sound.MusicPlayer;

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

    private Json json = new Json();

    @Override
    public void render() {
        super.render();
    }

    public void setScreenSafe(ScreenType screenType) {
        setScreen(screens.get(screenType));
    }

    public void create() {

        this.gameData = tryLoadGameData();
        this.assetsLoader = new AssetsLoader(gameData);
        this.levelLoader = new LevelLoader(assetsLoader);
        this.musicPlayer = new MusicPlayer(gameData);
        screens.put(ScreenType.MAIN_MENU, new MainMenuScreen(this));
        screens.put(ScreenType.SETTINGS, new SettingsScreen(this));
        screens.put(ScreenType.INTRO, new IntroScreen(this));
        screens.put(ScreenType.BEFORE_LEVEL, new BeforeLevelScreen(this));
        screens.put(ScreenType.INSTRUCTION, new InstructionScreen(this));
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
        saveGameData();
        return true;
    }

    public void muteMusic(){
        gameData.setMusicVolume(0f);
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

    public void saveGameData(){
        FileHandle file = Gdx.files.local("save.json");
        String text = json.toJson(gameData);
        file.writeString(text, false);
    }

    public GameData tryLoadGameData(){
        try {
            FileHandle file = Gdx.files.local("save.json");
            String text = file.readString();
            return json.fromJson(GameData.class, text);
        } catch (Exception e){
            Gdx.app.log("", "Cannot read save ", e);
            return new GameData();
        }
    }

}
