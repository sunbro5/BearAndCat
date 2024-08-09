package com.mygdx.game.physics;

import static com.mygdx.game.level.LevelLoader.TILE_SIZE;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.ControlAbleEntity;
import com.mygdx.game.entity.EntityType;
import com.mygdx.game.entity.MoveAbleEntity;
import com.mygdx.game.entity.PickAbleEntity;
import com.mygdx.game.level.LevelData;
import com.mygdx.game.map.TilesetType;

import java.util.ArrayList;
import java.util.List;

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
        terrain = createTerrainColMap(levelData.getMapTiles());
    }

    public void update(float delta) {
        collisionEntities = getSortedCollisionEntities(levelData);
    }

    private List<MoveAbleEntity> getSortedCollisionEntities(LevelData levelData) {
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

    private Rectangle[][] createTerrainColMap(int[][] mapTilesType) {
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
                    terrain[mapX][mapY] = new Rectangle(x, y, TILE_SIZE, TILE_SIZE);
                }
            }
        }
        return terrain;
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

    public List<EntityCollision> entitiesCollisionCheck(ControlAbleEntity entityToMove) {
        List<EntityCollision> collisions = new ArrayList<>();
        if (entityToMove.getVelocity().x == 0 && entityToMove.getVelocity().y == 0) {
            return collisions;
        }
        Rectangle moveFrom = entityToMove.getPosition();
        Rectangle moveTo = new Rectangle(entityToMove.getPosition());
        moveTo.y += entityToMove.getVelocity().y;
        moveTo.x += entityToMove.getVelocity().x;
        for (MoveAbleEntity entity : collisionEntities) {
            if (!entity.equals(entityToMove)) { //NOT entityToMove
                if (moveTo.overlaps(entity.getPosition())) {
                    Direction horizontalDirection = Direction.ofHorizontal(entityToMove.getVelocity().y);
                    boolean onTop = false;
                    //check if entityToMove jumped on another entity
                    if (horizontalDirection == Direction.DOWN && !entityToMove.isOnGround() && moveFrom.y > entity.getPosition().y) {
                        if (entity.canWalkOn() && (!moveFrom.overlaps(entity.getPosition()) || entityToMove.getIsOnTopOf() == entity.getEntityType())) {
                            float velocityToLand = -(moveFrom.y - (entity.getPosition().y + entity.getPosition().height));
                            if (velocityToLand <= 0 || ((entity instanceof ControlAbleEntity))) {
                                entityToMove.getVelocity().y = velocityToLand;
                            }
                        }
                        onTop = true;
                    }
                    // check if entityToMove pushing another entity
                    if (entity.canPush() && entityToMove.isOnGround()) {
                        Rectangle rectangleMoveX = new Rectangle(moveFrom);
                        rectangleMoveX.x += entityToMove.getVelocity().x;
                        if (rectangleMoveX.overlaps(entity.getPosition()) && !moveFrom.overlaps(entity.getPosition())) {

                            Direction verticalDirection = Direction.ofVertical(entityToMove.getVelocity().x);
                            if (verticalDirection != null) {
                                Vector2 pushVelocity = new Vector2(entityToMove.getVelocity().x, 0);
                                ForceMoveResponse response = forceMove(entity, pushVelocity);
                                if (response.isPushed()) {
                                    entityToMove.getVelocity().x = response.getPushedX();
                                } else {
                                    entityToMove.getVelocity().x = 0;
                                }
                            }
                        }
                    }
                    collisions.add(new EntityCollision(entity.getEntityType(), onTop));
                }
            }
        }
        return collisions;
    }

    public void pickAbleEntitiesCheck(Rectangle entityMoved){
        List<PickAbleEntity> pickAbleEntities = levelData.getPickAbleEntities();
        List<PickAbleEntity> pickAbleToRemove = new ArrayList<>();
        for (PickAbleEntity pickAbleEntity : pickAbleEntities) {
            if(entityMoved.overlaps(pickAbleEntity.getPosition())){
                levelData.setScore(levelData.getScore() + 1);
                pickAbleToRemove.add(pickAbleEntity);
                System.out.println("PICK");
            }
        }
        pickAbleEntities.removeAll(pickAbleToRemove);
    }

    public ForceMoveResponse forceMove(MoveAbleEntity entity, Vector2 velocity) {
        Vector2 forceVelocity = new Vector2(velocity.x, 0);
        WorldPhysics.Direction direction = WorldPhysics.Direction.ofVertical(forceVelocity.x);
        if (direction == null) {
            return new ForceMoveResponse(false, 0);
        }
        float startingPosition = entity.getPosition().x;
        WorldPhysics.TerrainCollision response = entityMoveWithTerrain(entity.getPosition(), forceVelocity);
        entity.getPosition().x = response.getMoveTo().x;
        entity.getPosition().y = response.getMoveTo().y;
        entity.setOnGround(response.isOnGround());
        if (startingPosition == entity.getPosition().x) {
            return new ForceMoveResponse(false, 0);
        }
        return new ForceMoveResponse(true, entity.getPosition().x - startingPosition);

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
