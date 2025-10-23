package cz.mares.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import cz.mares.game.physics.WorldPhysics;

public interface DrawableEntity extends GameEntity {

    void render(SpriteBatch spriteBatch);

    void update(float delta, WorldPhysics worldPhysics);

    void afterUpdate();
}
