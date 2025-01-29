package com.mygdx.game.entity;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.level.LevelData;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ActionEntity implements GameEntity {

    public abstract void action(LevelData levelData, ControlAbleEntity controlAbleEntity);

    @Getter
    protected final Rectangle position;
}
