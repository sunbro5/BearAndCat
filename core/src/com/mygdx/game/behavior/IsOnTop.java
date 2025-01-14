package com.mygdx.game.behavior;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IsOnTop implements EntityBehavior {

    private final float OFFSET_CHECK = -1f;

    private final MoveAbleEntity entity;

    @Override
    public BehaviorType getType() {
        return BehaviorType.IS_ON_TOP;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        Rectangle haveOnTopPosition = new Rectangle(entity.getPosition().x, entity.getPosition().y + OFFSET_CHECK, entity.getPosition().width, entity.getPosition().height);
        Rectangle checkPosition = new Rectangle(moveAbleEntity.getPosition());
        checkPosition.y += Math.min(entity.getVelocity().y + OFFSET_CHECK, OFFSET_CHECK);
        WorldPhysics.VerticalDirection direction = WorldPhysics.VerticalDirection.of(moveAbleEntity.getVelocity().y);
        if (direction == WorldPhysics.VerticalDirection.UP || haveOnTopPosition.overlaps(moveAbleEntity.getPosition()) || !entity.getPosition().overlaps(checkPosition)) {
            Gdx.app.log("", "Not on top");
            return new BehaviorResult(moveAbleEntity.getVelocity(), true);
        }
        Vector2 velocity = new Vector2(moveAbleEntity.getVelocity());
        return new BehaviorResult(velocity, false);
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {

    }
}
