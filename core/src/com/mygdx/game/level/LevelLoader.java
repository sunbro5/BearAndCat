package com.mygdx.game.level;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AssetsLoader;
import com.mygdx.game.entity.Bear;
import com.mygdx.game.entity.Box;
import com.mygdx.game.entity.Cat;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.entity.PickAbleEntity;
import com.mygdx.game.entity.Star;
import com.mygdx.game.map.TilesetType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelLoader {

    public static final int TILE_SIZE = 50;
    public static final int CACHE_SIZE = 5;
    private final List<String> levels = new ArrayList<>();
    private final AssetsLoader assetsLoader;

    public LevelLoader(AssetsLoader assetsLoader) {
        this.assetsLoader = assetsLoader;

//        levels.add("level1.png");
//        levels.add("level2.png");
//        levels.add("level3.png");
//        levels.add("level4.png");
        levels.add("level5.png");
        levels.add("level6.png");
        levels.add("level7.png");
    }

    public LevelData getLevel(int index) {
        if (index >= levels.size()) {
            return null;
        }
        System.out.println(index);
        return loadLevel(levels.get(index));
    }

    public int getLevelSize() {
        return levels.size();
    }

    private LevelData loadLevel(String name) {
        Pixmap pixmap = new Pixmap(Gdx.files.internal(name));
        Map<TilesetType, TextureRegion> mapTextures = creteMapTextures(assetsLoader.getTexture(AssetsLoader.TextureType.MAP_TILES_2));
        int[][] mapTiles = loadMapTiles(pixmap);
        pixmap.dispose();

        return buildLevel(mapTiles, mapTextures);
    }

    private Map<TilesetType, TextureRegion> creteMapTextures(Texture tileset) {
        TextureRegion[][] tmp = TextureRegion.split(tileset, tileset.getWidth() / 16, tileset.getHeight() / 8);
        Map<TilesetType, TextureRegion> textures = new HashMap<>();
        for (TilesetType type : TilesetType.values()) {
            textures.put(type, tmp[type.getTilesetY()][type.getTilesetX()]);
        }
        return Collections.unmodifiableMap(textures);
    }


    private int[][] loadMapTiles(Pixmap pixmap) {
        int[][] mapTiles = new int[pixmap.getWidth()][pixmap.getHeight()];
        int yReverse = 0;
        for (int y = pixmap.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int pix = (pixmap.getPixel(x, y) >>> 8) & 0xffffff;
                mapTiles[x][yReverse] = pix;
            }
            yReverse++;
        }
        return mapTiles;
    }

    private LevelData buildLevel(int[][] mapTiles, Map<TilesetType, TextureRegion> mapTextures) {
        SpriteCache cache = new SpriteCache(mapTiles.length * mapTiles[0].length, false);
        int[][] cacheBlocks = new int[(int) Math.ceil(mapTiles.length / ((float) (CACHE_SIZE)))][(int) Math.ceil(mapTiles[0].length / ((float) (CACHE_SIZE)))];
        ControlAbleEntity bear = null;
        ControlAbleEntity cat = null;
        List<MoveAbleEntity> moveAbleEntities = new ArrayList<>();
        List<PickAbleEntity> pickAbleEntities = new ArrayList<>();
        Rectangle endRectangle = null;
        Texture backGround = assetsLoader.getTexture(AssetsLoader.TextureType.BACKGROUND);
        backGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        Texture frontBackGround = assetsLoader.getTexture(AssetsLoader.TextureType.FRONT_BACKGROUND);
        frontBackGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

        for (int blockY = 0; blockY < cacheBlocks[0].length; blockY++) {
            for (int blockX = 0; blockX < cacheBlocks.length; blockX++) {
                cache.beginCache();
                for (int y = blockY * CACHE_SIZE; y < blockY * CACHE_SIZE + CACHE_SIZE; y++) {
                    for (int x = blockX * CACHE_SIZE; x < blockX * CACHE_SIZE + CACHE_SIZE; x++) {
                        int screenPosX = x * TILE_SIZE;
                        int screenPosY = y * TILE_SIZE;
                        TilesetType type = TilesetType.typeByColor(mapTiles[x][y]);
                        if (type == null) {
                            continue;
                        }
                        TextureRegion textureRegion = mapTextures.get(type);
                        switch (type) {
                            case BOX: {
                                moveAbleEntities.add(new Box(screenPosX, screenPosY, textureRegion));
                                break;
                            }
                            case STAR: {
                                pickAbleEntities.add(new Star(screenPosX, screenPosY, assetsLoader.getTexture(AssetsLoader.TextureType.STAR)));
                                break;
                            }
                            case BEAR_AND_CAT: {
                                cat = new Cat(screenPosX + TILE_SIZE, screenPosY + 40, assetsLoader.getTexture(AssetsLoader.TextureType.CAT));
                                bear = new Bear(screenPosX + TILE_SIZE, screenPosY, assetsLoader.getTexture(AssetsLoader.TextureType.BEAR_1));
//                                bear.setHaveOnTop(cat); TODO
//                                cat.setIsOnTopOf(EntityType.BEAR);
                                break;
                            }
                            case END_3: { // bottom left
                                endRectangle = new Rectangle(screenPosX, screenPosY, TILE_SIZE * 2, TILE_SIZE * 2);
                                cache.add(textureRegion, screenPosX, screenPosY, TILE_SIZE, TILE_SIZE);
                            }
                            default: {
                                Rectangle tile = calculateTile(type, screenPosX, screenPosY);
                                //textureRegion = TextureUtils.cropTileSet(textureRegion, (int) tile.width, (int) tile.height);
                                cache.add(textureRegion, tile.x, tile.y, tile.width, tile.height);
                            }
                        }
                    }
                }
                cacheBlocks[blockX][blockY] = cache.endCache();
            }
        }
        return new LevelData(bear, cat, moveAbleEntities, pickAbleEntities, endRectangle, mapTiles, cache, cacheBlocks, backGround, frontBackGround);
    }

    /**
     * Smaller size of tile is pushed to UP
     */
    public static Rectangle calculateTile(TilesetType type, int x, int y) {
        int tileHeight = type.getTileHeight();
        if (tileHeight != TILE_SIZE) {
            y += TILE_SIZE - type.getTileHeight();
        }
        return new Rectangle(x, y, TILE_SIZE, tileHeight);
    }
}
