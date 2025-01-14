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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Disposable;
import com.mygdx.game.entity.DrawableEntity;
import com.mygdx.game.level.LevelData;

import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;
import lombok.Setter;


public class WorldRenderer implements Disposable {

    private final SpriteBatch spriteBatch;
    private final SpriteBatch hudSpriteBatch;
    private final SpriteBatch backgroundSpriteBatch;
    private final OrthographicCamera camera;
    private final OrthographicCamera backGroundCamera;
    private final FPSLogger fpsLogger;
    private final BitmapFont font;
    private final OrthogonalTiledMapRenderer renderer;
    private final AtomicBoolean renderDebug;
    private final ShapeRenderer debugRenderer;

    @Getter
    @Setter
    private boolean lerpCamera = false;

    public WorldRenderer(LevelData levelData, AtomicBoolean renderDebug) {
        spriteBatch = new SpriteBatch();
        backgroundSpriteBatch = new SpriteBatch();
        hudSpriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 400, 200);
        camera.position.x = levelData.getEndRectangle().x;
        camera.position.y = levelData.getEndRectangle().y;
        backGroundCamera = new OrthographicCamera();
        backGroundCamera.setToOrtho(false, 1000, 500);
        fpsLogger = new FPSLogger();
        font = new BitmapFont();
        font.getData().setScale(2);
        renderer = new OrthogonalTiledMapRenderer(levelData.getTerrain(), 1);
        this.renderDebug = renderDebug;
        debugRenderer = new ShapeRenderer();
    }

    public void render(float delta, LevelData levelData) {
        ScreenUtils.clear(0, 0, 0f, 1);
        camera.update();

        backgroundSpriteBatch.setProjectionMatrix(backGroundCamera.combined);

        renderBackGround(levelData);
        renderGameMap();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        for (DrawableEntity entity : levelData.getAllDrawEntities()) {
            entity.render(spriteBatch);
        }
        spriteBatch.end();

        if(renderDebug.get()){
            renderDebug(levelData);
        }

        fpsLogger.log();
    }

    private void renderDebug (LevelData levelData) {
        debugRenderer.setProjectionMatrix(camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);

        debugRenderer.setColor(Color.RED);
        Rectangle bear = levelData.getBear().getPosition();
        debugRenderer.rect(bear.x, bear.y, bear.width, bear.height);
        Rectangle cat = levelData.getCat().getPosition();
        debugRenderer.setColor(Color.BLUE);
        debugRenderer.rect(cat.x, cat.y, cat.width, cat.height);

        debugRenderer.end();
    }

    public void renderScore(LevelData levelData) {
        hudSpriteBatch.setProjectionMatrix(backGroundCamera.combined);
        hudSpriteBatch.begin();
        font.draw(hudSpriteBatch, "Stars: " + levelData.getScore() + " / " + levelData.getStarsCount(), 50, 450, 50, Align.left, false);
        hudSpriteBatch.end();
    }

    private void renderGameMap() {
        renderer.setView(camera);
        renderer.render();
    }

    private void renderBackGround(LevelData levelData) {
        int backGroundOffset = (int) (camera.position.x / 100);
        int frontBackGroundOffset = (int) (camera.position.x / 25);
        backgroundSpriteBatch.begin();
        backgroundSpriteBatch.draw(levelData.getBackGround(), 0, 0, 0, 0, 1000, 500, 1, 1, 0, backGroundOffset, 0, 500, 200, false, false);
        backgroundSpriteBatch.draw(levelData.getFrontBackGround(), 0, 0, 0, 0, 1000, 500, 1, 1, 0, frontBackGroundOffset, 0, 500, 200, false, false);
        backgroundSpriteBatch.end();
    }

    public Vector3 getCameraPosition(){
        return camera.position;
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundSpriteBatch.dispose();
    }
}
