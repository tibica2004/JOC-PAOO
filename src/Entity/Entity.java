package Entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity  {
    GamePanel gp;
    public int worldx, worldy;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction="down";

    public int spriteCounter=0;
    public int spriteNum=1;
    public Rectangle solidArea=new Rectangle(0,0,48,48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn=false;
    public int actionLockCounter=0;
    String dialogues[]=new String[20];
    int dialogueIndex=0;
    public boolean invincible=false;
    public int invincibleCounter=0;
    public BufferedImage image, image2,image3;
    public String name;
    public boolean collision = false;
    public int maxLife;
    public int life;
    public int type;

    public Entity (GamePanel gp){
        this.gp=gp;
    }

    public void setAction(){

    }
    public void speak(){
        if(dialogues[dialogueIndex]==null) {
            dialogueIndex=0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch(gp.player.direction){
            case "up":
                direction="down";
                break;
            case "down":
                direction="up";
                break;
            case "left":
                direction="right";
                break;
            case "right":
                direction="left";
                break;
        }
    }
    public void update(){
        setAction();
        collisionOn=false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this,false);
        gp.cChecker.checkEntity(this,gp.npc);
        gp.cChecker.checkEntity(this,gp.monster);
        boolean contactPlayer=gp.cChecker.checkPlayer(this);

        if(this.type==2 &&  contactPlayer==true){
            if(gp.player.invincible==false){
                gp.player.life-=1;
                gp.player.invincible=true;
            }
        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image=null;
        int screenX=worldx-gp.player.worldx+gp.player.screenX;
        int screenY=worldy-gp.player.worldy+gp.player.screenY;
        if(worldx+gp.tileSize>gp.player.worldx-gp.player.screenX && worldx-gp.tileSize<gp.player.worldx+gp.player.screenX && worldy+gp.tileSize>gp.player.worldy - gp.player.screenY && worldy-gp.tileSize<gp.player.worldy+gp.player.screenY) {
            switch(direction){
                case "up":
                    if(spriteNum==1) {
                        image = up1;
                    }
                    if(spriteNum==2) {
                        image=up2;
                    }
                    break;

                case "down":
                    if(spriteNum==1) {
                        image = down1;
                    }
                    if(spriteNum==2) {
                        image = down2;
                    }
                    break;
                case "left":
                    if(spriteNum==1) {
                        image = left1;
                    }
                    if(spriteNum==2) {
                        image=left2;
                    }
                    break;
                case "right":
                    if(spriteNum==1) {
                        image = right1;
                    }
                    if(spriteNum==2) {
                        image=right2;
                    }
                    break;
            }
            g2.drawImage(image, screenX, screenY,null);
        }
    }

    public BufferedImage setup(String imagePath){
        UtilityTool uTool=new UtilityTool();
        BufferedImage image=null;

        try{
            image=ImageIO.read(getClass().getResourceAsStream(imagePath));
            image=uTool.scaleImage(image,gp.tileSize,gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }
}
