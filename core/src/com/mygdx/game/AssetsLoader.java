package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.sound.EntitySound;
import com.mygdx.game.sound.EntitySoundType;
import com.mygdx.game.utils.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;


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
    private EntitySound bearSound;

    @Getter
    private EntitySound catSound;

    @Getter
    private EntitySound boxSound;

    public AssetsLoader() {
        atlas = new TextureAtlas(Gdx.files.internal("clean-crispy-ui.atlas"));
        skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"), atlas);
        BitmapFont font = skin.getFont("font");
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        loadTextures();
        loadSounds();
    }

    private void loadTextures() {
        for (TextureType type : TextureType.values()) {
            Texture texture = new Texture(Gdx.files.internal(type.name));
            textures.put(type, texture);
        }
    }

    private void loadSounds(){
        Map<EntitySoundType, List<Sound>> bears = new HashMap<>();
        bears.put(EntitySoundType.WALK, CollectionUtils.listOf(
                loadSound("BEAR_WALK_01_LOOP.wav"),
                loadSound("BEAR_WALK_02_LOOP.wav")
        ));
        bears.put(EntitySoundType.JUMP, CollectionUtils.listOf(
                loadSound("BEAR_JUMP_01.wav"),
                loadSound("BEAR_JUMP_01.wav"),
                loadSound("BEAR_JUMP_03.wav")
        ));
        bears.put(EntitySoundType.HIT_STAR, CollectionUtils.listOf(
                loadSound("STAR_COLLECT_01.wav"),
                loadSound("STAR_COLLECT_02.wav"),
                loadSound("STAR_COLLECT_03.wav"),
                loadSound("STAR_COLLECT_04.wav")
        ));
        bears.put(EntitySoundType.SLEEP, CollectionUtils.listOf(
                loadSound("BEAR_SNORE_01_LOOP.wav"),
                loadSound("BEAR_SNORE_02_LOOP.wav"),
                loadSound("BEAR_SNORE_03_LOOP.wav")
        ));
        bears.put(EntitySoundType.LAND, CollectionUtils.listOf(
                loadSound("BEAR_LAND_01.wav"),
                loadSound("BEAR_LAND_02.wav"),
                loadSound("BEAR_LAND_03.wav")
        ));
        bears.put(EntitySoundType.EAT, CollectionUtils.listOf(
                loadSound("BEAR_EAT_01.wav"),
                loadSound("BEAR_EAT_02.wav"),
                loadSound("BEAR_EAT_03.wav"),
                loadSound("BEAR_EAT_04.wav")
        ));
        bears.put(EntitySoundType.GROWL, CollectionUtils.listOf(
                loadSound("BEAR_GROWL_01.wav"),
                loadSound("BEAR_GROWL_02.wav")
        ));
        bearSound = new EntitySound(bears, 1f);


        Map<EntitySoundType, List<Sound>> cats = new HashMap<>();
        cats.put(EntitySoundType.WALK, CollectionUtils.listOf(
                loadSound("CAT_WALK_01_LOOP.wav"),
                loadSound("CAT_WALK_02_LOOP.wav")
        ));
        cats.put(EntitySoundType.JUMP, CollectionUtils.listOf(
                loadSound("CAT_JUMP_01.wav"),
                loadSound("CAT_JUMP_01.wav"),
                loadSound("CAT_JUMP_03.wav")
        ));
        cats.put(EntitySoundType.HIT_STAR, CollectionUtils.listOf(
                loadSound("STAR_COLLECT_01.wav"),
                loadSound("STAR_COLLECT_02.wav"),
                loadSound("STAR_COLLECT_03.wav"),
                loadSound("STAR_COLLECT_04.wav")
        ));
        cats.put(EntitySoundType.LAND, CollectionUtils.listOf(
                loadSound("CAT_LAND_01.wav"),
                loadSound("CAT_LAND_02.wav"),
                loadSound("CAT_LAND_03.wav")
        ));
        catSound = new EntitySound(cats, 1f);

        Map<EntitySoundType, List<Sound>> boxes = new HashMap<>();

        boxes.put(EntitySoundType.LAND, CollectionUtils.listOf(
                loadSound("BEAR_LAND_01.wav"),
                loadSound("BEAR_LAND_02.wav"),
                loadSound("BEAR_LAND_03.wav")
        ));
        boxes.put(EntitySoundType.WALK, CollectionUtils.listOf(
                loadSound("PUSH_BOX_01_LOOP_V2.wav")
        ));
        boxSound = new EntitySound(boxes, 1f);
    }


    public static Sound loadSound(String name) {
        return Gdx.audio.newSound(Gdx.files.internal("sound/" + name));
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
        bearSound.dispose();
        catSound.dispose();
    }

}
