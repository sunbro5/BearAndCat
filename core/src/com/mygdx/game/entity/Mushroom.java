package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.MushroomEat;
import com.mygdx.game.level.LevelData;

public class Mushroom extends PickAbleEntity {

    public Mushroom(Rectangle position, Texture texture) {
        super(position, texture);
    }

    @Override
    public void onPick(LevelData levelData, ControlAbleEntity controlAbleEntity) {
        if (controlAbleEntity.getPossibleStates().contains(BehaviorType.MUSHROOM_EAT) && !controlAbleEntity.getStates().containsKey(BehaviorType.MUSHROOM_EAT)) {
            PickAbleEntity pickAbleEntity = this;
            controlAbleEntity.setState(new MushroomEat(
                    () -> levelData.getPickAbleEntities().remove(pickAbleEntity)));

        }
    }
}
