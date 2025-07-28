package com.mygdx.game.sound;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.Disposable;
import com.mygdx.game.GameData;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Value;

@Value
public class EntitySound implements Disposable {

    Map<EntitySoundType, List<Sound>> sounds;
    float volume;
    GameData gameData;

    public void dispose() {
        for (List<Sound> soundSet : sounds.values()) {
            for (Sound sound: soundSet) {
                sound.dispose();
            }
        }
    }
}
