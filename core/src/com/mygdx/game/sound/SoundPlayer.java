package com.mygdx.game.sound;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.utils.CollectionUtils;

import java.util.List;

public class SoundPlayer {

    public static void play(EntitySounds entitySoundS, EntitySoundType entitySoundType) {
        if (entitySoundS == null || entitySoundS.getGameData().isSoundMute()) {
            return;
        }
        EntitySound sound = CollectionUtils.randomObject(entitySoundS.getSounds().get(entitySoundType));
        if (sound != null) {
            sound.getSound().play(sound.getVolume());
        }
    }

    public static EntitySound getSound(EntitySounds entitySoundS, EntitySoundType entitySoundType) {
        if (entitySoundS == null || entitySoundS.getGameData().isSoundMute()) {
            return null;
        }
        return CollectionUtils.randomObject(entitySoundS.getSounds().get(entitySoundType));
    }

    public static Long playLoop(EntitySounds entitySoundS, EntitySoundType entitySoundType) {
        if (entitySoundS == null || entitySoundS.getGameData().isSoundMute()) {
            return null;
        }
        EntitySound sound = CollectionUtils.randomObject(entitySoundS.getSounds().get(entitySoundType));
        if (sound != null) {
            return sound.getSound().loop(sound.getVolume());
        }
        return null;
    }

    public static void stopPlayLoop(EntitySounds entitySoundS, EntitySoundType entitySoundType, Long id) {
        if (entitySoundS == null || id == null || entitySoundS.getGameData().isSoundMute()) {
            return;
        }
        EntitySound sound = CollectionUtils.randomObject(entitySoundS.getSounds().get(entitySoundType));
        if (sound != null) {
            sound.getSound().stop(id);
        }
    }

    public static void stopPlayLoop(Sound sound, Long id) {
        if (sound == null || id == null ) {
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

    public static void stopAll(EntitySounds entitySoundS) {
        if (entitySoundS == null) {
            return;
        }
        for (List<EntitySound> sounds : entitySoundS.getSounds().values()) {
            for (EntitySound sound : sounds) {
                sound.getSound().stop();
            }
        }
    }

}
