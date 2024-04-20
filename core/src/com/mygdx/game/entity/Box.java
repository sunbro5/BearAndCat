package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AssetsLoader;
import com.mygdx.game.physics.WorldPhysics;

public class Box extends MoveAbleEntity {

    private Rectangle drawRectangle;
    private Texture texture;


    public Box(WorldPhysics worldPhysics, AssetsLoader assetsLoader) {
        super(worldPhysics);
        Rectangle rectangle = new Rectangle(700, 50, 50, 50);
        this.texture = assetsLoader.getTexture(AssetsLoader.TextureType.BOX);
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
