package cz.mares.game.behavior;

import com.badlogic.gdx.math.Vector2;

import cz.mares.game.entity.ControlAbleEntity;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.physics.WorldPhysics;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FlyUp implements EntityBehavior {

    private float time;
    @Override
    public BehaviorType getType() {
        return BehaviorType.FLY_UP;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) {
        if (moveAbleEntity instanceof ControlAbleEntity) {
            ControlAbleEntity controlAbleEntity = (ControlAbleEntity) moveAbleEntity;
            float flyVelocity = controlAbleEntity.getJumpVelocity() / 2;
            flyVelocity = flyVelocity * worldPhysics.getLastDelta();
            Vector2 velocity = new Vector2(moveAbleEntity.getVelocity().x, flyVelocity);
            time+= worldPhysics.getLastDelta();
            if(time > 4){
                moveAbleEntity.getStates().remove(BehaviorType.FLY_UP);
            }
            return new BehaviorResult(velocity, true);
        }
        return new BehaviorResult(moveAbleEntity.getVelocity());
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
