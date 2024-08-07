package com.mygdx.game.screens;

import com.badlogic.gdx.Screen;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.World;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.renderer.WorldRenderer;

public class LevelScreen implements Screen {

    private final World world;

    public LevelScreen(MyGdxGame game, LevelData levelData) {
        this.world = new World(game, new WorldRenderer(), levelData);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        world.update(delta);
        world.render(delta);
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
        world.dispose();
    }

}
