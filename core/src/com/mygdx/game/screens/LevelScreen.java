package com.mygdx.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.EndWalk;
import com.mygdx.game.entity.Bear;
import com.mygdx.game.entity.Cat;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.renderer.WorldRenderer;
import com.mygdx.game.sound.SoundPlayer;
import com.mygdx.game.utils.LevelUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class LevelScreen implements Screen {

    private final MyGdxGame game;
    private final WorldPhysics worldPhysics;
    private final WorldRenderer worldRenderer;
    private final LevelData levelData;
    private final AtomicBoolean renderDebug = new AtomicBoolean(false);

    public LevelScreen(MyGdxGame game, LevelData levelData) {
        this.game = game;
        this.worldPhysics = new WorldPhysics(levelData);
        this.worldRenderer = new WorldRenderer(levelData, renderDebug, game.getAssetsLoader());
        this.levelData = levelData;
    }

    @Override
    public void show() {
        game.getMusicPlayer().next();
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
            dispose();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            LevelUtils.setLevelScreen(game);
            dispose();
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

    private void handleEnd() {
        if (levelData.getBear().getStates().containsKey(BehaviorType.END_WALK) &&
                levelData.getCat().getStates().containsKey(BehaviorType.END_WALK) &&
                levelData.getBear().getStates().get(BehaviorType.END_WALK).isFinished() &&
                levelData.getCat().getStates().get(BehaviorType.END_WALK).isFinished()
        ) {
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
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            levelData.getControlEntity().move(ControlAbleEntity.Move.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            levelData.getControlEntity().move(ControlAbleEntity.Move.RIGHT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            levelData.getControlEntity().jump();
        }
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
