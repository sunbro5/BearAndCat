package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SlowDown implements EntityBehavior {

    private boolean reset = true;
    private int value = 4;

    public SlowDown(int value) {
        this.value = value;
    }

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
        Vector2 slowedVelocity = new Vector2(moveAbleEntity.getVelocity().x / value, moveAbleEntity.getVelocity().y / 2);
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
