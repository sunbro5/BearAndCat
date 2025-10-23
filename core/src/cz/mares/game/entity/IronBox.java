package cz.mares.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import cz.mares.game.behavior.BehaviorType;
import cz.mares.game.behavior.MoveSound;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.physics.collision.CollisionStrategy;
import cz.mares.game.physics.collision.LandOnTopStrategy;
import cz.mares.game.physics.collision.PushStrategy;
import cz.mares.game.sound.EntitySounds;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IronBox extends MoveAbleEntity {
    private final Texture texture;

    public IronBox(Rectangle rectangle, Texture texture, EntitySounds entitySoundS) {
        super(rectangle, rectangle);
        this.texture = texture;
        this.entitySoundS = entitySoundS;
        setState(new MoveSound());
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
    protected Set<BehaviorType> initBehaviour() {
        Set<BehaviorType> behaviors = new HashSet<>();
        behaviors.add(BehaviorType.HAVE_ON_TOP);
        behaviors.add(BehaviorType.IS_ON_TOP);
        behaviors.add(BehaviorType.WALL_PUSH);
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
        return true;
    }

    @Override
    public int getWeight() {
        return 100;
    }
}
