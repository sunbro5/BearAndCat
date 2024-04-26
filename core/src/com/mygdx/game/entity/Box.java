package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AssetsLoader;
import com.mygdx.game.physics.WorldPhysics;

public class Box extends MoveAbleEntity {

    private Rectangle drawRectangle;
    private TextureRegion texture;


    public Box(float x, float y, WorldPhysics worldPhysics, TextureRegion texture) {
        super(worldPhysics);
        Rectangle rectangle = new Rectangle(700, 150, 49, 49);
        this.texture = texture;
        this.drawRectangle = rectangle;
        this.position = rectangle;
    }


    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, drawRectangle.x, drawRectangle.y, drawRectangle.width, drawRectangle.height);
    }

    @Override
    public void update(float delta) {
        this.drawRectangle.x = position.x;
        this.drawRectangle.y = position.y;
        super.update(delta);
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
