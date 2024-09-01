package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;


public class AssetsLoader {

    public enum TextureType {
        SKY("sky.png"),
        MAP_TILES("maptileset.png"),
        MAP_TILES_2("Tileset.png"),
        CAT("cat1.png"),
        CAT_2("cat2.png"),
        BEAR_1("bear1.png"),
        BEAR_2("bear2.png"),
        BACKGROUND("Background.png"),
        FRONT_BACKGROUND("Middleground.png"),
        STAR("Star.png");


        TextureType(String name) {
            this.name = name;
        }
        private final String name;
    }

    private final Map<TextureType, Texture> textures = new HashMap<>();

    public AssetsLoader() {
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
    }

}
