package com.mygdx.game.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.physics.WorldPhysics;

import lombok.Getter;
import lombok.Setter;

public abstract class MoveAbleEntity implements DrawableEntity {

    @Getter
    protected Rectangle position;

    @Getter
    @Setter
    protected boolean onGround = true;

    @Getter
    protected Vector2 velocity = new Vector2();

    @Getter
    @Setter
    protected int forcePushCount = 0;

    @Getter
    @Setter
    protected boolean wasPushed = false;

    protected int maxFallSpeed = 50;

    public MoveAbleEntity() {
    }

    public abstract boolean canWalkOn();

    public abstract boolean canPush();

    public abstract EntityType getEntityType();

    @Override
    public void update(float delta, WorldPhysics worldPhysics) {
        effectOfGravity(delta);

        WorldPhysics.TerrainCollision response = worldPhysics.entityMoveWithTerrain(position, velocity);
        position.x = response.getMoveTo().x;
        position.y = response.getMoveTo().y;
        setOnGround(response.isOnGround());
        if (wasPushed) {
            wasPushed = false;
        } else {
            forcePushCount = 0;
        }
    }

    protected void effectOfGravity(float delta) {
        if (velocity.y > maxFallSpeed) {
            return;
        }
        float deltaGravity = (WorldPhysics.GRAVITY * delta);
        velocity.y -= deltaGravity;
    }
}
