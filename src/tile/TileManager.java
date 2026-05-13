package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][][];
    public TileManager(GamePanel gp){
        this.gp=gp;
        tile=new Tile[50];
        mapTileNum=new int [gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/map01.txt",0);
        loadMap("/maps/map02.txt",1);
        loadMap("/maps/map03.txt",2);
    }

    public void getTileImage(){
        setup(0,"/tiles/iarbaM1.png",false);
        setup(7,"/tiles/drumM1.png",false);
        setup(9,"/tiles/th.png",true);
        setup(1,"/tiles/drumM2.png",false);
        setup(2,"/tiles/negruM2.png",true);
        setup(3,"/tiles/perete1M2.png",true);
        setup(4,"/tiles/perete2M2.png",true);
        setup(5,"/tiles/iarbaM3.png",false);
        setup(6,"/tiles/drumM3.png",false);
    }
    public void setup(int index,String imageName,boolean collision){
        UtilityTool uTool=new UtilityTool();
        try{
            tile[index]=new Tile();
            tile[index].image= ImageIO.read(getClass().getResourceAsStream(imageName));
            tile[index].image=uTool.scaleImage(tile[index].image,gp.tileSize,gp.tileSize);
            tile[index].collision=collision;
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void loadMap(String filePath,int map){
        try{
            InputStream is=getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col=0;
            int row=0;

            while(col < gp.maxWorldCol && row<gp.maxWorldRow){
                String line=br.readLine();

                while(col<gp.maxWorldCol){
                    String numbers[]=line.split(" ");
                    int num=Integer.parseInt(numbers[col]);
                    mapTileNum[map][col][row]=num;
                    col++;
                }
                if(col==gp.maxWorldCol){
                    col=0;
                    row++;
                }
            }
            br.close();
        }catch(Exception e){

        }
    }

    public void draw(Graphics2D g2) {
        int worldCol=0;
        int worldRow=0;

        while(worldCol<gp.maxWorldCol && worldRow<gp.maxWorldRow){
            int tileNum=mapTileNum[gp.currentMap][worldCol][worldRow];
            int worldX=worldCol*gp.tileSize;
            int worldY=worldRow*gp.tileSize;
            int screenX=worldX-gp.player.worldx+gp.player.screenX;
            int screenY=worldY-gp.player.worldy+gp.player.screenY;
            if(worldX+gp.tileSize>gp.player.worldx-gp.player.screenX && worldX-gp.tileSize<gp.player.worldx+gp.player.screenX && worldY+gp.tileSize>gp.player.worldy - gp.player.screenY && worldY-gp.tileSize<gp.player.worldy+gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);

            }
            worldCol++;

            if(worldCol==gp.maxWorldCol){
                worldCol=0;
                worldRow++;
            }
        }
    }
}
