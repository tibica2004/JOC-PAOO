package main;

public class GameState {
    public String username;
    public int map, posX, posY, life, coin;
    public double playTime;

    public GameState(String username, int map, int posX, int posY, int life, int coin, double playTime) {
        this.username = username;
        this.map = map;
        this.posX = posX;
        this.posY = posY;
        this.life = life;
        this.coin = coin;
        this.playTime=playTime;
    }
}