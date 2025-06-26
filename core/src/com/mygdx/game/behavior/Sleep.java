package com.mygdx.game.behavior;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.renderer.AnimationType;
import com.mygdx.game.sound.EntitySoundType;
import com.mygdx.game.sound.SoundPlayer;

public class Sleep implements EntityBehavior {
    private boolean sleepAnimationSet = false;
    private Long soundId;
    private Sound sound;

    public Sleep() {
    }

    @Override
    public BehaviorType getType() {
        return BehaviorType.SLEEP;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        if (moveAbleEntity instanceof ControlAbleEntity) {
            ControlAbleEntity controlAbleEntity = (ControlAbleEntity) moveAbleEntity;
            controlAbleEntity.setHaveControl(false);

            if (!sleepAnimationSet) {
                sound = SoundPlayer.getSound(controlAbleEntity.getEntitySound(), EntitySoundType.SLEEP);
                soundId = SoundPlayer.playLoop(controlAbleEntity.getEntitySound(), EntitySoundType.SLEEP);
                SoundPlayer.setPitch(sound, soundId, 0.8f);
                controlAbleEntity.setAnimation(AnimationType.SLEEP, true);
                sleepAnimationSet = true;
                moveAbleEntity.getPossibleStates().add(BehaviorType.HAVE_ON_TOP);
            }

        }
        return new BehaviorResult(new Vector2(0, moveAbleEntity.getVelocity().y > 0 ? 0 : moveAbleEntity.getVelocity().y));
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {
        SoundPlayer.stopPlayLoop(sound, soundId);
    }

    @Override
    public boolean isFinished() {
        return sleepAnimationSet;
    }
}
