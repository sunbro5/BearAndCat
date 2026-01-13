package cz.mares.game.screens;

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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import cz.mares.game.AssetsLoader;
import cz.mares.game.MyGdxGame;
import cz.mares.game.utils.LevelUtils;

public abstract class AbstractUIScreen implements TypedScreen {
    protected MyGdxGame game;
    protected OrthographicCamera camera;
    protected OrthographicCamera backgroundCamera;
    protected SpriteBatch spriteBatch;
    protected Texture backGround;
    protected Stage stage;
    protected Skin skin;
    protected Viewport viewport;

    protected Viewport backgroundViewport;

    public AbstractUIScreen(final MyGdxGame game) {

        this.game = game;
        camera = new OrthographicCamera();
        backgroundCamera = new OrthographicCamera();
        spriteBatch = new SpriteBatch();

        backGround = game.getAssetsLoader().getTexture(AssetsLoader.TextureType.BACKGROUND);
        skin = game.getAssetsLoader().getSkin();

        viewport = new FitViewport(500, 250, camera);
        viewport.apply();

        backgroundViewport = new FillViewport(500, 250, backgroundCamera);
        backgroundViewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        backgroundCamera.position.set(backgroundCamera.viewportWidth / 2, backgroundCamera.viewportHeight / 2, 0);
        backgroundCamera.update();
        spriteBatch.setProjectionMatrix(backgroundCamera.combined);

        stage = new Stage(viewport);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        backgroundViewport.apply();
        spriteBatch.setProjectionMatrix(backgroundCamera.combined);

        spriteBatch.begin();
        spriteBatch.draw(backGround, 0, 0, backgroundViewport.getWorldWidth(), backgroundViewport.getWorldHeight());
        spriteBatch.end();

        viewport.apply();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        backgroundViewport.update(width, height, true);

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        backgroundCamera.position.set(backgroundCamera.viewportWidth / 2, backgroundCamera.viewportHeight / 2, 0);
        backgroundCamera.update();
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
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
        spriteBatch.dispose();

    }
}
