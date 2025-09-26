package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AssetsLoader;
import com.mygdx.game.MyGdxGame;

public class WinnerScreen implements TypedScreen {
    private MyGdxGame game;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private Texture backGround;
    private Stage stage;

    private Skin skin;
    private Viewport viewport;

    public WinnerScreen(final MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        spriteBatch = new SpriteBatch();
        camera.setToOrtho(false, 500, 250);
        backGround = game.getAssetsLoader().getTexture(AssetsLoader.TextureType.BACKGROUND);
        skin = game.getAssetsLoader().getSkin();
        viewport = new FillViewport(500, 250, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, spriteBatch);
    }

    @Override
    public void show() {
        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();


        Label text1 = new Label("You have finished the game!", skin);
        text1.setFontScale(1.5f);
        Label text2 = new Label("Score: " + game.getGameData().getFinalScore() + "/" + game.getGameData().getMaxFinalScore(), skin);
        text2.setFontScale(1.5f);
        Label text3 = new Label("Press anything!", skin);
        text3.setFontScale(1.5f);

        //Add buttons to table
        mainTable.add(text1);
        mainTable.row();
        mainTable.add(text2).padTop(10);
        mainTable.row();
        mainTable.add(text3).padTop(10);

        //Add table to stage
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        spriteBatch.begin();
        spriteBatch.draw(backGround, 0, 0, 500, 250);
        spriteBatch.end();

        stage.act();
        stage.draw();
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.resetGameLevel();
            game.setScreenSafe(ScreenType.MAIN_MENU);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        //font.dispose();
        spriteBatch.dispose();
    }

    @Override
    public ScreenType getType() {
        return ScreenType.WIN;
    }
}
