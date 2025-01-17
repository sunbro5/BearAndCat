package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.physics.collision.CollisionStrategy;
import com.mygdx.game.physics.collision.LandOnTopStrategy;
import com.mygdx.game.physics.collision.PushStrategy;
import com.mygdx.game.utils.TextureUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cat extends ControlAbleEntity {

    public Cat(float x, float y, Texture texture) {
        super(new Rectangle(x, y, 24, 10), new Rectangle(-2, 0, 2, 0));

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 10, texture.getHeight() / 3);
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
    protected Set<BehaviorType> initBehaviour() {
        Set<BehaviorType> behaviors = new HashSet<>();
        behaviors.add(BehaviorType.IS_ON_TOP);
        return behaviors;
    }

    @Override
    protected List<CollisionStrategy> initCollisionStrategies() {
        List<CollisionStrategy> strategies = new ArrayList<>();
        strategies.add(new PushStrategy());
        strategies.add(new LandOnTopStrategy());
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

    @Override
    public float getJumpVelocity() {
        return 7f;
    }

    @Override
    public float getMoveVelocity() {
        return 1.5f;
    }

    @Override
    public int getStrength() {
        return 60;
    }
}
