package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.behavior.BehaviorHandler;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.EntityBehavior;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.physics.collision.CollisionHandler;
import com.mygdx.game.physics.collision.CollisionStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    protected boolean wasForceMoved = false;
    protected int maxFallSpeed = 50;
    @Getter
    protected final Map<BehaviorType, EntityBehavior> states = new HashMap<>();
    @Getter
    protected final Set<BehaviorType> possibleStates;
    protected final List<CollisionStrategy> possibleCollisionStrategies;

    public MoveAbleEntity(Rectangle position, Rectangle drawRectangle) {
        this.position = position;
        this.drawRectangle = drawRectangle;
        this.possibleStates = initBehaviour();
        this.possibleCollisionStrategies = initCollisionStrategies();
    }

    protected abstract Set<BehaviorType> initBehaviour();

    protected abstract List<CollisionStrategy> initCollisionStrategies();

    public abstract boolean canBeOnTop();

    public abstract boolean canBePush();

    @Override
    public void update(float delta, WorldPhysics worldPhysics) {
        if (wasForceMoved) {
            return;
        }
        effectOfGravity(delta);
        this.velocity = move(this.velocity, worldPhysics);
        setFinalPosition();
        this.velocity.x = 0;
    }

    public void afterUpdate(){
        wasForceMoved = false;
    }

    public Vector2 forceMove(Vector2 velocity, WorldPhysics worldPhysics) {
        this.wasForceMoved = true;
        Vector2 resultVelocity = new Vector2(move(velocity, worldPhysics));
        setFinalPosition();
        this.velocity.y = 0;
        this.velocity.x = 0;
        return resultVelocity;
    }

    private Vector2 move(Vector2 velocity, WorldPhysics worldPhysics) {
        this.velocity = velocity;
        WorldPhysics.TerrainCollision response = worldPhysics.entityMoveWithTerrain(this.position, this.velocity);
        setOnGround(response.isOnGround());

        this.velocity = response.getVelocity();

        List<WorldPhysics.EntityCollision> entityCollisions = worldPhysics.entitiesCollisionCheck(this);
        this.velocity = CollisionHandler.handleCollision(this, entityCollisions, possibleCollisionStrategies, worldPhysics);
        this.velocity = BehaviorHandler.handleBehavior(states, this, worldPhysics);
        return this.velocity;
    }

    protected void setFinalPosition() {
        if (this.velocity.y == 0 && this.velocity.x == 0) {
            return;
        }
        Gdx.app.log("","Velocity " +this.getClass().getName() + " = " + velocity + " position:" + position);
        this.position.x += this.velocity.x;
        this.position.y += this.velocity.y;
    }

    protected void effectOfGravity(float delta) {
        if (velocity.y > maxFallSpeed) {
            return;
        }
        float deltaGravity = Math.round(WorldPhysics.GRAVITY * delta * 100f) / 100f;
        velocity.y -= deltaGravity;
    }

    @Override
    public String toString() {
        return "MoveAbleEntity{" + this.getClass().getName() +
                //", states=" + states +
                ", position=" + position +
                '}';
    }
}
