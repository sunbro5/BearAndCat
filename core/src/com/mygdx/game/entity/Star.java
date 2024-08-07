package com.mygdx.game.entity;

import static com.mygdx.game.level.LevelLoader.TILE_SIZE;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Star extends PickAbleEntity {

    public Star(float x, float y, Texture texture) {
        super(new Rectangle(x, y, TILE_SIZE, TILE_SIZE));
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 13, texture.getHeight());
        TextureRegion[] frames = tmp[0];
        animation = new Animation<>(1f / ((float) frames.length), frames);
    }
}
