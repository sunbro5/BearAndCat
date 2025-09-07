package com.mygdx.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.EndWalk;
import com.mygdx.game.entity.Bear;
import com.mygdx.game.entity.Cat;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.renderer.WorldRenderer;
import com.mygdx.game.sound.SoundPlayer;
import com.mygdx.game.utils.CirclePauseButton;
import com.mygdx.game.utils.CircleSwitchButton;
import com.mygdx.game.utils.CircleSwitchButton2;
import com.mygdx.game.utils.LevelUtils;
import com.mygdx.game.utils.ScreenInputProcessor;

import java.util.concurrent.atomic.AtomicBoolean;

public class LevelScreen implements Screen {

    private final MyGdxGame game;
    private final WorldPhysics worldPhysics;
    private final WorldRenderer worldRenderer;
    private final LevelData levelData;
    private final AtomicBoolean renderDebug = new AtomicBoolean(false);
    private final ScreenInputProcessor screenInputProcessor;

    public LevelScreen(MyGdxGame game, LevelData levelData) {
        this.game = game;
        this.worldPhysics = new WorldPhysics(levelData);
        this.worldRenderer = new WorldRenderer(levelData, renderDebug, game.getAssetsLoader());
        this.levelData = levelData;
        this.screenInputProcessor = new ScreenInputProcessor(Gdx.graphics.getWidth(), handleJump());

    }

    @Override
    public void show() {
        game.getMusicPlayer().next();

        // Tlačítko menu
        CirclePauseButton menuBtn = new CirclePauseButton(100, 700, 80, () -> {
            game.setScreen(new MainMenuScreen(game));
            Gdx.app.postRunnable(this::dispose);
        }, () -> {
            LevelUtils.setLevelScreen(game);
            Gdx.app.postRunnable(this::dispose);
        });

        // Tlačítko změna postavy
        CircleSwitchButton changeBtn = new CircleSwitchButton(1700, 700, 80);
        changeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                event.stop();
                switchControlEntity();
            }
        });

        worldRenderer.getUiStage().addActor(menuBtn);
        worldRenderer.getUiStage().addActor(changeBtn);

        // Multiplexer - spojuje Stage (UI) a náš input
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(worldRenderer.getUiStage());     // nejdřív UI
        multiplexer.addProcessor(screenInputProcessor); // pak herní ovládání
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        //update
        handleControls();
        worldPhysics.update(delta);
        worldRenderer.getCameraPosition().lerp(levelData.getControlEntity().getCameraPositionVector(), 10 * delta);

        //render
        worldRenderer.render(delta, levelData);
        handleFinish();
        handleRestartKeys();
    }

    private void handleRestartKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.game.setScreen(new MainMenuScreen(game));
            Gdx.app.postRunnable(() -> {
                dispose();
            });
            dispose();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            LevelUtils.setLevelScreen(game);
            Gdx.app.postRunnable(() -> {
                dispose();
            });
        }
    }

    private void handleFinish() {
        if (levelData.getBear().getPosition().overlaps(levelData.getEndRectangle()) && levelData.getCat().getPosition().overlaps(levelData.getEndRectangle())) {
            if (!levelData.getCat().getStates().containsKey(BehaviorType.END_WALK)) {
                levelData.getCat().setHaveControl(false);
                levelData.getCat().setState(new EndWalk(true, levelData));
            }
            if (!levelData.getBear().getStates().containsKey(BehaviorType.END_WALK)) {
                levelData.getBear().setHaveControl(false);
                levelData.getBear().setState(new EndWalk(false, levelData));
            }

        }
        handleEnd();
    }

    private Runnable handleJump() {
        return () -> levelData.getControlEntity().jump();
    }

    private void handleEnd() {
        if (levelData.getBear().getStates().containsKey(BehaviorType.END_WALK) && levelData.getCat().getStates().containsKey(BehaviorType.END_WALK) && levelData.getBear().getStates().get(BehaviorType.END_WALK).isFinished() && levelData.getCat().getStates().get(BehaviorType.END_WALK).isFinished()) {
            if (game.levelFinished(levelData.getScore(), levelData.getStarsCount())) {
                Gdx.app.log("", "Score : " + game.getGameData().getFinalScore() + " / " + game.getGameData().getMaxFinalScore());
                LevelUtils.setLevelScreen(game);
            } else {
                game.setScreen(new WinnerScreen(game));
            }
            dispose();
        }
    }

    private void handleControls() {
        levelData.getControlEntity().move(screenInputProcessor.getMove());

        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
            switchControlEntity();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            printEntities();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.toggleMuteMusic();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            game.toggleMuteSound();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            renderDebug.set(!renderDebug.get());
            if (renderDebug.get()) {
                Gdx.app.setLogLevel(Application.LOG_DEBUG);
            } else {
                Gdx.app.setLogLevel(Application.LOG_INFO);
            }
        }
    }

    private void printEntities() {
        Gdx.app.log("", "Cat " + levelData.getCat().getPosition().y);
        Gdx.app.log("", "Bear " + (levelData.getBear().getPosition().y + levelData.getBear().getPosition().height));
    }

    public void switchControlEntity() {
        if (someOneJumping()) {
            return;
        }
        Gdx.app.debug("", "Bear " + levelData.getBear());
        Gdx.app.debug("", "Cat " + levelData.getCat());
        if (levelData.getControlEntity() instanceof Bear && levelData.getCat().isHaveControl()) {
            levelData.setControlEntity(levelData.getCat());
        } else if (levelData.getControlEntity() instanceof Cat && levelData.getBear().isHaveControl()) {
            levelData.setControlEntity(levelData.getBear());
        }
    }

    private boolean someOneJumping() {
        return levelData.getCat().inAir() || levelData.getBear().inAir();
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
        screenInputProcessor.setScreenWidth(width);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        SoundPlayer.stopAll(levelData.getBear().getEntitySoundS());
        SoundPlayer.stopAll(levelData.getCat().getEntitySoundS());
        worldRenderer.dispose();
    }

}
