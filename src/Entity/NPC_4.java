package Entity;

import main.GamePanel;

public class NPC_4 extends BaseNPC {
    public NPC_4(GamePanel gp) {
        super(gp, "npc4");
        addRiddle(new Riddle("Ce se udă în timp ce usucă?", new String[]{"Fierul de călcat", "Prosopul", "Umbrela"}, 1));
    }

    @Override
    public void setDialogue() {
    }
}