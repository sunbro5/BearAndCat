package com.mygdx.game.behavior;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.physics.WorldPhysics;

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
            seconds += Gdx.graphics.getDeltaTime();
            levelData.setFadeOverlay(Math.min(seconds / DURATION, 1f));
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
