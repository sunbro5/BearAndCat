package com.mygdx.game.behavior;

import com.badlogic.gdx.math.Vector2;

import lombok.Value;

@Value
public class BehaviorResult {
    Vector2 velocity;
    boolean remove;
}
