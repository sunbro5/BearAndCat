package cz.mares.game.physics.collision;

import cz.mares.game.behavior.BehaviorType;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;

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
