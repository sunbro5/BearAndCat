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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import cz.mares.game.AssetsLoader;
import cz.mares.game.MyGdxGame;

public class InstructionScreen implements TypedScreen {
    private MyGdxGame game;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private Stage stage;
    private Viewport viewport;
    private Texture instruction;
    private float instructionTime;

    public InstructionScreen(final MyGdxGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        spriteBatch = new SpriteBatch();

        viewport = new StretchViewport(500, 250, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        stage = new Stage(viewport);
    }

    @Override
    public void show() {
        instruction = game.getAssetsLoader().getTexture(AssetsLoader.TextureType.INSTRUCTION);
    }

    @Override
    public void render(float delta) {
        if (instructionTime > 0.5f) {
            if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                game.setScreenSafe(ScreenType.LEVEL);
            }
        }
        ScreenUtils.clear(0, 0, 0.2f, 1);

        viewport.apply();
        spriteBatch.setProjectionMatrix(camera.combined);

        instructionTime += delta;
        spriteBatch.begin();
        spriteBatch.draw(instruction, 0, 0, 500, 250);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

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
        Gdx.input.setInputProcessor(null);
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
        spriteBatch.dispose();

    }

    @Override
    public ScreenType getType() {
        return ScreenType.INSTRUCTION;
    }
}
