package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LongPressButton extends TextButton {
    private float holdTime = 0f;
    private float requiredHold = 1f;
    private boolean holding = false;
    private boolean triggered = false;

    public LongPressButton(String text, Skin skin) {
        super(text, skin);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                holding = true;
                triggered = false;
                holdTime = 0f;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                holding = false;
                if (!triggered) {
                    System.out.println("Krátký klik → otevři menu");
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
                System.out.println("Dlouhý stisk → speciální akce");
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (holding || triggered) {
            batch.end();

            ShapeRenderer sr = new ShapeRenderer();
            sr.setProjectionMatrix(batch.getProjectionMatrix());

            float progress = Math.min(holdTime / requiredHold, 1f);

            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(0, 0.6f, 1f, 0.5f);
            sr.arc(getX() + getWidth() / 2f, getY() + getHeight() / 2f,
                    getWidth() / 2f, 90, -360 * progress);
            sr.end();

            batch.begin();
        }
    }
}
