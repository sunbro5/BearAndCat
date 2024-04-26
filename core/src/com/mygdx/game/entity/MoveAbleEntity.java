package com.mygdx.game.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.physics.WorldPhysics;

import lombok.Getter;
import lombok.Value;

public abstract class MoveAbleEntity implements Drawable {

    @Getter
    protected Rectangle position;
    protected boolean onGround = true;

    private final Vector2 velocity = new Vector2();

    protected WorldPhysics worldPhysics;

    public MoveAbleEntity(WorldPhysics worldPhysics) {
        this.worldPhysics = worldPhysics;
    }

    public abstract boolean canWalkOn();

    public abstract boolean canPush();

    public abstract EntityType getEntityType();

    public ForceMoveResponse forceMove(Vector2 velocity) {
        Vector2 forceVelocity = new Vector2(velocity.x, 0);
        WorldPhysics.Direction direction = WorldPhysics.Direction.ofVertical(forceVelocity.x);
        if (direction == null) {
            return new ForceMoveResponse(false, 0);
        }
        float startingPosition = position.x;
        WorldPhysics.TerrainCollision response = worldPhysics.entityMoveWithTerrain(position, forceVelocity);
        this.position = response.getMoveTo();
        this.onGround = response.isOnGround();
        if (startingPosition == position.x) {
            return new ForceMoveResponse(false, 0);
        }
        return new ForceMoveResponse(true, position.x - startingPosition);

    }

    @Override
    public void update(float delta) {
        float deltaGravity = (WorldPhysics.GRAVITY * delta);
        velocity.y -= deltaGravity;

        WorldPhysics.TerrainCollision response = worldPhysics.entityMoveWithTerrain(position, velocity);
        this.position = response.getMoveTo();
        this.onGround = response.isOnGround();
    }

    @Value
    public static class ForceMoveResponse {
        boolean pushed;
        float pushedX;
    }
}
