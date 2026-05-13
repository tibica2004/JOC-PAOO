package Entity;
import main.*;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    KeyHandler keyH;

    public  int screenX;
    public  int screenY;
    public int hasCoin=0;
    public int hasKey=0;
    public boolean vorbitGoblin=false;

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH=keyH;
        screenX=gp.screenWidth/2-(gp.tileSize/2);
        screenY=gp.screenHeight/2-(gp.tileSize/2);

        solidArea=new Rectangle();
        solidArea.x=8;
        solidArea.y=16;
        solidAreaDefaultX=solidArea.x;
        solidAreaDefaultY=solidArea.y;
        solidArea.width=32;
        solidArea.height=32;
        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues(){
        if(gp.currentMap==0){
            worldx = 1 * gp.tileSize;
            worldy = 38 * gp.tileSize;
            direction = "right";
        }
        else
        if(gp.currentMap==1){
            worldx = 8 * gp.tileSize;
            worldy = 27 * gp.tileSize;
            direction = "up";
        }
        else
        if(gp.currentMap==2){
            worldx = 31 * gp.tileSize;
            worldy = 49 * gp.tileSize;
            direction = "up";
        }

        this.speed = 4;
        maxLife=6;
        life=maxLife;
    }

    public void getPlayerImage(){
        try{
            up1= ImageIO.read(getClass().getResourceAsStream("/player/tile000.png"));
            up2= ImageIO.read(getClass().getResourceAsStream("/player/tile002.png"));
            down1= ImageIO.read(getClass().getResourceAsStream("/player/tile006.png"));
            down2= ImageIO.read(getClass().getResourceAsStream("/player/tile008.png"));
            left1= ImageIO.read(getClass().getResourceAsStream("/player/tile009.png"));
            left2= ImageIO.read(getClass().getResourceAsStream("/player/tile011.png"));
            right1= ImageIO.read(getClass().getResourceAsStream("/player/tile003.png"));
            right2= ImageIO.read(getClass().getResourceAsStream("/player/tile005.png"));

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public BufferedImage setup(String imageName){
        UtilityTool uTool=new UtilityTool();
        BufferedImage image=null;

        try{
            image=ImageIO.read(getClass().getResourceAsStream("/player/"+imageName+".png"));
            image=uTool.scaleImage(image,gp.tileSize,gp.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public void update(){
        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed==true || keyH.rightPressed==true){
            if(keyH.upPressed==true){
                direction="up";

            }
            else if(keyH.downPressed==true){
                direction="down";


            } else if (keyH.leftPressed==true) {
                direction="left";


            }
            else if(keyH.rightPressed ==true){
                direction="right";

            }
            collisionOn=false;
            gp.cChecker.checkTile(this);

            int objIndex = gp.cChecker.checkObject(this,true);
            pickUpObject(objIndex);

            int npcIndex=gp.cChecker.checkEntity(this,gp.npc);
            interctNPC(npcIndex);
            int monsterIndex=gp.cChecker.checkEntity(this,gp.monster);
            contactMonster(monsterIndex);

            gp.eHandler.checkEvent();
            if(collisionOn==false){
                switch(direction){
                    case "up":
                        worldy-=speed;
                        break;
                    case "down":
                        worldy+=speed;
                        break;
                    case "left":
                        worldx-=speed;
                        break;
                    case "right":
                        worldx+=speed;
                        break;
                }
            }

            spriteCounter++;
            if(spriteCounter>12){
                if(spriteNum==1){
                    spriteNum=2;
                }
                else if(spriteNum == 2){
                    spriteNum=1;
                }
                spriteCounter=0;
            }
        }
        if(invincible==true){
            invincibleCounter++;
            if(invincibleCounter>60){
                invincible=false;
                invincibleCounter=0;
            }
        }
    }
    public void pickUpObject(int i){
        if(i !=999){
            String objectName=gp.obj[i].name;
            switch(objectName){
                case "Coin":
                    hasCoin++;
                    gp.obj[i]=null;
                    break;
                case "Potion":
                    if(hasCoin>0){
                        gp.obj[i]=null;
                        hasCoin--;
                        life=maxLife;
                        gp.battleManager.playerHP = 100;
                        gp.ui.currentDialogue = "Ai folosit o poțiune!\nCompanionul e complet vindecat.";
                        gp.gameState = gp.dialogueState;
                    }
                    else{
                        gp.ui.currentDialogue = "Nu ai suficienți bani!";
                        gp.gameState = gp.dialogueState;}
                    break;
            }
        }
    }

    public void interctNPC(int i){
        if(i != 999){
            if(gp.keyH.enterPressed){
                gp.npc[i].speak();  // speak decide ce state să seteze
            }
        }
        gp.keyH.enterPressed = false;
    }

    public void contactMonster(int i){
        if(i!=999){
            if(invincible==false){
                life-=1;
                invincible=true;
            }
        }
    }
    public void draw(Graphics2D g2){
        //g2.setColor(Color.white);
        //g2.fillRect(x,y,gp.tileSize,gp.tileSize);

        BufferedImage image=null;
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
        if(invincible==true){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        g2.drawImage(image,screenX,screenY,gp.tileSize,gp.tileSize,null);
        // Reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        //   g2.setFont(new Font("Arial", Font.PLAIN,26));
        //  g2.setColor(Color.white);
        // g2.drawString("Invincible: "+ invincibleCounter,10,400);
    }
}
