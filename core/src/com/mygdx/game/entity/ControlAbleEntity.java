package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.physics.WorldPhysics;

public abstract class ControlAbleEntity extends MoveAbleEntity {

    private static final int CAMERA_OFFSET = 100;
    private static final int JUMP_VELOCITY_STRENGTH = 25;

    protected Animation<TextureRegion> idleAnimation;

    protected Animation<TextureRegion> standAnimation;
    protected Animation<TextureRegion> walkAnimation;
    private TextureRegion currentFrame;

    private float stateTime;

    private Move move = Move.STAND;

    private float idleTimeout = 4;

    private final Rectangle drawRectangle;

    private Vector2 velocity = new Vector2();

    private Direction direction = Direction.RIGHT;

    private ControlAbleEntity haveOnTop;

    private EntityType isOnTopOf;

    private boolean jumping = false;

    public ControlAbleEntity(WorldPhysics worldPhysics, Rectangle position, Rectangle drawRectangle) {
        super(worldPhysics);
        this.position = position;
        this.drawRectangle = drawRectangle;
    }

    public void jump() {
        if (onGround || isOnTopOf != null) {
            jumping = true;
        }

    }

    public void move(Move move) {
        this.move = move;
    }

    public void update(float delta) {
        if (haveOnTop != null && haveOnTop.isOnTopOf == null) {
            haveOnTop = null;
        }
        stateTime += delta;

        switch (this.move) {
            case LEFT:
                velocity.x = -5;
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                direction = Direction.LEFT;
                idleTimeout = 4;
                break;
            case RIGHT:
                velocity.x = 5;
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                direction = Direction.RIGHT;
                idleTimeout = 4;
                break;
            case STAND:
                idleTimeout -= delta;

                if (idleTimeout < 0 && idleAnimation != null) {
                    currentFrame = idleAnimation.getKeyFrame(stateTime, false);
                    break;
                }
                currentFrame = standAnimation.getKeyFrame(stateTime, true);
                break;
        }
        this.move = Move.STAND;

        if (jumping) {
            velocity.y = JUMP_VELOCITY_STRENGTH;
            onGround = false;
            jumping = false;
            isOnTopOf = null;
        }

        float deltaGravity = (WorldPhysics.GRAVITY * delta);
        velocity.y -= deltaGravity;

        WorldPhysics.EntityCollision entityCollision = worldPhysics.entitiesCollisionCheck(this, position, velocity);
        if (entityCollision.isOnTopOf()) {
            isOnTopOf = entityCollision.getEntityType();
        } else {
            onGround = false;
        }
        WorldPhysics.TerrainCollision response = worldPhysics.entityMoveWithTerrain(position, velocity);
        if (response.isOnGround()) {
            isOnTopOf = null;
        }
        this.position = response.getMoveTo();
        this.onGround = response.isOnGround();
        if (haveOnTop != null) {
            haveOnTop.forceMove(velocity);
            haveOnTop.setDirection(this.direction);
        }
        velocity.x = 0;
    }

    public void render(SpriteBatch spriteBatch) {
        float x = position.x + drawRectangle.x;
        float y = position.y + drawRectangle.y;
        float width = position.width + drawRectangle.width;
        float height = position.height + drawRectangle.height;

        if (direction == Direction.LEFT) {
            spriteBatch.draw(currentFrame, x + width, y, -width, height);
        } else {
            spriteBatch.draw(currentFrame, x, y, width, height);
        }
    }

    public Vector3 getCameraPositionVector() {
        return new Vector3(position.x + (position.width / 2), position.y + (position.height / 2) + CAMERA_OFFSET, 0);
    }

    public enum Move {
        STAND,
        IDLE,
        RIGHT,
        LEFT;
    }

    enum Direction {
        RIGHT,
        LEFT
    }

    public Rectangle getPosition() {
        return position;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public EntityType getIsOnTopOf() {
        return isOnTopOf;
    }

    public void setHaveOnTop(ControlAbleEntity haveOnTop) {
        this.haveOnTop = haveOnTop;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
