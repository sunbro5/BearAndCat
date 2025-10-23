package cz.mares.game.sound;

import cz.mares.game.utils.CollectionUtils;

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

    public static void stopPlayLoop(EntitySound sound, Long id) {
        if (sound == null || id == null || sound.getSound() == null ) {
            return;
        }
        sound.getSound().stop(id);
    }

    public static void setPitch(EntitySound sound, Long id, float pitch) {
        if (sound == null || id == null || sound.getSound() == null ) {
            return;
        }
        sound.getSound().setPitch(id, pitch);
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
