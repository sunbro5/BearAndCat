package cz.mares.game.physics.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import cz.mares.game.behavior.BehaviorType;
import cz.mares.game.behavior.HaveOnTop;
import cz.mares.game.behavior.IsOnTop;
import cz.mares.game.entity.Box;
import cz.mares.game.entity.ControlAbleEntity;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;

public class LandOnTopStrategy implements CollisionStrategy {

    @Override
    public boolean apply(MoveAbleEntity entity, WorldPhysics.EntityCollision collision) {
        return !(entity instanceof Box && collision.getMoveAbleEntity() instanceof ControlAbleEntity) &&
                collision.getVerticalDirection() == WorldPhysics.VerticalDirection.DOWN &&
                collision.getMoveAbleEntity().canBeOnTop() &&
                entity.getPossibleStates().contains(BehaviorType.IS_ON_TOP) &&
                collision.getMoveAbleEntity().getPossibleStates().contains(BehaviorType.HAVE_ON_TOP);
    }

    @Override
    public CollisionHandlerResult handle(MoveAbleEntity entity, WorldPhysics.EntityCollision collision, WorldPhysics worldPhysics) {
        Gdx.app.debug("","Lands " + entity + " on top of " + collision.getMoveAbleEntity());
        Vector2 resultVelocity = new Vector2(entity.getVelocity());
        resultVelocity.y = collision.getVelocityToCollision().y;
        if (entity.getStates().containsKey(BehaviorType.IS_ON_TOP)) {
            resultVelocity = new Vector2(entity.getVelocity());
            resultVelocity.y = collision.getMoveAbleEntity().getVelocity().y;
            return new CollisionHandlerResult(resultVelocity);
        }
        entity.setState(new IsOnTop(collision.getMoveAbleEntity()));
        collision.getMoveAbleEntity().setState(new HaveOnTop(entity));
        return new CollisionHandlerResult(resultVelocity);
    }
}
