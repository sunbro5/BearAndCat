package cz.mares.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import cz.mares.game.level.LevelData;
import cz.mares.game.physics.WorldPhysics;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PickAbleEntity implements DrawableEntity {

    protected final Rectangle position;
    protected final Texture texture;

    public abstract void onPick(LevelData levelData, ControlAbleEntity controlAbleEntity);

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, position.x, position.y, position.width, position.height);
    }

    @Override
    public void update(float delta, WorldPhysics worldPhysics) {

    }

    @Override
    public void afterUpdate() {

    }

    public Rectangle getPosition() {
        return this.position;
    }
}
