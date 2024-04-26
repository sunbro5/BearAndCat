package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AssetsLoader;
import com.mygdx.game.physics.WorldPhysics;
import com.mygdx.game.utils.TextureUtils;

public class Cat extends ControlAbleEntity {

    public Cat(AssetsLoader assetsLoader, WorldPhysics worldPhysics) {
        super(worldPhysics, new Rectangle(600, 52, 50, 25), new Rectangle(0, 0, 0, 0));
        Texture bearSpriteTexture = assetsLoader.getTexture(AssetsLoader.TextureType.CAT);

        TextureRegion[][] tmp = TextureRegion.split(bearSpriteTexture, bearSpriteTexture.getWidth() / 10, bearSpriteTexture.getHeight() / 3);
        TextureUtils.cropTextures(tmp, 14, 13,17 ,18);
        new TextureRegion();

        TextureRegion[] walkFrames = new TextureRegion[]{tmp[1][0], tmp[1][1], tmp[1][2], tmp[1][3], tmp[1][4], tmp[1][5], tmp[1][6], tmp[1][7]};
        walkAnimation = new Animation<>(1f / ((float) walkFrames.length), walkFrames);
        TextureRegion[] standFrames = new TextureRegion[]{tmp[0][0], tmp[0][1], tmp[0][2], tmp[0][3], tmp[0][4], tmp[0][5], tmp[0][6], tmp[0][7], tmp[0][8], tmp[0][9]};
        standAnimation = new Animation<>(1f / ((float) standFrames.length), standFrames);
        TextureRegion[] idleFrames = new TextureRegion[]{tmp[2][0], tmp[2][1], tmp[2][2], tmp[2][3], tmp[2][4], tmp[2][5], tmp[2][6], tmp[2][7]};
        idleAnimation = new Animation<>(7f / ((float) idleFrames.length), idleFrames);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.CAT;
    }

    @Override
    public boolean canWalkOn() {
        return false;
    }

    @Override
    public boolean canPush() {
        return false;
    }
}
