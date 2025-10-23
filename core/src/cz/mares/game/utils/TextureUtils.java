package cz.mares.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cz.mares.game.level.LevelLoader;

public class TextureUtils {


    public static void cropTextures(TextureRegion[][] region, int x1, int x2, int y1, int y2) {
        for (int i = 0; i < region.length; i++) {
            for (int j = 0; j < region[0].length; j++) {
                TextureRegion current = region[i][j];
                TextureRegion newTexture = cropTexture(current, x1, x2, y1, y2);
                region[i][j] = newTexture;
            }
        }
    }
    public static TextureRegion cropTexture(TextureRegion region, int x1, int x2, int y1, int y2) {
        return new TextureRegion(region, x1, y1, region.getRegionWidth() - (x1 + x2 ), region.getRegionHeight() - (y1 + y2));
    }

    public static TextureRegion cropTileSet(TextureRegion region, int width, int height){
        int tileX = LevelLoader.TILE_SIZE - width;
        int tileY = LevelLoader.TILE_SIZE - height;
        return cropTexture(region, 0, tileX, 0, tileY);
    }
}
