package cz.mares.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import cz.mares.game.level.LevelData;
import cz.mares.game.physics.WorldPhysics;
import cz.mares.game.sound.EntitySoundType;
import cz.mares.game.sound.SoundPlayer;
import cz.mares.game.screens.LevelScreen;

public class Star extends PickAbleEntity {

    private final Animation<TextureRegion> animation;

    private TextureRegion currentFrame;
    private float stateTime;

    public Star(Rectangle rectangle, Texture texture) {
        super(new Rectangle(rectangle), null);
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / 13, texture.getHeight());
        TextureRegion[] frames = tmp[0];
        animation = new Animation<>(1f / ((float) frames.length), frames);
        stateTime += LevelScreen.STEP;
        currentFrame = animation.getKeyFrame(stateTime, true);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(currentFrame, position.x, position.y, position.width, position.height);
    }

    @Override
    public void update(float delta, WorldPhysics worldPhysics) {
        stateTime += delta;
        currentFrame = animation.getKeyFrame(stateTime, true);
    }

    @Override
    public void onPick(LevelData levelData, ControlAbleEntity controlAbleEntity) {
        levelData.setScore(levelData.getScore() + 1);
        levelData.getPickAbleEntities().remove(this);
        SoundPlayer.play(controlAbleEntity.getEntitySoundS(), EntitySoundType.HIT_STAR);
    }
}
