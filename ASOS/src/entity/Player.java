package entity;
import Main.GamePanel;
import Main.KeyHandler;
import object.OBJ_Fireball;
import Main.UI;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Player extends Entity {
    KeyHandler keyH;
    // Coordonatele pe ecran unde jucătorul va fi desenat
    public final int screenX;
    public final int screenY;


    // Inițializează jucătorul cu dimensiuni, zone solide si de atac, setările implicite
    // și imaginile pentru mișcare și atac
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        type=0;
        this.keyH = keyH;
        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;
        solidArea = new Rectangle(8, 16, 32, 32);
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;
        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Setează valorile implicite pentru jucător
    public void setDefaultValues() {
        // Coordonatele pentru poziția jucătorului pe hartă
        worldX = gp.tileSize * 39;
        worldY = gp.tileSize * 40;
        speed = 8; // Viteza cu care se deplasează acesta
        direction = "down";
        maxLife = 12; // HP-ul jucătorului
        life = maxLife;
        attack=15;
        projectile=new OBJ_Fireball(gp);
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Încarcă și setează imaginile jucătorului pentru diferite direcții și stări
    public void getPlayerImage() {
        int tileSize=gp.tileSize+16;
        up1 = setup("/res/player/up1.png", tileSize, tileSize);
        up2 = setup("/res/player/up2.png", tileSize, tileSize);
        up3 = setup("/res/player/up3.png", tileSize, tileSize);

        down1 = setup("/res/player/down1.png", tileSize, tileSize);
        down2 = setup("/res/player/down2.png", tileSize, tileSize);
        down3 = setup("/res/player/down3.png", tileSize, tileSize);

        left1 = setup("/res/player/left1.png", tileSize, tileSize);
        left2 = setup("/res/player/left2.png", tileSize, tileSize);
        left3 = setup("/res/player/left3.png", tileSize, tileSize);

        right1 = setup("/res/player/right1.png", tileSize, tileSize);
        right2 = setup("/res/player/right2.png", tileSize, tileSize);
        right3 = setup("/res/player/right3.png", tileSize, tileSize);
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Încarcă și setează imaginile jucătorului pentru diferite direcții în timpul atacului
    public void getPlayerAttackImage() {
        int tileSize=gp.tileSize+16;
        attackUp1 = setup("/res/player_attack/up1.png", tileSize, tileSize * 2);
        attackUp2 = setup("/res/player_attack/up2.png", tileSize, tileSize * 2);
        attackUp3 = setup("/res/player_attack/up3.png", tileSize, tileSize * 2);

        attackDown1 = setup("/res/player_attack/down1.png", tileSize, tileSize * 2);
        attackDown2 = setup("/res/player_attack/down2.png", tileSize, tileSize * 2);
        attackDown3 = setup("/res/player_attack/down3.png", tileSize, tileSize * 2);

        attackLeft1 = setup("/res/player_attack/left1.png", tileSize * 2, tileSize);
        attackLeft2 = setup("/res/player_attack/left2.png", tileSize * 2, tileSize);
        attackLeft3 = setup("/res/player_attack/left3.png", tileSize * 2, tileSize);

        attackRight1 = setup("/res/player_attack/right1.png", tileSize * 2, tileSize);
        attackRight2 = setup("/res/player_attack/right2.png", tileSize * 2, tileSize);
        attackRight3 = setup("/res/player_attack/right3.png", tileSize * 2, tileSize);
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public void update() {
        /*
        Dacă jucătorul apasă butonul responsabil pentru atac, atunci variabila attacking = true și se
        apelează funcția pentru atac. Dacă attacking = false și jucătorul apasă butonul pentru blocarea
        atacurilor, atunci guarding devine true, iar jucătorul va lua mai puțin damage
         */
        if (keyH.attackPressed == true) {
            attacking = true;
        }
        if (attacking == true) {
            attacking();
        }
        else if(keyH.blockStatus == true){
            guarding=true;
                // Verificarea direcției de deplasare
        }else if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {
            if (keyH.upPressed == true) {
                direction = "up";
            } else if (keyH.downPressed == true) {
                direction = "down";
            } else if (keyH.leftPressed == true) {
                direction = "left";
            } else if (keyH.rightPressed == true) {
                direction = "right";
            }

            collisionOn = false;
            gp.colChecker.checkTile(this); // Verificare coliziune cu tile-uri

            gp.eHandler.checkEvent(); // Verificare evenimente
            guarding = false;

                // Verificare coliziune cu inamicii
            int monsterIndex = gp.colChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex); // În cazul în care jucătorul se lovește de un inamic, va primi damage

                // Dacă nu exista coliziune, atunci se actualizează poziția jucătorului pe hartă
            if (collisionOn == false) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
                    // Actualizăm animația jucătorului
                spriteCounter++;
                if (spriteCounter > 12) {
                    if (spriteNum == 1)
                        spriteNum = 2;
                    else if (spriteNum == 2)
                        spriteNum = 3;
                    else if (spriteNum == 3)
                        spriteNum = 1;
                    spriteCounter = 0;
                }
            }
        }

            // Dacă butonul responsabil pentru proiectile este apăsat și proiectilul nu este activ
            // atunci îl setează și adaugă în lista de proiectile
        if (gp.keyH.projectilePressed == true && projectile.alive == false) {
            projectile.set(worldX, worldY, direction, true, this);
            gp.projectileList.add(projectile);
        }

            // Dacă jucătorul este imun, se incrementează invincibleCounter și se oprște după
            // 60 de cadre și se resetează
        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

            // Dacă viața jucătorului este mai mică sau egală cu 0, atunci starea jocului devine starea de final
        if(life <= 0){ gp.gameState = gp.gameOverState; }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Dacă jucătorul intră în contact cu un inamic, acesta va pierde un punct de viață
    public void contactMonster(int i) {
        if (i != 999) {
            if (invincible == false &&gp.monster[gp.currentMap][i].dying==false) {
                life -= 1;
                invincible = true;
            }
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public void damageMonster(int i,int attack) {
        if (i != 999) {
            if (gp.monster[gp.currentMap][i].invincible == false) { // Verificăm dacă inamicul este invincibil sau nu
                gp.monster[gp.currentMap][i].life -= attack; // Aplică daune inamicului
                gp.monster[gp.currentMap][i].invincible = true; // Setează starea de invincibilitate pentru inamicul atacat
                gp.monster[gp.currentMap][i].damageReaction(); // Apelează metoda care se ocupă de comportamentul inamicul
                                                               // în momentul în care acesta primește damage
                if (gp.monster[gp.currentMap][i].life <= 0) {
                    gp.monster[gp.currentMap][i].dying = true;
                }
            }
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metodă ce se ocupă cu desenarea sprite-urilor jucătorului pe ecran, în funcție de direcția de deplasare
    și starea acestuia ( atac,invincibilitate)
    Dacă jucătorul atacă, coordonatele temporare sunt ajustate pentru a încerca poziționarea corectă a imaginii
    de atac
     */
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction) {    // Alegem imaginea corespunzătoare în funcție de direcție și stare
            case "up":
                if (attacking == false) {
                    if (spriteNum == 1) { image = up1; }
                    if (spriteNum == 2) { image = up2; }
                    if (spriteNum == 3) { image = up3; }
                }
                if (attacking == true) {
                    tempScreenY = screenY - 24;
                    if (spriteNum == 1) { image = attackUp1; }
                    if (spriteNum == 2) { image = attackUp2; }
                    if (spriteNum == 3) { image = attackUp3; }
                }
                break;
            case "down":
                if (attacking == false) {
                    if (spriteNum == 1) { image = down1; }
                    if (spriteNum == 2) { image = down2; }
                    if (spriteNum == 3) { image = down3; }
                }
                if (attacking == true) {
                    tempScreenY = screenY - 24;
                    if (spriteNum == 1) { image = attackDown1; }
                    if (spriteNum == 2) { image = attackDown2; }
                    if (spriteNum == 3) { image = attackDown3; }
                }
                break;
            case "left":
                if (attacking == false) {
                    if (spriteNum == 1) { image = left1; }
                    if (spriteNum == 2) { image = left2; }
                    if (spriteNum == 3) { image = left3; }
                }
                if (attacking == true) {
                    tempScreenX = screenX - gp.tileSize;
                    if (spriteNum == 1) { image = attackLeft1; }
                    if (spriteNum == 2) { image = attackLeft2; }
                    if (spriteNum == 3) { image = attackLeft3; }
                }
                break;
            case "right":
                if (attacking == false) {
                    if (spriteNum == 1) { image = right1; }
                    if (spriteNum == 2) { image = right2; }
                    if (spriteNum == 3) { image = right3; }
                }
                if (attacking == true) {
                    if (spriteNum == 1) { image = attackRight1; }
                    if (spriteNum == 2) { image = attackRight2; }
                    if (spriteNum == 3) { image = attackRight3; }
                }
                break;
        }

        /* Daca jucatorul primeste damage, acesta devine imun, iar caracterul va deveni transparent pentru aceasta perioada */
        if (invincible == true) { g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f)); }
        g2.drawImage(image, tempScreenX, tempScreenY, null);
        /* Dupa ce trece perioada de imunitate, dispare efectul de transparenta */
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





