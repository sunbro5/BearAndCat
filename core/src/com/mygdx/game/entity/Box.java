package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.IsOnTop;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.physics.collision.CollisionHandler;
import com.sun.tools.javac.util.List;

import java.util.ArrayList;

public class Box extends MoveAbleEntity {
    private final TextureRegion texture;

    public Box(float x, float y, TextureRegion texture) {
        super(new Rectangle(x, y, 49, 49), new Rectangle(x, y, 49, 49),
                new ArrayList<>(), new ArrayList<>());
        this.texture = texture;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, drawRectangle.x, drawRectangle.y, drawRectangle.width, drawRectangle.height);
    }

    @Override
    public void update(float delta, WorldPhysics worldPhysics, CollisionHandler collisionHandler) {
        this.drawRectangle.x = position.x;
        this.drawRectangle.y = position.y;
        super.update(delta, worldPhysics, collisionHandler);
    }

    @Override
    public boolean canWalkOn() {
        return true;
    }

    @Override
    public boolean canPush() {
        return true;
    }

}
