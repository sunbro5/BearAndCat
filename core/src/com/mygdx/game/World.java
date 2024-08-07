package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.entity.Bear;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.DrawableEntity;
import com.mygdx.game.entity.EntityType;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.renderer.WorldRenderer;
import com.mygdx.game.screens.BeforeLevelScreen;
import com.mygdx.game.screens.LevelScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.WinnerScreen;

public class World implements Disposable {

    private final MyGdxGame game;
    private final WorldPhysics worldPhysics;
    private final WorldRenderer worldRenderer;
    private final LevelData levelData;


    public World(MyGdxGame game, WorldRenderer worldRenderer, LevelData levelData) {
        this.game = game;
        this.worldPhysics = new WorldPhysics(levelData);
        this.worldRenderer = worldRenderer;
        this.levelData = levelData;
    }

    public void update(float delta) {
        handleControls();
        handleFinish();
        worldPhysics.update(delta);
        for (DrawableEntity entity : levelData.getAllDrawEntities()) {
            entity.update(delta, worldPhysics);
        }

    }


    private void handleFinish() {
        if (levelData.getBear().getPosition().overlaps(levelData.getEndRectangle()) && levelData.getCat().getPosition().overlaps(levelData.getEndRectangle())) {
            if(game.incrementGameLevel()){
                game.setScreen(new BeforeLevelScreen(game));
                dispose();
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

    public ControlAbleEntity getControlEntity() {
        return levelData.getControlEntity();
    }

    public void switchControlEntity() {
        if (levelData.getControlEntity() instanceof Bear) {
            levelData.setControlEntity(levelData.getCat());
        } else {
            if (levelData.getCat().getIsOnTopOf() == EntityType.BEAR) {
                levelData.getBear().setHaveOnTop(levelData.getCat());
            }
            levelData.setControlEntity(levelData.getBear());
        }
    }

    public void render(float delta) {
        worldRenderer.render(delta, levelData);
    }


    @Override
    public void dispose() {
        worldRenderer.dispose();
    }
}
