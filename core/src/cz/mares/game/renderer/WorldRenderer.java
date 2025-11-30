package cz.mares.game.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import cz.mares.game.AssetsLoader;
import cz.mares.game.Disposable;
import cz.mares.game.entity.ActionEntity;
import cz.mares.game.entity.DrawableEntity;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.entity.PickAbleEntity;
import cz.mares.game.level.LevelData;

import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;
import lombok.Setter;


public class WorldRenderer implements Disposable {

    private final SpriteBatch spriteBatch;
    private final SpriteBatch backgroundSpriteBatch;
    private final OrthographicCamera camera;
    private final OrthographicCamera backGroundCamera;
    private final OrthographicCamera uiCamera;
    private final FPSLogger fpsLogger;
    private final AtomicBoolean renderDebug;
    private final ShapeRenderer debugRenderer;

    private final ShapeRenderer shapeRenderer;
    private final Viewport viewport;
    private final Viewport backgroundViewport;

    private final Viewport uiViewport;
    @Getter
    private final Stage uiStage;
    private final Label label;

    private final BitmapFont font;

    private final SpriteBatch debugBatch;

    @Getter
    @Setter
    private boolean lerpCamera = false;

    public WorldRenderer(AtomicBoolean renderDebug, AssetsLoader assetsLoader) {
        spriteBatch = new SpriteBatch();
        backgroundSpriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        backGroundCamera = new OrthographicCamera();
        backGroundCamera.setToOrtho(false, 2000, 1000);
        fpsLogger = new FPSLogger();

        this.renderDebug = renderDebug;
        debugRenderer = new ShapeRenderer();
        shapeRenderer = new ShapeRenderer();
        // TODO terrain flickering is probably because of scaling of map
        viewport = new ExtendViewport(400, 200, camera);
        viewport.apply();
        backgroundViewport = new FitViewport(2000, 1000, backGroundCamera);
        backgroundViewport.apply();

        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(2000, 1000, uiCamera);
        uiViewport.apply();

        uiStage = new Stage(uiViewport);
        label = new Label("", assetsLoader.getSkin());
        label.setFontScale(3f);
        label.setPosition(50, 900);
        uiStage.addActor(label);

        font = new BitmapFont();
        font.setColor(Color.GOLD);
        font.getData().setScale(2f);
        debugBatch = new SpriteBatch();
    }

    public void setLevelData(LevelData levelData){
        camera.position.x = levelData.getCat().getPosition().x;
        camera.position.y = levelData.getCat().getPosition().y;
    }

    public void render(float delta, LevelData levelData) {
        ScreenUtils.clear(255, 255, 255, 1);
        camera.update();
        viewport.apply();

        backgroundSpriteBatch.setProjectionMatrix(backGroundCamera.combined);

        renderBackGround(levelData);
        renderGameMap(levelData);

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        for (DrawableEntity entity : levelData.getAllDrawEntities()) {
            entity.render(spriteBatch);
        }
        spriteBatch.end();

        if (renderDebug.get()) {
            renderDebug(levelData);
        }
        renderScore(levelData);
        renderFade(levelData);

    }

    public void renderIntro(float delta, LevelData levelData, float barHeight) {
        ScreenUtils.clear(0, 0, 0f, 1);
        camera.update();
        viewport.apply();

        backgroundSpriteBatch.setProjectionMatrix(backGroundCamera.combined);

        renderBackGround(levelData);
        renderGameMap(levelData);

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        for (DrawableEntity entity : levelData.getAllDrawEntities()) {
            entity.render(spriteBatch);
        }
        spriteBatch.end();

        if (renderDebug.get()) {
            renderDebug(levelData);
        }

        shapeRenderer.setProjectionMatrix(uiViewport.getCamera().combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);

        shapeRenderer.rect(0, 1000 - barHeight, 2000, barHeight);

        shapeRenderer.rect(0, 0, 2000, barHeight);

        shapeRenderer.end();


    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void renderDebug(LevelData levelData) {
        debugRenderer.setProjectionMatrix(camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);

        debugRenderer.setColor(Color.RED);
        Rectangle bear = levelData.getBear().getPosition();
        debugRenderer.rect(bear.x, bear.y, bear.width, bear.height);
        Rectangle cat = levelData.getCat().getPosition();
        debugRenderer.setColor(Color.BLUE);
        debugRenderer.rect(cat.x, cat.y, cat.width, cat.height);
        Rectangle end = levelData.getEndRectangle();
        debugRenderer.setColor(Color.YELLOW);
        debugRenderer.rect(end.x, end.y, end.width, end.height);
        for (PickAbleEntity entity : levelData.getPickAbleEntities()) {
            debugRenderer.setColor(Color.GOLD);
            debugRenderer.rect(entity.getPosition().x, entity.getPosition().y, entity.getPosition().width, entity.getPosition().height);
        }
        for (MoveAbleEntity entity : levelData.getMoveAbleEntities()) {
            debugRenderer.setColor(Color.GRAY);
            debugRenderer.rect(entity.getPosition().x, entity.getPosition().y, entity.getPosition().width, entity.getPosition().height);
        }
        for (ActionEntity entity : levelData.getActionEntities()) {
            debugRenderer.setColor(Color.CYAN);
            debugRenderer.rect(entity.getPosition().x, entity.getPosition().y, entity.getPosition().width, entity.getPosition().height);
        }
        fpsLogger.log();
        debugRenderer.end();

        debugBatch.setProjectionMatrix(uiCamera.combined);
        debugBatch.begin();
        font.draw(debugBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 1700, 500);

        debugBatch.end();
    }

    public void renderFade(LevelData levelData) {
        float fadeOverlay = levelData.getFadeOverlay();
        if (fadeOverlay > 0.001) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.setProjectionMatrix(uiViewport.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, fadeOverlay);
            shapeRenderer.rect(0, 0, 2000, 1000);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    public void renderScore(LevelData levelData) {
        label.setText("Stars: " + levelData.getScore() + " / " + levelData.getStarsCount());
        uiStage.act();
        uiStage.draw();
    }

    private void renderGameMap(LevelData levelData) {
        levelData.getMapRenderer().setView(camera);
        levelData.getMapRenderer().render();
    }

    private void renderBackGround(LevelData levelData) {
        int backGroundOffset = (int) (camera.position.x / 100);
        int frontBackGroundOffset = (int) (camera.position.x / 15);
        backgroundSpriteBatch.begin();
        backgroundSpriteBatch.draw(levelData.getBackGround(), 0, 0, 0, 0, 2000, 1000, 1, 1, 0, backGroundOffset, 0, 500, 200, false, false);
        backgroundSpriteBatch.draw(levelData.getFrontBackGround(), 0, 0, 0, 0, 2000, 1000, 1, 1, 0, frontBackGroundOffset, 0, 500, 200, false, false);
        backgroundSpriteBatch.end();
    }

    public Vector3 getCameraPosition() {
        return camera.position;
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundSpriteBatch.dispose();
        uiStage.dispose();
        debugBatch.dispose();
        debugRenderer.dispose();
        shapeRenderer.dispose();

    }
}
