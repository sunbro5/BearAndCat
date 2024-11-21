package com.mygdx.game.physics.collision;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

import java.util.List;

public class CollisionHandler {

    public Vector2 handleCollision(MoveAbleEntity entity, List<WorldPhysics.EntityCollision> entityCollisions, List<CollisionStrategy> possibleCollisionStrategies) {
        Vector2 resultVelocity = entity.getVelocity();
        if(entityCollisions.isEmpty()){
            return resultVelocity;
        }
        for (WorldPhysics.EntityCollision collision : entityCollisions) {
            for (CollisionStrategy collisionStrategy : possibleCollisionStrategies) {
                if (collisionStrategy.apply(collision)) {
                    CollisionHandlerResult collisionResult = collisionStrategy.handle(entity, collision);
                    setSmallerValueToVelocity(resultVelocity, collisionResult.getVelocity());
                }
            }
        }
        return resultVelocity;
    }

    private void setSmallerValueToVelocity(Vector2 resultVelocity, Vector2 velocityFromCollision) {
        if (Math.abs(resultVelocity.x) > Math.abs(velocityFromCollision.x)) {
            resultVelocity.x = velocityFromCollision.x;
        }
        if (Math.abs(resultVelocity.y) > Math.abs(velocityFromCollision.y)) {
            resultVelocity.y = velocityFromCollision.y;
        }
    }

}
