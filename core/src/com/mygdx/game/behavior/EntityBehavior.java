package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

public interface EntityBehavior {
    BehaviorType getType();
    BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics);

    void onCollision(MoveAbleEntity moveAbleEntity);

}
