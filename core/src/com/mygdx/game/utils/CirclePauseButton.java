package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class CirclePauseButton extends Actor {
    private float radius;
    private ShapeRenderer shapeRenderer;

    private float holdTime = 0f;
    private float requiredHold = 1f;
    private boolean holding = false;
    private boolean triggered = false;

    private Runnable longPressCallback;

    public CirclePauseButton(float x, float y, float radius, Runnable quickPressCallback, Runnable longPressCallback) {
        this.radius = radius;
        this.shapeRenderer = new ShapeRenderer();

        this.longPressCallback = longPressCallback;

        setPosition(x, y);
        setSize(radius * 2, radius * 2);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                holding = true;
                triggered = false;
                holdTime = 0f;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                holding = false;
                if (!triggered) {
                    quickPressCallback.run();
                }
            }
        });

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (holding) {
            holdTime += delta;
            if (holdTime >= requiredHold && !triggered) {
                triggered = true;
                longPressCallback.run();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        float centerX = getX() + getWidth() / 2f + radius / 2; // TODO workaround bug, different hitbox
        float centerY = getY() + getHeight() / 2f;


        // Okrajový kruh
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(5f);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.circle(centerX, centerY, radius);
        shapeRenderer.end();

        // Pause symbol
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);

        float barWidth = radius * 0.25f;
        float barHeight = radius * 1.2f;
        float gap = radius * 0.3f;

        shapeRenderer.rect(centerX - gap / 2f - barWidth, centerY - barHeight / 2f, barWidth, barHeight);
        shapeRenderer.rect(centerX + gap / 2f, centerY - barHeight / 2f, barWidth, barHeight);
        shapeRenderer.end();

        // Progres výplň (arc)
        if (holding || triggered) {
            float progress = Math.min(holdTime / requiredHold, 1f);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.75f, 0.75f, 0.75f, 0.3f); // průhledná šedá
            shapeRenderer.arc(
                    centerX,         // stejné jako u circle()
                    centerY,
                    radius,          // stejné jako u circle()
                    90,
                    -360 * progress,
                    100
            );
            shapeRenderer.end();

        }

        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

}
