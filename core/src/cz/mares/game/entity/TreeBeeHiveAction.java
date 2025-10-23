package cz.mares.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import cz.mares.game.behavior.BehaviorType;
import cz.mares.game.behavior.TreeBeeHiveStuck;
import cz.mares.game.level.LevelData;

public class TreeBeeHiveAction extends ActionEntity {

    private final BeeHive beeHive;

    public TreeBeeHiveAction(Rectangle position, BeeHive beeHive) {
        super(position);
        this.beeHive = beeHive;
    }

    public void action(LevelData levelData, ControlAbleEntity controlAbleEntity) {
        if (controlAbleEntity.getPossibleStates().contains(BehaviorType.BEE_HIVE_TREE_STUCK) && !controlAbleEntity.getStates().containsKey(BehaviorType.BEE_HIVE_TREE_STUCK)) {
            Gdx.app.debug("", "Beehive activated");
            ControlAbleEntity.Move direction = calculateDirection(controlAbleEntity.getPosition(), beeHive.getPosition());
            controlAbleEntity.setState(new TreeBeeHiveStuck(beeHive.getPosition(), direction, () -> levelData.getMoveAbleEntities().remove(beeHive)));
            beeHive.setActive(true);
            levelData.getActionEntities().remove(this);
        }
    }

    private ControlAbleEntity.Move calculateDirection(Rectangle controlPosition, Rectangle beeHivePosition) {
        return controlPosition.x + (controlPosition.width / 2) > beeHivePosition.x + (beeHivePosition.width / 2) ? ControlAbleEntity.Move.LEFT : ControlAbleEntity.Move.RIGHT;
    }

}
