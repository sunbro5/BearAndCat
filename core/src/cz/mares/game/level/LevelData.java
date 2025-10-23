package cz.mares.game.level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import cz.mares.game.Disposable;
import cz.mares.game.entity.ActionEntity;
import cz.mares.game.entity.ControlAbleEntity;
import cz.mares.game.entity.DrawableEntity;
import cz.mares.game.entity.MoveAbleEntity;
import cz.mares.game.entity.PickAbleEntity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

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
    private float fadeOverlay;
    private OrthogonalTiledMapRenderer mapRenderer;
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

    }

}
