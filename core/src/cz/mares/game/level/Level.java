package cz.mares.game.level;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {
    //LEVEL2("level2.tmx", "Level 2", false, "", false, false, true, false),
    INTRO("intro.tmx", "Intro", false, "", false, false, false, false),
    //LEVELX("levelx1.tmx", "Level 1", true, "", true, false, false, false),
    //LEVEL1("level1.tmx", "Level 1", true, "Find bear and return home.", true, false, true, true),
    //LEVEL2("level2.tmx", "Level 2", false, "", false, false, true, false),
    //LEVEL3("level3.tmx", "Level 3", false, "", false, false, false, false),
    LEVEL4("level4.tmx", "Level 4", false, "", false, true, false, false),
    LEVEL5("level5.tmx", "Level 5", false, "", true, false, false, false),
    LEVEL6("level6.tmx", "Level 6", false, "", true, false, true, false),
    LEVEL7("level7.tmx", "Level 7", false, "", false, false, true, false);
    private final String name;
    private final String displayName;
    private final boolean beforeLevelScreen;
    private final String text;
    private final boolean bearSleep;
    private final boolean catSleep;
    private final boolean bearIdle;
    private final boolean instruction;
}
