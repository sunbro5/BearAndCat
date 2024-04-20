package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.AssetsLoader;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.utils.TextureUtils;

public class Bear extends ControlAbleEntity {

    public Bear(AssetsLoader assetsLoader, WorldPhysics worldPhysics) {
        super(worldPhysics, new Rectangle(500, 52, 100, 30), new Rectangle(500, 52, 100, 50));
        Texture bearSpriteTexture = assetsLoader.getTexture(AssetsLoader.TextureType.BEAR_1);

        TextureRegion[][] tmp = TextureRegion.split(bearSpriteTexture, bearSpriteTexture.getWidth() / 10, bearSpriteTexture.getHeight() / 8);
        TextureUtils.cropTextures(tmp, 3, 5,15 ,1);

        TextureRegion[] walkFrames = new TextureRegion[]{tmp[0][0], tmp[1][0], tmp[1][3], tmp[1][4], tmp[1][5]};
        walkAnimation = new Animation<>(1f / ((float) walkFrames.length), walkFrames);
        TextureRegion[] standFrames = new TextureRegion[]{tmp[0][0], tmp[0][0], tmp[0][1], tmp[0][1], tmp[0][2], tmp[0][2], tmp[0][3], tmp[0][3]};
        standAnimation = new Animation<>(1f / ((float) standFrames.length), standFrames);
        idleAnimation = new Animation<>(1f / ((float) standFrames.length), standFrames);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.BEAR;
    }

    @Override
    public boolean canWalkOn() {
        return true;
    }

    @Override
    public boolean canPush() {
        return false;
    }
}
