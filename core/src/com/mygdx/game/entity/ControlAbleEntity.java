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

    private static final int CAMERA_OFFSET = 100;
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

    public WorldPhysics.ForceMoveResponse forceMove(MoveAbleEntity entity, Vector2 velocity, ControlAbleEntity entityToMove) {
//        Vector2 forceVelocity = new Vector2(velocity.x, 0);
//        WorldPhysics.Direction direction = WorldPhysics.Direction.ofVertical(forceVelocity.x);
//        if (direction == null) {
//            return new WorldPhysics.ForceMoveResponse(false, 0);
//        }
//        float startingPosition = entity.getPosition().x;
//        WorldPhysics.TerrainCollision response = entityMoveWithTerrain(entity.getPosition(), forceVelocity);
//        entity.setWasPushed(true);
//        entity.getPosition().x = response.getMoveTo().x;
//        entity.getPosition().y = response.getMoveTo().y;
//        //entity.setOnGround(response.isOnGround());
//        if (startingPosition == entity.getPosition().x) {
//            if (entity.isOnGround()) {
//                if (entity.canPush() && entity.getForcePushCount() > 10) {
//                    swapEntities(entity, entityToMove);
//                } else {
//                    entity.setForcePushCount(entity.getForcePushCount() + 1);
//                }
//            }
//            return new WorldPhysics.ForceMoveResponse(false, 0);
//        }
//        return new WorldPhysics.ForceMoveResponse(true, entity.getPosition().x - startingPosition);
        return null;
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
