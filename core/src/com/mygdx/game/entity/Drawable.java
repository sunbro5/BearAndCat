package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Drawable {

    void render(SpriteBatch spriteBatch);

    void update(float delta);
}
