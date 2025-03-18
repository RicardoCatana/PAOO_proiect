package monster;

import Main.GamePanel;
import entity.Entity;

public class Guard extends Entity {

    GamePanel gp;

    public Guard(GamePanel gp) {
        super(gp);
        this.gp=gp;

        type = 2;
        name = "Guard";
        speed = 1;
        maxLife = 80;
        life = maxLife;
        attack=2;

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
        up1=setup("/res/guard/up1.png",gp.tileSize,gp.tileSize);
        up2=setup("/res/guard/up2.png",gp.tileSize,gp.tileSize);

        down1=setup("/res/guard/down1.png",gp.tileSize,gp.tileSize);
        down2=setup("/res/guard/down2.png",gp.tileSize,gp.tileSize);

        left1=setup("/res/guard/left1.png",gp.tileSize,gp.tileSize);
        left2=setup("/res/guard/left2.png",gp.tileSize,gp.tileSize);

        right1=setup("/res/guard/right1.png",gp.tileSize,gp.tileSize);
        right2=setup("/res/guard/right2.png",gp.tileSize,gp.tileSize);
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void getAttackImage(){

        attackUp1 = setup("/res/guard_attack/up1.png", gp.tileSize, gp.tileSize );
        attackUp2 = setup("/res/guard_attack/up2.png", gp.tileSize, gp.tileSize * 2);
        attackUp3 = setup("/res/guard_attack/up3.png", gp.tileSize, gp.tileSize * 2);

        attackDown1 = setup("/res/guard_attack/down1.png", gp.tileSize, gp.tileSize );
        attackDown2 = setup("/res/guard_attack/down2.png", gp.tileSize, gp.tileSize * 2);
        attackDown3 = setup("/res/guard_attack/down3.png", gp.tileSize, gp.tileSize * 2);

        attackLeft1 = setup("/res/guard_attack/left1.png", gp.tileSize , gp.tileSize);
        attackLeft2 = setup("/res/guard_attack/left2.png", gp.tileSize , gp.tileSize);
        attackLeft3 = setup("/res/guard_attack/left3.png", gp.tileSize * 2, gp.tileSize);

        attackRight1 = setup("/res/guard_attack/right1.png", gp.tileSize , gp.tileSize);
        attackRight2 = setup("/res/guard_attack/right2.png", gp.tileSize , gp.tileSize);
        attackRight3 = setup("/res/guard_attack/right3.png", gp.tileSize * 2, gp.tileSize);
    }



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
