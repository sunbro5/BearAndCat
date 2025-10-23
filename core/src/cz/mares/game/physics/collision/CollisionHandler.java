package cz.mares.game.physics.collision;

import com.badlogic.gdx.math.Vector2;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;

import java.util.List;

public class CollisionHandler {

    public static Vector2 handleCollision(MoveAbleEntity entity, List<WorldPhysics.EntityCollision> entityCollisions, List<CollisionStrategy> possibleCollisionStrategies, WorldPhysics worldPhysics) {
        Vector2 resultVelocity = entity.getVelocity();
        if (entityCollisions.isEmpty()) {
            return resultVelocity;
        }
        for (WorldPhysics.EntityCollision collision : entityCollisions) {
            for (CollisionStrategy collisionStrategy : possibleCollisionStrategies) {
                if (collisionStrategy.apply(entity, collision)) {
                    correctCollisionToVelocity(entity, collision);
                    CollisionHandlerResult collisionResult = collisionStrategy.handle(entity, collision, worldPhysics);
                    setSmallerValueToVelocity(resultVelocity, collisionResult.getVelocity());
                }
            }
        }
        return resultVelocity;
    }

    private static void correctCollisionToVelocity(MoveAbleEntity entity, WorldPhysics.EntityCollision collision) {
        if (collision.getMoveAbleEntity().isWasForceMoved()) {
            Vector2 velocityToCollision = WorldPhysics.calculateVelocityToCollision(entity, collision.getMoveAbleEntity(), collision.getVerticalDirection(), collision.getHorizontalDirection());
            collision.setVelocityToCollision(velocityToCollision);
        }
    }

    public static void setSmallerValueToVelocity(Vector2 resultVelocity, Vector2 velocityFromCollision) { // TODO utils ?
        if (Math.abs(resultVelocity.x) > Math.abs(velocityFromCollision.x)) {
            resultVelocity.x = velocityFromCollision.x;
        }
        if (Math.abs(resultVelocity.y) > Math.abs(velocityFromCollision.y)) {
            resultVelocity.y = velocityFromCollision.y;
        }
    }

}
