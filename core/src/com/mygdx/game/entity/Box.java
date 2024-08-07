package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.physics.WorldPhysics;

public class Box extends MoveAbleEntity {

    private final Rectangle drawRectangle;
    private final TextureRegion texture;


    public Box(float x, float y, TextureRegion texture) {
        Rectangle rectangle = new Rectangle(x, y, 49, 49);
        this.texture = texture;
        this.drawRectangle = rectangle;
        this.position = rectangle;
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
    public boolean canWalkOn() {
        return true;
    }

    @Override
    public boolean canPush() {
        return true;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.BOX;
    }
}
