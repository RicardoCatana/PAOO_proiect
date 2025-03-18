package Main;
import monster.*;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    Pentru fiecare tip de inamic, se crează o instanță nouă a acestuia și se stabilesc
    coordonatele acestora, cât si poziția acestora pe hartă
     */
    public void setMonster() {
        int mapNum=0; // Numărul hărții
        int i = 0; // Indexul fiecărui inamic

        gp.monster[mapNum][i] = new Soldier(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*21;
        gp.monster[mapNum][i].worldY=gp.tileSize*27;
        i++;
        gp.monster[mapNum][i] = new Soldier(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*22;
        gp.monster[mapNum][i].worldY=gp.tileSize*23;
        i++;
        gp.monster[mapNum][i] = new Soldier(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*24;
        gp.monster[mapNum][i].worldY=gp.tileSize*25;
        i++;

        mapNum=1;
        gp.monster[mapNum][i] = new Soldier(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*24;
        gp.monster[mapNum][i].worldY=gp.tileSize*22;
        i++;
        gp.monster[mapNum][i] = new Soldier(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*33;
        gp.monster[mapNum][i].worldY=gp.tileSize*33;
        i++;
        gp.monster[mapNum][i] = new Soldier(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*16;
        gp.monster[mapNum][i].worldY=gp.tileSize*34;
        i++;
        gp.monster[mapNum][i] = new Guard(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*34;
        gp.monster[mapNum][i].worldY=gp.tileSize*18;
        i++;
        gp.monster[mapNum][i] = new Guard(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*33;
        gp.monster[mapNum][i].worldY=gp.tileSize*23;
        i++;
        gp.monster[mapNum][i] = new Guard(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*27;
        gp.monster[mapNum][i].worldY=gp.tileSize*20;
        i++;

        mapNum=2;
        gp.monster[mapNum][i] = new Guard(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*22;
        gp.monster[mapNum][i].worldY=gp.tileSize*19;
        i++;
        gp.monster[mapNum][i] = new Guard(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*24;
        gp.monster[mapNum][i].worldY=gp.tileSize*17;
        i++;
        gp.monster[mapNum][i] = new Guard(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*26;
        gp.monster[mapNum][i].worldY=gp.tileSize*17;
        i++;
        gp.monster[mapNum][i] = new Guard(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*27;
        gp.monster[mapNum][i].worldY=gp.tileSize*19;
        i++;
        gp.monster[mapNum][i] = new Soldier(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*28;
        gp.monster[mapNum][i].worldY=gp.tileSize*30;
        i++;
        gp.monster[mapNum][i] = new Soldier(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*21;
        gp.monster[mapNum][i].worldY=gp.tileSize*30;
        i++;
        gp.monster[mapNum][i] = new Lord(gp);
        gp.monster[mapNum][i].worldX=gp.tileSize*25;
        gp.monster[mapNum][i].worldY=gp.tileSize*16;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
