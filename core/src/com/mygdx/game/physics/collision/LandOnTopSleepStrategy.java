package com.mygdx.game.physics.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.HaveOnTop;
import com.mygdx.game.behavior.IsOnTop;
import com.mygdx.game.entity.Box;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

public class LandOnTopSleepStrategy implements CollisionStrategy {

    @Override
    public boolean apply(MoveAbleEntity entity, WorldPhysics.EntityCollision collision) {
        return collision.getMoveAbleEntity().getStates().containsKey(BehaviorType.SLEEP) &&
                collision.getVerticalDirection() == WorldPhysics.VerticalDirection.DOWN &&
                collision.getMoveAbleEntity().getStates().get(BehaviorType.SLEEP).isFinished();
    }

    @Override
    public CollisionHandlerResult handle(MoveAbleEntity entity, WorldPhysics.EntityCollision collision, WorldPhysics worldPhysics) {
        Gdx.app.debug("","Lands on top of sleeping " + collision.getMoveAbleEntity().toString());
        if(collision.getMoveAbleEntity() instanceof ControlAbleEntity){
            ControlAbleEntity controlAbleEntity = (ControlAbleEntity) collision.getMoveAbleEntity();
            controlAbleEntity.setHaveControl(true);
            controlAbleEntity.setAnimation(null, false);
        }
        collision.getMoveAbleEntity().getStates().remove(BehaviorType.SLEEP);
        return new CollisionHandlerResult(entity.getVelocity());
    }
}
