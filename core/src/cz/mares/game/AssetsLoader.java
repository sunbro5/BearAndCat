package cz.mares.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import cz.mares.game.level.Level;
import cz.mares.game.sound.EntitySound;
import cz.mares.game.sound.EntitySounds;
import cz.mares.game.sound.EntitySoundType;
import cz.mares.game.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Value;


public class AssetsLoader {

    public enum TextureType {
        SKY("sky.png"),
        MAP_TILES("maptileset.png"),
        MAP_TILES_2("Tileset.png"),
        BOX_2("box2.png"),
        BOX_3("box3.png"),
        CAT("cat1.png"),
        CAT_2("cat2.png"),
        BEAR_1("bear1.png"),
        BEAR_2("bear2.png"),
        BACKGROUND("Background.png"),
        FRONT_BACKGROUND("Middleground.png"),
        STAR("Star.png"),
        MUSHROOM("mushroom.png"),
        FLY_MUSHROOM("flymushroom.png"),
        BEE_HIVE("beehive.png"),

        WEB("web.png");

        TextureType(String name) {
            this.name = name;
        }

        private final String name;
    }

    @Getter
    private final Skin skin;
    private final TextureAtlas atlas;

    private final Map<TextureType, Texture> textures = new HashMap<>();

    @Getter
    private EntitySounds bearSound;

    @Getter
    private EntitySounds catSound;

    @Getter
    private EntitySounds boxSound;

    @Getter
    private List<EntitySounds> entitiesSounds = new ArrayList<>();

    @Getter
    private Map<Level, RendererAndMap> maps = new HashMap<>();

    public AssetsLoader(GameData gameData) {
        atlas = new TextureAtlas(Gdx.files.internal("clean-crispy-ui.atlas"));
        skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"), atlas);
        BitmapFont font = skin.getFont("font");
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        loadTextures();
        loadSounds(gameData);
        loadMaps();
    }

    private void loadMaps() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        for (Level level : Level.values()) {
            TiledMap tiledMap = mapLoader.load(level.getName());
            maps.put(level, new RendererAndMap(tiledMap, new OrthogonalTiledMapRenderer(tiledMap)));
        }
    }

    private void loadTextures() {
        for (TextureType type : TextureType.values()) {
            Texture texture = new Texture(Gdx.files.internal(type.name));
            textures.put(type, texture);
        }
    }

    private void loadSounds(GameData gameData) {
        Map<EntitySoundType, List<EntitySound>> bears = new HashMap<>();
        bears.put(EntitySoundType.WALK, CollectionUtils.listOf(
                loadSound("BEAR_WALK_01_LOOP.wav", 0.6f),
                loadSound("BEAR_WALK_02_LOOP.wav", 0.6f)
        ));
        bears.put(EntitySoundType.JUMP, CollectionUtils.listOf(
                loadSound("BEAR_JUMP_01.wav", 0.8f),
                loadSound("BEAR_JUMP_01.wav", 0.8f),
                loadSound("BEAR_JUMP_03.wav", 0.8f)
        ));
        bears.put(EntitySoundType.HIT_STAR, CollectionUtils.listOf(
                loadSound("STAR_COLLECT_01.wav", 0.8f),
                loadSound("STAR_COLLECT_02.wav", 0.8f),
                loadSound("STAR_COLLECT_03.wav", 0.8f),
                loadSound("STAR_COLLECT_04.wav", 0.8f)
        ));
        bears.put(EntitySoundType.SLEEP, CollectionUtils.listOf(
                loadSound("BEAR_SNORE_01_LOOP.wav", 0.8f),
                loadSound("BEAR_SNORE_02_LOOP.wav", 0.8f),
                loadSound("BEAR_SNORE_03_LOOP.wav", 0.8f)
        ));
        bears.put(EntitySoundType.LAND, CollectionUtils.listOf(
                loadSound("BEAR_LAND_01.wav", 0.4f),
                loadSound("BEAR_LAND_02.wav", 0.4f),
                loadSound("BEAR_LAND_03.wav", 0.4f)
        ));
        bears.put(EntitySoundType.EAT, CollectionUtils.listOf(
                loadSound("BEAR_EAT_01.wav", 0.7f),
                loadSound("BEAR_EAT_02.wav", 0.7f),
                loadSound("BEAR_EAT_03.wav", 0.7f),
                loadSound("BEAR_EAT_04.wav", 0.7f)
        ));
        bears.put(EntitySoundType.GROWL, CollectionUtils.listOf(
                loadSound("BEAR_GROWL_01.wav", 0.8f),
                loadSound("BEAR_GROWL_02.wav", 0.8f)
        ));
        bearSound = new EntitySounds(bears, gameData);


        Map<EntitySoundType, List<EntitySound>> cats = new HashMap<>();
        cats.put(EntitySoundType.WALK, CollectionUtils.listOf(
                loadSound("CAT_WALK_01_LOOP.wav", 0.5f),
                loadSound("CAT_WALK_02_LOOP.wav", 0.5f)
        ));
        cats.put(EntitySoundType.JUMP, CollectionUtils.listOf(
                loadSound("CAT_JUMP_01.wav", 0.7f),
                loadSound("CAT_JUMP_01.wav", 0.7f),
                loadSound("CAT_JUMP_03.wav", 0.7f)
        ));
        cats.put(EntitySoundType.HIT_STAR, CollectionUtils.listOf(
                loadSound("STAR_COLLECT_01.wav", 0.8f),
                loadSound("STAR_COLLECT_02.wav", 0.8f),
                loadSound("STAR_COLLECT_03.wav", 0.8f),
                loadSound("STAR_COLLECT_04.wav", 0.8f)
        ));
        cats.put(EntitySoundType.LAND, CollectionUtils.listOf(
                loadSound("CAT_LAND_01.wav", 0.6f),
                loadSound("CAT_LAND_02.wav", 0.6f),
                loadSound("CAT_LAND_03.wav", 0.6f)
        ));
        catSound = new EntitySounds(cats, gameData);

        Map<EntitySoundType, List<EntitySound>> boxes = new HashMap<>();

        boxes.put(EntitySoundType.LAND, CollectionUtils.listOf(
                loadSound("BEAR_LAND_01.wav", 0.9f),
                loadSound("BEAR_LAND_02.wav", 0.9f),
                loadSound("BEAR_LAND_03.wav", 0.9f)
        ));
        boxes.put(EntitySoundType.WALK, CollectionUtils.listOf(
                loadSound("PUSH_BOX_01_LOOP_V2.wav", 1f)
        ));
        boxSound = new EntitySounds(boxes, gameData);

        entitiesSounds.add(bearSound);
        entitiesSounds.add(catSound);
        entitiesSounds.add(boxSound);
    }


    public static EntitySound loadSound(String name, float volume) {
        return new EntitySound(Gdx.audio.newSound(Gdx.files.internal("sound/" + name)), volume);
    }

    public Texture getTexture(TextureType textureType) {
        return this.textures.get(textureType);
    }

    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        atlas.dispose();
        skin.dispose();
        for (EntitySounds entitySoundS : entitiesSounds) {
            entitySoundS.dispose();
        }
        for (RendererAndMap rendererAndMap : maps.values()) {
            rendererAndMap.getRenderer().dispose();
            rendererAndMap.getMap().dispose();
        }

    }

    @Value
    public class RendererAndMap {
        TiledMap map;
        OrthogonalTiledMapRenderer renderer;

    }

}
