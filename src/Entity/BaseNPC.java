package Entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BaseNPC extends Entity {
    protected String numeNpc;
    protected List<Riddle> riddles = new ArrayList<>();
    protected int currentRiddleIndex = 0;
    protected int riddleScore = 0;

    public BaseNPC(GamePanel gp, String numeNpc) {
        super(gp);
        this.numeNpc = numeNpc;
        direction = "down";
        speed = 1;
        getImage();
        setDialogue();
    }

    private void getImage() {
        up1 = setup(numeNpc + "U1");
        up2 = setup(numeNpc + "U2");
        down1 = setup(numeNpc + "D1");
        down2 = setup(numeNpc + "D2");
        left1 = setup(numeNpc + "L1");
        left2 = setup(numeNpc + "L2");
        right1 = setup(numeNpc + "R1");
        right2 = setup(numeNpc + "R2");
    }

    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/npc/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public abstract void setDialogue();

    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;
            direction = (i <= 25) ? "up" :
                    (i <= 50) ? "down" :
                            (i <= 75) ? "left" : "right";
            actionLockCounter = 0;
        }
    }

    private boolean riddlesCompleted = false;

    public void speak() {
        System.out.println("speak() called for NPC: " + this.name);
        if (this.hasRiddles()&& !riddlesCompleted) {
            System.out.println("NPC has riddles. Setting gameState to riddleState.");
            gp.currentInteractingNPC = this;
            gp.setGameState(gp.riddleState);
        } else {
            System.out.println("NPC has no riddles. Calling super.speak().");
            super.speak();
        }
    }

    public void markRiddlesCompleted() {
        riddlesCompleted = true;
    }

    @Override
    public void update() {
        setAction();
        collisionOn = false;
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkPlayer(this);
        gp.cChecker.checkTile(this);
        if (!collisionOn) {
            switch (direction) {
                case "up": worldy -= speed; break;
                case "down": worldy += speed; break;
                case "left": worldx -= speed; break;
                case "right": worldx += speed; break;
            }
        }
        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }
    public void addRiddle(Riddle r) {
        riddles.add(r);
    }

    public Riddle getCurrentRiddle() {
        if (currentRiddleIndex < riddles.size()) {
            return riddles.get(currentRiddleIndex);
        }
        return null;
    }

    public boolean answerRiddle(int answerIndex) {
        Riddle r = getCurrentRiddle();
        if (r == null) return false;

        boolean correct = r.isCorrect(answerIndex);
        if (correct) riddleScore++;

        currentRiddleIndex++;
        return correct;
    }

    public boolean isRiddleSessionOver() {
        return currentRiddleIndex >= riddles.size();
    }

    public int getRiddleScore() {
        return riddleScore;
    }

    public int getTotalRiddles() {
        return riddles.size();
    }

    public boolean hasRiddles() {
        return !riddles.isEmpty();
    }
}