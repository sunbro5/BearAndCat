package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;
import java.util.Map;

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

    public AssetsLoader() {
        atlas = new TextureAtlas(Gdx.files.internal("clean-crispy-ui.atlas"));
        skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"), atlas);
        loadTextures();
    }

    private void loadTextures() {
        for (TextureType type : TextureType.values()) {
            Texture texture = new Texture(Gdx.files.internal(type.name));
            textures.put(type, texture);
        }
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
    }

}
