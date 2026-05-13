package Entity;

import main.GamePanel;
import main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NPC_static3 extends Entity {

    public NPC_static3(GamePanel gp) {
        super(gp);
        this.gp = gp;
        direction = "down";
        speed = 0; // fără mișcare
        getImage();
    }

    public void getImage() {
        // Imagine unică pentru toate direcțiile (static)
        down1 = setup("/objects/magazin3.png");
        down2 = down1;
        up1 = down1;
        up2 = down1;
        left1 = down1;
        left2 = down1;
        right1 = down1;
        right2 = down1;
    }

    @Override
    public void setAction() {
        // NPC static nu face nicio acțiune
    }

    @Override
    public void speak() {
        if (gp.player.vorbitGoblin) {
            if (gp.player.hasKey > 0) {
                gp.ui.currentDialogue = "Deja ai cheia.";
            } else {
                gp.ui.currentDialogue = "Cheia nu e aici!";
            }
            gp.gameState = gp.dialogueState;
        }
    }
}
