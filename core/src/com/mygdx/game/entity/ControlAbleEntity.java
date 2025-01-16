package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.physics.collision.CollisionStrategy;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public abstract class ControlAbleEntity extends MoveAbleEntity {

    private static final int CAMERA_OFFSET = 30;
    protected Animation<TextureRegion> idleAnimation;
    protected Animation<TextureRegion> standAnimation;
    protected Animation<TextureRegion> walkAnimation;
    private TextureRegion currentFrame;
    private float stateTime;

    private Move move = Move.STAND;
    private float idleTimeout = 4;

    @Getter
    private Direction direction = Direction.RIGHT;

    private boolean jumping = false;

    @Setter
    private boolean haveControl = false;

    public ControlAbleEntity(Rectangle position, Rectangle drawRectangle) {
        super(position, drawRectangle);
    }

    public abstract float getJumpVelocity();

    public abstract float getMoveVelocity();

    public void jump() {
        if (onGround || states.containsKey(BehaviorType.IS_ON_TOP)) {
            jumping = true;
        }

    }

    public void move(Move move) {
        this.move = move;
    }

    @Override
    public void update(float delta, WorldPhysics worldPhysics) {
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
        }

        worldPhysics.pickAbleEntitiesCheck(position);
        super.update(delta, worldPhysics);
    }

    @Override
    public Vector2 forceMove(Vector2 velocity, WorldPhysics worldPhysics) {
        WorldPhysics.HorizontalDirection direction = WorldPhysics.HorizontalDirection.of(velocity.x);
        if(direction == WorldPhysics.HorizontalDirection.LEFT){
            this.direction = Direction.LEFT;
        } else if (direction == WorldPhysics.HorizontalDirection.RIGHT){
            this.direction = Direction.RIGHT;
        }
        return super.forceMove(velocity, worldPhysics);
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

    public boolean inAir() {
        return !onGround && !states.containsKey(BehaviorType.IS_ON_TOP);
    }


}
