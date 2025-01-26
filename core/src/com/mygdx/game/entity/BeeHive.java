package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.level.LevelData;

public class BeeHive extends PickAbleEntity {

    public BeeHive(Rectangle position, Texture texture) {
        super(position, texture);
    }

    @Override
    public void onPick(LevelData levelData, ControlAbleEntity controlAbleEntity) {

    }
}
