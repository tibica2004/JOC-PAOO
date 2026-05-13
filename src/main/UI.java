package main;

import Entity.Entity;
import object.OBJ_Heart;
import object.OBJ_coin;
import Entity.Riddle;
import Entity.BaseNPC;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;

public class UI implements Observer {
    GamePanel gp;
    Graphics2D g2;
    Font arial_40;
    BufferedImage coinImage;
    BufferedImage heart_full,heart_half,heart_blank;
    double playTime;
    DecimalFormat dFromat=new DecimalFormat("#0.00");
    public String currentDialogue="";
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<Integer> messageTimers = new ArrayList<>();

    public UI(GamePanel gp){
        this.gp=gp;
        arial_40=new Font("Arial",Font.PLAIN,25);
        OBJ_coin coin=new OBJ_coin(gp);
        Entity heart=new OBJ_Heart(gp);
        coinImage=coin.down1;
        heart_full=heart.image;
        heart_half=heart.image2;
        heart_blank=heart.image3;
    }
    public void update(int newGameState){
        if (gp.gameState == gp.pauseState) {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Salvezi progresul?",
                    "Save Game",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                gp.db.saveGameState(
                        gp.playerName,
                        gp.currentMap,
                        gp.player.worldx,
                        gp.player.worldy,
                        gp.player.life,
                        gp.player.hasCoin,
                        playTime
                );
            }
        }
        System.out.println("GameState changed to " + newGameState);
    }
    public void draw(Graphics2D g2) {

        this.g2 = g2;
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        if (gp.gameState == gp.playstate) {
            // Desenează imaginea monedei
            g2.drawImage(coinImage, gp.tileSize / 2 - 10, 57 - gp.tileSize / 2, gp.tileSize, gp.tileSize, null);


            // Desenează numărul de monede colectate
            g2.drawString("x " + gp.player.hasCoin, gp.tileSize + 10, 65);

            // Timpul de joc (opțional)
            playTime += (double) 1 / 60;  // dacă rulezi la 60 FPS
            g2.drawString("Time:" + dFromat.format(playTime), gp.tileSize * 11, 65);

            drawPlayerLife();
        }

        if (gp.gameState == gp.pauseState) {
            drawPlayerLife();

            drawPauseScreen();
        }
        if(gp.gameState==gp.dialogueState){
            drawPlayerLife();

            drawDialogueScreen();
        }
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.setColor(Color.WHITE);

        int y = gp.screenHeight - 50;
        for (int i = 0; i < messages.size(); i++) {
            g2.drawString(messages.get(i), 20, y);
            y -= 40;

            messageTimers.set(i, messageTimers.get(i) + 1);
            if (messageTimers.get(i) > 180) {  // ~3 secunde la 60 FPS
                messages.remove(i);
                messageTimers.remove(i);
                i--;
            }
        }

    }

    public void addMessage(String text) {
        messages.add(text);
        messageTimers.add(0);
    }

    public void drawPauseScreen(){
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
        String text = "PAUSED";
        int x=getXforCenteredText(text);
        int y=gp.screenHeight/2;
        g2.drawString(text,x,y);
    }

    public void drawPlayerLife(){
        int x=gp.tileSize*11;
        int y=90;
        int i=0;
        while(i<gp.player.maxLife/2){
            g2.drawImage(heart_blank,x,y,null);
            i++;
            x+=gp.tileSize;
        }
        x=gp.tileSize*11;
        y=90;
        i=0;


        while(i<gp.player.life){
            g2.drawImage(heart_half,x,y,null);
            i++;
            if(i<gp.player.life){
                g2.drawImage(heart_full,x,y,null);
            }
            i++;
            x+=gp.tileSize;
        }
    }

    public void drawDialogueScreen(){
        int x=gp.tileSize*2;
        int y=gp.tileSize/2;
        int width=gp.screenWidth-(gp.tileSize*4);
        int height=gp.tileSize*4;
        drawSubWindow(x,y,width,height);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
        x+=gp.tileSize;
        y+=gp.tileSize;

        for(String line:currentDialogue.split("\n")){
            g2.drawString(line,x,y);
            y+=40;
        }
    }
    public void drawSubWindow(int x, int y, int width, int height){
        Color c=new Color(0,0,0,190);
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);

        c=new Color(255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);
    }
    public int getXforCenteredText(String text){
        int length=(int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x=gp.screenWidth/2-length/2;
        return x;
    }
    private void saveGame() {
        long currentTime = System.currentTimeMillis();
        long totalMs = currentTime - gp.sessionStartTime;
        Duration playTime = Duration.ofMillis(totalMs);

        gp.db.saveGameStats(
                gp.playerName,
                playTime,
                gp.player.life,
                gp.player.hasCoin
        );

        JOptionPane.showMessageDialog(null, "Jocul a fost salvat cu succes!");
    }
    //ghicitori
    public int riddleSelection = 0;

    public void drawRiddleScreen(Graphics2D g2, BaseNPC npc) {
        Riddle r = npc.getCurrentRiddle();
        if (r == null) return;

        int x = 100;
        int y = 100;

        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("Ghicitoare:", x, y);
        y += 40;
        g2.drawString(r.question, x, y);
        y += 50;

        for (int i = 0; i < r.choices.length; i++) {
            if (i == riddleSelection) {
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.drawString((i + 1) + ". " + r.choices[i], x, y);
            y += 30;
        }
    }

    public void drawRiddleCounter(Graphics2D g2, int score, int total) {
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);
        g2.drawString("Ghicitori rezolvate: " + score + "/" + total, gp.screenWidth - 300, 40);
    }
}


