package Entity;

import main.GamePanel;

public class NPC_5 extends BaseNPC {
    public NPC_5(GamePanel gp) {
        super(gp, "npc5");
        addRiddle(new Riddle("Ce cade dar nu se lovește?", new String[]{"Frunza", "Noaptea", "Umbra"}, 1));
    }

    @Override
    public void setDialogue() {
    }
}