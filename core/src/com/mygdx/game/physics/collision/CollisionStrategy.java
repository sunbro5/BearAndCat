package com.mygdx.game.physics.collision;

import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

public interface CollisionStrategy {
    boolean apply(WorldPhysics.EntityCollision collision);

    CollisionHandlerResult handle(MoveAbleEntity entity, WorldPhysics.EntityCollision collision);

}
