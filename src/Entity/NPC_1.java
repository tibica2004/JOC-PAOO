package Entity;

import main.GamePanel;

public class NPC_1 extends BaseNPC {
    public NPC_1(GamePanel gp) {
        super(gp, "npc1");
        addRiddle(new Riddle("Are nas dar nu miroase.", new String[]{"Clovnul", "Fața", "Avionul"}, 2));
    }

    @Override
    public void setDialogue() {}
}