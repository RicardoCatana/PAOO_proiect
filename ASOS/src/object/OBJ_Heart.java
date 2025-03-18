package object;
import Main.GamePanel;
import entity.Entity;


public class OBJ_Heart extends Entity {

    /*
    Încărcăm imaginile pentru a putea afișa HP-ul jucătorului
     */
    public OBJ_Heart(GamePanel gp){
        super(gp);
        name="Heart";
        image=setup("/res/obj/heart_full.png",gp.tileSize,gp.tileSize);
        image2=setup("/res/obj/heart_half.png",gp.tileSize,gp.tileSize);
        image3=setup("/res/obj/heart_blank.png",gp.tileSize,gp.tileSize);
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
