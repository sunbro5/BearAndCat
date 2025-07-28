package com.mygdx.game.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.mygdx.game.Disposable;
import com.mygdx.game.GameData;

import java.util.Collections;
import java.util.LinkedList;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class MusicPlayer implements Disposable {

    private final LinkedList<MusicType> musicTypes;
    private Music music;

    private GameData gameData;

    public MusicPlayer(GameData gameData) {
        musicTypes = new LinkedList<>();
        Collections.addAll(musicTypes, MusicType.values());
        this.gameData = gameData;
    }

    public void play() {
        if (gameData.isMusicMute()) {
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
        stop();
    }

    public void unMute() {
        play();
    }

    public void stop() {
        if(music != null){
            music.stop();
            music.dispose();
            music = null;
        }
    }

    public void next(){
        stop();
        play();
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
        PIANO_INTRO("Pinecone_Ambiance_MP3.mp3"),
        PIANO_1("Piano1.mp3"),
        PIANO_2("Piano2.mp3"),
        PIANO_3("Piano3.mp3"),
        PIANO_4("Piano4.mp3"),
        PIANO_5("Piano5.mp3"),
        PIANO_6("Piano6.mp3"),
        PIANO_7("Piano7.mp3"),
        PIANO_8("Piano8.mp3"),
        ;
        private final String name;
    }
}
