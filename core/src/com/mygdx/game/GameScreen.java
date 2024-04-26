package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.entity.Bear;
import com.mygdx.game.entity.Box;
import com.mygdx.game.entity.Cat;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.Drawable;
import com.mygdx.game.entity.EntityType;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.physics.WorldPhysics;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    private MyGdxGame game;
    private SpriteBatch spriteBatch;
    private SpriteBatch backgroundSpriteBatch;
    private BitmapFont font;
    private OrthographicCamera camera;

    private OrthographicCamera backGroundCamera;

    private ControlAbleEntity controlEntity;

    private List<Drawable> entities = new ArrayList<>();

    private Cat cat;
    private Bear bear;
    private GameMap gameMap;
    private AssetsLoader assetsLoader;
    private WorldPhysics worldPhysics;
    private FPSLogger fpsLogger = new FPSLogger();

    private Texture backGround;
    private Texture frontBackGround;

    public GameScreen(MyGdxGame game) {

        this.game = game;
        this.assetsLoader = new AssetsLoader();
        spriteBatch = new SpriteBatch();
        backgroundSpriteBatch = new SpriteBatch();
        font = new BitmapFont();
        worldPhysics = new WorldPhysics();
        gameMap = new GameMap(worldPhysics, assetsLoader);
        worldPhysics.initTerrain(gameMap.getMapTilesType());
        List<MoveAbleEntity> moveAbleEntities = gameMap.getMoveAbleEntities();

        cat = new Cat(assetsLoader, worldPhysics);
        bear = new Bear(assetsLoader, worldPhysics);
        worldPhysics.addEntity(cat);
        worldPhysics.addEntity(bear);
        worldPhysics.addEntities(moveAbleEntities);
        this.entities.add(bear);
        this.entities.add(cat);
        this.entities.addAll(moveAbleEntities);
        this.controlEntity = bear;
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 500);
        backGroundCamera = new OrthographicCamera();
        backGroundCamera.setToOrtho(false, 1000, 500);
        backGround = assetsLoader.getTexture(AssetsLoader.TextureType.BACKGROUND);
        backGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
        frontBackGround = assetsLoader.getTexture(AssetsLoader.TextureType.FRONT_BACKGROUND);
        frontBackGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
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
        backgroundSpriteBatch.setProjectionMatrix(backGroundCamera.combined);
        renderBackGround();
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

    private void renderBackGround() {
        int backGroundOffset = (int) (camera.position.x / 100);
        int frontBackGroundOffset = (int) (camera.position.x / 25);
        backgroundSpriteBatch.begin();
        backgroundSpriteBatch.draw(backGround, 0, 0, 1000, 500);
        backgroundSpriteBatch.draw(backGround, 0, 0, 0, 0, 1000, 500,1,1,0,backGroundOffset,0,500,200,false,false);
        backgroundSpriteBatch.draw(frontBackGround, 0, 0, 0, 0, 1000, 500,1,1,0,frontBackGroundOffset,0,500,200,false,false);
        backgroundSpriteBatch.end();
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
