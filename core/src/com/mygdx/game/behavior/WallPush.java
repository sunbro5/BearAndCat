package com.mygdx.game.behavior;

import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

import lombok.Getter;

public class WallPush implements EntityBehavior {

    private final MoveAbleEntity entity;

    @Getter
    protected int forcePushCount = 0;

    @Getter
    protected boolean wasPushed = false;

    public WallPush(MoveAbleEntity entity){
        this.entity = entity;
    }

    @Override
    public BehaviorType getType() {
        return BehaviorType.WALL_PUSH;
    }

    @Override
    public void update(float delta) {
        if (wasPushed) {
            wasPushed = false;
        } else {
            forcePushCount = 0;
        }
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {
        wasPushed = true;
        forcePushCount++; // TODO DELTA ?

        if(forcePushCount > 20){
            WorldPhysics.swapEntities(entity, moveAbleEntity);
        }
    }
}
