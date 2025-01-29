package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.entity.PickAbleEntity;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.renderer.AnimationType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TreeBeeHiveStuck implements EntityBehavior {

    private final Rectangle target;
    private final ControlAbleEntity.Move direction;
    private final Runnable eatCallback;

    private boolean isClose = false;
    private boolean beeHiveFall = false;
    private boolean howl = false;
    private boolean eating = false;

    @Override
    public BehaviorType getType() {
        return BehaviorType.BEE_HIVE_TREE_STUCK;
    }

    @Override
    public BehaviorResult update(MoveAbleEntity moveAbleEntity, WorldPhysics worldPhysics) { // TODO this looks like good candidate for state pattern
        Vector2 resultVelocity = new Vector2(moveAbleEntity.getVelocity());
        if (moveAbleEntity instanceof ControlAbleEntity) {
            ControlAbleEntity controlAbleEntity = (ControlAbleEntity) moveAbleEntity;
            controlAbleEntity.setHaveControl(false);
            if (!isClose) {
                if (isClose(moveAbleEntity.getPosition(), target)) {
                    isClose = true;
                } else {
                    controlAbleEntity.setMove(direction);
                }
            } else {
                if (beeHiveFall) {
                    if (!eating) {
                        controlAbleEntity.setAnimation(AnimationType.EAT, false);
                        eating = true;

                    } else {
                        if (controlAbleEntity.isCustomAnimationFinished()) {
                            eatCallback.run();
                            controlAbleEntity.setAnimation(null, false);
                            controlAbleEntity.setHaveControl(true);
                            controlAbleEntity.getStates().remove(BehaviorType.BEE_HIVE_TREE_STUCK);
                            moveAbleEntity.getPossibleStates().add(BehaviorType.HAVE_ON_TOP);
                        }
                    }
                } else {
                    if (!howl) {
                        controlAbleEntity.setAnimation(AnimationType.HOWL, true);
                        moveAbleEntity.getStates().remove(BehaviorType.HAVE_ON_TOP);
                        moveAbleEntity.getPossibleStates().remove(BehaviorType.HAVE_ON_TOP);
                        howl = true;
                    }
                }
            }
        }
        return new BehaviorResult(resultVelocity);
    }

    private boolean isClose(Rectangle control, Rectangle beeHive) {
        return control.x < beeHive.x + beeHive.width && control.x + control.width > beeHive.x;
    }

    @Override
    public void onCollision(MoveAbleEntity moveAbleEntity) {
        beeHiveFall = true;
    }

    @Override
    public boolean isFinished() {
        return isClose && beeHiveFall && eating;
    }
}
