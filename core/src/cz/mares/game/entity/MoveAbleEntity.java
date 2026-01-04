package cz.mares.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import cz.mares.game.behavior.BehaviorHandler;
import cz.mares.game.behavior.BehaviorType;
import cz.mares.game.behavior.EntityBehavior;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.physics.collision.CollisionHandler;
import cz.mares.game.physics.collision.CollisionStrategy;
import cz.mares.game.sound.EntitySounds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;

public abstract class MoveAbleEntity implements DrawableEntity {

    private static final AtomicInteger idSequence = new AtomicInteger(1);
    private static final int FORCE_COUNT_LIMIT = 10;

    protected final Rectangle position;

    @Getter
    protected final Rectangle drawRectangle;

    @Getter
    @Setter
    protected boolean onGround = true;

    @Getter
    protected Vector2 velocity = new Vector2();

    @Getter
    protected Vector2 accel = new Vector2();

    @Getter
    @Setter
    protected int forcePushCount = 0;

    @Getter
    @Setter
    protected boolean wasForceMoved = false;

    private int forceMoveCount;

    @Getter
    protected boolean moved = false;
    protected int maxFallSpeed = -40;

    @Getter
    protected final Map<BehaviorType, EntityBehavior> states = new HashMap<>();
    @Getter
    protected final Set<BehaviorType> possibleStates;
    protected final List<CollisionStrategy> possibleCollisionStrategies;

    @Getter
    protected EntitySounds entitySoundS;

    @Getter
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

    public boolean inAir() {
        return !onGround && !states.containsKey(BehaviorType.IS_ON_TOP);
    }

    @Override
    public void update(float delta, WorldPhysics worldPhysics) {
        if (wasForceMoved) {
            return;
        }
        this.accel = effectOfGravity(delta, this.accel);
        this.velocity.add(accel.x, accel.y);
        this.velocity.scl(delta);
        this.velocity = move(this.velocity, worldPhysics);
        setFinalPosition(delta);
        this.velocity.scl(1.0f / delta);
        this.velocity.x = 0;
    }

    public void afterUpdate() {
        wasForceMoved = false;
        moved = false;
        forceMoveCount = 0;
    }

    public Vector2 forceMove(Vector2 velocity, WorldPhysics worldPhysics, boolean useYVelocity) {
        this.wasForceMoved = true;
        this.forceMoveCount++;
        if (forceMoveCount > FORCE_COUNT_LIMIT) {
            Gdx.app.error("Force-overflow", "Entity: " + this + "force overflow hit limit.");
            return new Vector2(0,0);
        }
        boolean wasAlreadyMoved = moved;
        float yVelocityBefore = this.velocity.y;
        Vector2 forceVelocity;
        if (useYVelocity) {
            forceVelocity = new Vector2(velocity.x, velocity.y);
        } else {
            if (wasAlreadyMoved) {
                forceVelocity = new Vector2(velocity.x, 0);
            } else {
                this.accel = effectOfGravity(worldPhysics.getLastDelta(), this.accel);
                float yVelocity = (this.velocity.y + accel.y) * worldPhysics.getLastDelta();
                forceVelocity = new Vector2(velocity.x, yVelocity);
            }
        }
        Vector2 resultVelocity = new Vector2(move(forceVelocity, worldPhysics));
        setFinalPosition(worldPhysics.getLastDelta());

        this.velocity.x = 0;
        this.velocity.scl(1.0f / worldPhysics.getLastDelta());
        if (wasAlreadyMoved) {
            this.velocity.y = yVelocityBefore;
        }
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

        if (response.getVelocity().y != this.velocity.y) {
            response = worldPhysics.entityMoveWithTerrain(this.position, this.velocity);
            this.velocity = response.getVelocity();
        }

        return this.velocity;
    }

    protected void setFinalPosition(float delta) {
        if (this.velocity.y == 0 && this.velocity.x == 0) {
            return;
        }
        Gdx.app.debug("", " " + this.getClass().getName() + " id: " + id + " velocity: " + velocity + " accel :" + accel + " delta:" + delta);
        this.position.x += this.velocity.x;
        this.position.y += this.velocity.y;
    }

    public Vector2 effectOfGravity(float delta, Vector2 accel) {
        accel.y -= WorldPhysics.GRAVITY;
        accel.scl(delta);
        return accel;
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
                " , position= " + position +
                " , velocity= " + velocity +
                '}';
    }
}
