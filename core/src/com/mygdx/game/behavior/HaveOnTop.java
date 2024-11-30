package com.mygdx.game.behavior;

import com.mygdx.game.entity.MoveAbleEntity;

public class HaveOnTop implements EntityBehavior {
    @Override
    public BehaviorType getType() {
        return BehaviorType.HAVE_ON_TOP;
    }

    @Override
    public void update(float delta) {
        // TODO move TOP
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }
}
