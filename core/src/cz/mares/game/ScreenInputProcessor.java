package cz.mares.game;

import com.badlogic.gdx.InputAdapter;
import cz.mares.game.entity.ControlAbleEntity;

import lombok.Getter;

public class ScreenInputProcessor extends InputAdapter {

    private static final int MAX_SPEED_LENGTH = 100;

    private static final int MIN_SPEED_LENGTH = 5;
    private float screenWidth;
    private float startX;

    @Getter
    private ControlAbleEntity.Move move = ControlAbleEntity.Move.STAND;

    @Getter
    private int moveSpeedPercent;

    private int leftPointer = -1;
    private int rightPointer = -1;

    private final Runnable jumpCallback;

    public ScreenInputProcessor(float screenWidth, Runnable jumpCallback) {
        this.screenWidth = screenWidth;
        this.jumpCallback = jumpCallback;
    }

    public void setScreenWidth(float screenWidth) {
        this.screenWidth = screenWidth;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX < screenWidth / 2f && leftPointer == -1) {
            leftPointer = pointer;
            startX = screenX;
        } else if (screenX >= screenWidth / 2f && rightPointer == -1) {
            rightPointer = pointer;
            jump();
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == leftPointer) {
            float dx = screenX - startX;

            if (dx > MIN_SPEED_LENGTH) {
                move = ControlAbleEntity.Move.RIGHT;
                moveSpeedPercent = Math.min(
                        100,
                        (int) (((dx - MIN_SPEED_LENGTH) / (MAX_SPEED_LENGTH - MIN_SPEED_LENGTH)) * 100));
            } else if (dx < -MIN_SPEED_LENGTH) {
                move = ControlAbleEntity.Move.LEFT;
                moveSpeedPercent = Math.min(
                        100,
                        (int) (((-dx - MIN_SPEED_LENGTH) / (MAX_SPEED_LENGTH - MIN_SPEED_LENGTH)) * 100));
            } else {
                move = ControlAbleEntity.Move.STAND;
                moveSpeedPercent = 0;
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer == leftPointer) {
            leftPointer = -1;
            move = ControlAbleEntity.Move.STAND;
        }
        if (pointer == rightPointer) {
            rightPointer = -1;
        }
        return true;
    }

    private void jump() {
        jumpCallback.run();
    }


}

