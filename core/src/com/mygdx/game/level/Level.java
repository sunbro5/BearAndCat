package com.mygdx.game.level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Level {
    LEVEL_1("level.1");
    private String name;
}
