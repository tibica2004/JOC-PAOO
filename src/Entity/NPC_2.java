package Entity;

import main.GamePanel;

public class NPC_2 extends BaseNPC {
    public NPC_2(GamePanel gp) {
        super(gp, "npc2");
        addRiddle(new Riddle("Zboară dar n-are aripi.", new String[]{"Norul", "Vântul", "Timpul"}, 2));
    }

    @Override
    public void setDialogue() {
    }
}