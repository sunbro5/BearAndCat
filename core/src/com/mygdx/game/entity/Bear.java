package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.behavior.BehaviorType;
import com.mygdx.game.behavior.MoveSound;
import com.mygdx.game.physics.collision.CollisionStrategy;
import com.mygdx.game.physics.collision.LandOnTopStrategy;
import com.mygdx.game.physics.collision.PushStrategy;
import com.mygdx.game.renderer.AnimationType;
import com.mygdx.game.sound.EntitySound;
import com.mygdx.game.utils.TextureUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bear extends ControlAbleEntity {

    public Bear(float x, float y, Texture texture, EntitySound entitySound) {
        super(new Rectangle(x, y, 45, 17), new Rectangle(-13, 0, 26, 12));
        //super(new Rectangle(x, y, 45, 17), new Rectangle(-4, 0, 6, 12));

        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 10, texture.getHeight() / 8);
        TextureUtils.cropTextures(tmp, 0, 0, 15, 1);

        TextureRegion[] walkFrames = new TextureRegion[]{tmp[0][0], tmp[1][0], tmp[1][3], tmp[1][4], tmp[1][5]};
        walkAnimation = new Animation<>(1f / ((float) walkFrames.length), walkFrames);
        TextureRegion[] standFrames = new TextureRegion[]{tmp[0][0], tmp[0][0], tmp[0][1], tmp[0][1], tmp[0][2], tmp[0][2], tmp[0][3], tmp[0][3]};
        standAnimation = new Animation<>(1f / ((float) standFrames.length), standFrames);

        TextureRegion[] eatFrames = new TextureRegion[]{tmp[4][0], tmp[4][0], tmp[3][4], tmp[3][0], tmp[4][0], tmp[4][0], tmp[7][0], tmp[7][1]};
        animations.put(AnimationType.EAT, new Animation<>(2f / ((float) eatFrames.length), eatFrames));
        TextureRegion[] sleepFrames = new TextureRegion[]{tmp[7][2], tmp[7][3], tmp[7][2], tmp[7][3], tmp[7][2], tmp[7][3], tmp[7][2], tmp[7][3]};
        animations.put(AnimationType.SLEEP, new Animation<>(5f / ((float) sleepFrames.length), sleepFrames));

        TextureRegion[] howlFrames = new TextureRegion[]{tmp[0][0], tmp[4][1], tmp[4][2], tmp[4][3], tmp[4][4], tmp[0][0], tmp[3][0], tmp[3][1], tmp[3][2], tmp[3][3], tmp[3][4], tmp[0][0]};
        animations.put(AnimationType.HOWL, new Animation<>(3f / ((float) howlFrames.length), howlFrames));

        this.entitySound = entitySound;
        setState(new MoveSound());
    }

    @Override
    protected Set<BehaviorType> initBehaviour() {
        Set<BehaviorType> behaviors = new HashSet<>();
        behaviors.add(BehaviorType.HAVE_ON_TOP);
        behaviors.add(BehaviorType.IS_ON_TOP);
        behaviors.add(BehaviorType.MUSHROOM_EAT);
        behaviors.add(BehaviorType.BEE_HIVE_TREE_STUCK);
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
        return true;
    }

    @Override
    public boolean canBePush() {
        return false;
    }

    @Override
    public float getJumpVelocity() {
        return 4.9f;
    }

    @Override
    public float getMoveVelocity() {
        return 2.5f;
    }

    @Override
    public float getIdleTimeout() {
        return 20;
    }

    @Override
    public int getStrength() {
        return 200;
    }
}
