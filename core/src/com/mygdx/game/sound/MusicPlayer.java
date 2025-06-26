package com.mygdx.game.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mygdx.game.Disposable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class MusicPlayer implements Disposable {

    private final LinkedList<MusicType> musicTypes;
    private Music music;
    private final AtomicBoolean mute = new AtomicBoolean(false);

    public MusicPlayer() {
        musicTypes = new LinkedList<>();
        Collections.addAll(musicTypes, MusicType.values());
        loadMusic(getNextMusic());
    }

    public void play() {
        if (mute.get()) {
            return;
        }
        Gdx.app.debug("", "Playing - " + musicTypes.getFirst());
        if (music == null) {
            loadMusic(getNextMusic());
        }
        music.setLooping(false);
        music.setOnCompletionListener(onCompletionListener());
        music.play();
    }

    public void mute() {
        mute.set(false);
    }

    public void unMute() {
        mute.set(true);
        play();
    }

    public void toggle() {
        if (mute.get()) {
            return;
        }
        if (music == null || !music.isPlaying()) {
            play();
        } else {
            stop();
        }
    }

    public void stop() {
        if (mute.get()) {
            return;
        }
        music.stop();
        music.dispose();
        music = null;
    }

    private Music.OnCompletionListener onCompletionListener() {
        return new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                stop();
                play();
            }
        };
    }

    private MusicType getNextMusic() {
        MusicType next = musicTypes.poll();
        musicTypes.offer(next);
        return next;
    }

    private void loadMusic(MusicType musicType) {
        if (music != null) {
            music.stop();
            music.dispose();
        }
        music = Gdx.audio.newMusic(Gdx.files.internal("music/" + musicType.name));
    }

    @Override
    public void dispose() {
        if (music != null) {
            stop();
        }
    }


    @Getter
    @RequiredArgsConstructor
    enum MusicType {
        PIANO_X("Pinecone_Ambiance_MP3.mp3"),
//        PIANO_1("Piano1.mp3"),
//        PIANO_2("Piano2.mp3"),
//        PIANO_3("Piano3.mp3"),
//        PIANO_4("Piano4.mp3"),
//        PIANO_5("Piano5.mp3"),
//        PIANO_6("Piano6.mp3"),
//        PIANO_7("Piano7.mp3"),
//        PIANO_8("Piano8.mp3"),
        ;
        private final String name;
    }
}
