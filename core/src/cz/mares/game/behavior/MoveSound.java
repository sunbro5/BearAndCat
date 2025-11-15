package cz.mares.game.behavior;

import static cz.mares.game.behavior.BehaviorType.MOVE_SOUND;

import com.badlogic.gdx.Gdx;

import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.sound.EntitySound;
import cz.mares.game.sound.EntitySoundType;
import cz.mares.game.sound.SoundPlayer;

public class MoveSound implements EntityBehavior {

    private final static float MOVE_OFFSET_CHECK = 000.1f;

    private boolean playing;

    private boolean lastInAir;

    private EntitySound sound;

    @Override
    public BehaviorType getType() {
        return MOVE_SOUND;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        boolean isMoving = moveAbleEntity.getVelocity().x > MOVE_OFFSET_CHECK || moveAbleEntity.getVelocity().x < -MOVE_OFFSET_CHECK;
        if (isMoving && !playing && moveAbleEntity.isOnGround()) {
            sound = SoundPlayer.getSound(moveAbleEntity.getEntitySoundS(), EntitySoundType.WALK);
            SoundPlayer.playLoop(sound, moveAbleEntity.getEntitySoundS().getGameData());
            playing = true;
        }
        if ((!isMoving || !moveAbleEntity.isOnGround())) {
            SoundPlayer.stop(sound);
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
