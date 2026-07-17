package com.yattsun.minesweeperweb.domain;

import lombok.Getter;

@Getter
public enum Difficulty {
    EASY(9, 9, 10),
    NORMAL(16, 16, 48),
    HARD(16, 30, 99);

    private final int height;
    private final int width;
    private final int bombCount;

    Difficulty(int height, int width, int bombCount) {
        this.height = height;
        this.width = width;
        this.bombCount = bombCount;
    }
}
