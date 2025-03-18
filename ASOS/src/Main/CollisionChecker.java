package Main;
import entity.Entity;

public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp){
        this.gp=gp;
    }


    /*
    Metoda se ocupă cu verificarea coliziunii entităților cu tile-urile
    Calculează coordonatele entității în raport cu tile-urile din jurul său în funcție de direcția de mișcare
    Verifică coliziunea cu tile-urile și setează collisionOn dacă este cazul
     */
    public void checkTile(Entity entity){
        int entityLeftWorldX= entity.worldX+entity.solidArea.x;
        int entityRightWorldX=entity.worldX+entity.solidArea.x+entity.solidArea.width;
        int entityTopWorldY=entity.worldY+entity.solidArea.y;
        int entityBottomWorldY=entity.worldY+entity.solidArea.y+entity.solidArea.height;
        int entityLeftCol=entityLeftWorldX/gp.tileSize;
        int entityRightCol=entityRightWorldX/gp.tileSize;
        int entityTopRow=entityTopWorldY/gp.tileSize;
        int entityBottomRow=entityBottomWorldY/gp.tileSize;
        int tileNum1,tileNum2;

        /*
        Se verifică direcția entității
        Pentru fiecare direcție se calculează coloanele si rândurile  care ar trebui verificate pentru coliziune
        Daca unul din tie-uri are proprietatea de coliziune, se seteaza entity.collisionOn la true
         */
        switch(entity.direction){
            case "up":
                entityTopRow=(entityTopWorldY-entity.speed)/gp.tileSize;
                tileNum1=gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2=gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                if(gp.tileM.tile[tileNum1].collision==true || gp.tileM.tile[tileNum2].collision==true)
                {
                    entity.collisionOn=true;
                }
                break;
            case "down":
                entityBottomRow=(entityBottomWorldY+entity.speed)/gp.tileSize;
                tileNum1=gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                tileNum2=gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision==true || gp.tileM.tile[tileNum2].collision==true)
                {
                    entity.collisionOn=true;
                }
                break;
            case "left":
                entityLeftCol=(entityLeftWorldX-entity.speed)/gp.tileSize;
                tileNum1=gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityTopRow];
                tileNum2=gp.tileM.mapTileNum[gp.currentMap][entityLeftCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision==true || gp.tileM.tile[tileNum2].collision==true)
                {
                    entity.collisionOn=true;
                }
                break;
            case  "right":
                entityRightCol=(entityRightWorldX+entity.speed)/gp.tileSize;
                tileNum1=gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityTopRow];
                tileNum2=gp.tileM.mapTileNum[gp.currentMap][entityRightCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision==true || gp.tileM.tile[tileNum2].collision==true)
                {
                    entity.collisionOn=true;
                }
                break;
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Verifică coliziunea între entități
    Parcurge matricea de entități target și verifică dacă entitatea se intersectează cu orice altă entitate din acea matrice
    Returnează indexul entității cu care se intersectează
     */
    public int checkEntity(Entity entity, Entity[][] target){
        int index=999; // inițializată cu o valoare mare ce va fi returnată daca nu se găsește nicio coliziune
        for(int i=0;i< target[1].length;i++){
            if(target[gp.currentMap][i]!=null){ //Se verifică dacă entitatea din matrice la indexul curent nu este nulă
                //Get entity's solid area position
                entity.solidArea.x=entity.worldX+entity.solidArea.x;
                entity.solidArea.y=entity.worldY+entity.solidArea.y;
                //Get the entity solid area position
                target[gp.currentMap][i].solidArea.x=target[gp.currentMap][i].worldX+target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y=target[gp.currentMap][i].worldY+target[gp.currentMap][i].solidArea.y;

                /*
                Se verifică direcția entității și se ajustează poziția zonei solide a enității în
                funcție de direcție
                 */
                switch (entity.direction){
                    case "up": entity.solidArea.y-=entity.speed; break;
                    case "down": entity.solidArea.y+=entity.speed; break;
                    case "left": entity.solidArea.x-=entity.speed; break;
                    case "right": entity.solidArea.x+=entity.speed; break;
                }
                /*
                Metoda intersects() verifică daca zona solidă a entității date se intersectează cu zona solidă a entității din matrice
                Dacă există coliziune, se seteazp entity.collsionOn la true și se actualizează indexul pentru a indica
                cu ce entitate a avut loc coliziunea
                 */
                if(entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                    if(target[gp.currentMap][i]!=entity){
                        entity.collisionOn = true;
                        index = i;
                    }
                }
                /*
                Pozițiile zonelor solide ale entității date și a celei din matrice sunt resetate la pozițiile inițiale
                La findal, indexul entității cu care a avut loc coliziunea  este returnat
                 */
                entity.solidArea.x=entity.solidAreaDefaultX;
                entity.solidArea.y=entity.solidAreaDefaultY;
                target[gp.currentMap][i].solidArea.x=target[gp.currentMap][i].solidAreaDefaultX;
                target[gp.currentMap][i].solidArea.y=target[gp.currentMap][i].solidAreaDefaultY;
            }
        }
        return index;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Verifică coliziunea entității date cu jucătorul
    Calculează coordontatele entității in raport cu jucătorul si verifică daca se intersectează
    Returnează daca există coliziune cu jucătorul
     */
    public boolean checkPlayer(Entity entity){
        boolean contactPlayer=false;

        //Get entity's solid area position
        entity.solidArea.x=entity.worldX+entity.solidArea.x;
        entity.solidArea.y=entity.worldY+entity.solidArea.y;
        //Get the entity solid area position
        gp.player.solidArea.x=gp.player.worldX+gp.player.solidArea.x;
        gp.player.solidArea.y=gp.player.worldY+gp.player.solidArea.y;

        switch (entity.direction){
            case "up": entity.solidArea.y-=entity.speed; break;
            case "down": entity.solidArea.y+=entity.speed; break;
            case "left": entity.solidArea.x-=entity.speed; break;
            case "right": entity.solidArea.x+=entity.speed; break;
        }

        if(entity.solidArea.intersects(gp.player.solidArea)) {
            if(gp.player!=entity){
                entity.collisionOn = true;
                contactPlayer=true;
            }
        }

        entity.solidArea.x=entity.solidAreaDefaultX;
        entity.solidArea.y=entity.solidAreaDefaultY;
        gp.player.solidArea.x=gp.player.solidAreaDefaultX;
        gp.player.solidArea.y=gp.player.solidAreaDefaultY;
        return contactPlayer;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
