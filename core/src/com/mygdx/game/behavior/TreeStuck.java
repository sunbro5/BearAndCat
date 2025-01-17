package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TreeStuck implements EntityBehavior {

    private final Vector2 target;

    @Override
    public BehaviorType getType() {
        return BehaviorType.TREE_STUCK;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        //Vector2 velocityToTarget = new Vector2()
        return null;
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }
}
