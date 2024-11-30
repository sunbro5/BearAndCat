package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.physics.collision.CollisionStrategy;
import com.mygdx.game.physics.collision.PushStrategy;
import com.mygdx.game.utils.TextureUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bear extends ControlAbleEntity {

    public Bear(int x, int y, Texture texture) {
        super(new Rectangle(x, y, 90, 30), new Rectangle(-9, 0, +8, +20));

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 10, texture.getHeight() / 8);
        TextureUtils.cropTextures(tmp, 3, 5,15 ,1);

        TextureRegion[] walkFrames = new TextureRegion[]{tmp[0][0], tmp[1][0], tmp[1][3], tmp[1][4], tmp[1][5]};
        walkAnimation = new Animation<>(1f / ((float) walkFrames.length), walkFrames);
        TextureRegion[] standFrames = new TextureRegion[]{tmp[0][0], tmp[0][0], tmp[0][1], tmp[0][1], tmp[0][2], tmp[0][2], tmp[0][3], tmp[0][3]};
        standAnimation = new Animation<>(1f / ((float) standFrames.length), standFrames);
    }

    @Override
    protected Set<BehaviorType> initBehaviour() {
        return new HashSet<>();
    }

    @Override
    protected List<CollisionStrategy> initCollisionStrategies() {
        List<CollisionStrategy> strategies = new ArrayList<>();
        strategies.add(new PushStrategy());
        return strategies;
    }

    @Override
    public boolean canWalkOn() {
        return true;
    }

    @Override
    public boolean canBePush() {
        return false;
    }

    @Override
    public float getJumpVelocity() {
        return 21;
    }

    @Override
    public float getMoveVelocity() {
        return 8;
    }
}
