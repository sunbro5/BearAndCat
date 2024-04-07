package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    private MyGdxGame game;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Bear bear;

    private GameMap gameMap;

    private AssetsLoader assetsLoader;


    public GameScreen(MyGdxGame game){
        this.game = game;
        this.assetsLoader = new AssetsLoader();
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        bear = new Bear(assetsLoader.getTexture(AssetsLoader.TextureType.BEAR));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 500);
        gameMap = new GameMap(assetsLoader);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0f, 1);
        camera.update();
        bear.update();
        camera.position.lerp(bear.getBearCameraPositionVector(), 4f * delta);

        gameMap.render(camera);

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        bear.render(spriteBatch);
        spriteBatch.end();

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
        spriteBatch.dispose();
        font.dispose();
        assetsLoader.dispose();
        gameMap.dispose();
    }

}
