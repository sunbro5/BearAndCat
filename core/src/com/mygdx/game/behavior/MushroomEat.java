package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.renderer.AnimationType;
import com.mygdx.game.sound.EntitySoundType;
import com.mygdx.game.sound.SoundPlayer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MushroomEat implements EntityBehavior {

    private final Runnable eatCallback;
    private boolean mushroomEat = false;
    private int currentEatFrame = 0;
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
            } else {
                Integer eatFrame = controlAbleEntity.getCustomAnimationIndex();
                if(eatFrame != null){
                    if(eatFrame == 6 && currentEatFrame != 6){
                        SoundPlayer.play(controlAbleEntity.getEntitySound(), EntitySoundType.EAT);
                        eatCallback.run();
                    }
                    currentEatFrame = eatFrame;
                }
            }
            if (controlAbleEntity.isCustomAnimationFinished()) {
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
