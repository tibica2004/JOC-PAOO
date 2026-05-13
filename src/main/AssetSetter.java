package main;

import Entity.*;
import monster.MONSTER1;
import object.OBJ_Heart;
import object.OBJ_coin;
import object.OBJ_potion;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp=gp;
    }
    public void setObject(){

        for (int i = 0; i < gp.obj.length; i++) {
            gp.obj[i] = null;
        }

        if (gp.currentMap == 0) {
            gp.npc[0] = new NPC_Static(gp);
            gp.npc[0].worldx = gp.tileSize * 20;
            gp.npc[0].worldy = gp.tileSize * 7;

            gp.npc[1] = new NPC_Static2(gp);
            gp.npc[1].worldx = gp.tileSize * 8;
            gp.npc[1].worldy = gp.tileSize * 36;

            gp.npc[2] = new NPC_static3(gp);
            gp.npc[2].worldx = gp.tileSize * 22;
            gp.npc[2].worldy = gp.tileSize * 40;

            gp.npc[3] = new NPC_Static4(gp);
            gp.npc[3].worldx = gp.tileSize * 30;
            gp.npc[3].worldy = gp.tileSize * 34;

            gp.npc[4] = new NPC_Static5(gp);
            gp.npc[4].worldx = gp.tileSize * 5;
            gp.npc[4].worldy = gp.tileSize * 14;

            gp.npc[16] = new NPC_static3(gp);
            gp.npc[16].worldx = gp.tileSize * 12;
            gp.npc[16].worldy = gp.tileSize * 14;

            gp.npc[6] = new NPC_Static2(gp);
            gp.npc[6].worldx = gp.tileSize * 19;
            gp.npc[6].worldy = gp.tileSize * 18;

            gp.npc[7] = new NPC_Static5(gp);
            gp.npc[7].worldx = gp.tileSize * 23;
            gp.npc[7].worldy = gp.tileSize * 18;

            gp.npc[8] = new NPC_Static4(gp);
            gp.npc[8].worldx = gp.tileSize * 8;
            gp.npc[8].worldy = gp.tileSize * 18;

            gp.npc[9] = new NPC_static3(gp);
            gp.npc[9].worldx = gp.tileSize * 14;
            gp.npc[9].worldy = gp.tileSize * 26;

            gp.npc[10] = new NPC_Static2(gp);
            gp.npc[10].worldx = gp.tileSize * 19;
            gp.npc[10].worldy = gp.tileSize * 24;

            gp.npc[11] = new NPC_static3(gp);
            gp.npc[11].worldx = gp.tileSize * 23;
            gp.npc[11].worldy = gp.tileSize * 27;

            gp.npc[12] = new NPC_Static5(gp);
            gp.npc[12].worldx = gp.tileSize * 19;
            gp.npc[12].worldy = gp.tileSize * 14;

            gp.npc[13] = new NPC_Static2(gp);
            gp.npc[13].worldx = gp.tileSize * 30;
            gp.npc[13].worldy = gp.tileSize * 18;

            gp.npc[14] = new NPC_static3(gp);
            gp.npc[14].worldx = gp.tileSize * 30;
            gp.npc[14].worldy = gp.tileSize * 31;

            gp.npc[15] = new NPC_StaticGoblin(gp);
            gp.npc[15].worldx = gp.tileSize * 38;
            gp.npc[15].worldy = gp.tileSize * 25;
        }

        if(gp.currentMap==1) {
            setupOBJ(1, 9, 26);
            setupOBJ(2, 12, 22);
            setupOBJ(3, 8, 24);
            setupOBJ(4, 16, 25);
            setupOBJ(5, 23, 26);
            setupOBJ(6, 29, 24);
            setupOBJ(7, 18, 19);
            setupOBJ(8, 30, 16);
            setupOBJ(9, 32, 12);
            setupOBJ(10, 32, 6);
            setupOBJ(11, 27, 6);
            setupOBJ(12, 18, 6);
            setupOBJ(13, 14, 4);
            setupOBJ(14, 8, 6);
            setupOBJ(15, 8, 14);
            setupOBJ(16, 13, 16);
            setupOBJ(17, 16, 14);
            setupOBJ(18, 20, 15);
            setupOBJ(19, 26, 12);
            setupOBJ(20, 29, 18);
        }

        if (gp.currentMap==2) {
            gp.obj[21]=new OBJ_potion(gp);
            gp.obj[21].worldx=gp.tileSize*20;
            gp.obj[21].worldy=gp.tileSize*20;
        }

    }
    public void setNPC() {
        // Resetăm toți NPC-ii existenți
        for (int i = 0; i < gp.npc.length; i++) {
            gp.npc[i] = null;
        }

        // Poziții predefinite pentru cei 5 NPC-uri de pe harta 3 (currentMap == 1)
        int[][] npcPositions = {
                {10, 10},
                {12, 14},
                {16, 18},
                {20, 22},
                {24, 26}
        };

        if(gp.currentMap==2) {
            for (int i = 0; i < 5; i++) {
                BaseNPC npc = NPCFactory.createNPC(gp, i + 1);
                npc.worldx = gp.tileSize * npcPositions[i][0];
                npc.worldy = gp.tileSize * npcPositions[i][1];
                gp.npc[i] = npc;
            }
        }
    }
    public void setMonster(){
        gp.monster[0]=new MONSTER1(gp);
        gp.monster[0].worldx=gp.tileSize*23;
        gp.monster[0].worldy=gp.tileSize*36;
    }
    public void setBoss() {
        if (gp.currentMap == 0) {
            Boss boss = new Boss(gp,0);
            boss.worldx = gp.tileSize * 1; // poziție în tile-uri
            boss.worldy = gp.tileSize * 15 + gp.tileSize / 2;
            gp.npc[5] = boss; // îl pui pe harta 0, slotul 5
        }
        else if (gp.currentMap == 2) {
            gp.npc[5] = null;
            Boss boss = new Boss(gp,2);
            boss.worldx = gp.tileSize * 1;
            boss.worldy = gp.tileSize * 19 + gp.tileSize / 2;
            gp.npc[5] = boss;
        }
    }
    public void setupOBJ(int i,int pozx,int pozy){
        gp.obj[i]=new OBJ_coin(gp);
        gp.obj[i].worldx=gp.tileSize*pozx;
        gp.obj[i].worldy=gp.tileSize*pozy;
    }
}
