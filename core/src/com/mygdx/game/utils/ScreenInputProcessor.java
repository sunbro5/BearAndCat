package com.mygdx.game.utils;

import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.entity.ControlAbleEntity;

import lombok.Getter;

public class ScreenInputProcessor extends InputAdapter {
    private float screenWidth;
    private float startX;

    @Getter
    private ControlAbleEntity.Move move = ControlAbleEntity.Move.STAND;

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

            if (dx > 60) {
                move = ControlAbleEntity.Move.RIGHT;
            } else if (dx < -60) {
                move = ControlAbleEntity.Move.LEFT;
            } else {
                move = ControlAbleEntity.Move.STAND;
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

