package com.mygdx.game.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {
    INTRO("intro.tmx", "Intro", false, "", false, false),
    LEVELX("levelx1.tmx", "Level 1", true, "", true, true),
    LEVEL1("level1.tmx", "Level 1", true, "", true, true),
    LEVEL2("level2.tmx", "Level 2", true, "", false, true),
    LEVEL3("level3.tmx", "Level 3", true, "", false, false),
    LEVEL4("level4.tmx", "Level 4", true, "", true, true),
    LEVEL5("level5.tmx", "Level 5", true, "", false, true)
    ;
    private final String name;
    private final String displayName;
    private final boolean beforeLevelScreen;
    private final String text;
    private final boolean bearSleep;
    private final boolean bearIdle;
}
