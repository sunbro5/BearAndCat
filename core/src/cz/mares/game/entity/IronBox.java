package cz.mares.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import cz.mares.game.behavior.BehaviorType;
import cz.mares.game.behavior.MoveSound;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.physics.collision.CollisionStrategy;
import cz.mares.game.physics.collision.LandOnTopStrategy;
import cz.mares.game.physics.collision.PushStrategy;
import cz.mares.game.sound.EntitySounds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IronBox extends Box {

    public IronBox(Rectangle rectangle, Texture texture, EntitySounds entitySoundS) {
        super(rectangle, texture, entitySoundS);
    }

    @Override
    protected List<CollisionStrategy> initCollisionStrategies() {
        List<CollisionStrategy> strategies = new ArrayList<>();
        strategies.add(new PushStrategy());
        strategies.add(new LandOnTopStrategy());
        return strategies;
    }

    @Override
    public int getWeight() {
        return 100;
    }
}
