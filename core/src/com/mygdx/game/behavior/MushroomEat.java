package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.renderer.AnimationType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MushroomEat implements EntityBehavior {

    private final Runnable eatCallback;
    private boolean mushroomEat = false;

    @Override
    public BehaviorType getType() {
        return BehaviorType.MUSHROOM_EAT;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        if (moveAbleEntity instanceof ControlAbleEntity) {
            ControlAbleEntity controlAbleEntity = (ControlAbleEntity) moveAbleEntity;
            controlAbleEntity.setHaveControl(false);

            if (!mushroomEat) {
                controlAbleEntity.setAnimation(AnimationType.EAT, false);
                controlAbleEntity.getVelocity().y = 0; //to prevent jump
                mushroomEat = true;
                moveAbleEntity.getStates().remove(BehaviorType.HAVE_ON_TOP);
                moveAbleEntity.getPossibleStates().remove(BehaviorType.HAVE_ON_TOP);
            } else if (controlAbleEntity.isCustomAnimationFinished()) {
                eatCallback.run();
                controlAbleEntity.getStates().remove(BehaviorType.MUSHROOM_EAT);
                controlAbleEntity.setState(new Sleep());

            }
        }
        return new BehaviorResult(new Vector2(0, moveAbleEntity.getVelocity().y > 0 ? 0 : moveAbleEntity.getVelocity().y));
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }

    @Override
    public boolean isFinished() {
        return mushroomEat;
    }
}
