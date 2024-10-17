package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.physics.WorldPhysics;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public abstract class ControlAbleEntity extends MoveAbleEntity {

    private static final int CAMERA_OFFSET = 100;
    protected Animation<TextureRegion> idleAnimation;

    protected Animation<TextureRegion> standAnimation;
    protected Animation<TextureRegion> walkAnimation;
    private TextureRegion currentFrame;

    private float stateTime;

    private Move move = Move.STAND;

    private float idleTimeout = 4;

    private final Rectangle drawRectangle;

    @Getter
    private Direction direction = Direction.RIGHT;

    @Getter
    private ControlAbleEntity haveOnTop;

    @Getter
    @Setter
    private EntityType isOnTopOf;

    private boolean jumping = false;

    @Setter
    private boolean haveControl = false;

    public ControlAbleEntity(Rectangle position, Rectangle drawRectangle) {
        this.position = position;
        this.drawRectangle = drawRectangle;
    }

    public abstract float getJumpVelocity();

    public abstract float getMoveVelocity();

    public void jump() {
        if (onGround || isOnTopOf != null) {
            jumping = true;
        }

    }

    public void move(Move move) {
        this.move = move;
    }

    @Override
    public void update(float delta, WorldPhysics worldPhysics) {
        if (haveOnTop != null && haveOnTop.isOnTopOf == null) {
            haveOnTop = null;
        }
        stateTime += delta;

        switch (this.move) {
            case LEFT:
                velocity.x = -getMoveVelocity();
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                direction = Direction.LEFT;
                idleTimeout = 4;
                break;
            case RIGHT:
                velocity.x = getMoveVelocity();
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
            velocity.y = getJumpVelocity();
            onGround = false;
            jumping = false;
            isOnTopOf = null;
        }

        float deltaGravity = (WorldPhysics.GRAVITY * delta);
        velocity.y -= deltaGravity;

        List<WorldPhysics.EntityCollision> entityCollisions = worldPhysics.entitiesCollisionCheck(this);
        for (WorldPhysics.EntityCollision collision : entityCollisions) {
            if (collision.isOnTopOf()) {
                setIsOnTopOf(collision.getEntityType());
                break;
            } else {
                setOnGround(false);
            }
        }

        WorldPhysics.TerrainCollision response = worldPhysics.entityMoveWithTerrain(position, velocity);
        if (response.isOnGround()) {
            setIsOnTopOf(null);
        }
        float moveToX = response.getMoveTo().x;
        float moveToY = response.getMoveTo().y;

        if (haveOnTop != null) {
            WorldPhysics.TerrainCollision responseOnTop = worldPhysics.entityMoveWithTerrain(haveOnTop.position, velocity);
            if (responseOnTop.isHitCeiling()) {
                moveToY = responseOnTop.getMoveTo().y - position.height;
                velocity.y = 0;
                moveToX = position.x;
            }
        }

        position.x = moveToX;
        position.y = moveToY;
        setOnGround(response.isOnGround());
        if (getHaveOnTop() != null && haveControl) {
            worldPhysics.forceMove(getHaveOnTop(), velocity, this);
            getHaveOnTop().setDirection(getDirection());
        }
        worldPhysics.pickAbleEntitiesCheck(position);
        velocity.x = 0;
        setForcePushCount(0);
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
        RIGHT,
        LEFT
    }

    public enum Direction {
        RIGHT,
        LEFT
    }

    public Rectangle getPosition() {
        return position;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setHaveOnTop(ControlAbleEntity haveOnTop) {
        this.haveOnTop = haveOnTop;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
