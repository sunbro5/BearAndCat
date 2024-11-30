package com.mygdx.game.behavior;

import com.mygdx.game.entity.MoveAbleEntity;

public interface EntityBehavior {
    BehaviorType getType();
    void update(float delta);

    void onCollision(MoveAbleEntity moveAbleEntity);

}
