package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.Idle;
import com.mygdx.game.behavior.SlowDown;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.renderer.AnimationType;
import com.mygdx.game.renderer.WorldRenderer;
import com.mygdx.game.utils.LevelUtils;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class IntroScreen implements Screen {

    private final MyGdxGame game;
    private final WorldPhysics worldPhysics;
    private final WorldRenderer worldRenderer;
    private final LevelData levelData;

    private float sleepTime;

    private final Queue<Function<LevelData, Boolean>> states = new ArrayDeque<>();

    public IntroScreen(MyGdxGame game, LevelData levelData) {
        this.game = game;
        this.worldPhysics = new WorldPhysics(levelData);
        this.worldRenderer = new WorldRenderer(levelData, new AtomicBoolean(false), game.getAssetsLoader());
        this.levelData = levelData;

        levelData.getBear().setState(new SlowDown(2));
        levelData.getBear().setAnimation(AnimationType.SLEEP, true);
        levelData.getCat().setState(new Idle());
        levelData.getCat().setIdleValue(-1);
        worldRenderer.getCameraPosition().set(levelData.getCat().getCameraPositionVector());

        states.add(bearWait(2));
        states.add(bearMoveLeft());
        states.add(bearWait(1));
        states.add(bearMoveRight(700));
        states.add(catBearMoveRightWithCamera());
        states.add(bearMoveRight(1050));
        states.add(catBearMoveRight());
    }

    private Function<LevelData, Boolean> bearWait(float value) {
        return data -> {
            sleepTime += Gdx.graphics.getDeltaTime();
            if (sleepTime > value) {
                levelData.getBear().setAnimation(null, false);
                sleepTime = 0;
                return true;
            }
            return false;
        };
    }

    private Function<LevelData, Boolean> bearMoveLeft() {
        return data -> {
            data.getBear().move(ControlAbleEntity.Move.LEFT);
            Gdx.app.debug("", "POS " + data.getBear().getPosition().x);
            return data.getBear().getPosition().x < 450;
        };
    }

    private Function<LevelData, Boolean> bearMoveRight(int distance) {
        return data -> {
            data.getBear().move(ControlAbleEntity.Move.RIGHT);
            Gdx.app.debug("", "POS " + data.getBear().getPosition().x);
            return data.getBear().getPosition().x > distance;
        };
    }

    private Function<LevelData, Boolean> catBearMoveRightWithCamera() {
        return data -> {
            if (data.getBear().getPosition().x < 1050) {
                data.getBear().move(ControlAbleEntity.Move.RIGHT);
            }
            data.getCat().move(ControlAbleEntity.Move.RIGHT);
            worldRenderer.getCameraPosition().lerp(levelData.getCat().getCameraPositionVector(), 10 * Gdx.graphics.getDeltaTime());
            Gdx.app.debug("", "POS " + data.getCat().getPosition().x);
            return data.getCat().getPosition().x > 900;
        };
    }

    private Function<LevelData, Boolean> catBearMoveRight() {
        return data -> {
            data.getCat().move(ControlAbleEntity.Move.RIGHT);
            data.getBear().move(ControlAbleEntity.Move.RIGHT);
            Gdx.app.debug("", "POS " + data.getCat().getPosition().x);
            return data.getCat().getPosition().x > 1200;
        };
    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {

        Function<LevelData, Boolean> stateFunction = states.peek();
        if (stateFunction != null) {
            boolean stateFinished = stateFunction.apply(levelData);
            if (stateFinished) {
                states.poll();
            }

            worldPhysics.update(delta);

            levelData.getBear().getStates().get(BehaviorType.SLOW).onCollision(levelData.getCat());
            //render
            worldRenderer.renderIntro(delta, levelData);
            handleControls();
        } else {

            game.levelFinished(0, 0);
            LevelUtils.setLevelScreen(game);
            dispose();
        }
    }

    private void handleControls() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            game.levelFinished(0, 0);
            LevelUtils.setLevelScreen(game);
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
