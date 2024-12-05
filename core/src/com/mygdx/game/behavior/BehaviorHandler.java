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
        Set<BehaviorType> behaviorTypesToRemove = new HashSet<>();
        for (Map.Entry<BehaviorType, EntityBehavior> state : states.entrySet()) {
            BehaviorResult result = state.getValue().update(moveAbleEntity, worldPhysics);
            if (result.isRemove()) {
                behaviorTypesToRemove.add(state.getKey());
            }
            CollisionHandler.setSmallerValueToVelocity(resultVelocity, result.getVelocity());
        }
        for (BehaviorType type : behaviorTypesToRemove) {
            states.remove(type);
        }
        return resultVelocity;
    }
}
