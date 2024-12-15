package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.ControlAbleEntity;
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
            return new BehaviorResult(moveAbleEntity.getVelocity(), true);
        }
        WorldPhysics.VerticalDirection verticalDirection = WorldPhysics.VerticalDirection.of(moveAbleEntity.getVelocity().y);
//        if (verticalDirection == WorldPhysics.VerticalDirection.DOWN) {
//            return new BehaviorResult(moveAbleEntity.getVelocity(), false);
//        }
        Vector2 forceVelocity = new Vector2(moveAbleEntity.getVelocity());
        Vector2 forceMoveVelocity = entity.forceMove(forceVelocity, worldPhysics);
        Vector2 resultVelocity = new Vector2(moveAbleEntity.getVelocity());
        if(verticalDirection == WorldPhysics.VerticalDirection.UP) {
            resultVelocity.y = forceMoveVelocity.y;
        }
        return new BehaviorResult(resultVelocity, false);
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }
}
