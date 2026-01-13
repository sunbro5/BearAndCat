package cz.mares.game.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import cz.mares.game.AssetsLoader;
import cz.mares.game.GameData;
import cz.mares.game.MyGdxGame;

public class BeforeLevelScreen extends AbstractUIScreen {

    private Label text1;
    private Label text2;
    private Label text3;

    private boolean instructionShow;
    private Texture instruction;

    private float instructionTime;

    public BeforeLevelScreen(final MyGdxGame game) {
        super(game);
    }

    @Override
    public void show() {
        instructionShow = false;
        instruction = game.getAssetsLoader().getTexture(AssetsLoader.TextureType.INSTRUCTION);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();


        text1 = new Label("", skin);
        text1.setFontScale(1.5f);
        text2 = new Label("", skin);
        text2.setFontScale(1.5f);
        text3 = new Label("Press anything to continue", skin);
        text3.setFontScale(1.5f);


        //Add buttons to table
        mainTable.add(text1);
        mainTable.row();
        mainTable.add(text2).padTop(10);
        mainTable.row();
        mainTable.add(text3).padTop(10);

        //Add table to stage
        stage.addActor(mainTable);

        text1.setText(game.getGameData().getCurrentLeveData().getMetadata().getDisplayName());
        text2.setText(game.getGameData().getCurrentLeveData().getMetadata().getText());
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            if (game.getGameData().getCurrentLeveData().getMetadata().isInstruction() && Gdx.app.getType() == Application.ApplicationType.Android) {
                game.setScreenSafe(ScreenType.INSTRUCTION);
            } else {
                game.setScreenSafe(ScreenType.LEVEL);
            }
        }
        super.render(delta);
    }

    @Override
    public ScreenType getType() {
        return ScreenType.BEFORE_LEVEL;
    }
}
