package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.MushroomSleep;
import com.mygdx.game.level.LevelData;

public class Mushroom extends PickAbleEntity {

    public Mushroom(Rectangle position, Texture texture) {
        super(position, texture);
    }

    @Override
    public void onPick(LevelData levelData, ControlAbleEntity controlAbleEntity) {
        if (controlAbleEntity.getPossibleStates().contains(BehaviorType.SLEEP) && !controlAbleEntity.getStates().containsKey(BehaviorType.SLEEP)) {
            PickAbleEntity pickAbleEntity = this;
            controlAbleEntity.getStates().put(BehaviorType.SLEEP, new MushroomSleep(controlAbleEntity.getDirection(),
                    () -> levelData.getPickAbleEntities().remove(pickAbleEntity)));

        }
    }
}
