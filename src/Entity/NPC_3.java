package Entity;

import main.GamePanel;

public class NPC_3 extends BaseNPC {
    public NPC_3(GamePanel gp) {
        super(gp, "npc3");
        addRiddle(new Riddle("Are dinți dar nu mușcă.", new String[]{"Pieptenele", "Ursul", "Ceasul"}, 0));
    }

    @Override
    public void setDialogue() {}
}