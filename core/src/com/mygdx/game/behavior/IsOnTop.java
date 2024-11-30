package com.mygdx.game.behavior;

import com.mygdx.game.entity.MoveAbleEntity;

public class IsOnTop implements EntityBehavior {
    @Override
    public BehaviorType getType() {
        return BehaviorType.IS_ON_TOP;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }
}
