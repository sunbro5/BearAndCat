package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entity.Bear;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.DrawableEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.physics.collision.CollisionHandler;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.renderer.WorldRenderer;

import java.util.concurrent.atomic.AtomicBoolean;

public class LevelScreen implements Screen {

    private final MyGdxGame game;
    private final WorldPhysics worldPhysics;
    private final WorldRenderer worldRenderer;
    private final LevelData levelData;

    private float secondInit = 0;
    private AtomicBoolean renderDebug = new AtomicBoolean(false);

    public LevelScreen(MyGdxGame game, LevelData levelData) {
        this.game = game;
        this.worldPhysics = new WorldPhysics(levelData);
        this.worldRenderer = new WorldRenderer(levelData, renderDebug);
        this.levelData = levelData;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //update
        handleControls();
        handleFinish();
        worldPhysics.update(delta);
        for (DrawableEntity entity : levelData.getAllDrawEntities()) {
            entity.update(delta, worldPhysics);
        }
        for (DrawableEntity entity : levelData.getAllDrawEntities()) {
            entity.afterUpdate();
        }

        if (secondInit > 0.5f) { 
            worldRenderer.getCameraPosition().lerp(levelData.getControlEntity().getCameraPositionVector(), 10f * delta);
        } else {
            secondInit += delta;
        }

        //render
        worldRenderer.render(delta, levelData);
        worldRenderer.renderScore(levelData);
    }

    private void handleFinish() {
        if (levelData.getBear().getPosition().overlaps(levelData.getEndRectangle()) && levelData.getCat().getPosition().overlaps(levelData.getEndRectangle())) {
            if (game.levelFinished(levelData.getScore(), levelData.getStarsCount())) {
                game.setScreen(new BeforeLevelScreen(game));
            } else {
                game.setScreen(new WinnerScreen(game));
            }

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
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            this.game.setScreen(new MainMenuScreen(game));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
            switchControlEntity();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            printEntities();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            renderDebug.set(!renderDebug.get());
        }
    }

    private void printEntities() {
//        for (MoveAbleEntity entity : levelData.getC()) {
//            Gdx.app.log("", entity.toString());
//        }
        Gdx.app.log("", "Cat " + levelData.getCat().getPosition().y);
        Gdx.app.log("", "Bear " + (levelData.getBear().getPosition().y + levelData.getBear().getPosition().height));
    }

    public void switchControlEntity() {
        if (someOneJumping()) {
            return;
        }
        Gdx.app.log("", "Bear " + levelData.getBear());
        Gdx.app.log("", "Cat " + levelData.getCat());
        if (levelData.getControlEntity() instanceof Bear) {
            levelData.setControlEntity(levelData.getCat());
            levelData.getCat().setHaveControl(true);
            levelData.getBear().setHaveControl(false);
        } else {
            levelData.setControlEntity(levelData.getBear());
            levelData.getCat().setHaveControl(false);
            levelData.getBear().setHaveControl(true);
        }
    }

    private boolean someOneJumping() {
        return levelData.getCat().inAir() || levelData.getBear().inAir();
    }


    @Override
    public void resize(int width, int height) {

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
