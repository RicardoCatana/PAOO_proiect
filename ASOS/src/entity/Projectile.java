package entity;

import Main.GamePanel;

public class Projectile extends Entity{
    Entity user; // Referință către entitatea care a tras proiectilul (jucătorul)
    public Projectile(GamePanel gp){
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user){
        this.worldX = worldX; // Coordonatele în lume unde proiectilul este creat
        this.worldY = worldY;
        this.direction = direction; // Direcția în care proiectilul se va deplasa
        this.alive = alive; // Starea proiectilului
        this.user=user; // Entitatea care a tras proiectilul
        this.life = this.maxLife;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Dacă proiectilul este tras de jucător, se verifică coliziunea cu monștrii
    Daca proiectilul atinge inamicul (index != 999), atunci inamicul primește damage
    iar proiectilul dispare (alive = false)
     */
    public void update(){
        if(user == gp.player){
            int monsterIndex = gp.colChecker.checkEntity(this,gp.monster);
            if(monsterIndex != 999){
                gp.player.damageMonster(monsterIndex,attack);
                alive = false;
            }
        }

        switch(direction){ // Deplasează proiectilul în direcția specificată
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }

        life--; // Scade durata de viață a proiectilului
        if(life <= 0){ alive = false; } // Dacă viața proiectilului ajunge la 0, acesta dispare

        // Gestionează animația proiectilului
        // Schimbă sprite-ul la fiecare 12 actualizări
        spriteCounter++;
        if(spriteCounter > 12){
            if(spriteNum == 1){
                spriteNum = 2;
            }
            else if(spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
