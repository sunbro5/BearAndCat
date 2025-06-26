package com.mygdx.game.behavior;

import com.mygdx.game.entity.Bear;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.renderer.AnimationType;

public class Idle implements EntityBehavior {

    private boolean sitDown = false;

    @Override
    public BehaviorType getType() {
        return BehaviorType.IDLE;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        if (moveAbleEntity instanceof Bear) {
            if(moveAbleEntity.getStates().containsKey(BehaviorType.SLEEP)){
                return new BehaviorResult(moveAbleEntity.getVelocity());
            }
            Bear bear = (Bear) moveAbleEntity;
            bear.setState(new Sleep());
            bear.getPossibleStates().remove(BehaviorType.HAVE_ON_TOP);
            bear.getStates().remove(BehaviorType.HAVE_ON_TOP);
            bear.getStates().remove(BehaviorType.IDLE);
            return new BehaviorResult(moveAbleEntity.getVelocity());
        }
        if (moveAbleEntity instanceof ControlAbleEntity) {
            ControlAbleEntity controlAbleEntity = (ControlAbleEntity) moveAbleEntity;
            if (!sitDown) {
                controlAbleEntity.setAnimation(AnimationType.IDLE, false);
                sitDown = true;
            } else {
                if (controlAbleEntity.getIdleValue() > 0) {
                    controlAbleEntity.setAnimation(null, false);
                    controlAbleEntity.getStates().remove(BehaviorType.IDLE);
                }
            }
        }
        return new BehaviorResult(moveAbleEntity.getVelocity());
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
