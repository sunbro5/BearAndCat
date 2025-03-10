package com.mygdx.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entity.Bear;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.renderer.WorldRenderer;

import java.util.concurrent.atomic.AtomicBoolean;

public class IntroScreen implements Screen {

    private final MyGdxGame game;
    private final WorldPhysics worldPhysics;
    private final WorldRenderer worldRenderer;
    private final LevelData levelData;
    public IntroScreen(MyGdxGame game, LevelData levelData) {
        this.game = game;
        this.worldPhysics = new WorldPhysics(levelData);
        this.worldRenderer = new WorldRenderer(levelData, new AtomicBoolean(false), game.getAssetsLoader());
        this.levelData = levelData;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        worldPhysics.update(delta);
        worldRenderer.getCameraPosition().lerp(levelData.getControlEntity().getCameraPositionVector(), 10 * delta);

        //render
        worldRenderer.render(delta, levelData);
        handleFinish();
        handleControls();
    }

    private void handleFinish() {
        if (levelData.getBear().getPosition().overlaps(levelData.getEndRectangle()) && levelData.getCat().getPosition().overlaps(levelData.getEndRectangle())) {
            if (game.levelFinished(levelData.getScore(), levelData.getStarsCount())) {
                game.setScreen(new BeforeLevelScreen(game));
            } else {
                game.setScreen(new WinnerScreen(game));
            }
            dispose();

        }
    }

    private void handleControls() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new LevelScreen(game, game.getGameLevelData()));
            dispose();
        }
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
        worldRenderer.dispose();
    }

}
