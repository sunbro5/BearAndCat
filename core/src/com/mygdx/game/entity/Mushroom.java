package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.MushroomEat;
import com.mygdx.game.level.LevelData;

import lombok.Getter;

public class Mushroom extends PickAbleEntity {

    @Getter
    protected final Rectangle drawRectangle;

    public Mushroom(Rectangle position, Rectangle drawRectangle, Texture texture) {
        super(position, texture);
        this.drawRectangle = drawRectangle;
    }

    @Override
    public void onPick(LevelData levelData, ControlAbleEntity controlAbleEntity) {
        if (controlAbleEntity.getPossibleStates().contains(BehaviorType.MUSHROOM_EAT) && !controlAbleEntity.getStates().containsKey(BehaviorType.MUSHROOM_EAT)) {
            PickAbleEntity pickAbleEntity = this;
            controlAbleEntity.setState(new MushroomEat(
                    () -> levelData.getPickAbleEntities().remove(pickAbleEntity)));

        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, drawRectangle.x, drawRectangle.y, drawRectangle.width, drawRectangle.height);
    }
}
