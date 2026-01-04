package cz.mares.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.mares.game.behavior.BehaviorType;
import cz.mares.game.behavior.BoxMoveSound;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.physics.collision.CollisionStrategy;
import cz.mares.game.physics.collision.LandOnTopSleepStrategy;
import cz.mares.game.physics.collision.LandOnTopStrategy;
import cz.mares.game.physics.collision.PushStrategy;
import cz.mares.game.sound.EntitySounds;

public class Box extends MoveAbleEntity {
    private final Texture texture;

    public Box(Rectangle rectangle, Texture texture, EntitySounds entitySoundS) {
        super(offsetBox(rectangle), offsetBox(rectangle));
        this.texture = texture;
        this.entitySoundS = entitySoundS;
        setState(new BoxMoveSound());
    }

    private static Rectangle offsetBox(Rectangle rectangle){
        Rectangle box = new Rectangle(rectangle);
        box.height -= 1;
        return box;
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
        strategies.add(new LandOnTopSleepStrategy());
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
        return 50;
    }
}
