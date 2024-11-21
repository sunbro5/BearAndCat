package com.mygdx.game.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.EntityBehavior;
import com.mygdx.game.physics.collision.CollisionHandler;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.physics.collision.CollisionStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public abstract class MoveAbleEntity implements DrawableEntity {

    @Getter
    protected final Rectangle position;

    @Getter
    protected final Rectangle drawRectangle;

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

    protected final Map<BehaviorType, EntityBehavior> states = new HashMap<>();
    protected final List<BehaviorType> possibleStates;
    protected final List<CollisionStrategy> possibleCollisionStrategies;

    public MoveAbleEntity(Rectangle position, Rectangle drawRectangle, List<BehaviorType> possibleStates,
                          List<CollisionStrategy> possibleCollisionStrategies) {
        this.position = position;
        this.drawRectangle = drawRectangle;
        this.possibleStates = possibleStates;
        this.possibleCollisionStrategies = possibleCollisionStrategies;
    }

    public abstract boolean canWalkOn();

    public abstract boolean canPush();

    @Override
    public void update(float delta, WorldPhysics worldPhysics, CollisionHandler collisionHandler) {
        effectOfGravity(delta);

        WorldPhysics.TerrainCollision response = worldPhysics.entityMoveWithTerrain(position, velocity);
        setOnGround(response.isOnGround());
        this.velocity = response.getVelocity();

        List<WorldPhysics.EntityCollision> entityCollisions = worldPhysics.entitiesCollisionCheck(this);
        this.velocity = collisionHandler.handleCollision(this, entityCollisions, possibleCollisionStrategies);

        setFinalPosition();

        for (EntityBehavior entityState: states.values()) {
            entityState.update(delta);
        }

        this.velocity.x = 0;
    }

    protected void setFinalPosition(){
        this.position.x += velocity.x;
        this.position.y += velocity.y;
    }

    protected void effectOfGravity(float delta) {
        if (velocity.y > maxFallSpeed) {
            return;
        }
        float deltaGravity = (WorldPhysics.GRAVITY * delta);
        velocity.y -= deltaGravity;
    }
}
