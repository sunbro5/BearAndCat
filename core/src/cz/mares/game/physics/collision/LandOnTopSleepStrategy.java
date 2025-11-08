package cz.mares.game.physics.collision;

import com.badlogic.gdx.Gdx;
import cz.mares.game.behavior.BehaviorType;
import cz.mares.game.entity.ControlAbleEntity;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;

public class LandOnTopSleepStrategy implements CollisionStrategy {

    @Override
    public boolean apply(MoveAbleEntity entity, WorldPhysics.EntityCollision collision) {
        return collision.getMoveAbleEntity().getPossibleStates().contains(BehaviorType.HAVE_ON_TOP) &&
                collision.getMoveAbleEntity().getStates().containsKey(BehaviorType.SLEEP) &&
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
            controlAbleEntity.resetIdle();
        }
        collision.getMoveAbleEntity().getStates().get(BehaviorType.SLEEP).onCollision(collision.getMoveAbleEntity());
        collision.getMoveAbleEntity().getStates().remove(BehaviorType.SLEEP);
        return new CollisionHandlerResult(entity.getVelocity());
    }
}
