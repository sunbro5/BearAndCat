package cz.mares.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import cz.mares.game.AssetsLoader;
import cz.mares.game.MyGdxGame;
import cz.mares.game.utils.LevelUtils;

public class MainMenuScreen extends AbstractUIScreen {

    private Label footer;
    private Label footer2;
    private Label footer3;

    public MainMenuScreen(MyGdxGame game) {
        super(game);
    }

    @Override
    public void show() {
        game.getMusicPlayer().stop();

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();

        //Create buttons
        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.setScale(0.8f);
        TextButton playButton = new TextButton("New Game", skin);
        playButton.setScale(0.8f);
        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.setScale(0.8f);
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setScale(0.8f);

        Label header = new Label("Silly Paws", skin);
        header.setFontScale(1.5f);

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            footer = new Label("Move: Left screen drag, Jump: Right screen touch", skin);
            footer2 = new Label("Menu: Touch left button, Restart: Hold left button", skin);
            footer3 = new Label("Change character: Right button", skin);
        } else {
            footer = new Label("Move: Arrows, Jump: Space", skin);
            footer2 = new Label("Change character: Alt, ", skin);
            footer3 = new Label("Menu: Esc, Restart: R", skin);
        }

        footer.setFontScale(0.5f);
        footer2.setFontScale(0.5f);
        footer3.setFontScale(0.5f);

        //Add listeners to buttons
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LevelUtils.continueLevelScreen(game);
            }
        });
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.resetGameLevel();
                LevelUtils.setLevelScreen(game);
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreenSafe(ScreenType.SETTINGS);
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
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
        mainTable.row();
        mainTable.add(footer3);

        //Add table to stage
        stage.addActor(mainTable);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            LevelUtils.setLevelScreen(game);
        }
        super.render(delta);
    }

    @Override
    public ScreenType getType() {
        return ScreenType.MAIN_MENU;
    }
}
