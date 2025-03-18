package monster;
import Main.GamePanel;
import entity.Entity;

public class Lord extends Entity {
    GamePanel gp;

    public Lord(GamePanel gp) {
        super(gp);
        this.gp=gp;

        type = 2;
        name = "Lord";
        speed = 1;
        maxLife = 400;
        life = maxLife;
        attack=3;

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


    public void getImage(){
        up1=setup("/res/lord/up1.png",gp.tileSize,gp.tileSize);
        up2=setup("/res/lord/up2.png",gp.tileSize,gp.tileSize);

        down1=setup("/res/lord/down1.png",gp.tileSize,gp.tileSize);
        down2=setup("/res/lord/down2.png",gp.tileSize,gp.tileSize);

        left1=setup("/res/lord/left1.png",gp.tileSize,gp.tileSize);
        left2=setup("/res/lord/left2.png",gp.tileSize,gp.tileSize);

        right1=setup("/res/lord/right1.png",gp.tileSize,gp.tileSize);
        right2=setup("/res/lord/right2.png",gp.tileSize,gp.tileSize);
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void getAttackImage(){
        attackUp1 = setup("/res/lord_attack/up1.png", gp.tileSize, gp.tileSize );
        attackUp2 = setup("/res/lord_attack/up2.png", gp.tileSize, gp.tileSize );
        attackUp3 = setup("/res/lord_attack/up3.png", gp.tileSize, gp.tileSize * 2);

        attackDown1 = setup("/res/lord_attack/down1.png", gp.tileSize, gp.tileSize );
        attackDown2 = setup("/res/lord_attack/down2.png", gp.tileSize, gp.tileSize );
        attackDown3 = setup("/res/lord_attack/down3.png", gp.tileSize, gp.tileSize * 2);

        attackLeft1 = setup("/res/lord_attack/left1.png", gp.tileSize , gp.tileSize*2);
        attackLeft2 = setup("/res/lord_attack/left2.png", gp.tileSize , gp.tileSize*2);
        attackLeft3 = setup("/res/lord_attack/left3.png", gp.tileSize * 2, gp.tileSize);

        attackRight1 = setup("/res/lord_attack/right1.png", gp.tileSize , gp.tileSize*2);
        attackRight2 = setup("/res/lord_attack/right2.png", gp.tileSize , gp.tileSize*2);
        attackRight3 = setup("/res/lord_attack/right3.png", gp.tileSize * 2, gp.tileSize);
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void setAction() {
        if(onPath == true){
            checkStopChasingOrNot(gp.player,20,100);
            searchPath(getGoalCol(gp.player),getGoalRow(gp.player));
        }
        else{
            checkStartChasingOrNot(gp.player,5,100);
            getRandomDirection();
        }

        if(attacking == false){
            checkAttackOrNot(30,gp.tileSize*4, gp.tileSize);
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void damageReaction(){
        actionLockCounter=0;
        onPath = true;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
