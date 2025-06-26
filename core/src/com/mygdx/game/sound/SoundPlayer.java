package com.mygdx.game.sound;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.utils.CollectionUtils;

import java.util.List;
import java.util.Set;

public class SoundPlayer {

    public static void play(EntitySound entitySound, EntitySoundType entitySoundType) {
        if (entitySound == null) {
            return;
        }
        Sound sound = CollectionUtils.randomObject(entitySound.getSounds().get(entitySoundType));
        if (sound != null) {
            sound.play(entitySound.getVolume());
        }
    }

    public static Sound getSound(EntitySound entitySound, EntitySoundType entitySoundType) {
        if (entitySound == null) {
            return null;
        }
        return CollectionUtils.randomObject(entitySound.getSounds().get(entitySoundType));
    }

    public static Long playLoop(EntitySound entitySound, EntitySoundType entitySoundType) {
        if (entitySound == null) {
            return null;
        }
        Sound sound = CollectionUtils.randomObject(entitySound.getSounds().get(entitySoundType));
        if (sound != null) {
            return sound.loop(entitySound.getVolume());
        }
        return null;
    }

    public static void stopPlayLoop(EntitySound entitySound, EntitySoundType entitySoundType, Long id) {
        if (entitySound == null || id == null) {
            return;
        }
        Sound sound = CollectionUtils.randomObject(entitySound.getSounds().get(entitySoundType));
        if (sound != null) {
            sound.stop(id);
        }
    }

    public static void stopPlayLoop(Sound sound, Long id) {
        if (sound == null || id == null) {
            return;
        }
        sound.stop(id);
    }

    public static void setPitch(Sound sound, Long id, float pitch) {
        if (sound == null || id == null) {
            return;
        }
        sound.setPitch(id, pitch);
    }

    public static void stopAll(EntitySound entitySound) {
        if (entitySound == null) {
            return;
        }
        for (List<Sound> sounds : entitySound.getSounds().values()) {
            for (Sound sound : sounds) {
                sound.stop();
            }
        }
    }

}
