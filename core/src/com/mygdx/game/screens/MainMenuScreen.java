package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AssetsLoader;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.utils.LevelUtils;

public class MainMenuScreen implements Screen {
    private MyGdxGame game;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private Texture backGround;
    private Stage stage;
    private Skin skin;
    private Viewport viewport;

    public MainMenuScreen(final MyGdxGame game) {

        this.game = game;
        camera = new OrthographicCamera();
        spriteBatch = new SpriteBatch();

        backGround = game.getAssetsLoader().getTexture(AssetsLoader.TextureType.BACKGROUND);
        skin = game.getAssetsLoader().getSkin();

        viewport = new FillViewport(500, 250, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        stage = new Stage(viewport);
    }

    @Override
    public void show() {
        game.getMusicPlayer().stop();
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();

        //Create buttons
        TextButton continueButton = new TextButton("Continue", skin);
        TextButton playButton = new TextButton("New Game", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        Label header = new Label("Silly Paws", skin);
        header.setFontScale(1.5f);
        Label footer = new Label("Move: Arrows, Jump: Space", skin);
        footer.setFontScale(0.7f);
        Label footer2 = new Label("Change character: Alt, Restart: R", skin);
        footer2.setFontScale(0.7f);

        //Add listeners to buttons
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LevelUtils.continueLevelScreen(game);
                dispose();
            }
        });
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.resetGameLevel();
                LevelUtils.setLevelScreen(game);
                dispose();
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
                dispose();
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                dispose();
            }
        });

        //Add buttons to table
        mainTable.add(header).padBottom(10);
        mainTable.row();
        if (game.getGameData().getGameLevel() != 0) {
            mainTable.add(continueButton).minWidth(100);
            mainTable.row();
        }
        mainTable.add(playButton).minWidth(100);
        mainTable.row();
        mainTable.add(settingsButton).minWidth(100);
        mainTable.row();
        mainTable.add(exitButton).minWidth(100);
        mainTable.row();
        mainTable.add(footer).padTop(10);
        mainTable.row();
        mainTable.add(footer2);

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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            LevelUtils.setLevelScreen(game);
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
        stage.dispose();
        spriteBatch.dispose();

    }
}
