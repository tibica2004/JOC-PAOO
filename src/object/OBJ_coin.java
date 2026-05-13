package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import Entity.Entity;
public class OBJ_coin extends Entity{
    public OBJ_coin(GamePanel gp){
        super(gp);
        name="Coin";
        down1=setup("/objects/coin.png");
    }
}
