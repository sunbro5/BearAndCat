package com.mygdx.game.physics.collision;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.WallPush;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

public class PushStrategy implements CollisionStrategy {

    @Override
    public boolean apply(MoveAbleEntity entity, WorldPhysics.EntityCollision collision) {
        return collision.getMoveAbleEntity().canBePush() && collision.getVerticalDirection() != null;
    }

    @Override
    public CollisionHandlerResult handle(MoveAbleEntity entity, WorldPhysics.EntityCollision collision, WorldPhysics worldPhysics) {
        Vector2 velocity = new Vector2(entity.getVelocity().x, 0);
        float velocityOffset = collision.getVelocityToCollision().x;
        velocity.x -= velocityOffset;
        Vector2 resultVelocity = collision.getMoveAbleEntity().forceMove(velocity, worldPhysics);
        resultVelocity.x += velocityOffset;

        if (resultVelocity.x == 0) {
            if (collision.getMoveAbleEntity().getPossibleStates().contains(BehaviorType.WALL_PUSH)) {
                if (collision.getMoveAbleEntity().getStates().containsKey(BehaviorType.WALL_PUSH)) {
                    collision.getMoveAbleEntity().getStates().get(BehaviorType.WALL_PUSH).onCollision(entity);
                } else {
                    collision.getMoveAbleEntity().getStates().put(BehaviorType.WALL_PUSH, new WallPush(collision.getMoveAbleEntity()));
                }
            }
        }

        return new CollisionHandlerResult(resultVelocity);
    }
}
