package com.mygdx.game.physics.collision;

import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

public class BeeHiveLandOnTopStrategy implements CollisionStrategy {

    @Override
    public boolean apply(MoveAbleEntity entity, WorldPhysics.EntityCollision collision) {
        return collision.getMoveAbleEntity().getStates().containsKey(BehaviorType.BEE_HIVE_TREE_STUCK) && !collision.getMoveAbleEntity().getStates().get(BehaviorType.BEE_HIVE_TREE_STUCK).isFinished();
    }

    @Override
    public CollisionHandlerResult handle(MoveAbleEntity entity, WorldPhysics.EntityCollision collision, WorldPhysics worldPhysics) {
        collision.getMoveAbleEntity().getStates().get(BehaviorType.BEE_HIVE_TREE_STUCK).onCollision(entity);
        return new CollisionHandlerResult(entity.getVelocity());
    }
}
