package cz.mares.game.entity;

import com.badlogic.gdx.math.Rectangle;
import cz.mares.game.level.LevelData;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ActionEntity implements GameEntity {

    public abstract void action(LevelData levelData, ControlAbleEntity controlAbleEntity);

    protected final Rectangle position;

    @Override
    public Rectangle getPosition() {
        return this.position;
    }
}
