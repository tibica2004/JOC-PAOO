package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import Entity.Entity;
public class OBJ_potion extends Entity{
    public OBJ_potion(GamePanel gp){
        super(gp);
        name="Potion";
        down1=setup("/objects/Potion.png");
    }
}
