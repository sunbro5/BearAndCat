package com.mygdx.game.sound;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.Disposable;

import java.util.Map;

import lombok.Getter;
import lombok.Value;

@Value
public class EntitySound implements Disposable {

    Map<EntitySoundType, Sound> sounds;

    float volume;

    public void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
    }
}
