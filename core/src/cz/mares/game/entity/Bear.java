package cz.mares.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import cz.mares.game.behavior.BehaviorType;
import cz.mares.game.behavior.MoveSound;
import cz.mares.game.physics.collision.CollisionStrategy;
import cz.mares.game.physics.collision.LandOnTopStrategy;
import cz.mares.game.physics.collision.PushStrategy;
import cz.mares.game.renderer.AnimationType;
import cz.mares.game.sound.EntitySounds;
import cz.mares.game.utils.TextureUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bear extends ControlAbleEntity {

    public Bear(float x, float y, Texture texture, EntitySounds entitySoundS) {
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

        this.entitySoundS = entitySoundS;
        setState(new MoveSound());
    }

    @Override
    protected Set<BehaviorType> initBehaviour() {
        Set<BehaviorType> behaviors = new HashSet<>();
        behaviors.add(BehaviorType.HAVE_ON_TOP);
        behaviors.add(BehaviorType.IS_ON_TOP);
        behaviors.add(BehaviorType.MUSHROOM_EAT);
        behaviors.add(BehaviorType.BEE_HIVE_TREE_STUCK);
        behaviors.add(BehaviorType.FLY_UP);
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
        return 294f;
    }

    @Override
    public float getMoveVelocity() {
        return 150f;
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
