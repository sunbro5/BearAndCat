package com.mygdx.game.behavior;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IsOnTop implements EntityBehavior {

    private final float CHECK_OFFSET = -6f;
    private final float POSITION_OFFSET = 2f;

    private final MoveAbleEntity entity;

    @Override
    public BehaviorType getType() {
        return BehaviorType.IS_ON_TOP;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        if (!entity.getStates().containsKey(BehaviorType.HAVE_ON_TOP)) {
            moveAbleEntity.getStates().remove(getType());
            return new BehaviorResult(moveAbleEntity.getVelocity());
        }
        Rectangle position = new Rectangle(
                moveAbleEntity.getPosition().x,
                moveAbleEntity.getPosition().y + POSITION_OFFSET,
                moveAbleEntity.getPosition().width,
                moveAbleEntity.getPosition().height);
        Rectangle checkHaveOnTopPosition = new Rectangle(
                entity.getPosition().x + entity.getVelocity().x,
                entity.getPosition().y + entity.getVelocity().y,
                entity.getPosition().width,
                entity.getPosition().height);
        Rectangle checkPosition = new Rectangle(
                moveAbleEntity.getPosition().x + moveAbleEntity.getVelocity().x,
                moveAbleEntity.getPosition().y + moveAbleEntity.getVelocity().y + CHECK_OFFSET,
                moveAbleEntity.getPosition().width,
                moveAbleEntity.getPosition().height);

        if (!checkHaveOnTopPosition.overlaps(checkPosition) || WorldPhysics.overlapsWith2Precision(entity.getPosition(), position)) {
            Gdx.app.debug("", "Not on top");
            moveAbleEntity.getStates().remove(getType());
            return new BehaviorResult(moveAbleEntity.getVelocity());
        }
        Vector2 velocity = new Vector2(moveAbleEntity.getVelocity());
        return new BehaviorResult(velocity);
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
