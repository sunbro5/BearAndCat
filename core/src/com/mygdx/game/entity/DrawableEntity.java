package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.physics.WorldPhysics;

public interface DrawableEntity {

    void render(SpriteBatch spriteBatch);

    void update(float delta, WorldPhysics worldPhysics);

    void afterUpdate();
}
