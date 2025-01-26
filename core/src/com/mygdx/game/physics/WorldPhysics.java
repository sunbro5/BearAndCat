package com.mygdx.game.physics;

import static com.mygdx.game.level.LevelLoader.TILE_SIZE;
import static com.mygdx.game.level.LevelLoader.WALL_LAYER;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.DrawableEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.entity.PickAbleEntity;
import com.mygdx.game.level.LevelData;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Value;

public class WorldPhysics {

    public static final int GRAVITY = 25;
    private static final int COLLISION_DEPTH_CHECK = 1;
    private static final float POSITION_OFFSET = 0.1f;
    private final Rectangle[][] walls;
    private final int terrainPositionWidth;
    private final int terrainPositionHeight;
    private final LevelData levelData;
    List<MoveAbleEntity> collisionEntities;

    @Getter
    private float lastDelta;

    public WorldPhysics(LevelData levelData) {
        this.levelData = levelData;
        TiledMapTileLayer wallLayer = (TiledMapTileLayer) levelData.getTerrain().getLayers().get(WALL_LAYER);
        terrainPositionWidth = wallLayer.getWidth();
        terrainPositionHeight = wallLayer.getHeight();
        walls = levelData.getWalls();
    }

    public void update(float delta) {
        if (delta * WorldPhysics.GRAVITY > 5 ) { // accidental gap when resize
            return;
        }
        this.lastDelta = delta;
        collisionEntities = getSortedCollisionEntities(levelData);
        for (DrawableEntity entity : levelData.getAllDrawEntities()) {
            entity.update(delta, this);
        }
        for (DrawableEntity entity : levelData.getAllDrawEntities()) {
            entity.afterUpdate();
        }
    }

    private List<MoveAbleEntity> getSortedCollisionEntities(LevelData levelData) { //TODO add priority to entities
        List<MoveAbleEntity> collEntities = new ArrayList<>();
        if (levelData.getControlEntity() == levelData.getBear()) {
            collEntities.add(levelData.getBear());
            collEntities.add(levelData.getCat());
        } else {
            collEntities.add(levelData.getCat());
            collEntities.add(levelData.getBear());
        }
        collEntities.addAll(levelData.getMoveAbleEntities());
        return collEntities;
    }

    public TerrainCollision entityMoveWithTerrain(Rectangle position, Vector2 velocity) {
        Rectangle resultPosition = new Rectangle(position);
        Vector2 resultVelocity = new Vector2(velocity);
        Direction horizontalDirection = Direction.ofHorizontal(velocity.y);

        resultPosition.y += velocity.y;
        boolean onGround = false;
        boolean hitCeiling = false;

        if (checkTerrainCollision(position, resultPosition, resultVelocity, horizontalDirection)) {
            if (horizontalDirection == Direction.DOWN) {
                onGround = true;
            } else if (horizontalDirection == Direction.UP) {
                hitCeiling = true;
            }
        }
        resultPosition.x += velocity.x;
        checkTerrainCollision(position, resultPosition, resultVelocity, Direction.ofVertical(velocity.x));
        return new TerrainCollision(resultVelocity, onGround, hitCeiling);
    }

    public List<EntityCollision> entitiesCollisionCheck(MoveAbleEntity entityToMove) {
        List<EntityCollision> collisions = new ArrayList<>();
        if (entityToMove.getVelocity().x == 0 && entityToMove.getVelocity().y == 0) {
            return collisions;
        }
        Rectangle moveTo = new Rectangle(entityToMove.getPosition());
        moveTo.y += entityToMove.getVelocity().y;
        moveTo.x += entityToMove.getVelocity().x;

        for (MoveAbleEntity entity : collisionEntities) {
            if (!entity.equals(entityToMove)) { //NOT itself
                if (moveTo.overlaps(entity.getPosition()) && !entityToMove.getPosition().overlaps(entity.getPosition())) {
                    VerticalDirection verticalDirection = getHorizontalCollision(entityToMove.getPosition(), entity.getPosition(), VerticalDirection.of(entityToMove.getVelocity().y));
                    HorizontalDirection horizontalDirection = getVerticalCollision(entityToMove.getPosition(), entity.getPosition(), HorizontalDirection.of(entityToMove.getVelocity().x));

                    Vector2 velocityToCollision = new Vector2(entityToMove.getVelocity());

                    if (verticalDirection == VerticalDirection.UP) {
                        velocityToCollision.y = entity.getPosition().y - (entityToMove.getPosition().y + entityToMove.getPosition().height);
                    } else if (verticalDirection == VerticalDirection.DOWN) {
                        velocityToCollision.y = -(entityToMove.getPosition().y - (entity.getPosition().y + entity.getPosition().height));
                    }

                    if (horizontalDirection == HorizontalDirection.LEFT) {
                        velocityToCollision.x = -(entityToMove.getPosition().x - (entity.getPosition().x + entity.getPosition().width));
                    } else if (horizontalDirection == HorizontalDirection.RIGHT) {
                        velocityToCollision.x = entity.getPosition().x - (entityToMove.getPosition().x + entityToMove.getPosition().width);
                    }
                    collisions.add(new EntityCollision(entity, velocityToCollision, verticalDirection, horizontalDirection));
                }
            }
        }
        return collisions;
    }

    private VerticalDirection getHorizontalCollision(Rectangle entityToMovePosition, Rectangle checkedEntityPosition, VerticalDirection verticalDirection) {
        return verticalDirection; //TODO
    }

    // TODO maybe handle under ?
    private HorizontalDirection getVerticalCollision(Rectangle entityToMovePosition, Rectangle checkedEntityPosition, HorizontalDirection horizontalDirection) {
        if (horizontalDirection == HorizontalDirection.LEFT) {
            if ((entityToMovePosition.x < (checkedEntityPosition.x + checkedEntityPosition.width)) || // entity did not hit right side
                    (entityToMovePosition.y >= (checkedEntityPosition.y + checkedEntityPosition.height))) { // entity was above
                return null;
            }
        } else if (horizontalDirection == HorizontalDirection.RIGHT) {
            if (((entityToMovePosition.x + entityToMovePosition.width) > checkedEntityPosition.x) ||
                    (entityToMovePosition.y >= (checkedEntityPosition.y + checkedEntityPosition.height))) { // entity did not hit right side
                return null;
            }
        }
        return horizontalDirection;
    }

    public void pickAbleEntitiesCheck(ControlAbleEntity controlAbleEntity) {
        List<PickAbleEntity> pickAbleEntities = levelData.getPickAbleEntities();
        for (PickAbleEntity pickAbleEntity : new ArrayList<>(pickAbleEntities)) {
            if (controlAbleEntity.getPosition().overlaps(pickAbleEntity.getPosition())) {
                pickAbleEntity.onPick(levelData, controlAbleEntity);
                Gdx.app.log("", "Pick");
            }
        }
    }


    public static void swapEntities(MoveAbleEntity entity, MoveAbleEntity entityToMove) {
        if (entity.getPosition().x > entityToMove.getPosition().x) {
            // entity is on right side
            float swapXPosition = entity.getPosition().x + entity.getPosition().width;
            entityToMove.getPosition().x = swapXPosition - entityToMove.getPosition().width;
            entity.getPosition().x = swapXPosition - entityToMove.getPosition().width - entity.getPosition().width;
        } else {
            // entity is on left side
            float swapXPosition = entity.getPosition().x;
            entityToMove.getPosition().x = swapXPosition;
            entity.getPosition().x = swapXPosition + entityToMove.getPosition().width;
        }
    }


    private boolean checkTerrainCollision(Rectangle position, Rectangle rectangle, Vector2 resultVelocity, Direction direction) {
        if (direction == null) {
            return false;
        }
        int leftX = (int) rectangle.x;
        int rightX = (int) (rectangle.x + rectangle.width);
        int topY = (int) (rectangle.y + rectangle.height);
        int bottomY = (int) (rectangle.y);

        int fromXOffset = direction == Direction.RIGHT ? 0 : TILE_SIZE * COLLISION_DEPTH_CHECK;
        int toXOffset = direction == Direction.LEFT ? 0 : TILE_SIZE * COLLISION_DEPTH_CHECK;
        int fromYOffset = direction == Direction.UP ? 0 : TILE_SIZE * COLLISION_DEPTH_CHECK;
        int toYOffset = direction == Direction.DOWN ? 0 : TILE_SIZE * COLLISION_DEPTH_CHECK;

        int fromTilesX = Math.max((leftX - fromXOffset) / TILE_SIZE, 0);
        int toTilesX = Math.min((rightX + toXOffset) / TILE_SIZE, terrainPositionWidth - 1);
        int fromTilesY = Math.max((bottomY - fromYOffset) / TILE_SIZE, 0);
        int toTilesY = Math.min((topY + toYOffset) / TILE_SIZE, terrainPositionHeight - 1);

        for (int tilesY = fromTilesY; tilesY <= toTilesY; tilesY++) {
            for (int tilesX = fromTilesX; tilesX <= toTilesX; tilesX++) {
                Rectangle checkedRectangle = walls[tilesY][tilesX];
                if (checkedRectangle != null && rectangle.overlaps(checkedRectangle)) {
                    switch (direction) {
                        case RIGHT:
                            rectangle.x = checkedRectangle.x - rectangle.width;
                            resultVelocity.x = checkedRectangle.x - (position.x + position.width);
                            break;
                        case LEFT:
                            rectangle.x = checkedRectangle.x + checkedRectangle.width;
                            resultVelocity.x = -(position.x - (checkedRectangle.x + checkedRectangle.width));
                            break;
                        case UP:
                            rectangle.y = checkedRectangle.y - rectangle.height;
                            resultVelocity.y = checkedRectangle.y - (position.y + position.height);
                            break;
                        case DOWN:
                            rectangle.y = checkedRectangle.y + checkedRectangle.height;
                            resultVelocity.y = -(position.y - (checkedRectangle.y + checkedRectangle.height));
                            break;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean overlapsWith2Precision(Rectangle rectangle1, Rectangle rectangle2) {
        Rectangle rectangle1Precision = rectangleTo2Precision(rectangle1);
        Rectangle rectangle2Precision = rectangleTo2Precision(rectangle2);
        return rectangle1Precision.overlaps(rectangle2Precision);
    }

    public static Rectangle rectangleTo2Precision(Rectangle rectangle) {
        return new Rectangle(
                Math.round(rectangle.x * 100f) / 100f,
                Math.round(rectangle.y * 100f) / 100f,
                rectangle.width,
                rectangle.height
        );
    }

    @Value
    public static class EntityCollision {
        MoveAbleEntity moveAbleEntity;
        Vector2 velocityToCollision;
        VerticalDirection verticalDirection;
        HorizontalDirection horizontalDirection;
    }

    @Value
    public static class TerrainCollision {
        Vector2 velocity;
        boolean onGround;
        boolean hitCeiling;
    }

    @Value
    public static class ForceMoveResponse {
        boolean pushed;
        float pushedX;
    }

    public enum HorizontalDirection {
        RIGHT,
        LEFT;

        public static HorizontalDirection of(float value) {
            if (value > 0) {
                return RIGHT;
            }
            if (value < 0) {
                return LEFT;
            }
            return null;
        }
    }

    public enum VerticalDirection {
        UP,
        DOWN;

        public static VerticalDirection of(float value) {
            if (value > 0) {
                return UP;
            }
            if (value < 0) {
                return DOWN;
            }
            return null;
        }
    }


    public enum Direction {
        RIGHT,
        LEFT,
        UP,
        DOWN;

        public static Direction ofHorizontal(float value) {
            if (value > 0) {
                return UP;
            }
            if (value < 0) {
                return DOWN;
            }
            return null;
        }

        public static Direction ofVertical(float value) {
            if (value > 0) {
                return RIGHT;
            }
            if (value < 0) {
                return LEFT;
            }
            return null;
        }

    }

}
