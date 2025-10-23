package cz.mares.game.behavior;

import com.badlogic.gdx.math.Vector2;
import cz.mares.game.entity.ControlAbleEntity;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.renderer.AnimationType;
import cz.mares.game.sound.EntitySound;
import cz.mares.game.sound.EntitySoundType;
import cz.mares.game.sound.SoundPlayer;

public class Sleep implements EntityBehavior {
    private boolean sleepAnimationSet = false;
    private Long soundId;
    private EntitySound sound;

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
                sound = SoundPlayer.getSound(controlAbleEntity.getEntitySoundS(), EntitySoundType.SLEEP);
                soundId = SoundPlayer.playLoop(controlAbleEntity.getEntitySoundS(), EntitySoundType.SLEEP);
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
