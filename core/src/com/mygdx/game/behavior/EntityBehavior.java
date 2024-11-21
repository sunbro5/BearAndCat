package com.mygdx.game.behavior;

public interface EntityBehavior {
    BehaviorType getType();
    void update(float delta);

}
