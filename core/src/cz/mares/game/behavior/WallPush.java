package cz.mares.game.behavior;

import com.badlogic.gdx.Gdx;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WallPush implements EntityBehavior {

    private final MoveAbleEntity entity;

    @Getter
    protected float forcePushCount = 0;

    @Getter
    protected boolean wasPushed = false;

    @Override
    public BehaviorType getType() {
        return BehaviorType.WALL_PUSH;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        if (wasPushed) {
            wasPushed = false;
        } else {
            forcePushCount = 0;
        }
        return new BehaviorResult(moveAbleEntity.getVelocity());
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {
        wasPushed = true;
        forcePushCount += Gdx.graphics.getDeltaTime();

        if (forcePushCount > 0.7f) {
            moveAbleEntity.getStates().remove(getType());
            WorldPhysics.swapEntities(entity, moveAbleEntity);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
