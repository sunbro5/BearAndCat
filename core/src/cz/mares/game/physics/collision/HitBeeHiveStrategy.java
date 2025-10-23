package cz.mares.game.physics.collision;

import com.badlogic.gdx.Gdx;

import cz.mares.game.entity.BeeHive;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;

public class HitBeeHiveStrategy implements CollisionStrategy {

    @Override
    public boolean apply(MoveAbleEntity entity, WorldPhysics.EntityCollision collision) {
        return collision.getMoveAbleEntity() instanceof BeeHive;
    }

    @Override
    public CollisionHandlerResult handle(MoveAbleEntity entity, WorldPhysics.EntityCollision collision, WorldPhysics worldPhysics) {
        Gdx.app.debug("", "Hit beehive " + collision.getMoveAbleEntity().toString());
        BeeHive beeHive = (BeeHive) collision.getMoveAbleEntity();
        if (beeHive.isActive()) {
            beeHive.setHit(true);
        }
        return new CollisionHandlerResult(entity.getVelocity());
    }
}
