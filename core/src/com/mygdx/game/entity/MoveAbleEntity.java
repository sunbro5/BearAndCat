package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.physics.WorldPhysics;

import lombok.Getter;

public abstract class MoveAbleEntity implements Drawable {

    @Getter
    protected Rectangle position;
    protected boolean onGround = true;

    protected WorldPhysics worldPhysics;

    public MoveAbleEntity(WorldPhysics worldPhysics) {
        this.worldPhysics = worldPhysics;
    }

    public abstract boolean canWalkOn();

    public abstract boolean canPush();

    public abstract EntityType getEntityType();

    public void forceMove(Vector2 velocity) {
        WorldPhysics.TerrainCollision response = worldPhysics.entityMoveTo(position, velocity);
        this.position = response.getMoveTo();
        this.onGround = response.isOnGround();
    }
}
