package cz.mares.game.behavior;

import static cz.mares.game.behavior.BehaviorType.MOVE_SOUND;

import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.sound.EntitySoundType;
import cz.mares.game.sound.SoundPlayer;

public class MoveSound implements EntityBehavior {

    private final static float MOVE_OFFSET_CHECK = 1;

    private boolean playing;
    private Long walkSoundId;

    private boolean lastInAir;

    @Override
    public BehaviorType getType() {
        return MOVE_SOUND;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        boolean isMoving = moveAbleEntity.getVelocity().x > MOVE_OFFSET_CHECK || moveAbleEntity.getVelocity().x < -MOVE_OFFSET_CHECK;
        if (isMoving && !playing && moveAbleEntity.isOnGround()) {
            walkSoundId = SoundPlayer.playLoop(moveAbleEntity.getEntitySoundS(), EntitySoundType.WALK);
            playing = true;
        }
        if (!isMoving || !moveAbleEntity.isOnGround()) {
            SoundPlayer.stopPlayLoop(moveAbleEntity.getEntitySoundS(), EntitySoundType.WALK, walkSoundId);
            playing = false;
        }

        if (lastInAir && !moveAbleEntity.inAir()) {
            SoundPlayer.play(moveAbleEntity.getEntitySoundS(), EntitySoundType.LAND);
        }
        lastInAir = moveAbleEntity.inAir();
        return new BehaviorResult(moveAbleEntity.getVelocity());
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
