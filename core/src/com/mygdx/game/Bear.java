package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Bear {

    private static final int CAMERA_OFFSET = 100;

    private static final int FRAME_COLS = 5;

    Animation<TextureRegion> standAnimation;
    Animation<TextureRegion> walkAnimation;
    TextureRegion currentFrame;
    float stateTime = 0;

    private Move move = Move.STAND;
    private Rectangle position;
    private Direction direction = Direction.RIGHT;

    private Jump jumping = null;

    public Bear(Texture bearSpriteTexture) {

        TextureRegion[][] tmp = TextureRegion.split(bearSpriteTexture,
                bearSpriteTexture.getWidth() / FRAME_COLS,
                bearSpriteTexture.getHeight());

        TextureRegion[] walkFrames = new TextureRegion[]{tmp[0][0], tmp[0][3], tmp[0][4]};
        walkAnimation = new Animation<>(0.33f, walkFrames);
        TextureRegion[] standFrames = new TextureRegion[]{tmp[0][0], tmp[0][0], tmp[0][0], tmp[0][0], tmp[0][0], tmp[0][0], tmp[0][0], tmp[0][0], tmp[0][1], tmp[0][2]};
        standAnimation = new Animation<>(0.1f, standFrames);

        position = new Rectangle(500, 2, 100, 100);
    }

    public void jump() {
        if (jumping == null) {
            jumping = Jump.UP;
        }
    }

    public void move(Move move) {
        this.move = move;
    }

    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            move(Bear.Move.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            move(Bear.Move.RIGHT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            jump();
        }

        stateTime += Gdx.graphics.getDeltaTime();
        switch (this.move) {
            case LEFT:
                position.x -= 5;
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                direction = Direction.LEFT;
                break;
            case RIGHT:
                position.x += 5;
                currentFrame = walkAnimation.getKeyFrame(stateTime, true);
                direction = Direction.RIGHT;
                break;
            case STAND:
                currentFrame = standAnimation.getKeyFrame(stateTime, true);
                break;
        }
        this.move = Move.STAND;

        if (jumping == Jump.DOWN) {
            position.y -= 10;
            if (position.y <= 5) {
                jumping = null;
            }
        } else if (jumping == Jump.UP) {
            position.y += 10;
            if (position.y > 200) {
                jumping = Jump.DOWN;
            }
        }


    }

    public void render(SpriteBatch spriteBatch) {
        if (direction == Direction.LEFT) {
            spriteBatch.draw(currentFrame, position.x + position.width, position.y, -position.width, position.height);
        } else {
            spriteBatch.draw(currentFrame, position.x, position.y, position.width, position.height);
        }
    }

    public Vector3 getBearCameraPositionVector(){
        return new Vector3(position.x + (position.width /2), position.y + (position.height /2) + CAMERA_OFFSET,0);
    }

    enum Move {
        STAND,
        RIGHT,
        LEFT;
    }

    enum Jump {
        UP,
        DOWN;
    }

    enum Direction {
        RIGHT,
        LEFT
    }

}
