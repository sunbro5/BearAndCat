package cz.mares.game.behavior;

import com.badlogic.gdx.math.Vector2;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.physics.collision.CollisionHandler;

import java.util.HashSet;
import java.util.Map;

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
