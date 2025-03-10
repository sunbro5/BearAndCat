package com.mygdx.game.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {
    LEVEL1("level1.tmx", true, "", true, true),
    LEVEL2("level2.tmx", false, "", false, true),
    LEVEL3("level3.tmx", false, "", false, false);
    ;
    private final String name;
    private final boolean beforeLevelScreen;
    private final String text;
    private final boolean bearSleep;
    private final boolean bearIdle;
}
