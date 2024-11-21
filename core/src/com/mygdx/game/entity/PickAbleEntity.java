package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.physics.collision.CollisionHandler;
import com.mygdx.game.physics.WorldPhysics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class PickAbleEntity implements DrawableEntity {

    @Getter
    private final Rectangle position;
    protected Animation<TextureRegion> animation;
    private TextureRegion currentFrame;
    private float stateTime;

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(currentFrame, position.x, position.y, position.width, position.height);
    }

    @Override
    public void update(float delta, WorldPhysics worldPhysics, CollisionHandler collisionHandler) {
        stateTime += delta;
        currentFrame = animation.getKeyFrame(stateTime, true);
    }
}
