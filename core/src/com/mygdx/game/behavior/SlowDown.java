package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

public class SlowDown implements EntityBehavior {

    private boolean reset = true;

    @Override
    public BehaviorType getType() {
        return BehaviorType.SLOW;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        if (reset) {
            reset = false;
        } else {
            moveAbleEntity.getStates().remove(BehaviorType.SLOW);
            return new BehaviorResult(moveAbleEntity.getVelocity());
        }
        Vector2 slowedVelocity = new Vector2(moveAbleEntity.getVelocity().x / 4, moveAbleEntity.getVelocity().y / 2);
        return new BehaviorResult(slowedVelocity);
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {
        reset = true;
    }

    @Override
    public boolean isFinished() {
        return !reset;
    }
}
