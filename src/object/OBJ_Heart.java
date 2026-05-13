package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import Entity.Entity;
public class OBJ_Heart extends Entity{
    public OBJ_Heart(GamePanel gp){
        super(gp);
        name="Heart";
        image=setup("/objects/tile000-removebg-preview.png");
        image2=setup("/objects/tile001-removebg-preview.png");
        image3=setup("/objects/tile002-removebg-preview.png");
    }
}
