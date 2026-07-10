package com.yattsun.minesweeperweb.domain;

import lombok.Getter;

import java.util.Random;

public  class Board {

    private final int[][] board;
    private final char[][] visible;
    private final int bombCount;
    private boolean initialized = false;
    private long startTime;
    private long elapsedTime;
    @Getter
    private boolean clearTimeSaved;
    @Getter
    private boolean timerRunning;
    @Getter
    private boolean gameOver = false;

    public int getHeight() {
        return board.length;
    }

    public int getWidth() {
        return board[0].length;
    }

    public Board(int sizeY, int sizeX, int bombCount) {
        board = new int[sizeY][sizeX];
        visible = new char[sizeY][sizeX];

        this.bombCount = bombCount;

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                visible[y][x] = '□';
            }
        }
    }

    //爆弾の数を選択
    public void putBombs(int bombNum, int safeY, int safeX) {
        var rand = new Random();
        int count = 0;
        while (count < bombNum) {
            int y = rand.nextInt(board.length);
            int x = rand.nextInt(board[0].length);
            /*ランダムに置いた爆弾の座標が被らないように、かつ
            　最初に指定した座標が爆弾ではない
            */
            if (board[y][x] != 9 && !(y == safeY && x == safeX)) {
                board[y][x] = 9;
                count++;
            }
        }
    }

    //周囲の爆弾の数
    public void calcBombs() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                //爆弾を見つけたら
                if (board[y][x] == 9) {
                    //周囲8マスを見る(8マスは-1~1で表せる)
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            int ny = y + dy;
                            int nx = x + dx;
                            //範囲内か確認
                            if (ny >= 0 && ny < board.length &&
                                    nx >= 0 && nx < board[0].length) {
                                //爆弾じゃなければ+1
                                if (board[ny][nx] != 9) {
                                    board[ny][nx]++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //初期化メソッド,時間の計測開始
    public void init(int safeY, int safeX){
        if(initialized){
            return;
        }

        putBombs(bombCount,safeY,safeX);
        calcBombs();
        initialized = true;

        if(!timerRunning){
            startTime = System.currentTimeMillis();
            timerRunning = true;
        }
    }

    //経過時間取得
    public long getElapsedSeconds(){

        if(!timerRunning){
            return elapsedTime;
        }

        return (System.currentTimeMillis() - startTime) / 1000;
    }

    //計測終了
    public void stopTimer(){
        if(timerRunning){
            elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            timerRunning = false;
        }
    }

    //タイマーを３桁表示
    public String getFormattedElapsedTime(){
        return String.format("%03d",getElapsedSeconds());
    }

    public void markClearTimeSaved(){
        clearTimeSaved = true;
    }

    public void showBombs() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == 9) {
                    visible[y][x] = '*';
                }
            }
        }
    }

    public void openBoard(int y, int x) {
        int value = board[y][x];
        //すでに開いているマスなら何もしない
        if (visible[y][x] != '□') {
            return;
        }

        if (value == 9) {
            visible[y][x] = '*';
            gameOver = true;
            return;
        }
        //int型をchar型に変換
        visible[y][x] = (char) ('0' + value);
        //0のマスを開いて、隣接したマスも0、更にその隣も0であれば連鎖的に開いていく
        if (value == 0) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {

                    int ny = y + dy;
                    int nx = x + dx;

                    if (ny >= 0 && ny < board.length &&
                            nx >= 0 && nx < board[0].length) {
                        openBoard(ny, nx);
                    }
                }
            }
        }
    }

    public char getVisible(int y, int x) {
        return visible[y][x];
    }

    //フラグの入れ替え
    public void toggleFlag(int y, int x) {
        if (visible[y][x] == '□') {
            visible[y][x] = 'F';
        } else if (visible[y][x] == 'F') {
            visible[y][x] = '□';
        }
    }

    public boolean isBomb(int y, int x) {
        return board[y][x] == 9;
    }

    public boolean isClear() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] != 9 && visible[y][x] == '□') {
                    return false;
                }
            }
        }
        return true;
    }

    public char[][] getVisibleBoard() {
        return visible;
    }

    public boolean isClosed(int y, int x) {
        return visible[y][x] == '□';
    }

    public boolean isFlag(int y, int x) {
        return visible[y][x] == 'F';
    }

    public boolean isMineVisible(int y, int x) {
        return visible[y][x] == '*';
    }

    public char getCell(int y, int x) {
        return visible[y][x];
    }

    //盤面の数字、記号を取得
    public String getCellClass(int y, int x){

        char cell = visible[y][x];

        if(cell == '*'){
            return "mine";
        }
        if(cell >= '1' && cell <= '8'){
            return "num-" + cell;
        }

        return "";
    }

    public boolean isOpened(int y, int x){
        return !isClosed(y, x) && !isFlag(y, x);
    }

    public String getDisplayValue(int y, int x){

        if(isClosed(y, x) || isFlag(y, x)){
            return "";
        }

        if(isMineVisible(y, x)){
            return "";
        }

        return visible[y][x] == '0'
                ? ""
                : String.valueOf(visible[y][x]);
    }


}
