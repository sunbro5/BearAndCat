package cz.mares.game.physics.collision;

import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;

public interface CollisionStrategy {
    boolean apply(MoveAbleEntity entity, WorldPhysics.EntityCollision collision);

    CollisionHandlerResult handle(MoveAbleEntity entity, WorldPhysics.EntityCollision collision, WorldPhysics worldPhysics);

}
