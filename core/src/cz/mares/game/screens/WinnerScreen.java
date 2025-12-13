package cz.mares.game.screens;

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
import cz.mares.game.MyGdxGame;
import cz.mares.game.utils.LevelUtils;

public class WinnerScreen extends AbstractUIScreen {

    private float winnerScreenTime;

    public WinnerScreen(final MyGdxGame game) {
        super(game);
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
        if (winnerScreenTime > 0.5f && (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
            game.resetGameLevel();
            game.setScreenSafe(ScreenType.MAIN_MENU);
        }
        winnerScreenTime += delta;
        super.render(delta);
    }

    @Override
    public ScreenType getType() {
        return ScreenType.WIN;
    }
}
