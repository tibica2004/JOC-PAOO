package Entity;

import main.GamePanel;

public class NPCFactory {
    public static BaseNPC createNPC(GamePanel gp, int type) {
        return switch (type) {
            case 1 -> new NPC_1(gp);
            case 2 -> new NPC_2(gp);
            case 3 -> new NPC_3(gp);
            case 4 -> new NPC_4(gp);
            case 5 -> new NPC_5(gp);
            default -> new NPC_1(gp); // fallback NPC
        };
    }
}