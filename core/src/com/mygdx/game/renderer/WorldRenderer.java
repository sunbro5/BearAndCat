package com.mygdx.game.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AssetsLoader;
import com.mygdx.game.Disposable;
import com.mygdx.game.entity.ActionEntity;
import com.mygdx.game.entity.DrawableEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.entity.PickAbleEntity;
import com.mygdx.game.level.LevelData;

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
    private final OrthogonalTiledMapRenderer renderer;
    private final AtomicBoolean renderDebug;
    private final ShapeRenderer debugRenderer;
    private final Viewport viewport;
    private final Viewport backgroundViewport;

    private final Viewport uiViewport;
    private final Stage stage;
    private final Label label;

    @Getter
    @Setter
    private boolean lerpCamera = false;

    public WorldRenderer(LevelData levelData, AtomicBoolean renderDebug, AssetsLoader assetsLoader) {
        spriteBatch = new SpriteBatch();
        backgroundSpriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.position.x = levelData.getCat().getPosition().x;
        camera.position.y = levelData.getCat().getPosition().y;
        backGroundCamera = new OrthographicCamera();
        backGroundCamera.setToOrtho(false, 2000, 1000);
        fpsLogger = new FPSLogger();

        renderer = new OrthogonalTiledMapRenderer(levelData.getTerrain(), 1);
        this.renderDebug = renderDebug;
        debugRenderer = new ShapeRenderer();
        viewport = new ExtendViewport(400, 200, camera);
        viewport.apply();
        backgroundViewport = new FitViewport(2000, 1000, backGroundCamera);
        backgroundViewport.apply();

        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(2000, 1000, uiCamera);
        uiViewport.apply();

        stage = new Stage(uiViewport);
        label = new Label("", assetsLoader.getSkin());
        label.setFontScale(3f);
        label.setPosition(50, 900);
        stage.addActor(label);
    }

    public void render(float delta, LevelData levelData) {
        ScreenUtils.clear(0, 0, 0f, 1);
        camera.update();
        viewport.apply();

        backgroundSpriteBatch.setProjectionMatrix(backGroundCamera.combined);

        renderBackGround(levelData);
        renderGameMap();

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

        fpsLogger.log();
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

        debugRenderer.end();
    }

    public void renderScore(LevelData levelData) {
        label.setText("Stars: " + levelData.getScore() + " / " + levelData.getStarsCount());
        stage.act();
        stage.draw();
    }

    private void renderGameMap() {
        renderer.setView(camera);
        renderer.render();
    }

    private void renderBackGround(LevelData levelData) {
        int backGroundOffset = (int) (camera.position.x / 100);
        int frontBackGroundOffset = (int) (camera.position.x / 25);
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
        stage.dispose();
    }
}
