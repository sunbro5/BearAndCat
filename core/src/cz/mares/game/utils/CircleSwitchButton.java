package cz.mares.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CircleSwitchButton extends Actor {
    private final float radius;
    private final ShapeRenderer shapeRenderer;

    public CircleSwitchButton(float x, float y, float radius) {
        this.radius = radius;
        this.shapeRenderer = new ShapeRenderer();
        setPosition(x, y);
        setSize(radius * 2, radius * 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        shapeRenderer.setProjectionMatrix(getStage().getViewport().getCamera().combined);

        // lokální střed actoru
        float centerX = getX() + getWidth() / 2f - radius / 2; // TODO workaround bug, different hitbox
        float centerY = getY() + getHeight() / 2f;

        // rámeček kolečka
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.circle(centerX, centerY, radius);
        shapeRenderer.end();

        // šipky + spojovací čárky
        float arrowSize = radius * 0.3f;
        float lineLength = radius * 0.5f;
        float lineThickness = radius * 0.08f;
        float offset = radius * 0.1f;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // levá šipka (trojúhelník směřující vlevo)
        shapeRenderer.triangle(
                centerX - lineLength, centerY + offset,
                centerX - lineLength + arrowSize, centerY + arrowSize * 0.6f + offset,
                centerX - lineLength + arrowSize, centerY - arrowSize * 0.6f + offset
        );

        // pravá šipka (trojúhelník směřující vpravo)
        shapeRenderer.triangle(
                centerX + lineLength, centerY - offset,
                centerX + lineLength - arrowSize, centerY + arrowSize * 0.6f - offset,
                centerX + lineLength - arrowSize, centerY - arrowSize * 0.6f - offset
        );

        // levá čárka
        shapeRenderer.rect(
                centerX - lineLength, centerY - lineThickness / 2f + offset,
                lineLength, lineThickness
        );

        // pravá čárka
        shapeRenderer.rect(
                centerX, centerY - lineThickness / 2f - offset,
                lineLength, lineThickness
        );

        shapeRenderer.end();

        batch.begin();
    }

}