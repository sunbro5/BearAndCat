package com.mygdx.game.behavior;

import lombok.Getter;

public class WallPush implements EntityBehavior {

    @Getter
    protected int forcePushCount = 0;

    @Getter
    protected boolean wasPushed = false;

    public void push(){
        wasPushed = true;
        forcePushCount++;
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
}
