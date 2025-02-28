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
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;

public abstract class MoveAbleEntity implements DrawableEntity {

    private static final AtomicInteger idSequence = new AtomicInteger(1);

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

    @Getter
    protected boolean moved = false;
    protected int maxFallSpeed = 50;
    @Getter
    protected final Map<BehaviorType, EntityBehavior> states = new HashMap<>();
    @Getter
    protected final Set<BehaviorType> possibleStates;
    protected final List<CollisionStrategy> possibleCollisionStrategies;

    private final int id;

    public MoveAbleEntity(Rectangle position, Rectangle drawRectangle) {
        this.id = idSequence.getAndIncrement();
        this.position = position;
        this.drawRectangle = drawRectangle;
        this.possibleStates = initBehaviour();
        this.possibleCollisionStrategies = initCollisionStrategies();
    }

    protected abstract Set<BehaviorType> initBehaviour();

    protected abstract List<CollisionStrategy> initCollisionStrategies();

    public abstract boolean canBeOnTop();

    public abstract boolean canBePush();

    public int getStrength() {
        return 0;
    }

    public int getWeight() {
        return 0;
    }

    @Override
    public void update(float delta, WorldPhysics worldPhysics) {
        if (wasForceMoved) {
            return;
        }
        this.velocity = effectOfGravity(delta, this.velocity);
        this.velocity = move(this.velocity, worldPhysics);
        setFinalPosition();
        this.velocity.x = 0;
    }

    public void afterUpdate() {
        wasForceMoved = false;
        moved = false;
    }

    public Vector2 forceMove(Vector2 velocity, WorldPhysics worldPhysics, boolean yVelocity) {
        this.wasForceMoved = true;
        boolean wasAlreadyMoved = moved;
        float yVelocityBefore = this.velocity.y;
        Vector2 forceVelocity;
        if (yVelocity) {
            forceVelocity = new Vector2(velocity.x, velocity.y);
        } else {
            if (wasAlreadyMoved) {
                forceVelocity = new Vector2(velocity.x, 0);
            } else {
                forceVelocity = new Vector2(velocity.x, effectOfGravity(worldPhysics.getLastDelta(), this.velocity).y);
            }
        }
        Vector2 resultVelocity = new Vector2(move(forceVelocity, worldPhysics));
        setFinalPosition();
        if (wasAlreadyMoved) {
            this.velocity.y = yVelocityBefore;
        }
        //this.velocity.y = 0;
        this.velocity.x = 0;
        return resultVelocity;
    }

    private Vector2 move(Vector2 velocity, WorldPhysics worldPhysics) {
        moved = true;
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
        Gdx.app.debug("", "Velocity " + this.getClass().getName() + " id: " + id + " = " + velocity + " position:" + position + " velocity: " + velocity + ", forced: " + isWasForceMoved() + ", states " + getStates().keySet());
        this.position.x += this.velocity.x;
        this.position.y += this.velocity.y;
    }

    protected Vector2 effectOfGravity(float delta, Vector2 velocity) {
        if (velocity.y > maxFallSpeed) {
            return velocity;
        }
        float deltaGravity = Math.round(WorldPhysics.GRAVITY * delta * 100f) / 100f;
        velocity.y -= deltaGravity;
        return velocity;
    }

    public void setState(EntityBehavior state) {
        getStates().put(state.getType(), state);
    }

    @Override
    public Rectangle getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "MoveAbleEntity{" + this.getClass().getName() +
                " id :" + id +
                //", states=" + states +
                " , position=" + position +
                '}';
    }
}
