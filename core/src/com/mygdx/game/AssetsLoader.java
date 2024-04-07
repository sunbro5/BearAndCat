package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;


public class AssetsLoader {

    public enum TextureType {
        BEAR("bear_sprite.png"),
        SKY("sky.png"),
        MAP_TILES("maptileset.png");

        TextureType(String name) {
            this.name = name;
        }
        private final String name;
    }

    private final Map<TextureType, Texture> textures = new HashMap<>();
    private final Pixmap pixmap;

    public AssetsLoader() {
        pixmap = new Pixmap(Gdx.files.internal("level1.png"));
        loadTextures();
    }

    private void loadTextures() {
        for (TextureType type : TextureType.values()) {
            Texture texture = new Texture(Gdx.files.internal(type.name));
            textures.put(type, texture);
        }
    }

    public Pixmap getPixmap() {
        return pixmap;
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
