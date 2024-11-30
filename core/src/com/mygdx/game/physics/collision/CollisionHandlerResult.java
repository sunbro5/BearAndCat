package com.mygdx.game.physics.collision;

import com.badlogic.gdx.math.Vector2;

import lombok.Data;
import lombok.Value;

@Value
public class CollisionHandlerResult {
    Vector2 velocity;
}
