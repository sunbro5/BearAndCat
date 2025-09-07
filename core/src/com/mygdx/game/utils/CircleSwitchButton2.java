package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CircleSwitchButton2 extends Actor {
    private final float radius;
    private final Texture iconTexture;

    private final TextureRegion region;

    public CircleSwitchButton2(float x, float y, float radius) {
        this.radius = radius;

        // nastavíme pozici a velikost (bounds)
        setPosition(x, y);
        setSize(radius * 2f, radius * 2f);
        setOrigin(getWidth() / 2f, getHeight() / 2f);

        // vytvoříme texture ikony odpovídající velikosti bounds
        int texW = Math.max(2, Math.round(getWidth()));
        int texH = Math.max(2, Math.round(getHeight()));
        this.iconTexture = createIconTexture(texW, texH);
        this.region = new TextureRegion(iconTexture);

        // jednoduchý listener (ukázka) - zastaví event, aby ho nepřebral game input

    }

    // Vytvoří texture: průhledné pozadí, kruhový rámeček a dvě šipky (nahoře doprava, dole doleva)
    private Texture createIconTexture(int w, int h) {
        Pixmap p = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        p.setBlending(Pixmap.Blending.SourceOver);
        p.setColor(0, 0, 0, 0);
        p.fill(); // průhledné pozadí

        int cx = w / 2;
        int cy = h / 2;
        int outerR = Math.min(w, h) / 2 - 1;
        int thickness = Math.max(2, Math.round(outerR * 0.15f));

        // bílý rámeček: nakreslíme plné kruhy a vyrýsujeme "kruh-out" odstraněním středu
        p.setColor(1f, 1f, 1f, 1f);
        p.fillCircle(cx, cy, outerR);                 // celý velký kruh
        p.setColor(0f, 0f, 0f, 0f);
        p.fillCircle(cx, cy, outerR - thickness);     // vyřízneme vnitřek -> vznikne rámeček

        // šipky
        p.setColor(1f, 1f, 1f, 1f);
        int arrowW = Math.max(1, Math.round(outerR * 0.9f));    // šířka šipky (úplná "délka" v ose X)
        int arrowH = Math.max(1, Math.round(outerR * 0.35f));   // poloviční "výška" trojúhelníku
        int verticalOffset = Math.max(1, Math.round(outerR * 0.45f));

        // horní šipka směřuje doprava (nad středem)
        p.fillTriangle(
                cx - arrowW / 2, cy + verticalOffset,        // levý bod
                cx + arrowW / 2, cy + verticalOffset + arrowH / 2, // pravý horní
                cx + arrowW / 2, cy + verticalOffset - arrowH / 2  // pravý dolní
        );

        // dolní šipka směřuje doleva (pod středem)
        p.fillTriangle(
                cx + arrowW / 2, cy - verticalOffset,        // pravý bod
                cx - arrowW / 2, cy - verticalOffset + arrowH / 2, // levý horní
                cx - arrowW / 2, cy - verticalOffset - arrowH / 2  // levý dolní
        );

        Texture t = new Texture(p);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        p.dispose();
        return t;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // kreslíme ICON přesně přes bounds; batch je (obvykle) už nakonfigurován Stage/Viewportem
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        // použijeme transformované draw, aby origin/rotation/scale byly respektovány pokud je použiješ
        batch.draw(region,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation());
        // reset barvy
        batch.setColor(Color.WHITE);
    }

    // zavolej to někde při dispose Stage nebo screen.dispose()
    public void dispose() {
        if (iconTexture != null) iconTexture.dispose();
    }

}