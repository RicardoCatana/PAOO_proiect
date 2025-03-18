package object;
import Main.GamePanel;
import entity.Projectile;


public class OBJ_Fireball extends Projectile {
    GamePanel gp;

    /*
    Constructorul clasei inițializează caracteristicile proiectilului, cum ar fi viteza acestuia cu care se deplasează pe
    hartă, HP-ul acestuia ce va scădea treptat pentru a putea dispărea după un anumit moment de timp și damage-ul pe care
    îl va da inamicilor
     */
    public OBJ_Fireball(GamePanel gp) {
        super(gp);
        this.gp=gp;
        name = "Fireball";
        speed = 20;
        maxLife = 40;
        life = maxLife;
        attack = 15;
        alive=false;
        getImage();
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Încarcă și setează imaginile proiectilului pentru fiecare direcție pe care o poate lua
     */
    public void getImage(){
        int tileSize=gp.tileSize;
        up1 = setup("/res/projectile/fireball_up_1.png", tileSize, tileSize);
        up2 = setup("/res/projectile/fireball_up_2.png", tileSize, tileSize);
        down1 = setup("/res/projectile/fireball_down_1.png", tileSize, tileSize);
        down2 = setup("/res/projectile/fireball_down_2.png", tileSize, tileSize);
        left1 = setup("/res/projectile/fireball_left_1.png", tileSize, tileSize);
        left2 = setup("/res/projectile/fireball_left_2.png", tileSize, tileSize);
        right1 = setup("/res/projectile/fireball_right_1.png", tileSize, tileSize);
        right2 = setup("/res/projectile/fireball_right_2.png", tileSize, tileSize);
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
