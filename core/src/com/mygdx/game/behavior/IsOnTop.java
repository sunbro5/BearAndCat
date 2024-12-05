package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IsOnTop implements EntityBehavior {

    private final MoveAbleEntity entity;

    @Override
    public BehaviorType getType() {
        return BehaviorType.IS_ON_TOP;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        Rectangle checkPosition = new Rectangle(moveAbleEntity.getPosition());
        checkPosition.y -= 1; // TODO velocity need to be calculated !!!!
        if (entity.getPosition().overlaps(moveAbleEntity.getPosition()) || !entity.getPosition().overlaps(checkPosition)) {
            return new BehaviorResult(moveAbleEntity.getVelocity(), true);
        }
        Vector2 velocity = new Vector2(moveAbleEntity.getVelocity());
        velocity.y = 0;
        return new BehaviorResult(velocity, false);
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }
}
