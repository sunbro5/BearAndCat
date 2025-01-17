package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.physics.collision.CollisionHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BehaviorHandler {

    public static Vector2 handleBehavior(Map<BehaviorType, EntityBehavior> states, MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        Vector2 resultVelocity = new Vector2(moveAbleEntity.getVelocity());
        for (Map.Entry<BehaviorType, EntityBehavior> state : new HashSet<>(states.entrySet())) {
            BehaviorResult result = state.getValue().update(moveAbleEntity, worldPhysics);
            CollisionHandler.setSmallerValueToVelocity(resultVelocity, result.getVelocity());
        }
        return resultVelocity;
    }
}
