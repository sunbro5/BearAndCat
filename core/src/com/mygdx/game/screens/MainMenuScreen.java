package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.AssetsLoader;
import com.mygdx.game.MyGdxGame;

public class MainMenuScreen implements Screen {
    private MyGdxGame game;
    private OrthographicCamera camera;
    private BitmapFont font;
    private SpriteBatch spriteBatch;

    private Texture backGround;

    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;
        font = new BitmapFont();
        camera = new OrthographicCamera();
        spriteBatch = new SpriteBatch();
        camera.setToOrtho(false, 1000, 500);
        backGround = game.getAssetsLoader().getTexture(AssetsLoader.TextureType.BACKGROUND);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();
        spriteBatch.draw(backGround, 0, 0, 1000, 500);
        font.getData().setScale(4);
        font.draw(spriteBatch, "Bearo and Kitto", 400, 450, 200, Align.center, false);
        font.getData().setScale(3);
        font.draw(spriteBatch, "Pres anything to play!", 400, 200, 200, Align.center, false);
        font.getData().setScale(2);
        font.draw(spriteBatch, "Move: Arrows, Jump: Space, Change character: Alt, Restart: R", 100, 50, 800, Align.center, false);
        spriteBatch.end();

        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new BeforeLevelScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        spriteBatch.dispose();
    }
}