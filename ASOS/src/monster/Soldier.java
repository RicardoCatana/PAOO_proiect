package monster;
import Main.GamePanel;
import entity.Entity;

public class Soldier extends Entity {
    GamePanel gp;

    /*
    Constructorul clasei Soldier primește o referință la GamePanel și
     inițializează variabilele de instanță ale clasei.
     */
    public Soldier(GamePanel gp) {
        super(gp);
        this.gp=gp;

        type = 2;
        name = "Soldier";
        speed = 1;
        maxLife = 40;
        life = maxLife;
        attack=1;

        solidArea.x = 4;
        solidArea.y = 4;
        solidArea.width = 40;
        solidArea.height = 44;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        attackArea.width=48;
        attackArea.height=48;

        getImage();
        getAttackImage();
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metodă responsabilă pentru încărcarea imaginilor asociate cu
    mișcarea inamicului în diferite direcții
     */
    public void getImage(){
        up1=setup("/res/soldier/up1.png",gp.tileSize,gp.tileSize);
        up2=setup("/res/soldier/up2.png",gp.tileSize,gp.tileSize);

        down1=setup("/res/soldier/down1.png",gp.tileSize,gp.tileSize);
        down2=setup("/res/soldier/down2.png",gp.tileSize,gp.tileSize);

        left1=setup("/res/soldier/left1.png",gp.tileSize,gp.tileSize);
        left2=setup("/res/soldier/left2.png",gp.tileSize,gp.tileSize);

        right1=setup("/res/soldier/right1.png",gp.tileSize,gp.tileSize);
        right2=setup("/res/soldier/right2.png",gp.tileSize,gp.tileSize);
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metodă responsabilă pentru încărcarea imaginilor asociate cu
    atacul inamicului în diferite direcții
     */
    public void getAttackImage(){
        attackUp1 = setup("/res/soldier_attack/up1.png", gp.tileSize, gp.tileSize );
        attackUp2 = setup("/res/soldier_attack/up2.png", gp.tileSize, gp.tileSize );
        attackUp3 = setup("/res/soldier_attack/up3.png", gp.tileSize, gp.tileSize * 2);

        attackDown1 = setup("/res/soldier_attack/down1.png", gp.tileSize, gp.tileSize );
        attackDown2 = setup("/res/soldier_attack/down2.png", gp.tileSize, gp.tileSize );
        attackDown3 = setup("/res/soldier_attack/down3.png", gp.tileSize, gp.tileSize * 2);

        attackLeft1 = setup("/res/soldier_attack/left1.png", gp.tileSize , gp.tileSize);
        attackLeft2 = setup("/res/soldier_attack/left2.png", gp.tileSize , gp.tileSize);
        attackLeft3 = setup("/res/soldier_attack/left3.png", gp.tileSize * 2, gp.tileSize);

        attackRight1 = setup("/res/soldier_attack/right1.png", gp.tileSize , gp.tileSize);
        attackRight2 = setup("/res/soldier_attack/right2.png", gp.tileSize , gp.tileSize);
        attackRight3 = setup("/res/soldier_attack/right3.png", gp.tileSize * 2, gp.tileSize);
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metodă responsabilă pentru definirea acțiunilor soldatului
    în funcție de starea acestuia în joc
     */
    public void setAction() {
        if(onPath == true){
            checkStopChasingOrNot(gp.player,20,100); // Verificăm dacă inamicul trebuie să înceteze să urmărească jucătorul
            searchPath(getGoalCol(gp.player),getGoalRow(gp.player)); // Inamicul va căuta un nou traseu pentru poziția jucătorului
        }
        else{
            checkStartChasingOrNot(gp.player,5,100); // Dacă inamicul se află la o anumită distanță de jucător, atunci va porni spre acesta
            getRandomDirection(); // în cazul in care variabila onPath == false, atunci inamicul va urma o cale random
        }

        if(attacking == false){ // Dacă inamicul nu atacă, atunci verificăm dacă inamicul poate ataca jucătorul
            checkAttackOrNot(30,gp.tileSize*4, gp.tileSize);
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    În cazul în care inamicul este atacat, acesta va începe să urmărească jucătorul
     */
    public void damageReaction(){
        actionLockCounter=0;
        onPath = true;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
