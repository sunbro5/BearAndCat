package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.physics.collision.BeeHiveLandOnTopStrategy;
import com.mygdx.game.physics.collision.CollisionStrategy;
import com.mygdx.game.physics.collision.LandOnTopStrategy;
import com.mygdx.game.physics.collision.PushStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

public class BeeHive extends MoveAbleEntity {

    private final Texture texture;

    @Setter
    private boolean hit;

    @Setter
    @Getter
    private boolean active;


    public BeeHive(Rectangle position, Texture texture) {
        super(position, position);
        this.texture = texture;
    }

    @Override
    public void update(float delta, WorldPhysics worldPhysics) {
        if (active && hit) {
            super.update(delta, worldPhysics);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, drawRectangle.x, drawRectangle.y, drawRectangle.width, drawRectangle.height);
    }

    @Override
    protected Set<BehaviorType> initBehaviour() {
        return new HashSet<>();
    }

    @Override
    protected List<CollisionStrategy> initCollisionStrategies() {
        List<CollisionStrategy> strategies = new ArrayList<>();
        strategies.add(new BeeHiveLandOnTopStrategy());
        return strategies;
    }

    @Override
    public boolean canBeOnTop() {
        return false;
    }

    @Override
    public boolean canBePush() {
        return false;
    }
}
