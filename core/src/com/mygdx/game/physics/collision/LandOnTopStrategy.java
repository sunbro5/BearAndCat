package com.mygdx.game.physics.collision;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.HaveOnTop;
import com.mygdx.game.behavior.IsOnTop;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

public class LandOnTopStrategy implements CollisionStrategy {

    @Override
    public boolean apply(MoveAbleEntity entity, WorldPhysics.EntityCollision collision) {
        return collision.getHorizontalDirection() == WorldPhysics.HorizontalDirection.DOWN &&
                collision.getMoveAbleEntity().canBeOnTop() &&
                entity.getPossibleStates().contains(BehaviorType.IS_ON_TOP) &&
                collision.getMoveAbleEntity().getPossibleStates().contains(BehaviorType.HAVE_ON_TOP);
    }

    @Override
    public CollisionHandlerResult handle(MoveAbleEntity entity, WorldPhysics.EntityCollision collision, WorldPhysics worldPhysics) {
        Vector2 resultVelocity = new Vector2(entity.getVelocity());
        resultVelocity.y = collision.getVelocityToCollision().y;
        if (entity.getStates().containsKey(BehaviorType.IS_ON_TOP)) {
            return new CollisionHandlerResult(resultVelocity);
        }
        entity.getStates().put(BehaviorType.IS_ON_TOP, new IsOnTop(collision.getMoveAbleEntity()));
        collision.getMoveAbleEntity().getStates().put(BehaviorType.HAVE_ON_TOP, new HaveOnTop(entity));
        return new CollisionHandlerResult(resultVelocity);
    }
}
