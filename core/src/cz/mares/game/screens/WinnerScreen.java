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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import cz.mares.game.AssetsLoader;
import cz.mares.game.MyGdxGame;
import cz.mares.game.level.LevelScore;
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

        int overAllScore = 0;
        int maxOverAllScore = 0;

        Array<Label> scores = new Array<>();
        for (int i = 0; i < game.getGameData().getLevelScores().size; i++) {
            LevelScore score = game.getGameData().getLevelScores().get(i);
            Label scoreLabel = new Label("Level " + (i + 1) + " Score: " + score.getScore() + "/" + score.getMaxScore(), skin);
            scoreLabel.setFontScale(1.3f);
            scores.add(scoreLabel);
            overAllScore += score.getScore();
            maxOverAllScore += score.getMaxScore();
        }

        Label text1 = new Label("You have finished the game!", skin);
        text1.setFontScale(1.5f);
        Label text2 = new Label("Final Score: " + overAllScore + "/" + maxOverAllScore, skin);
        text2.setFontScale(1.3f);
        Label text3 = new Label("Made by Sunbro5!", skin);
        text3.setFontScale(1.5f);

        //Add buttons to table
        mainTable.add(text1);
        mainTable.row();
        mainTable.add(text2).padTop(10).padBottom(10);
        mainTable.row();
        for (Label scoreLabel : scores) {
            mainTable.add(scoreLabel);
            mainTable.row();
        }

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
