package cz.mares.game.behavior;

import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;

public interface EntityBehavior {
    BehaviorType getType();
    BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics);
    void onCollision(MoveAbleEntity moveAbleEntity);
    boolean isFinished();
}
