package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.SlowDown;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.physics.WorldPhysics;

public class Web extends ActionEntity implements DrawableEntity {

    private final Texture texture;

    public Web(Rectangle position, Texture texture) {
        super(position);
        this.texture = texture;
    }

    @Override
    public void action(LevelData levelData, ControlAbleEntity controlAbleEntity) {
        if (!controlAbleEntity.getPossibleStates().contains(BehaviorType.SLOW)) {
            return;
        }
        if (controlAbleEntity.getStates().containsKey(BehaviorType.SLOW)) {
            controlAbleEntity.getStates().get(BehaviorType.SLOW).onCollision(controlAbleEntity);
        } else {
            controlAbleEntity.setState(new SlowDown());
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, position.x, position.y, position.width, position.height);
    }

    @Override
    public void update(float delta, WorldPhysics worldPhysics) {

    }

    @Override
    public void afterUpdate() {

    }
}
