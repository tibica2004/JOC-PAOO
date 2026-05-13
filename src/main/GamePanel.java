package main;
import Entity.Entity;
import Entity.Player;
import battle.BattleManager;
import environment.EnvironmentManager;
import tile.TileManager;
import Entity.BaseNPC;


import javax.swing.*;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable, Subject {
    final int originalTileSize=16;
    final int scale=3;
    public final int tileSize=originalTileSize *scale;
    public final int maxScreenCol=16;
    public final int maxScreenRow=12;
    public final int screenWidth=tileSize*maxScreenCol;
    public final int screenHeight=tileSize*maxScreenRow;
    public final int maxWorldCol=50;
    public final int maxWorldRow=50;
    public final int maxMap = 3;
    public int currentMap = 0;
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public final int worldWidth=tileSize*maxWorldCol;
    public final int worldHeight=tileSize*maxWorldRow;
    int FPS=60;
    TileManager tileM=new TileManager(this);
    public KeyHandler keyH=new KeyHandler(this);
    public EventHandler eHandler=new EventHandler(this);
    Thread gameThread;
    public CollisionChecker cChecker=new CollisionChecker(this);
    public AssetSetter aSetter=new AssetSetter(this);
    public UI ui=new UI(this);
    public Player player=new Player(this,keyH);
    public Entity obj[]=new Entity[40];
    public Entity npc[]=new Entity[40];
    public Entity monster[]=new Entity[80];
    ArrayList<Entity> entityList=new ArrayList<>();
    EnvironmentManager eManager = new EnvironmentManager(this);
    public int gameState;
    public final int playstate=1;
    public final int pauseState=2;
    public final int dialogueState=3;
    public final int battleState = 4;
    public BattleManager battleManager = new BattleManager(this);
    DatabaseManager db;
    long sessionStartTime;
    String playerName;
    private List<Observer> observers = new ArrayList<>();
    public int riddleState = 5;
    public BaseNPC currentInteractingNPC;
    public int totalRiddlesSolved = 0;
    public int riddleCounter=0;

    public void setGameState(int newState) {
        this.gameState = newState;
        notifyObservers();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(gameState);
        }
    }


    public GamePanel(String playerName){
        this.playerName = playerName;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        db = new DatabaseManager();
        db.connect();
        sessionStartTime = System.currentTimeMillis();
        ui = new UI(this);
        registerObserver(ui);
    }

    public void setupGame(boolean isNewGame){
        if (isNewGame) {
            aSetter.setNPC();
            aSetter.setMonster();
            aSetter.setBoss();
            System.out.println("mapa curenta: "+ currentMap);
            aSetter.setObject();
        }
        else {
            System.out.println("mapa curenta: "+ currentMap);
            aSetter.setObject();
            aSetter.setBoss();
            if(currentMap==2) {
                aSetter.setNPC();
                aSetter.setBoss();
            }
        }
        eManager.setup();
        setGameState(playstate);
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();
    }

    public void startGameThread(){
        gameThread=new Thread(this);
        gameThread.start();
    }

    public void run(){
        double drawInterval=1000000000/FPS;
        double delta=0;
        long lastTime=System.nanoTime();
        long currentTime;
        long timer=0;
        int drawCount=0;
        while(gameThread !=null){
            currentTime=System.nanoTime();
            delta+=(currentTime-lastTime)/drawInterval;
            timer+=(currentTime-lastTime);
            lastTime=currentTime;
            if(delta>=1){
                update();
                drawToTempScreen();
                drawToScreen();
                delta--;
                drawCount++;
            }
            if(timer>=1000000000){
                System.out.println("FPS:"+drawCount);
                drawCount=0;
                timer=0;
            }
        }
    }

    public void update() {
        if (gameState == playstate) {
            player.update();
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) npc[i].update();
            }
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) monster[i].update();
            }
            if(riddleCounter==5 && totalRiddlesSolved<3 || player.life==0){
                SwingUtilities.invokeLater(() -> {
                    if (Main.gameFrame != null) Main.gameFrame.dispose();
                    new GameEndScreen(false,playerName);
                });
                gameThread = null;
            }
        } else if (gameState == battleState) {
            if (keyH.attackPressed) {
                battleManager.attack();
                keyH.attackPressed = false;
            }

            battleManager.update();

            if (battleManager.battleLost) {
                SwingUtilities.invokeLater(() -> {
                    if (Main.gameFrame != null) Main.gameFrame.dispose();
                    new GameEndScreen(false,playerName);
                });
                gameThread = null;
            }

            if (battleManager.battleWon) {
                if (currentMap + 1 < maxMap) {
                    currentMap++;
                    aSetter.setBoss();
                    aSetter.setObject();
                    for (int i = 0; i < npc.length; i++) npc[i] = null;
                    for (int i = 0; i < monster.length; i++) monster[i] = null;
                    eHandler.teleport(1, 8, 27);
                    setGameState(playstate);
                } else {
                    long endTime = System.currentTimeMillis();
                    long totalMs = endTime - sessionStartTime;
                    Duration playTime = Duration.ofMillis(totalMs);

                    db.saveGameStats(playerName, playTime, player.life, player.hasCoin); // ✅ Salvează cu numele jucătorului
                    db.disconnect();

                    SwingUtilities.invokeLater(() -> {
                        if (Main.gameFrame != null) Main.gameFrame.dispose();
                        new GameEndScreen(true,playerName);
                    });
                    gameThread = null;
                }
            }
        }
    }

    public void drawToTempScreen(){
        long drawStart=0;
        if(keyH.checkDrawTime==true){
            drawStart=System.nanoTime();
        }

        tileM.draw(g2);
        entityList.add(player);
        for(int i=0;i< npc.length;i++){
            if(npc[i]!=null){
                entityList.add(npc[i]);
            }
        }
        for(int i=0;i<obj.length;i++){
            if(obj[i]!=null){
                entityList.add(obj[i]);
            }
        }
        for(int i=0;i<monster.length;i++){
            if(monster[i]!=null){
                entityList.add(monster[i]);
            }
        }
        Collections.sort(entityList, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1,Entity e2) {
                int result = Integer.compare(e1.worldy,e2.worldx);
                return result;
            }
        });

        for(int i=0;i<entityList.size();i++){
            entityList.get(i).draw(g2);
        }

        entityList.clear();

        if (gameState == battleState) {
            battleManager.draw(g2);
            return;
        }
        //FOG OF WAR
        if(currentMap==1)
            eManager.draw(g2);
        //UI
        ui.draw(g2);
        if(keyH.checkDrawTime==true){
            long drawEnd=System.nanoTime();
            long passed=drawEnd-drawStart;

            g2.setFont(new Font("Arial",Font.PLAIN,20));
            g2.setColor(Color.white);
            int x=10;
            int y=400;
            int lineHeight=20;

            g2.drawString("WorldX"+player.worldx,x,y); y+=lineHeight;
            g2.drawString("WorldY"+player.worldy,x,y); y+=lineHeight;
            g2.drawString("Col" +(player.worldx + player.solidArea.x)/tileSize,x,y); y+=lineHeight;
            g2.drawString("Row" +(player.worldy + player.solidArea.y)/tileSize,x,y); y+=lineHeight;

            g2.drawString("Draw Time: "+passed,x,y);
            System.out.println("Draw Time: "+passed);
        }
        //ghicitori

        if (gameState == riddleState) {
            ui.drawRiddleScreen(g2, currentInteractingNPC);
        } else if(currentMap==2) {
            ui.drawRiddleCounter(g2, totalRiddlesSolved, 3); // arată mereu scorul în dreapta sus
        }
    }

    public void drawToScreen(){
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }
}
