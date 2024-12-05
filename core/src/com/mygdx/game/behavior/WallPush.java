package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WallPush implements EntityBehavior {

    private final MoveAbleEntity entity;

    @Getter
    protected int forcePushCount = 0;

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
        return new BehaviorResult(moveAbleEntity.getVelocity(), false);
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {
        wasPushed = true;
        forcePushCount++; // TODO DELTA ?

        if (forcePushCount > 20) {
            WorldPhysics.swapEntities(entity, moveAbleEntity);
        }
    }
}
