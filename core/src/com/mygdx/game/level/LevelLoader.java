package com.mygdx.game.level;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AssetsLoader;
import com.mygdx.game.entity.Bear;
import com.mygdx.game.entity.Box;
import com.mygdx.game.entity.Cat;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.IronBox;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.entity.Mushroom;
import com.mygdx.game.entity.PickAbleEntity;
import com.mygdx.game.entity.Star;
import com.mygdx.game.map.TilesetType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LevelLoader {

    public static final String WALL_LAYER = "walls";
    public static final int TILE_SIZE = 16;
    private final List<String> levels = new ArrayList<>();
    private final AssetsLoader assetsLoader;

    public LevelLoader(AssetsLoader assetsLoader) {
        this.assetsLoader = assetsLoader;
        levels.add("level1.tmx");
    }

    public LevelData getLevel(int index) {
        if (index >= levels.size()) {
            return null;
        }
        Gdx.app.log("", "Get level: " + index);
        return loadLevel(levels.get(index));
    }

    public int getLevelSize() {
        return levels.size();
    }

    private LevelData loadLevel(String name) {
        TiledMap map = new TmxMapLoader().load(name);
        return buildLevel(map);
    }

    private LevelData buildLevel(TiledMap map) {
        List<MoveAbleEntity> moveAbleEntities = new ArrayList<>();
        List<PickAbleEntity> pickAbleEntities = new ArrayList<>();

        Texture backGround = assetsLoader.getTexture(AssetsLoader.TextureType.BACKGROUND);
        backGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        Texture frontBackGround = assetsLoader.getTexture(AssetsLoader.TextureType.FRONT_BACKGROUND);
        frontBackGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

        TiledMapTileLayer walls = (TiledMapTileLayer) map.getLayers().get(WALL_LAYER);
        Rectangle[][] wallTileset = getTileCells(walls);
        TiledMapTileLayer terrainEntities = (TiledMapTileLayer) map.getLayers().get("terrainEntities");
        terrainEntities.setVisible(false);
        Map<String, List<Rectangle>> terrainEntitiesByType = getTileCellsByType(terrainEntities);
        List<Rectangle> boxes = terrainEntitiesByType.get("box");
        for (Rectangle box: boxes) {
            moveAbleEntities.add(new Box(box, assetsLoader.getTexture(AssetsLoader.TextureType.BOX_2)));
        }
        List<Rectangle> ironBoxes = terrainEntitiesByType.get("ironbox");
        for (Rectangle ironBox: ironBoxes) {
            moveAbleEntities.add(new IronBox(ironBox, assetsLoader.getTexture(AssetsLoader.TextureType.BOX_3)));
        }

        List<Rectangle> stars = terrainEntitiesByType.get("star");
        for (Rectangle star: stars) {
            pickAbleEntities.add(new Star(star, assetsLoader.getTexture(AssetsLoader.TextureType.STAR)));
        }

        List<Rectangle> mushrooms = terrainEntitiesByType.get("mushroom");
        for (Rectangle mushroom: mushrooms) {
            pickAbleEntities.add(new Mushroom(mushroom, assetsLoader.getTexture(AssetsLoader.TextureType.MUSHROOM)));
        }

        MapLayer entities = map.getLayers().get("entities");
        Map<String, List<Rectangle>> entitiesByType = getTileCellsByType(entities);
        Rectangle catRectangle = entitiesByType.get("cat").get(0);
        Rectangle bearRectangle = entitiesByType.get("bear").get(0);
        Rectangle endRectangle = entitiesByType.get("end").get(0);

        ControlAbleEntity cat = new Cat(catRectangle.x, catRectangle.y, assetsLoader.getTexture(AssetsLoader.TextureType.CAT));
        ControlAbleEntity bear = new Bear(bearRectangle.x, bearRectangle.y, assetsLoader.getTexture(AssetsLoader.TextureType.BEAR_1));
        bear.setHaveControl(true);
        return new LevelData(bear, cat, bear, moveAbleEntities, pickAbleEntities, endRectangle, wallTileset, map, backGround, frontBackGround,0, pickAbleEntities.size());
    }

    private Map<String, List<Rectangle>> getTileCellsByType(MapLayer layer) {
        Map<String, List<Rectangle>> items = new LinkedHashMap<>();

        for (MapObject object : layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleMapObject = (RectangleMapObject) object;
                String type = rectangleMapObject.getProperties().get("typ", String.class);
                if (type != null) {
                    List<Rectangle> cells = items.get(type);
                    if (cells == null) {
                        cells = new ArrayList<>();
                        items.put(type, cells);
                    }
                    cells.add(rectangleMapObject.getRectangle());
                }
            }
        }
        return items;
    }

    private Map<String, List<Rectangle>> getTileCellsByType(TiledMapTileLayer layer) {
        int tileHeight = layer.getTileHeight();
        int tileWidth = layer.getTileWidth();
        Map<String, List<Rectangle>> items = new LinkedHashMap<>();
        for (int y = 0; y <= layer.getHeight(); y++) {
            for (int x = 0; x <= layer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    String type = cell.getTile().getProperties().get("typ", String.class);
                    if (type != null) {
                        List<Rectangle> cells = items.get(type);
                        if (cells == null) {
                            cells = new ArrayList<>();
                            items.put(type, cells);
                        }
                        cells.add(new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight));
                    }
                }
            }
        }
        return items;
    }

    private Rectangle[][] getTileCells(TiledMapTileLayer layer) {
        int tileHeight = layer.getTileHeight();
        int tileWidth = layer.getTileWidth();
        Rectangle[][] tiles = new Rectangle[layer.getHeight()][layer.getWidth()];
        for (int y = 0; y <= layer.getHeight(); y++) {
            for (int x = 0; x <= layer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    tiles[y][x] = new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                }
            }
        }
        return tiles;
    }

}
