package battle;

import Entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.GamePanel;

import javax.imageio.ImageIO;

public class BattleManager {
    GamePanel gp;
    BufferedImage background;
    BufferedImage player1;
    BufferedImage player2;
    BufferedImage boss1;
    BufferedImage boss2;
    BufferedImage player;
    BufferedImage boss;

    public boolean battleWon = false;
    public boolean battleLost = false;
    String message = "Enemy used FIREBALL!";
    public int playerHP = 100;
    int enemyHP = 100;
    int attackCooldown = 0;
    int playerAttackCd = 0;

    public BattleManager(GamePanel gp) {
        this.gp = gp;
        try {
            background = ImageIO.read(getClass().getResourceAsStream("/Background/batalie.png"));
            player1 = ImageIO.read(getClass().getResourceAsStream("/batalie/player1.png"));
            player2 = ImageIO.read(getClass().getResourceAsStream("/batalie/player2.png"));
            boss1 = ImageIO.read(getClass().getResourceAsStream("/batalie/boss1.png"));
            boss2 = ImageIO.read(getClass().getResourceAsStream("/batalie/boss2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void update() {
        attackCooldown++;

        if (attackCooldown >= 90) {
            playerHP -= 10;
            message = "Boss used FIREBALL!";
            attackCooldown = 0;

            if (playerHP <= 0) {
                playerHP = 0;
                battleLost = true;
                message = "You were defeated!";
            }
        }
    }

    public void draw(Graphics2D g2) {
        // fundal bătălie
        g2.drawImage(background, 0, 0, gp.screenWidth, gp.screenHeight, null);

        // Player
        g2.drawImage(player, 80, 400, 250, 250, null);
        g2.setColor(Color.GREEN);
        g2.fillRect(160, 400, playerHP, 20);

        //boss
        g2.drawImage(boss, 430, 160, 250, 250, null);
        g2.setColor(Color.GREEN);
        g2.fillRect(500, 170, enemyHP, 20);

        // Mesaj
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString(message, 30, 100);
    }

    public void attack() {
            enemyHP -= 10;
            message = "!!!!!";
            attackCooldown+=40;
        if (enemyHP <= 0) {
            battleWon = true;
        }

    }
    public void resetBattle() {
        enemyHP = 100;
        battleWon = false;
        battleLost = false;
        switch (gp.currentMap) {
            case 0:
                boss = boss1;
                player = player1;
                break;
            case 2:
                boss = boss2;
                player = player2;
                break;
        }
    }
}
