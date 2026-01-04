package cz.mares.game.behavior;

import static cz.mares.game.behavior.BehaviorType.MOVE_SOUND;

import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.sound.EntitySound;
import cz.mares.game.sound.EntitySoundType;
import cz.mares.game.sound.SoundPlayer;

public class BoxMoveSound implements EntityBehavior {

    private final static float MOVE_OFFSET_CHECK = 000.1f;

    private static final Set<Integer> boxMoving = new HashSet<>();

    private boolean lastInAir;

    private EntitySound sound;

    @Override
    public BehaviorType getType() {
        return MOVE_SOUND;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        int id = moveAbleEntity.getId();
        boolean isMoving = moveAbleEntity.getVelocity().x > MOVE_OFFSET_CHECK || moveAbleEntity.getVelocity().x < -MOVE_OFFSET_CHECK;
        if (isMoving && moveAbleEntity.isOnGround() && !boxMoving.contains(id)) {
            if(boxMoving.isEmpty()){
                sound = SoundPlayer.getSound(moveAbleEntity.getEntitySoundS(), EntitySoundType.WALK);
                SoundPlayer.playLoop(sound, moveAbleEntity.getEntitySoundS().getGameData());
            }
            boxMoving.add(id);
        } else if (boxMoving.contains(id) && (!isMoving || !moveAbleEntity.isOnGround())) {
            boxMoving.remove(id);
            if(boxMoving.isEmpty()){
                SoundPlayer.stop(sound);
            }
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
