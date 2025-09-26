package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Slow implements EntityBehavior {
    private int value;

    public Slow(int value) {
        this.value = value;
    }

    @Override
    public BehaviorType getType() {
        return BehaviorType.SLOW;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        Vector2 slowedVelocity = new Vector2(moveAbleEntity.getVelocity().x / value, moveAbleEntity.getVelocity().y / 2);
        return new BehaviorResult(slowedVelocity);
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
