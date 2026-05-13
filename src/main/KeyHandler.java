package main;

import Entity.BaseNPC;
import Entity.Boss;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed,enterPressed,attackPressed;
    public int riddleCounter;
    boolean checkDrawTime=false;
    boolean canTriggerBattle = true;
    public KeyHandler(GamePanel gp){
        this.gp=gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code=e.getKeyCode();
        if(gp.gameState==gp.playstate){
            if(code==KeyEvent.VK_W){
                upPressed=true;
            }
            if(code==KeyEvent.VK_S){
                downPressed=true;
            }
            if(code==KeyEvent.VK_A){
                leftPressed=true;
            }
            if(code==KeyEvent.VK_D){
                rightPressed=true;
            }

            if(code==KeyEvent.VK_P){
                gp.setGameState(gp.pauseState);
            }
            if(code == KeyEvent.VK_B && gp.currentMap != 1) {

                // Găsim dacă player-ul e lângă Boss
                int bossIndex = gp.cChecker.checkEntity(gp.player, gp.npc);

                // Dacă ești pe harta 2 și nu ai rezolvat toate ghicitorile
                if (gp.currentMap == 2 && gp.totalRiddlesSolved < 3) {
                    gp.ui.addMessage("Trebuie să rezolvi toate cele 3 ghicitori înainte de luptă!");
                    return;
                }

                // Dacă ești lângă Boss (presupunem că e npc[5] sau folosim instanceof)
                if (bossIndex != 999 && gp.npc[bossIndex] instanceof Boss) {
                    if(gp.currentMap==0 && gp.player.hasKey > 0) {
                        gp.battleManager.resetBattle();
                        gp.setGameState(gp.battleState);
                    } else if(gp.currentMap==2){
                        gp.battleManager.resetBattle();
                        gp.setGameState(gp.battleState);
                    }
                } else {
                    gp.ui.addMessage("Apropie-te de Boss pentru a începe lupta.");
                }
            }
            if(code==KeyEvent.VK_ENTER){
                enterPressed=true;
            }
            if(code==KeyEvent.VK_T) {
                if (checkDrawTime == false) {
                    checkDrawTime = true;
                } else if (checkDrawTime == true) {
                    checkDrawTime = false;
                }
            }
        }
        else if(gp.gameState==gp.pauseState){
            if(code==KeyEvent.VK_P){
                gp.setGameState(gp.playstate);
            }
        }
        else if(gp.gameState==gp.battleState){
            if(code==KeyEvent.VK_B){
                gp.setGameState(gp.playstate);
            }
            if(code == KeyEvent.VK_F){
                attackPressed = true;
            }
        }

        else if(gp.gameState==gp.dialogueState){
            if(code==KeyEvent.VK_ENTER){
                gp.setGameState(gp.playstate);
            }
        }
        if (gp.gameState == gp.riddleState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.riddleSelection = (gp.ui.riddleSelection + 2) % 3;
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.riddleSelection = (gp.ui.riddleSelection + 1) % 3;
            }
            if (code == KeyEvent.VK_ENTER) {
                if (gp.currentInteractingNPC != null) {
                    BaseNPC npc = gp.currentInteractingNPC;
                    boolean correct = npc.answerRiddle(gp.ui.riddleSelection);
                    if (correct) System.out.print("merge");
                    else System.out.print("nu merge");

                    if (npc.isRiddleSessionOver()) {
                        gp.totalRiddlesSolved += npc.getRiddleScore();
                        npc.markRiddlesCompleted();  // marchează ca rezolvat
                        gp.riddleCounter++;
                        gp.currentInteractingNPC = null;
                        gp.gameState = gp.playstate;
                    }
                } else {
                    System.out.println("NPC is null! Eroare de interacțiune.");
                }
            }
        }

    }



    @Override
    public void keyReleased(KeyEvent e) {
        int code=e.getKeyCode();
        if(code==KeyEvent.VK_W){
            upPressed=false;
        }
        if (code == KeyEvent.VK_B) {
            canTriggerBattle = true;
        }
        if(code==KeyEvent.VK_S){
            downPressed=false;
        }
        if(code==KeyEvent.VK_A){
            leftPressed=false;
        }
        if(code==KeyEvent.VK_D){
            rightPressed=false;
        }
        if(code == KeyEvent.VK_F){
            attackPressed = false;
        }
    }
}
