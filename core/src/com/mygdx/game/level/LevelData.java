package com.mygdx.game.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Disposable;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.DrawableEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.entity.PickAbleEntity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class LevelData implements Disposable {

    ControlAbleEntity bear;
    ControlAbleEntity cat;
    ControlAbleEntity controlEntity;
    List<MoveAbleEntity> moveAbleEntities;
    List<PickAbleEntity> pickAbleEntities;
    Rectangle endRectangle;
    int[][] mapTiles;
    SpriteCache mapCache;
    int[][] cacheBlocks;
    Texture backGround;
    Texture frontBackGround;

    List<DrawableEntity> allDrawEntities;

    public LevelData(ControlAbleEntity bear, ControlAbleEntity cat, List<MoveAbleEntity> moveAbleEntities, List<PickAbleEntity> pickAbleEntities, Rectangle endRectangle, int[][] mapTiles, SpriteCache mapCache, int[][] cacheBlocks, Texture backGround, Texture frontBackGround) {
        this.bear = bear;
        this.cat = cat;
        this.controlEntity = bear;
        this.moveAbleEntities = moveAbleEntities;
        this.pickAbleEntities = pickAbleEntities;
        this.endRectangle = endRectangle;
        this.mapTiles = mapTiles;
        this.mapCache = mapCache;
        this.cacheBlocks = cacheBlocks;
        this.backGround = backGround;
        this.frontBackGround = frontBackGround;
        this.allDrawEntities = joinAllEntities(bear, cat, moveAbleEntities, pickAbleEntities);
    }

    private List<DrawableEntity> joinAllEntities(ControlAbleEntity bear, ControlAbleEntity cat, List<MoveAbleEntity> moveAbleEntities, List<PickAbleEntity> pickAbleEntities) {
        List<DrawableEntity> result = new ArrayList<>();
        result.add(bear);
        result.add(cat);
        result.addAll(moveAbleEntities);
        result.addAll(pickAbleEntities);
        return result;
    }

    @Override
    public void dispose() {
        mapCache.dispose();
    }
}
