package cz.mares.game.sound;

import cz.mares.game.GameData;
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

    public static void play(EntitySounds entitySoundS, EntitySoundType entitySoundType, float pitch) {
        if (entitySoundS == null || entitySoundS.getGameData().isSoundMute()) {
            return;
        }
        EntitySound sound = CollectionUtils.randomObject(entitySoundS.getSounds().get(entitySoundType));
        if (sound != null) {
            sound.getSound().play(sound.getVolume(), pitch, 0f);
        }
    }

    public static EntitySound getSound(EntitySounds entitySoundS, EntitySoundType entitySoundType) {
        if (entitySoundS == null || entitySoundS.getGameData().isSoundMute()) {
            return null;
        }
        return CollectionUtils.randomObject(entitySoundS.getSounds().get(entitySoundType));
    }

    public static void playLoop(EntitySounds entitySoundS, EntitySoundType entitySoundType) {
        if (entitySoundS == null || entitySoundS.getGameData().isSoundMute()) {
            return;
        }
        EntitySound sound = CollectionUtils.randomObject(entitySoundS.getSounds().get(entitySoundType));
        if (sound != null) {
            sound.getSound().loop(sound.getVolume());
        }
    }

    public static void playLoop(EntitySounds entitySoundS, EntitySoundType entitySoundType, float pitch) {
        if (entitySoundS == null || entitySoundS.getGameData().isSoundMute()) {
            return;
        }
        EntitySound sound = CollectionUtils.randomObject(entitySoundS.getSounds().get(entitySoundType));
        if (sound != null) {
            sound.getSound().loop(sound.getVolume(), pitch, 0f);
        }
    }

    public static void playLoop(EntitySound entitySound, GameData gameData) {
        if (entitySound == null || gameData.isSoundMute()) {
            return;
        }
        entitySound.getSound().loop(entitySound.getVolume());

    }

    public static void playLoop(EntitySound entitySound, GameData gameData, float pitch) {
        if (entitySound == null || gameData.isSoundMute()) {
            return;
        }
        entitySound.getSound().loop(entitySound.getVolume(), pitch, 0f);
    }

    public static void stop(EntitySound sound) {
        if (sound == null || sound.getSound() == null ) {
            return;
        }
        sound.getSound().stop();
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
