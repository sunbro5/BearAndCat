package com.mygdx.game.map;

import static com.mygdx.game.level.LevelLoader.TILE_SIZE;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TilesetType {

    BEAR_AND_CAT(0x8d4300, 0, 0, false), // 141,67,0
    BOX(0x000000, 1, 3, false), // 0,0,0
    GREEN_BOX(0x000000, 11, 7, true),
    DIRT_1_1(0xFF6464, 8, 1, true), // 255,100,100 --
    DIRT_1_2(0xFF3232, 9, 1, true), // 255,50,50
    DIRT_1_LEFT(0xFF3264, 7, 1, true), // 255,50,100
    DIRT_1_RIGHT(0xFF6432, 10, 1, true), // 255,100,50
    DIRT_2_1(0xF06464, 8, 2, true), // 240,100,100
    DIRT_2_2(0xF03232, 9, 2, true), // 240,50,50
    DIRT_2_LEFT(0xF03264, 7, 2, true), // 240,50,100
    DIRT_2_RIGHT(0xF06432, 10, 2, true), // 240,100,50
    DIRT_3_1(0xE16464, 8, 3, true), // 225,100,100
    DIRT_3_2(0xE13232, 9, 3, true), // 225,50,50
    DIRT_3_LEFT(0xE13264, 7, 3, true), // 225,50,100
    DIRT_3_RIGHT(0xE16432, 10, 3, true), // 225,100,50
    DIRT_3_1_SMALL(0xE19664, 8, 4, true, 20), // 225,150,100
    END_1(0x0A0101, 5, 6, false), // 10,1,1 --
    END_2(0x0A0000, 6, 6, false), // 10,0,0 --
    END_3(0x0A0001, 5, 7, false), // 10,0,1
    END_4(0x0A0100, 6, 7, false), // 10,1,0
    STAR(0x424242, 0, 0, false); // 10,1,0

    private final int typeColour;
    private final int tilesetX;
    private final int tilesetY;
    private final boolean collision;
    private final int tileHeight;

    TilesetType (int typeColour, int tilesetX, int tilesetY, boolean collision){
        this(typeColour, tilesetX, tilesetY, collision, TILE_SIZE);
    }

    public static TilesetType typeByColor(int typeColour) {
        for (TilesetType type : TilesetType.values()) {
            if (typeColour == type.typeColour) {
                return type;
            }
        }
        return null;
    }

}
