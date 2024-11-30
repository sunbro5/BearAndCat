package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entity.Bear;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.DrawableEntity;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.physics.collision.CollisionHandler;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.renderer.WorldRenderer;

public class LevelScreen implements Screen {

    private final MyGdxGame game;
    private final WorldPhysics worldPhysics;
    private final WorldRenderer worldRenderer;
    private final LevelData levelData;
    private final CollisionHandler collisionHandler;

    private float secondInit = 0;

    public LevelScreen(MyGdxGame game, LevelData levelData) {
        this.game = game;
        this.worldPhysics = new WorldPhysics(levelData);
        this.worldRenderer = new WorldRenderer(levelData.getEndRectangle());
        this.levelData = levelData;
        this.collisionHandler = new CollisionHandler();
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
        if (secondInit > 0.5f) {
            worldRenderer.getCameraPosition().lerp(levelData.getControlEntity().getCameraPositionVector(), 2f * delta);
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
    }

    public void switchControlEntity() {
        if (someOneJumping()) {
            return;
        }
        System.out.println("Bear " + levelData.getBear());
        System.out.println("Cat " + levelData.getCat());
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

    private boolean someOneJumping(){
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
