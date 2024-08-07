package com.mygdx.game.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Disposable;
import com.mygdx.game.entity.DrawableEntity;
import com.mygdx.game.level.LevelData;


public class WorldRenderer implements Disposable {

    private final SpriteBatch spriteBatch;
    private final SpriteBatch backgroundSpriteBatch;
    private final OrthographicCamera camera;
    private final OrthographicCamera backGroundCamera;
    private final FPSLogger fpsLogger;

    public WorldRenderer() {
        spriteBatch = new SpriteBatch();
        backgroundSpriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 500);
        backGroundCamera = new OrthographicCamera();
        backGroundCamera.setToOrtho(false, 1000, 500);
        fpsLogger = new FPSLogger();
    }

    public void render(float delta, LevelData levelData) {

        ScreenUtils.clear(0, 0, 0f, 1);
        camera.update();

        camera.position.lerp(levelData.getControlEntity().getCameraPositionVector(), 4f * delta);
        backgroundSpriteBatch.setProjectionMatrix(backGroundCamera.combined);

        renderBackGround(levelData);
        renderGameMap(levelData);

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        for (DrawableEntity entity : levelData.getAllDrawEntities()) {
            entity.render(spriteBatch);
        }
        spriteBatch.end();
        fpsLogger.log();
    }

    private void renderGameMap(LevelData levelData) {
        SpriteCache cache = levelData.getMapCache();
        int[][] cacheBlocks = levelData.getCacheBlocks();

        cache.setProjectionMatrix(camera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        cache.begin();
        for (int blockY = 0; blockY < cacheBlocks[0].length; blockY++) { // TODO draw only what is needed
            for (int blockX = 0; blockX < cacheBlocks.length; blockX++) {
                cache.draw(cacheBlocks[blockX][blockY]);
            }
        }
        cache.end();
    }

    private void renderBackGround(LevelData levelData) {
        int backGroundOffset = (int) (camera.position.x / 100);
        int frontBackGroundOffset = (int) (camera.position.x / 25);
        backgroundSpriteBatch.begin();
        backgroundSpriteBatch.draw(levelData.getBackGround(), 0, 0, 0, 0, 1000, 500,1,1,0,backGroundOffset,0,500,200,false,false);
        backgroundSpriteBatch.draw(levelData.getFrontBackGround(), 0, 0, 0, 0, 1000, 500,1,1,0,frontBackGroundOffset,0,500,200,false,false);
        backgroundSpriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundSpriteBatch.dispose();
    }
}
