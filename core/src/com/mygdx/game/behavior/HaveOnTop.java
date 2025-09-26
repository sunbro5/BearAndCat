package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HaveOnTop implements EntityBehavior {

    private final MoveAbleEntity entity;

    @Override
    public BehaviorType getType() {
        return BehaviorType.HAVE_ON_TOP;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        if (!entity.getStates().containsKey(BehaviorType.IS_ON_TOP)) {
            moveAbleEntity.getStates().remove(getType());
            return new BehaviorResult(moveAbleEntity.getVelocity());
        }
        WorldPhysics.VerticalDirection verticalDirection = WorldPhysics.VerticalDirection.of(moveAbleEntity.getVelocity().y);
        Vector2 forceVelocity = new Vector2(moveAbleEntity.getVelocity());
        Vector2 forceMoveVelocity = entity.forceMove(forceVelocity, worldPhysics, true);
        Vector2 resultVelocity = new Vector2(moveAbleEntity.getVelocity());
        if (verticalDirection == WorldPhysics.VerticalDirection.UP) {
            resultVelocity.y = forceMoveVelocity.y;
        }
        return new BehaviorResult(resultVelocity);
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
