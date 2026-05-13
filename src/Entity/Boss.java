package Entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Boss extends Entity {
    public Boss(GamePanel gp,int map) {
        super(gp);
        direction = "down";
        speed = 0;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize *2;
        solidArea.height = gp.tileSize *2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        collision = true;
        getImage(map);
        setDialogue();
    }

    public void getImage(int map) {
            switch(map) {
                case 0:
                    up1 = setup("mage1");
                    up2 = setup("mage1");
                    down1 = setup("mage1");
                    down2 = setup("mage1");
                    left1 = setup("mage1");
                    left2 = setup("mage1");
                    right1 = setup("mage1");
                    right2 = setup("mage1");
                    System.out.println("Set image for map " + map);
                    break;
                case 2:
                    up1 = setup("mage2");
                    up2 = setup("mage2");
                    down1 = setup("mage2");
                    down2 = setup("mage2");
                    left1 = setup("mage2");
                    left2 = setup("mage2");
                    right1 = setup("mage2");
                    right2 = setup("mage2");
                    System.out.println("Set image for map " + map);
                    break;
            }
            /* up1 = ImageIO.read(getClass().getResourceAsStream("/npc/mage1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/npc/mage1.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/npc/mage1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/npc/mage1.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/npc/mage1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/npc/mage1.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/npc/mage1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/npc/mage1.png"));
             */

    }

    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/npc/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize*2, gp.tileSize*2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void setDialogue() {
        dialogues[0] = "Daca esti pregatit de lupta apasa tasta B";
        dialogues[1] = "Nu poți trece de mine fara o confruntare, \n daca esti gata apasa tasta B!";
    }

    @Override
    public void setAction() {
        // Boss-ul nu face nimic, stă pe loc.
    }

    @Override
    public void update() {
        // Nu se mișcă, dar tot apelăm super.update() dacă vrei să animezi sprite-ul
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }
}
