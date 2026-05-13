package Entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NPC_StaticGoblin extends Entity {


    public NPC_StaticGoblin(GamePanel gp) {
        super(gp);
        this.gp = gp;
        direction = "down";
        speed = 0;
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize;
        solidArea.height = gp.tileSize *2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
    }

    public void getImage() {
        down1 = setup("goblinCusca");
        down2 = down1;
        up1 = down1;
        up2 = down1;
        left1 = down1;
        left2 = down1;
        right1 = down1;
        right2 = down1;
    }

    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize*2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void setAction() {
        // NPC static, fără acțiuni automate
    }

    @Override
    public void speak() {
        if (gp.player.hasKey == 0) {
            gp.ui.currentDialogue = "Adu-mi cheia ascunsă printre\n magazine/butoaie si-ți voi\n spune cum sa te bati cu boss-ul";
            gp.player.vorbitGoblin=true;
        } else {
            gp.ui.currentDialogue = "Mergi lânga Boss si apasa B.";
        }
        gp.gameState = gp.dialogueState;
    }
}
