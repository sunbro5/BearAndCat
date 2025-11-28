package cz.mares.game.behavior;

import com.badlogic.gdx.math.Vector2;

import lombok.Value;

@Value
public class BehaviorResult {
    Vector2 velocity;
    boolean forceVelocity;

    public BehaviorResult(Vector2 velocity){
        this.velocity = velocity;
        this.forceVelocity = false;
    }

    public BehaviorResult(Vector2 velocity, boolean forceVelocity){
        this.velocity = velocity;
        this.forceVelocity = forceVelocity;
    }
}
