package cz.mares.game.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import cz.mares.game.behavior.BehaviorType;
import cz.mares.game.behavior.Idle;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.renderer.AnimationType;
import cz.mares.game.sound.EntitySoundType;
import cz.mares.game.sound.SoundPlayer;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public abstract class ControlAbleEntity extends MoveAbleEntity {

    private static final int CAMERA_OFFSET = 30;
    protected Animation<TextureRegion> standAnimation;
    protected Animation<TextureRegion> walkAnimation;

    protected Animation<TextureRegion> customAnimation;
    protected Map<AnimationType, Animation<TextureRegion>> animations = new HashMap<>();

    protected TextureRegion currentFrame;

    @Setter
    private float stateTime;

    @Getter
    private Move move = Move.STAND;

    @Getter
    private Move lastMove = Move.STAND;

    @Getter
    @Setter
    private Direction direction = Direction.RIGHT;

    private boolean jumping = false;

    @Getter
    @Setter
    private boolean haveControl = true;

    @Setter
    private float speed;

    public ControlAbleEntity(Rectangle position, Rectangle drawRectangle) {
        super(position, drawRectangle);
    }

    public abstract float getJumpVelocity();

    public abstract float getMoveVelocity();

    public abstract float getIdleTimeout();

    @Setter
    @Getter
    private float idleValue = getIdleTimeout();

    public void jump() {
        if ((onGround || states.containsKey(BehaviorType.IS_ON_TOP)) && haveControl) {
            resetIdle();
            jumping = true;
        }
    }

    @Override
    public void update(float delta, WorldPhysics worldPhysics) {
        stateTime += delta;

        if (move == Move.STAND) {
            if (customAnimation != null) {
                currentFrame = customAnimation.getKeyFrame(stateTime);
            } else {
                currentFrame = standAnimation.getKeyFrame(stateTime, true);
                calculateIdle(delta);
            }
        } else if (move == Move.LEFT) {
            velocity.x = - speed * delta;
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            direction = Direction.LEFT;
        } else if (move == Move.RIGHT) {
            velocity.x = speed * delta;
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            direction = Direction.RIGHT;
        }

        this.move = Move.STAND;

        if (!haveControl) {
            jumping = false;
        }
        if (jumping) {
            SoundPlayer.play(entitySoundS, EntitySoundType.JUMP);
            velocity.y = getJumpVelocity();
            onGround = false;
            jumping = false;
        }

        super.update(delta, worldPhysics);
        worldPhysics.pickAbleEntitiesCheck(this);
        worldPhysics.actionEntitiesCheck(this);
        this.lastMove = Move.STAND;
    }

    private void calculateIdle(float delta) {
        if (!getPossibleStates().contains(BehaviorType.IDLE)) {
            return;
        }
        idleValue -= delta;
        if (idleValue < 0 && (!getStates().containsKey(BehaviorType.SLEEP) || !getStates().containsKey(BehaviorType.IDLE))) {
            setState(new Idle());
        }
    }

    @Override
    public Vector2 forceMove(Vector2 velocity, WorldPhysics worldPhysics, boolean yVelocity) {
        WorldPhysics.HorizontalDirection direction = WorldPhysics.HorizontalDirection.of(velocity.x);
        if (direction == WorldPhysics.HorizontalDirection.LEFT) {
            this.direction = Direction.LEFT;
        } else if (direction == WorldPhysics.HorizontalDirection.RIGHT) {
            this.direction = Direction.RIGHT;
        }
        return super.forceMove(velocity, worldPhysics, yVelocity);
    }

    public void render(SpriteBatch spriteBatch) {
        if (currentFrame == null) {
            return;
        }
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

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isCustomAnimationFinished() {
        return customAnimation != null && customAnimation.isAnimationFinished(stateTime);
    }

    public Integer getCustomAnimationIndex() {
        return customAnimation != null ? customAnimation.getKeyFrameIndex(stateTime) : null;
    }

    public void setAnimation(AnimationType type, boolean looping) {
        stateTime = 0;
        if (type == null) {
            customAnimation = null;
            return;
        }
        customAnimation = animations.get(type);
        if (looping) {
            customAnimation.setPlayMode(Animation.PlayMode.LOOP);
        } else {
            customAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        }
    }

    public void resetIdle() {
        this.idleValue = getIdleTimeout();
    }

    public void move(Move move) {
        resetIdle();
        if (!haveControl) {
            return;
        }
        this.move = move;
        this.lastMove = move;
        this.speed = getMoveVelocity();
    }

    public void move(Move move, int speedPercent) {
        resetIdle();
        if (!haveControl) {
            return;
        }
        this.move = move;
        this.lastMove = move;
        this.speed = Math.min(getMoveVelocity() * speedPercent / 100, getMoveVelocity());
    }

    public void setMove(Move move) {
        this.move = move;
        this.lastMove = move;
    }

}
