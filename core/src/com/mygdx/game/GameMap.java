package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.entity.Box;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class GameMap {

    public static final int TILE_SIZE = 50;
    public static final int CACHE_SIZE = 5;

    TextureRegion dirt;
    TextureRegion dirtLeft;
    TextureRegion sky;

    TextureRegion box;

    public static final int DIRT = 0x000000;
    public static final int SKY = 0xffffff;

    static int DIRT_LEFT = 0xFF0000;
    static int DIRT_RIGHT = 0xC80000;
    public static int BOX = 0x8c4600;

    int[][] mapTilesType;

    int[][] cacheBlocks;

    SpriteCache cache;

    @Getter
    private List<MoveAbleEntity> moveAbleEntities = new ArrayList<>();

    private WorldPhysics worldPhysics;
    private AssetsLoader assetsLoader;

    public GameMap(WorldPhysics worldPhysics, AssetsLoader assetsLoader) {
        this.worldPhysics = worldPhysics;
        this.assetsLoader = assetsLoader;
        loadMapTiles(assetsLoader.getPixmap());
        this.cache = new SpriteCache(this.mapTilesType.length * this.mapTilesType[0].length, false);
        this.cacheBlocks = new int[(int) Math.ceil(this.mapTilesType.length / ( (float) (CACHE_SIZE)))][(int) Math.ceil(this.mapTilesType[0].length / ( (float) (CACHE_SIZE)))];
        this.sky = new TextureRegion(assetsLoader.getTexture(AssetsLoader.TextureType.SKY),10,10,20,20);
        this.dirt = new TextureRegion(assetsLoader.getTexture(AssetsLoader.TextureType.MAP_TILES),60,186,26,26);
        this.dirtLeft = new TextureRegion(assetsLoader.getTexture(AssetsLoader.TextureType.MAP_TILES),37,186,26,26);
        Texture tileset = assetsLoader.getTexture(AssetsLoader.TextureType.MAP_TILES_2);
        this.box = new TextureRegion(tileset,16,48,16,16);
        this.dirt = new TextureRegion(tileset,16,13,16,18);
        createBlocks();
    }

    private void loadMapTiles(Pixmap pixmap) {
        mapTilesType = new int[pixmap.getWidth()][pixmap.getHeight()];
        int yReverse = 0;
        for (int y = pixmap.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int pix = (pixmap.getPixel(x, y) >>> 8) & 0xffffff;
                mapTilesType[x][yReverse] = pix;
            }
            yReverse++;
        }
    }

    private void createBlocks() {
        for (int blockY = 0; blockY < cacheBlocks[0].length; blockY++) {
            for (int blockX = 0; blockX < cacheBlocks.length; blockX++) {
                cache.beginCache();
                for (int y = blockY * CACHE_SIZE; y < blockY * CACHE_SIZE + CACHE_SIZE; y++) {
                    for (int x = blockX * CACHE_SIZE; x < blockX * CACHE_SIZE + CACHE_SIZE; x++) {
                        int screenPosX = x * TILE_SIZE;
                        int screenPosY = y * TILE_SIZE;
                        if (mapTilesType[x][y] == DIRT) {
                            cache.add(dirt, screenPosX, screenPosY, TILE_SIZE, TILE_SIZE);
                        }
                        if (mapTilesType[x][y] == DIRT_LEFT) {
                            cache.add(dirtLeft, screenPosX, screenPosY, TILE_SIZE, TILE_SIZE);
                        }
                        if (mapTilesType[x][y] == DIRT_RIGHT) {
                            cache.add(dirtLeft, screenPosX + TILE_SIZE, screenPosY, - TILE_SIZE, TILE_SIZE);
                        }
                        if (mapTilesType[x][y] == DIRT_RIGHT) {
                            cache.add(dirtLeft, screenPosX + TILE_SIZE, screenPosY, - TILE_SIZE, TILE_SIZE);
                        }
                        if (mapTilesType[x][y] == BOX) {
                            moveAbleEntities.add(new Box(screenPosX + TILE_SIZE, screenPosY, worldPhysics, box));
                        }
                    }
                }
                cacheBlocks[blockX][blockY] = cache.endCache();
            }
        }
    }

    public void update(float deltaTime) {

    }

    public void render(OrthographicCamera cam) {
        cache.setProjectionMatrix(cam.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        cache.begin();
        for (int blockY = 0; blockY < cacheBlocks[0].length; blockY++) { // TODO draw only what is needed
            for (int blockX = 0; blockX < cacheBlocks.length; blockX++) {
                cache.draw(cacheBlocks[blockX][blockY]);
            }
        }
        cache.end();

    }

    public int[][] getMapTilesType() {
        return mapTilesType;
    }


    public void dispose() {
        this.cache.dispose();
    }
}
