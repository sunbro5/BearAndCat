package cz.mares.game.sound;


import com.badlogic.gdx.audio.Sound;

import lombok.Value;

@Value
public class EntitySound {

    Sound sound;
    float volume;
}
