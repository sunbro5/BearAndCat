package com.mygdx.game.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import lombok.RequiredArgsConstructor;

public class SoundPlayer {

    public static void play(EntitySound entitySound, EntitySoundType entitySoundType) {
        if (entitySound == null) {
            return;
        }
        Sound sound = entitySound.getSounds().get(entitySoundType);
        if (sound != null) {
            sound.play(entitySound.getVolume());
        }

    }

    public static void stop(EntitySound entitySound, EntitySoundType entitySoundType) {
        if (entitySound == null) {
            return;
        }
        Sound sound = entitySound.getSounds().get(entitySoundType);
        if (sound != null) {
            sound.stop();
        }

    }

    public static Sound loadSound(String name) {
        return Gdx.audio.newSound(Gdx.files.internal("sound/" + name));
    }

}
