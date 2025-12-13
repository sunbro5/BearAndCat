package cz.mares.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import cz.mares.game.AssetsLoader;
import cz.mares.game.MyGdxGame;

public class SettingsScreen extends AbstractUIScreen {

    public SettingsScreen(final MyGdxGame game) {
        super(game);
    }

    @Override
    public void show() {
        stage.clear();
        game.getMusicPlayer().stop();
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();

        //Create buttons
        TextButton backButton = new TextButton("Back", skin);

        CheckBox soundsCheckBox = new CheckBox("Sounds", skin);
        soundsCheckBox.setChecked(!game.getGameData().isSoundMute());

        Label musicLabel = new Label("Music: ", skin);

        Slider musicSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin);
        musicSlider.setValue(game.getGameData().getMusicVolume());

        CheckBox debugCheckbox = new CheckBox("Debug", skin);
        debugCheckbox.setChecked(game.getGameData().getRenderDebug().get());

        Table checkBoxTable = new Table();
        checkBoxTable.center();
        checkBoxTable.add(soundsCheckBox).padBottom(5).row();
        checkBoxTable.add(musicLabel).row();
        checkBoxTable.add(musicSlider).row();
        checkBoxTable.add(debugCheckbox).row();

        Label header = new Label("Settings", skin);
        header.setFontScale(1.5f);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreenSafe(ScreenType.MAIN_MENU);
            }
        });

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float vol = musicSlider.getValue();
                game.getMusicPlayer().setVolume(vol);
                Gdx.app.debug("", "Sound volume:" + vol);
            }
        });

        soundsCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getGameData().setSoundMute(!soundsCheckBox.isChecked());
            }
        });

        debugCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getGameData().getRenderDebug().set(!game.getGameData().getRenderDebug().get());
                if (game.getGameData().getRenderDebug().get()) {
                    Gdx.app.setLogLevel(Application.LOG_DEBUG);
                } else {
                    Gdx.app.setLogLevel(Application.LOG_INFO);
                }
            }
        });


        //Add buttons to table
        mainTable.add(header).padBottom(10);
        mainTable.row();
        mainTable.add(checkBoxTable).minWidth(100);
        mainTable.row();
        mainTable.add(backButton).minWidth(100);
        mainTable.row();


        //Add table to stage
        stage.addActor(mainTable);
    }


    @Override
    public ScreenType getType() {
        return ScreenType.SETTINGS;
    }
}
