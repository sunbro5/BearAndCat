package com.mygdx.game.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScreenCallback {

    private final Game game;

    public void setScreen(Screen screen) {
        game.setScreen(screen);
    }

}
