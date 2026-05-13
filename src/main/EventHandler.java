package main;

import java.awt.*;

public class EventHandler {
    GamePanel gp;
    EventRect eventRect[][][];
    int previousEventX, previousEventY;
    boolean canTouchEvent=true;
    public EventHandler(GamePanel gp){
        this.gp=gp;
        eventRect=new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        int map = 0;
        int col = 0;
        int row = 0;
        while(map<gp.maxMap && col<gp.maxWorldCol && row<gp.maxWorldRow){
            eventRect[map][col][row]=new EventRect();
            eventRect[map][col][row].x=23;
            eventRect[map][col][row].y=23;
            eventRect[map][col][row].width=2;
            eventRect[map][col][row].height=2;
            eventRect[map][col][row].eventRectDefaultX=eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY=eventRect[map][col][row].y;
            col++;
            if(col==gp.maxWorldCol){
                col=0;
                row++;

                if(row == gp.maxWorldRow){
                    row=0;
                    map++;
                }
            }
        }
    }

    public void checkEvent(){
        int xDistance=Math.abs(gp.player.worldx-previousEventY);
        int yDistance=Math.abs(gp.player.worldy - previousEventY);
        int distance = Math.max(xDistance,yDistance);
        if(distance>gp.tileSize){
            canTouchEvent=true;
        }
        if(canTouchEvent == true){
            if (hit(1,32,1,"any")==true){
                for (int i = 0; i < gp.obj.length; i++) {
                    gp.obj[i] = null;
                }
                gp.currentMap=2;
                gp.aSetter.setNPC();
                gp.aSetter.setObject();
                gp.aSetter.setMonster();
                gp.aSetter.setBoss();
                teleport(2,31,47);
            }

        }

    }

    public boolean hit(int map ,int col, int row, String reqDirection){
        boolean hit=false;

        if(map == gp.currentMap){
            gp.player.solidArea.x=gp.player.worldx+gp.player.solidArea.x;
            gp.player.solidArea.y=gp.player.worldy+gp.player.solidArea.y;
            eventRect[map][col][row].x=col*gp.tileSize+eventRect[map][col][row].x;
            eventRect[map][col][row].y=row*gp.tileSize+eventRect[map][col][row].y;

            if(gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false){
                if(reqDirection.contentEquals("any") || gp.player.direction.contentEquals(reqDirection)){
                    hit=true;

                    previousEventX=gp.player.worldx;
                    previousEventY=gp.player.worldy;
                }
            }

            gp.player.solidArea.x=gp.player.solidAreaDefaultX;
            gp.player.solidArea.y=gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x=eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y=eventRect[map][col][row].eventRectDefaultY;
        }


        return hit;
    }

    public void teleport(int map, int col, int row){
        gp.currentMap = map;
        gp.player.worldx=gp.tileSize*col;
        gp.player.worldy=gp.tileSize*row;
        previousEventX=gp.player.worldx;
        previousEventY=gp.player.worldy;
        canTouchEvent = false;
    }
}

