package cz.mares.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import cz.mares.game.MyGdxGame;
import cz.mares.game.behavior.Idle;
import cz.mares.game.behavior.Slow;
import cz.mares.game.entity.ControlAbleEntity;
import cz.mares.game.level.LevelData;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.renderer.AnimationType;
import cz.mares.game.renderer.WorldRenderer;
import cz.mares.game.sound.SoundPlayer;
import cz.mares.game.utils.LevelUtils;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class IntroScreen implements TypedScreen {

    private final MyGdxGame game;
    private final WorldPhysics worldPhysics;
    private final WorldRenderer worldRenderer;
    private LevelData levelData;

    private float sleepTime;

    private final Queue<Function<LevelData, Boolean>> states = new ArrayDeque<>();

    private float barHeight = 0f;
    private float targetHeight = 180f;
    private float speed = 100f;

    private boolean loaded = false;

    public IntroScreen(MyGdxGame game) {
        this.game = game;
        this.worldPhysics = new WorldPhysics();
        this.worldRenderer = new WorldRenderer(new AtomicBoolean(false), game.getAssetsLoader());

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
        this.levelData = game.getGameData().getCurrentLeveData();
        levelData.getBear().setState(new Slow(2));
        levelData.getBear().setAnimation(AnimationType.SLEEP, true);
        levelData.getCat().setState(new Idle());
        levelData.getCat().setIdleValue(-1);
        worldRenderer.getCameraPosition().set(levelData.getCat().getCameraPositionVector());
        worldPhysics.setLevelData(game.getGameData().getCurrentLeveData());
        worldRenderer.setLevelData(game.getGameData().getCurrentLeveData());
        game.getMusicPlayer().next();
        loaded = true;
    }


    @Override
    public void render(float delta) {
        if (!loaded){
            return;
        }
        Function<LevelData, Boolean> stateFunction = states.peek();
        if (stateFunction != null) {
            boolean stateFinished = stateFunction.apply(levelData);
            if (stateFinished) {
                states.poll();
            }

            if (barHeight < targetHeight) {
                barHeight += speed * delta;
                if (barHeight > targetHeight) barHeight = targetHeight;
            }

            worldPhysics.update(delta);
            //render
            worldRenderer.renderIntro(delta, levelData, barHeight);

            handleControls();
        } else {

            game.levelFinished(0, 0);
            LevelUtils.setLevelScreen(game);
        }
    }

    private void handleControls() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            game.levelFinished(0, 0);
            LevelUtils.setLevelScreen(game);
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
        Gdx.input.setInputProcessor(null);
        loaded = false;
        SoundPlayer.stopAll(levelData.getBear().getEntitySoundS());
        SoundPlayer.stopAll(levelData.getCat().getEntitySoundS());
    }

    @Override
    public void dispose() {
        SoundPlayer.stopAll(levelData.getBear().getEntitySoundS());
        SoundPlayer.stopAll(levelData.getCat().getEntitySoundS());
        worldRenderer.dispose();
    }

    @Override
    public ScreenType getType() {
        return ScreenType.INTRO;
    }
}
