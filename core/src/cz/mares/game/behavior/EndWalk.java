package cz.mares.game.behavior;

import cz.mares.game.entity.ControlAbleEntity;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.level.LevelData;
import cz.mares.game.physics.WorldPhysics;

public class EndWalk implements EntityBehavior {

    private static final float DURATION = 1.5f;
    private final boolean main;
    private float seconds = 0;

    private final LevelData levelData;

    public EndWalk(boolean main, LevelData levelData) {
        this.main = main;
        this.levelData = levelData;
    }

    @Override
    public BehaviorType getType() {
        return BehaviorType.END_WALK;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        if (moveAbleEntity instanceof ControlAbleEntity) {
            ((ControlAbleEntity) moveAbleEntity).setMove(ControlAbleEntity.Move.RIGHT);
            if (main) {
                seconds += worldPhysics.getLastDelta();
                levelData.setFadeOverlay(Math.min(seconds / DURATION, 1f));
            }
        }
        return new BehaviorResult(moveAbleEntity.getVelocity());
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }

    @Override
    public boolean isFinished() {
        return !main || seconds > DURATION;
    }

}
