package com.mygdx.game.behavior;

import static com.mygdx.game.behavior.BehaviorType.MOVE_SOUND;

import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.sound.EntitySoundType;
import com.mygdx.game.sound.SoundPlayer;

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
