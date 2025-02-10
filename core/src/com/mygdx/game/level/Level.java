package com.mygdx.game.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {
    LEVEL1("level1.tmx", true, "", true),
    LEVEL2("level2.tmx", false, "", false)
    ;
    private String name;
    private boolean beforeLevelScreen;
    private String text;
    private boolean bearSleep;
}
