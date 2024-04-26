package com.mygdx.game.physics;

import static com.mygdx.game.GameMap.DIRT;
import static com.mygdx.game.GameMap.TILE_SIZE;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.EntityType;
import com.mygdx.game.entity.MoveAbleEntity;

import java.util.ArrayList;
import java.util.List;

import lombok.Value;

public class WorldPhysics {

    public static final int GRAVITY = 100;
    private static final int COLLISION_DEPTH_CHECK = 1;

    private static final float POSITION_OFFSET = 0.1f;

    private Rectangle[][] terrain;

    private int terrainPositionWidth;
    private int terrainPositionHeight;

    private final List<MoveAbleEntity> entities = new ArrayList<>();

    public WorldPhysics() {
    }

    public void addEntity(MoveAbleEntity entity) {
        entities.add(entity);
    }

    public void addEntities(List<MoveAbleEntity> entities) {
        this.entities.addAll(entities);
    }

    public void initTerrain(int[][] mapTilesType) {
        terrain = new Rectangle[mapTilesType.length][mapTilesType[0].length];
        terrainPositionWidth = mapTilesType[0].length * TILE_SIZE;
        terrainPositionHeight = mapTilesType.length * TILE_SIZE;

        for (int mapY = 0; mapY < mapTilesType[0].length; mapY++) {
            for (int mapX = 0; mapX < mapTilesType.length; mapX++) {
                int x = mapX * TILE_SIZE;
                int y = mapY * TILE_SIZE;
                if (mapTilesType[mapX][mapY] == DIRT) {
                    terrain[mapX][mapY] = new Rectangle(x, y, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    public TerrainCollision entityMoveWithTerrain(Rectangle rectangle, Vector2 velocity) {
        Rectangle rectangleMove = new Rectangle(rectangle);
        rectangleMove.y += velocity.y;
        Direction horizontalDirection = Direction.ofHorizontal(velocity.y);
        boolean onGround = false;
        if (checkTerrainCollision(rectangleMove, horizontalDirection)) {
            velocity.y = 0;
            if (horizontalDirection == Direction.DOWN) {
                onGround = true;
            }
        }
        rectangleMove.x += velocity.x;
        if (checkTerrainCollision(rectangleMove, Direction.ofVertical(velocity.x))) {
            velocity.x = 0;
        }
        return new TerrainCollision(rectangleMove, onGround);
    }

    public EntityCollision entitiesCollisionCheck(ControlAbleEntity entityToMove, Rectangle rectangle, Vector2 velocity) {
        if (velocity.x == 0 && velocity.y == 0) {
            return new EntityCollision(null, false);
        }
        Rectangle rectangleMove = new Rectangle(rectangle);
        rectangleMove.y += velocity.y;
        rectangleMove.x += velocity.x;
        for (MoveAbleEntity entity : entities) {
            if (entity.getEntityType() != entityToMove.getEntityType()) {
                if (rectangleMove.overlaps(entity.getPosition())) {
                    Direction horizontalDirection = Direction.ofHorizontal(velocity.y);
                    boolean onTop = false;
                    if (horizontalDirection == Direction.DOWN && !entityToMove.isOnGround() && rectangle.y > entity.getPosition().y) {
                        if (entity.canWalkOn() && (!rectangle.overlaps(entity.getPosition()) || entityToMove.getIsOnTopOf() == entity.getEntityType())) {
                            float velocityToLand = -(rectangle.y - (entity.getPosition().y + entity.getPosition().height));
                            if (velocityToLand <= 0 || ((entity instanceof ControlAbleEntity))) {
                                velocity.y = velocityToLand;
                            }
                        }
                        onTop = true;
                    }
                    if (entity.canPush() && entityToMove.isOnGround()) {
                        Rectangle rectangleMoveX = new Rectangle(rectangle);
                        rectangleMoveX.x += velocity.x;
                        if (rectangleMoveX.overlaps(entity.getPosition()) && !rectangle.overlaps(entity.getPosition())) {

                            Direction verticalDirection = Direction.ofVertical(velocity.x);
                            if (verticalDirection != null) {
                                Vector2 pushVelocity = new Vector2(velocity.x, 0);
                                MoveAbleEntity.ForceMoveResponse response = entity.forceMove(pushVelocity);
                                if (response.isPushed()) {
                                    velocity.x = response.getPushedX();
                                } else {
                                    velocity.x = 0;
                                }
                            }
                        }
                    }
                    return new EntityCollision(entity.getEntityType(), onTop);
                }
            }
        }
        return new EntityCollision(null, false);
    }


    private boolean checkTerrainCollision(Rectangle rectangle, Direction direction) {
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

                            break;
                        case LEFT:
                            rectangle.x = checkedRectangle.x + checkedRectangle.width + POSITION_OFFSET;
                            break;
                        case UP:
                            rectangle.y = checkedRectangle.y - rectangle.height - POSITION_OFFSET;
                            break;
                        case DOWN:
                            rectangle.y = checkedRectangle.y + checkedRectangle.height + POSITION_OFFSET;
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
        EntityType entityType;
        boolean onTopOf;

    }

    @Value
    public static class TerrainCollision {
        Rectangle moveTo;
        boolean onGround;
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
