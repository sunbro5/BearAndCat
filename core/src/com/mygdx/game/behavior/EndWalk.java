package com.mygdx.game.behavior;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

public class EndWalk implements EntityBehavior {

    private float seconds = 0;

    @Override
    public BehaviorType getType() {
        return BehaviorType.END_WALK;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        if (moveAbleEntity instanceof ControlAbleEntity) {
            ((ControlAbleEntity) moveAbleEntity).setMove(ControlAbleEntity.Move.RIGHT);
            seconds += Gdx.graphics.getDeltaTime();
        }
        return new BehaviorResult(moveAbleEntity.getVelocity());
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }

    @Override
    public boolean isFinished() {
        return seconds > 1;
    }
}
