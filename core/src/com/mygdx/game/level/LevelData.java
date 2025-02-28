package com.mygdx.game.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Disposable;
import com.mygdx.game.entity.ActionEntity;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.DrawableEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.entity.PickAbleEntity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class LevelData implements Disposable {

    private ControlAbleEntity bear;
    private ControlAbleEntity cat;
    private ControlAbleEntity controlEntity;
    private List<MoveAbleEntity> moveAbleEntities;
    private List<PickAbleEntity> pickAbleEntities;
    private List<DrawableEntity> otherDrawableEntities;
    private List<ActionEntity> actionEntities;
    private Rectangle endRectangle;
    private Rectangle[][] walls;
    private TiledMap terrain;
    private Texture backGround;
    private Texture frontBackGround;
    private int score;
    private int starsCount;
    private Level metadata;

    public List<DrawableEntity> getAllDrawEntities(){
        List<DrawableEntity> collEntities = new ArrayList<>();
        if (getControlEntity() == getBear()) {
            collEntities.add(getBear());
            collEntities.add(getCat());
        } else {
            collEntities.add(getCat());
            collEntities.add(getBear());
        }
        collEntities.addAll(getMoveAbleEntities());
        collEntities.addAll(getPickAbleEntities());
        collEntities.addAll(getOtherDrawableEntities());
        return collEntities;
    }

    @Override
    public void dispose() {
        terrain.dispose();
    }
}
