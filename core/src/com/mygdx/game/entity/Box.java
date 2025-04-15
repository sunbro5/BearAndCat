package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.physics.collision.CollisionStrategy;
import com.mygdx.game.physics.collision.LandOnTopSleepStrategy;
import com.mygdx.game.physics.collision.LandOnTopStrategy;
import com.mygdx.game.physics.collision.PushStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Box extends MoveAbleEntity {
    private final Texture texture;

    public Box(Rectangle rectangle, Texture texture) {
        super(rectangle, rectangle);
        this.texture = texture;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, drawRectangle.x, drawRectangle.y, drawRectangle.width, drawRectangle.height);
    }

    @Override
    public void update(float delta, WorldPhysics worldPhysics) {
        this.drawRectangle.x = position.x;
        this.drawRectangle.y = position.y;
        super.update(delta, worldPhysics);
    }

    @Override
    protected Set<BehaviorType> initBehaviour() {
        Set<BehaviorType> behaviors = new HashSet<>();
        behaviors.add(BehaviorType.HAVE_ON_TOP);
        behaviors.add(BehaviorType.IS_ON_TOP);
        behaviors.add(BehaviorType.WALL_PUSH);
        return behaviors;
    }

    @Override
    protected List<CollisionStrategy> initCollisionStrategies() {
        List<CollisionStrategy> strategies = new ArrayList<>();
        strategies.add(new PushStrategy());
        strategies.add(new LandOnTopSleepStrategy());
        strategies.add(new LandOnTopStrategy());
        return strategies;
    }

    @Override
    public boolean canBeOnTop() {
        return true;
    }

    @Override
    public boolean canBePush() {
        return true;
    }

    @Override
    public int getWeight() {
        return 50;
    }
}
