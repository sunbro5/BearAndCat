package com.mygdx.game.renderer;

import static com.mygdx.game.level.LevelLoader.TILE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Disposable;
import com.mygdx.game.entity.DrawableEntity;
import com.mygdx.game.level.LevelData;

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

    @Getter
    @Setter
    private boolean lerpCamera = false;

    public WorldRenderer(Rectangle startPosition) {
        spriteBatch = new SpriteBatch();
        backgroundSpriteBatch = new SpriteBatch();
        hudSpriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 500);
        camera.position.x = startPosition.x;
        camera.position.y = startPosition.y;
        backGroundCamera = new OrthographicCamera();
        backGroundCamera.setToOrtho(false, 1000, 500);
        fpsLogger = new FPSLogger();
        font = new BitmapFont();
        font.getData().setScale(2);
    }

    public void render(float delta, LevelData levelData) {
        ScreenUtils.clear(0, 0, 0f, 1);
        camera.update();

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

    public void renderScore(LevelData levelData) {
        hudSpriteBatch.setProjectionMatrix(backGroundCamera.combined);
        hudSpriteBatch.begin();
        font.draw(hudSpriteBatch, "Stars: " + levelData.getScore() + " / " + levelData.getStarsCount(), 50, 450, 50, Align.left, false);
        hudSpriteBatch.end();
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
