package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.entity.Bear;
import com.mygdx.game.entity.Box;
import com.mygdx.game.entity.Cat;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.Drawable;
import com.mygdx.game.entity.EntityType;
import com.mygdx.game.physics.WorldPhysics;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    private MyGdxGame game;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private OrthographicCamera camera;

    private ControlAbleEntity controlEntity;

    private List<Drawable> entities = new ArrayList<>();

    private Cat cat;
    private Bear bear;
    private GameMap gameMap;
    private AssetsLoader assetsLoader;
    private WorldPhysics worldPhysics;
    private FPSLogger fpsLogger = new FPSLogger();

    public GameScreen(MyGdxGame game) {

        this.game = game;
        this.assetsLoader = new AssetsLoader();
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        gameMap = new GameMap(assetsLoader);
        worldPhysics = new WorldPhysics(gameMap.getMapTilesType());
        cat = new Cat(assetsLoader, worldPhysics);
        bear = new Bear(assetsLoader, worldPhysics);
        Box box = new Box(worldPhysics, assetsLoader);
        worldPhysics.addEntity(cat);
        worldPhysics.addEntity(bear);
        worldPhysics.addEntity(box);
        this.entities.add(bear);
        this.entities.add(cat);
        this.entities.add(box);
        this.controlEntity = bear;
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 500);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // updates
        handleControls();
        ScreenUtils.clear(0, 0, 0f, 1);
        camera.update();

        for (Drawable entity : entities) {
            entity.update(delta);
        }

        camera.position.lerp(controlEntity.getCameraPositionVector(), 4f * delta);

        // draws
        gameMap.render(camera);

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (Drawable entity : entities) {
            entity.render(spriteBatch);
        }
        spriteBatch.end();
        fpsLogger.log();
    }

    private void handleControls() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            controlEntity.move(ControlAbleEntity.Move.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            controlEntity.move(ControlAbleEntity.Move.RIGHT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            controlEntity.jump();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
            if (controlEntity == cat) {
                if (cat.getIsOnTopOf() == EntityType.BEAR) {
                    bear.setHaveOnTop(cat);
                }
                controlEntity = bear;
            } else {
                controlEntity = cat;
            }
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

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
        assetsLoader.dispose();
        gameMap.dispose();
    }

}
