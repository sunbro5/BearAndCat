package com.mygdx.game.physics.collision;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.entity.BeeHive;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

public class HitBeeHiveStrategy implements CollisionStrategy {

    @Override
    public boolean apply(MoveAbleEntity entity, WorldPhysics.EntityCollision collision) {
        return collision.getMoveAbleEntity() instanceof BeeHive;
    }

    @Override
    public CollisionHandlerResult handle(MoveAbleEntity entity, WorldPhysics.EntityCollision collision, WorldPhysics worldPhysics) {
        Gdx.app.debug("","Hit beehive " + collision.getMoveAbleEntity().toString());
        BeeHive beeHive = (BeeHive) collision.getMoveAbleEntity();
        if(beeHive.isActive()){
            beeHive.setHit(true);
        }
        return new CollisionHandlerResult(entity.getVelocity());
    }
}
