package com.mygdx.game.level;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AssetsLoader;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.Sleep;
import com.mygdx.game.behavior.StartFade;
import com.mygdx.game.entity.ActionEntity;
import com.mygdx.game.entity.Bear;
import com.mygdx.game.entity.BeeHive;
import com.mygdx.game.entity.Box;
import com.mygdx.game.entity.Cat;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.DrawableEntity;
import com.mygdx.game.entity.IronBox;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.entity.Mushroom;
import com.mygdx.game.entity.PickAbleEntity;
import com.mygdx.game.entity.Star;
import com.mygdx.game.entity.TreeBeeHiveAction;
import com.mygdx.game.entity.Web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LevelLoader {

    public static final String WALL_LAYER = "walls";
    public static final int TILE_SIZE = 16;
    private final List<Level> levels = Arrays.asList(Level.values());
    private final AssetsLoader assetsLoader;

    public LevelLoader(AssetsLoader assetsLoader) {
        this.assetsLoader = assetsLoader;
    }

    public LevelData getLevel(int index) {
        if (index >= levels.size()) {
            return null;
        }
        return loadLevel(levels.get(index));
    }

    public int getLevelSize() {
        return levels.size();
    }

    private LevelData loadLevel(Level level) {
        AssetsLoader.RendererAndMap rendererAndMap = assetsLoader.getMaps().get(level);
        return buildLevel(rendererAndMap.getMap(), level, rendererAndMap.getRenderer());
    }

    private LevelData buildLevel(TiledMap map, Level level, OrthogonalTiledMapRenderer mapRenderer) {
        List<MoveAbleEntity> moveAbleEntities = new ArrayList<>();
        List<PickAbleEntity> pickAbleEntities = new ArrayList<>();
        List<DrawableEntity> drawableEntities = new ArrayList<>();
        List<ActionEntity> actionEntities = new ArrayList<>();

        Texture backGround = assetsLoader.getTexture(AssetsLoader.TextureType.BACKGROUND);
        backGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        Texture frontBackGround = assetsLoader.getTexture(AssetsLoader.TextureType.FRONT_BACKGROUND);
        frontBackGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

        TiledMapTileLayer walls = (TiledMapTileLayer) map.getLayers().get(WALL_LAYER);
        Rectangle[][] wallTileset = getTileCells(walls);
        TiledMapTileLayer terrainEntities = (TiledMapTileLayer) map.getLayers().get("terrainEntities");
        terrainEntities.setVisible(false);
        Map<String, List<Rectangle>> terrainEntitiesByType = getTileCellsByType(terrainEntities);
        List<Rectangle> boxes = getOrEmpty(terrainEntitiesByType, "box");
        for (Rectangle box : boxes) {
            moveAbleEntities.add(new Box(box, assetsLoader.getTexture(AssetsLoader.TextureType.BOX_2), assetsLoader.getBoxSound()));
        }
        List<Rectangle> ironBoxes = getOrEmpty(terrainEntitiesByType, "ironbox");
        for (Rectangle ironBox : ironBoxes) {
            moveAbleEntities.add(new IronBox(ironBox, assetsLoader.getTexture(AssetsLoader.TextureType.BOX_3), assetsLoader.getBoxSound()));
        }

        List<Rectangle> stars = getOrEmpty(terrainEntitiesByType, "star");
        for (Rectangle star : stars) {
            pickAbleEntities.add(new Star(star, assetsLoader.getTexture(AssetsLoader.TextureType.STAR)));
        }

        List<Rectangle> mushrooms = getOrEmpty(terrainEntitiesByType, "mushroom");
        for (Rectangle mushroom : mushrooms) {
            Rectangle mushroomCollide = new Rectangle(mushroom.x + 4, mushroom.y, mushroom.width -8, mushroom.height - 8);
            pickAbleEntities.add(new Mushroom(mushroomCollide, mushroom, assetsLoader.getTexture(AssetsLoader.TextureType.MUSHROOM)));
        }

        List<Rectangle> beehives = getOrEmpty(terrainEntitiesByType, "beehive");
        List<BeeHive> beeHiveEntities = new ArrayList<>();
        for (Rectangle beehive : beehives) {
            BeeHive hive = new BeeHive(beehive, assetsLoader.getTexture(AssetsLoader.TextureType.BEE_HIVE));
            beeHiveEntities.add(hive);
            moveAbleEntities.add(hive);
        }

        List<Rectangle> webs = getOrEmpty(terrainEntitiesByType, "web");
        for (Rectangle web : webs) {
            Web webEntity = new Web(web, assetsLoader.getTexture(AssetsLoader.TextureType.WEB));
            drawableEntities.add(webEntity);
            actionEntities.add(webEntity);
        }

        MapLayer entities = map.getLayers().get("entities");
        Map<String, List<Rectangle>> entitiesByType = getTileCellsByType(entities);
        Rectangle catRectangle = entitiesByType.get("cat").get(0);
        Rectangle bearRectangle = entitiesByType.get("bear").get(0);
        Rectangle endRectangle = entitiesByType.get("end").get(0);

        List<Rectangle> actionBeeHives = getOrEmpty(entitiesByType, "actionhive");
        for (Rectangle actionBeeHive : actionBeeHives) {
            BeeHive found = null;
            for (BeeHive beeHive : beeHiveEntities) {
                if (beeHive.getPosition().overlaps(actionBeeHive)) {
                    found = beeHive;
                }
            }
            if (found == null) {
                Gdx.app.error("", "BeeHiveAction without BeeHive !!!!");
            } else {
                actionEntities.add(new TreeBeeHiveAction(actionBeeHive, found));
            }

        }

        ControlAbleEntity cat = new Cat(catRectangle.x, catRectangle.y, assetsLoader.getTexture(AssetsLoader.TextureType.CAT), assetsLoader.getCatSound());
        ControlAbleEntity bear = new Bear(bearRectangle.x, bearRectangle.y, assetsLoader.getTexture(AssetsLoader.TextureType.BEAR_1), assetsLoader.getBearSound());
        ControlAbleEntity controlEntity = cat;
        if (level.isBearSleep()) {
            bear.setHaveControl(false);
            bear.setState(new Sleep());
        }
        if (level.isBearIdle()) {
            bear.getPossibleStates().add(BehaviorType.IDLE);
        }
        if (level.isCatSleep()) {
            cat.setHaveControl(false);
            controlEntity = bear;
        }

        LevelData levelData = new LevelData(bear, cat, controlEntity, moveAbleEntities, pickAbleEntities, drawableEntities, actionEntities, endRectangle, wallTileset, map, backGround, frontBackGround, 0, stars.size(), level, 0, mapRenderer);
        cat.setState(new StartFade(levelData));

        return levelData;
    }

    private <T> List<T> getOrEmpty(Map<String, List<T>> map, String key) {
        List<T> values = map.get(key);
        if (values == null) {
            return new ArrayList<>();
        } else {
            return values;
        }
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
