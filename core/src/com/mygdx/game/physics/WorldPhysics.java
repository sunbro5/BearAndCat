package com.mygdx.game.physics;

import static com.mygdx.game.level.LevelLoader.TILE_SIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.entity.PickAbleEntity;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.level.LevelLoader;
import com.mygdx.game.map.TilesetType;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Value;

public class WorldPhysics {

    public static final int GRAVITY = 100;
    private static final int COLLISION_DEPTH_CHECK = 1;
    private static final float POSITION_OFFSET = 0.1f;
    private final Rectangle[][] terrain;
    private final int terrainPositionWidth;
    private final int terrainPositionHeight;
    private final LevelData levelData;
    List<MoveAbleEntity> collisionEntities;

    public WorldPhysics(LevelData levelData) {
        this.levelData = levelData;
        terrainPositionWidth = levelData.getMapTiles()[0].length * TILE_SIZE;
        terrainPositionHeight = levelData.getMapTiles().length * TILE_SIZE;
        terrain = createTerrainCollisionMap(levelData.getMapTiles());
    }

    public void update(float delta) {
        collisionEntities = getSortedCollisionEntities(levelData);
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

    private Rectangle[][] createTerrainCollisionMap(int[][] mapTilesType) {
        Rectangle[][] terrain = new Rectangle[mapTilesType.length][mapTilesType[0].length];

        for (int mapY = 0; mapY < mapTilesType[0].length; mapY++) {
            for (int mapX = 0; mapX < mapTilesType.length; mapX++) {
                int x = mapX * TILE_SIZE;
                int y = mapY * TILE_SIZE;
                TilesetType type = TilesetType.typeByColor(mapTilesType[mapX][mapY]);
                if (type == null) {
                    continue;
                }
                if (type.isCollision()) {
                    Rectangle rectangle = LevelLoader.calculateTile(type, x, y);
                    terrain[mapX][mapY] = rectangle;
                }
            }
        }
        return terrain;
    }

    public TerrainCollision entityMoveWithTerrain(Rectangle position, Vector2 velocity) {
        Rectangle resultPosition = new Rectangle(position);
        Vector2 resultVelocity = new Vector2(velocity);
        Direction horizontalDirection = Direction.ofHorizontal(velocity.y);

        resultPosition.y += velocity.y;
        boolean onGround = false;
        boolean hitCeiling = false;

        if (checkTerrainCollision(resultPosition, resultVelocity, horizontalDirection)) {
            if (horizontalDirection == Direction.DOWN) {
                onGround = true;
            } else if (horizontalDirection == Direction.UP) {
                hitCeiling = true;
            }
        }
        resultPosition.x += velocity.x;
        checkTerrainCollision(resultPosition, resultVelocity,  Direction.ofVertical(velocity.x));
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
                if (moveTo.overlaps(entity.getPosition()) && !entityToMove.getPosition().overlaps(entity.getPosition())) { // entityToMove move to collision
                    Direction horizontalDirection = Direction.ofHorizontal(entityToMove.getVelocity().y);
                    Direction verticalDirection = Direction.ofVertical(entityToMove.getVelocity().x);
                    Vector2 velocityToCollision = new Vector2(entityToMove.getVelocity());

                    if (horizontalDirection == Direction.UP) {
                        velocityToCollision.y = entity.getPosition().y - (entityToMove.getPosition().y + entityToMove.getPosition().height);
                    } else if (horizontalDirection == Direction.DOWN) {
                        velocityToCollision.y = - (entityToMove.getPosition().y - (entity.getPosition().y + entity.getPosition().height));
                    }

                    if (horizontalDirection == Direction.LEFT) {
                        velocityToCollision.x = - (entityToMove.getPosition().x - (entity.getPosition().x + entity.getPosition().width));
                    } else if (horizontalDirection == Direction.RIGHT) {
                        velocityToCollision.x = entity.getPosition().x - (entityToMove.getPosition().x + entityToMove.getPosition().width);
                    }
                    collisions.add(new EntityCollision(entity, velocityToCollision, horizontalDirection, verticalDirection));

//                    //check if entityToMove jumped on another entity
//                    if (horizontalDirection == Direction.DOWN && !entityToMove.isOnGround() && moveFrom.y > entity.getPosition().y + entity.getPosition().height - POSITION_OFFSET) {
//                        if (entity.canWalkOn() && (!moveFrom.overlaps(entity.getPosition()) || entityToMove.getIsOnTopOf() == entity.getEntityType())) {
//                            boolean isOn = (entity.getPosition().y + entity.getPosition().height) <= moveFrom.y;
//                            float moveUpToEntity = entity.getPosition().y + entity.getPosition().height;
//                            if (isOn || ((entity instanceof ControlAbleEntity))) {
//                                System.out.println("WTF");
//                                entityToMove.getPosition().y = moveUpToEntity + 1;
//                                entityToMove.getVelocity().y = -1;
//                                if (entity instanceof ControlAbleEntity) {
//                                    ((ControlAbleEntity) entity).setHaveOnTop(entityToMove); // TODO this should not be there, workaroud, ControlableEntity need tohave both isOnTop and haveOnTop ControlableEntity attribute or different approach
//                                }
//                            }
//                        }
//                        onTop = true;
//                    }
//                    // check if entityToMove pushing another entity
//                    if (entity.canPush() && entityToMove.isOnGround()) {
//                        Rectangle rectangleMoveX = new Rectangle(moveFrom);
//                        rectangleMoveX.x += entityToMove.getVelocity().x;
//                        if (rectangleMoveX.overlaps(entity.getPosition()) && !moveFrom.overlaps(entity.getPosition())) {
//
//                            Direction verticalDirection = Direction.ofVertical(entityToMove.getVelocity().x);
//                            if (verticalDirection != null) {
//                                Vector2 pushVelocity = new Vector2(entityToMove.getVelocity().x, 0);
//                                ForceMoveResponse response = forceMove(entity, pushVelocity, entityToMove);
//                                if (response.isPushed()) {
//                                    entityToMove.getVelocity().x = response.getPushedX();
//                                } else {
//                                    entityToMove.getVelocity().x = 0;
//                                }
//                            }
//                        }
//                    }
                }
            }
        }
        return collisions;
    }

    public void pickAbleEntitiesCheck(Rectangle entityMoved) {
        List<PickAbleEntity> pickAbleEntities = levelData.getPickAbleEntities();
        List<PickAbleEntity> pickAbleToRemove = new ArrayList<>();
        for (PickAbleEntity pickAbleEntity : pickAbleEntities) {
            if (entityMoved.overlaps(pickAbleEntity.getPosition())) {
                levelData.setScore(levelData.getScore() + 1);
                pickAbleToRemove.add(pickAbleEntity);
                Gdx.app.log("", "Pick");
            }
        }
        pickAbleEntities.removeAll(pickAbleToRemove);
    }



    private void swapEntities(MoveAbleEntity entity, ControlAbleEntity entityToMove) {
        if (entity.getPosition().x > entityToMove.getPosition().x) {
            // entity is on right side
            float swapXPosition = entity.getPosition().x + entity.getPosition().width;
//            if (entityToMove.getHaveOnTop() != null) { TODO
//                float entityToMoveDistance = swapXPosition - entityToMove.getPosition().width - entityToMove.getPosition().x;
//                entityToMove.getHaveOnTop().getPosition().x = Math.min(entityToMove.getHaveOnTop().getPosition().x + entityToMoveDistance, swapXPosition - entityToMove.getHaveOnTop().getPosition().width);
//            }
            entityToMove.getPosition().x = swapXPosition - entityToMove.getPosition().width;
            entity.getPosition().x = swapXPosition - entityToMove.getPosition().width - entity.getPosition().width;
        } else {
            // entity is on left side
            float swapXPosition = entity.getPosition().x;
//            if (entityToMove.getHaveOnTop() != null) { TODO
//                float entityToMoveDistance = entityToMove.getPosition().x - swapXPosition;
//                entityToMove.getHaveOnTop().getPosition().x = Math.max(entityToMove.getHaveOnTop().getPosition().x - entityToMoveDistance, swapXPosition);
//            }
            entityToMove.getPosition().x = swapXPosition;
            entity.getPosition().x = swapXPosition + entityToMove.getPosition().width;
        }
    }


    private boolean checkTerrainCollision(Rectangle rectangle, Vector2 resultVelocity, Direction direction) {
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
        int toTilesX = Math.min((rightX + toXOffset) / TILE_SIZE, terrainPositionWidth);
        int fromTilesY = Math.max((bottomY - fromYOffset) / TILE_SIZE, 0);
        int toTilesY = Math.min((topY + toYOffset) / TILE_SIZE, terrainPositionHeight);

        for (int tilesY = fromTilesY; tilesY <= toTilesY; tilesY++) {
            for (int tilesX = fromTilesX; tilesX <= toTilesX; tilesX++) {
                Rectangle checkedRectangle = terrain[tilesX][tilesY];
                if (checkedRectangle != null && rectangle.overlaps(checkedRectangle)) {
                    switch (direction) {
                        case RIGHT:
                            rectangle.x = checkedRectangle.x - rectangle.width - POSITION_OFFSET;
                            resultVelocity.x = checkedRectangle.x - (rectangle.x + rectangle.width) - POSITION_OFFSET;
                            break;
                        case LEFT:
                            rectangle.x = checkedRectangle.x + checkedRectangle.width + POSITION_OFFSET;
                            resultVelocity.x = - (rectangle.x - (checkedRectangle.x + checkedRectangle.width)) + POSITION_OFFSET;
                            break;
                        case UP:
                            rectangle.y = checkedRectangle.y - rectangle.height - POSITION_OFFSET;
                            resultVelocity.y = checkedRectangle.y - (rectangle.y + rectangle.height) - POSITION_OFFSET;
                            break;
                        case DOWN:
                            rectangle.y = checkedRectangle.y + checkedRectangle.height + POSITION_OFFSET;
                            resultVelocity.y = - (rectangle.y - (checkedRectangle.y + checkedRectangle.height)) + POSITION_OFFSET;
                            System.out.println("HEH + " + resultVelocity.y);
                            break;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Value
    public static class EntityCollision {
        MoveAbleEntity moveAbleEntity;
        Vector2 velocityToCollision;
        Direction horizontalDirection;
        Direction verticalDirection;
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
