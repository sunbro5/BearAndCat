package cz.mares.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import cz.mares.game.MyGdxGame;
import cz.mares.game.behavior.BehaviorType;
import cz.mares.game.behavior.EndWalk;
import cz.mares.game.entity.Bear;
import cz.mares.game.entity.Cat;
import cz.mares.game.entity.ControlAbleEntity;
import cz.mares.game.level.LevelData;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.renderer.WorldRenderer;
import cz.mares.game.sound.SoundPlayer;
import cz.mares.game.utils.CirclePauseButton;
import cz.mares.game.utils.CircleSwitchButton;
import cz.mares.game.utils.LevelUtils;
import cz.mares.game.ScreenInputProcessor;

public class LevelScreen implements TypedScreen {

    private final MyGdxGame game;
    private final WorldPhysics worldPhysics;
    private final WorldRenderer worldRenderer;
    private LevelData levelData;
    private final ScreenInputProcessor screenInputProcessor;

    private boolean loaded = false;

    private float accumulator = 0f;
    public static final float STEP = 1f / 60f;

    public LevelScreen(MyGdxGame game) {
        this.game = game;
        this.worldPhysics = new WorldPhysics();
        this.worldRenderer = new WorldRenderer(game.getGameData().getRenderDebug(), game.getAssetsLoader());
        this.screenInputProcessor = new ScreenInputProcessor(Gdx.graphics.getWidth(), handleJump());
    }

    @Override
    public void show() {
        this.levelData = game.getGameData().getCurrentLeveData();
        worldPhysics.setLevelData(levelData);
        worldRenderer.setLevelData(levelData);
        game.getMusicPlayer().next();

        CirclePauseButton menuBtn = new CirclePauseButton(100, 660, 100, () -> {
            game.saveGameData();
            game.setScreenSafe(ScreenType.MAIN_MENU);
        }, () -> {
            LevelUtils.setLevelScreen(game);
        });

        CircleSwitchButton changeBtn = new CircleSwitchButton(1700, 660, 100);
        changeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                event.stop();
                switchControlEntity();
            }
        });

        worldRenderer.getUiStage().addActor(menuBtn);
        worldRenderer.getUiStage().addActor(changeBtn);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(worldRenderer.getUiStage());
        multiplexer.addProcessor(screenInputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        worldPhysics.update(STEP);
        loaded = true;
    }

    @Override
    public void render(float delta) {
        if (!loaded) {
            return;
        }
        //delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());

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
            this.game.setScreenSafe(ScreenType.MAIN_MENU);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            LevelUtils.setLevelScreen(game);
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
                game.setScreenSafe(ScreenType.WIN);
            }
        }
    }

    private void handleControls() {
        levelData.getControlEntity().move(screenInputProcessor.getMove(), screenInputProcessor.getMoveSpeedPercent());
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
            game.muteMusic();
            game.muteSound();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            game.toggleMuteSound();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            game.getGameData().getRenderDebug().set(!game.getGameData().getRenderDebug().get());
            if (game.getGameData().getRenderDebug().get()) {
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
        Gdx.input.setInputProcessor(null);
        SoundPlayer.stopAll(levelData.getBear().getEntitySoundS());
        SoundPlayer.stopAll(levelData.getCat().getEntitySoundS());
        loaded = false;
    }

    @Override
    public void dispose() {
        worldRenderer.dispose();
    }

    @Override
    public ScreenType getType() {
        return ScreenType.LEVEL;
    }
}
