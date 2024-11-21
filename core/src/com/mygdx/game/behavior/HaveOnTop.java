package com.mygdx.game.behavior;

public class HaveOnTop implements EntityBehavior {
    @Override
    public BehaviorType getType() {
        return BehaviorType.HAVE_ON_TOP;
    }

    @Override
    public void update(float delta) {
        // TODO move TOP
    }
}
