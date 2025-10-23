package cz.mares.game.behavior;

import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.level.LevelData;
import cz.mares.game.physics.WorldPhysics;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StartFade implements EntityBehavior {

    private static final float DURATION = 1f;
    private float seconds = 0;
    private final LevelData levelData;

    @Override
    public BehaviorType getType() {
        return BehaviorType.START_FADE;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        seconds += worldPhysics.getLastDelta();
        levelData.setFadeOverlay(1 - Math.min(seconds / DURATION, 1f));
        if (isFinished()) {
            moveAbleEntity.getStates().remove(BehaviorType.START_FADE);
        }
        return new BehaviorResult(moveAbleEntity.getVelocity());
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }

    @Override
    public boolean isFinished() {
        return seconds > DURATION;
    }
}
