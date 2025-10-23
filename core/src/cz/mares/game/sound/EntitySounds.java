package cz.mares.game.sound;

import cz.mares.game.Disposable;
import cz.mares.game.GameData;

import java.util.List;
import java.util.Map;

import lombok.Value;

@Value
public class EntitySounds implements Disposable {

    Map<EntitySoundType, List<EntitySound>> sounds;
    GameData gameData;

    public void dispose() {
        for (List<EntitySound> soundSet : sounds.values()) {
            for (EntitySound sound: soundSet) {
                sound.getSound().dispose();
            }
        }
    }
}
