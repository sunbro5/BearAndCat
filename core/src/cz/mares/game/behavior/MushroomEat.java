package cz.mares.game.behavior;

import com.badlogic.gdx.math.Vector2;

import cz.mares.game.entity.ControlAbleEntity;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.renderer.AnimationType;
import cz.mares.game.sound.EntitySoundType;
import cz.mares.game.sound.SoundPlayer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MushroomEat implements EntityBehavior {

    private final EntityBehavior afterEatEntityBehavior;

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
                if (eatFrame != null) {
                    if (eatFrame == 6 && currentEatFrame != 6) {
                        SoundPlayer.play(controlAbleEntity.getEntitySoundS(), EntitySoundType.EAT);
                        eatCallback.run();
                    }
                    currentEatFrame = eatFrame;
                }
            }
            if (controlAbleEntity.isCustomAnimationFinished()) {
                controlAbleEntity.setHaveControl(true);
                controlAbleEntity.setAnimation(null, false);
                controlAbleEntity.getStates().remove(BehaviorType.MUSHROOM_EAT);
                controlAbleEntity.setState(afterEatEntityBehavior);

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
