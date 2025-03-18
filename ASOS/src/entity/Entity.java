package entity;
import Main.GamePanel;
import Main.UtilityTool;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Entity {
     GamePanel gp;
     public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
     public BufferedImage attackUp1, attackUp2, attackUp3, attackDown1, attackDown2, attackDown3, attackLeft1, attackLeft2, attackLeft3, attackRight1, attackRight2, attackRight3;
     public BufferedImage image, image2, image3;
     public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
     public Rectangle attackArea=new Rectangle(0,0,0,0);
     public int solidAreaDefaultX, solidAreaDefaultY;
     public int worldX, worldY;

     //STATE
     public boolean invincible=false;
     public boolean collisionOn = false;
     public String direction="down";
     public int spriteNum = 1;
     public boolean collision = false;
     public boolean attacking=false;
     public boolean alive=true;
     public boolean dying=false;
     public boolean hpBarON=false;
     public boolean onPath = false;
     public boolean guarding=false;

     //COUNTERS
     public int actionLockCounter=0;
     public int invincibleCounter=0;
     public int spriteCounter = 0;
     int dyingCounter=0;
     int hpBarCounter;

     // CHARACTER ATTRIBUTES
     public String name;
     public int maxLife;
     public int life;
     public int attack;

     public int type; //0 = PLAYER  1 = MONSTER 2 = SOLDIER
     public int speed;
     public Projectile projectile;


     public Entity(GamePanel gp) {
          this.gp = gp;
     }
     /*
     Metodele setAction() și damageReaction() sunt metode destinate a fi suprascrise de clasele
     derivate, cum ar fi Player sau Soldier, Guard, Lord
      */
     public void setAction(){}
     public void damageReaction(){}


     /*
     Se verifică coliziunile cu tile-urile, cu jucătorul și cu alte entități
     Dacă jucătorul se ciocnește de inamici, vor fi aplicate daune jucătorului
      */
     public void checkCollision(){
          collisionOn = false;
          gp.colChecker.checkTile(this);
          gp.colChecker.checkEntity(this,gp.monster);
          boolean contactPlayer=gp.colChecker.checkPlayer(this);
          if(this.type==1 && contactPlayer==true){
               damagePlayer(attack);
          }
     }

     /*
      Actualizează starea entităților, se execută la fiecare cadru al jocului si are rolul de a gestiona
      mișcarea, coliziunile, atacurile și animațiile
      */
     public void update() {
               // Verificăm dacă entitatea este în starea de atac
          if(attacking == true){
               attacking();
          }
          else{
          setAction(); // Setăm acțiunea curentă a entității
          checkCollision(); // Verificăm coliziunile

               // Dacă nu există coliziuni, modificăm coordonatele entității
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
          }
               // La fiecare 12 cadre, schimbam imaginea sprite-ului
          spriteCounter++;
          if (spriteCounter > 12) {
               if (spriteNum == 1)
                    spriteNum = 2;
               else if (spriteNum == 2)
                    spriteNum = 1;

               spriteCounter = 0;
               }

          }
               // Gestionăm invincibilitatea temporară a entităților, dupa 30 de cadre, este dezactivată
          if (invincible == true) {
               invincibleCounter++;
               if (invincibleCounter > 30) {
                    invincible = false;
                    invincibleCounter = 0;
               }
          }

     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

     public void attacking() {
          spriteCounter++;
          if (spriteCounter <= 5) { spriteNum = 1; }
          if (spriteCounter > 10 && spriteCounter <= 20) {
               spriteNum = 2;

                    // Salvăm coordonatele curente și dimensiunile zonei solide
               int currentWorldX = worldX;
               int currentWorldY = worldY;
               int solidAreaWidth = solidArea.width;
               int solidAreaHeight = solidArea.height;

                    // Ajustăm coordonatele pentru a include zona de atac
               switch (direction) {
                    case "up": worldY -= attackArea.height;
                         break;
                    case "down": worldY += attackArea.height;
                         break;
                    case "left": worldX -= attackArea.width;
                         break;
                    case "right": worldX += attackArea.width;
                         break;
               }
                    // Zona de atac devine zonă solidă
               solidArea.width = attackArea.width;
               solidArea.height = attackArea.height;

                    // Dacă entitatea este un inamic, jucătorul primește damage
               if(type == 2 || type == 3){
                    if(gp.colChecker.checkPlayer(this)==true){
                         damagePlayer(attack);
                    }
               }
               else{
                    int monsterIndex = gp.colChecker.checkEntity(this, gp.monster);
                    gp.player.damageMonster(monsterIndex,attack);
               }
                    // Restaurăm coordonatele si dimeniunile originale zonei solide
               worldX = currentWorldX;
               worldY = currentWorldY;
               solidArea.width = solidAreaWidth;
               solidArea.height = solidAreaHeight;


          }
          if (spriteCounter > 20 && spriteCounter <= 30) { spriteNum = 3; }
          if (spriteCounter > 30) {
               spriteNum = 1;
               spriteCounter = 0;
               attacking = false;
          }

     }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     /*
     Determină dacă entitatea ar trebui sa atace jucătorul, verificând daca jucătorul se află în raza de
     acțiune în funcție de direcția entității
      */
     public void checkAttackOrNot(int rate, int straight, int horizontal){
          boolean targetInRange = false;
          int xDis = getXdistance(gp.player);
          int yDis = getYdistance(gp.player);

          switch (direction){
               case "up":
                    if(gp.player.worldY < worldY && yDis <straight && xDis < horizontal){
                         targetInRange = true;
                    }
                    break;
               case "down":
                    if(gp.player.worldY > worldY && yDis <straight && xDis < horizontal){
                         targetInRange = true;
                    }
                    break;
               case "left":
                    if(gp.player.worldX < worldX && yDis <straight && xDis < horizontal){
                         targetInRange = true;
                    }
                    break;
               case "right":
                    if(gp.player.worldX > worldX && yDis <straight && xDis < horizontal){
                         targetInRange = true;
                    }
                    break;
          }
               // Element aleatoriu pentru a nu face atacurile complet previzibile
          if(targetInRange == true){
               int i = new Random().nextInt(rate);
               if(i == 0){
                    attacking = true;
                    spriteNum = 1;
                    spriteCounter = 0;
               }
          }
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     public void damagePlayer(int attack){
          if(gp.player.invincible==false){
               int damage=attack;
               String canGuardDirection = getOppositeDirection(direction);
               if(gp.player.guarding == true && gp.player.direction.equals(canGuardDirection)){
                    damage=damage/2;
               }
               gp.player.life-=damage;
               gp.player.invincible=true;
          }

     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

          // Returnează direcția opusă jucătorului, pentru a putea face ca inamicii să meargă în direcția opusă
     public String getOppositeDirection(String direction){
          String oppositeDirection="";
          switch(direction){
               case "up": oppositeDirection = "down"; break;
               case "down": oppositeDirection = "up"; break;
               case "left": oppositeDirection = "right"; break;
               case "right": oppositeDirection = "left"; break;
          }
          return oppositeDirection;
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     public void draw(Graphics2D g2) {
          BufferedImage image=null;

               // Calculează poziția pe ecran a entității în funcție de poziția sa în lumea jocului și poziția jucătorului
          int screenX = worldX - gp.player.worldX + gp.player.screenX;
          int screenY = worldY - gp.player.worldY + gp.player.screenY;
               // Verificăm dacă entitatea este vizibilă pe ecran
          if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                  && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                  && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                  && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
               int tempScreenX = screenX;
               int tempScreenY = screenY;

               switch (direction) { // Selectarea sprite-ului corect în funcție de direcție și stare
                    case "up":
                         if (attacking == false) {
                              if (spriteNum == 1) { image = up1; }
                              if (spriteNum == 2) { image = up2; }
                            //  if (spriteNum == 3) { image = up3; }
                         }
                         if (attacking == true) {
                              tempScreenY = screenY - gp.tileSize;
                              if (spriteNum == 1) { image = attackUp1; }
                              if (spriteNum == 2) { image = attackUp2; }
                              if (spriteNum == 3) { image = attackUp3; }
                         }
                         break;
                    case "down":
                         if (attacking == false) {
                              if (spriteNum == 1) { image = down1; }
                              if (spriteNum == 2) { image = down2; }
                             // if (spriteNum == 3) { image = down3; }
                         }
                         if (attacking == true) {
                              if (spriteNum == 1) { image = attackDown1; }
                              if (spriteNum == 2) { image = attackDown2; }
                              if (spriteNum == 3) { image = attackDown3; }
                         }
                         break;
                    case "left":
                         if (attacking == false) {
                              if (spriteNum == 1) { image = left1; }
                              if (spriteNum == 2) { image = left2; }
                             // if (spriteNum == 3) { image = left3; }
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
                             // if (spriteNum == 3) { image = right3; }
                         }
                         if (attacking == true) {
                              if (spriteNum == 1) { image = attackRight1; }
                              if (spriteNum == 2) { image = attackRight2; }
                              if (spriteNum == 3) { image = attackRight3; }
                         }
                         break;
               }

                    // Desenarea barei de viață pentru inamici
               if(type == 2 && hpBarON==true){
                    double oneScale = (double)gp.tileSize/maxLife;
                    double hpBarValue = oneScale*life;
                    g2.setColor(new Color(35,35,35));
                    g2.fillRect(screenX-1,screenY-16,gp.tileSize+2,12);

                    g2.setColor(new Color(255, 0, 3));
                    g2.fillRect(screenX,screenY - 15,(int)hpBarValue,10);

                    hpBarCounter++;

                    if(hpBarCounter > 600){
                         hpBarCounter=0;
                         hpBarON=false;
                    }
               }
                    // Efectul de invincibilitate
               if (invincible == true) {
                    hpBarON=true;
                    hpBarCounter=0;
                    changeAlpha(g2,0.4f);
               }

               if (dying == true) { dyingAnimation(g2); } // Animația specifică morții inamicului
               g2.drawImage(image, tempScreenX, tempScreenY, null);
               changeAlpha(g2,1f);
          }

     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

     // Crează un efect de blink înainte ca entitatea să dispară de pe ecran
     public void dyingAnimation(Graphics2D g2) {
          dyingCounter++;
          int i=5;
          if(dyingCounter<=i){changeAlpha(g2,0f);} //5
          if(dyingCounter>i && dyingCounter<=i*2){ changeAlpha(g2,1f);} //5 - 10
          if(dyingCounter>i*2 && dyingCounter<=i*3){ changeAlpha(g2,0f);} //10 - 15
          if(dyingCounter>i*3 && dyingCounter<=i*4){ changeAlpha(g2,1f);} //15 - 20
          if(dyingCounter>i*4 && dyingCounter<=i*5){ changeAlpha(g2,0f);} //20 - 25
          if(dyingCounter>i*5 && dyingCounter<=i*6){ changeAlpha(g2,1f);} //25 - 30
          if(dyingCounter>i*6 && dyingCounter<=i*7){ changeAlpha(g2,0f);} //30 - 35
          if(dyingCounter>i*7 && dyingCounter<=i*8){ changeAlpha(g2,1f);} //35 - 40

          if(dyingCounter>40){
               alive=false;
          }

     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     // Modifică transparența elementelor desenate
     public void changeAlpha(Graphics2D g2,float alpha){
          g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

     // Încarcă o imagine din fișierul de resurse și o redimensionează
     public BufferedImage setup(String imagePath,int width,int height){
          UtilityTool uTool=new UtilityTool();
          BufferedImage image= null;
          try{
               image= ImageIO.read(getClass().getResourceAsStream(imagePath));
               image=uTool.scaleImage(image,width,height);
          }catch(IOException e){
               e.printStackTrace();
          }
          return image;
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     /*
     Calculează și actualizează calea către un obiectiv specificat folosind un algorimt de căutarea a drumului
      */
     public void searchPath(int goalCol,int goalRow){
               // coloana și rândul de pornire al entității
          int startCol = (worldX + solidArea.x)/gp.tileSize;
          int startRow = (worldY + solidArea.y)/gp.tileSize;
               // Setează nodurile de start si obiectiv
          gp.pFinder.setNodes(startCol,startRow,goalCol, goalRow,this);
          if(gp.pFinder.search() == true){
                    // Determinarea următoarei poziții
               int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
               int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

                    // Determinarea limitelor entității
               int enLeftX = worldX + solidArea.x;
               int enRightX= worldX + solidArea.x + solidArea.width;
               int enTopY = worldY + solidArea.y;
               int enBottomY = worldY + solidArea.y + solidArea.height;
                    // Ajustarea direcției entității
               if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize){
                    direction="up";
               }
               else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize){
                    direction="down";
               }
               else if(enTopY >= nextY && enBottomY < nextY + gp.tileSize){ //LEFT OR RIGHT
                    if(enLeftX > nextX){
                         direction = "left";
                    }
                    if(enLeftX < nextX){
                         direction = "right";
                    }
               }
               else if(enTopY > nextY && enLeftX > nextX){ //UP OR LEFT
                    direction = "up";
                    checkCollision();
                    if(collisionOn == true){
                         direction = "left";
                    }
               }
               else if(enTopY > nextY && enLeftX < nextX){ //UP OR RIGHT
                    direction="up";
                    checkCollision();
                    if(collisionOn == true){
                         direction = "right";
                    }
               }
               else if(enTopY < nextY && enLeftX > nextX){ //DOWN OR LEFT
                    direction = "down";
                    checkCollision();
                    if(collisionOn == true){
                         direction="left";
                    }
               }
               else if(enTopY < nextY && enLeftX < nextX){ //DOWN OR RIGHT
                    direction = "down";
                    checkCollision();
                    if(collisionOn == true){
                         direction="right";
                    }
               }
                    // Dacă următorul pas coincide cu obiectivul, se oprește căutarea
               int nextCol = gp.pFinder.pathList.get(0).col;
               int nextRow = gp.pFinder.pathList.get(0).row;
               if(nextCol == goalCol && nextRow == goalRow){
                    onPath = false;
               }
          }
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     public int getXdistance(Entity target){
          int xDistance = Math.abs(worldX - target.worldX);
          return xDistance;
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     public int getYdistance(Entity target){
          int yDistance = Math.abs(worldY - target.worldY);
          return yDistance;
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     public int getTileDistance(Entity target){
          int tileDistance = (getXdistance(target)+getYdistance(target))/gp.tileSize;
          return tileDistance;
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     public int getGoalCol(Entity target){
          int goalCol = (target.worldX + target.solidArea.x)/gp.tileSize;
          return goalCol;
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     public int getGoalRow(Entity target){
          int goalRow = (target.worldY + target.solidArea.y)/gp.tileSize;
          return goalRow;
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

     // Verifică dacă entitatea ar trebui să oprească urmărirea jucătorului
     public void checkStopChasingOrNot(Entity target, int distance, int rate){
          if(getTileDistance(target)>distance){
               int i = new Random().nextInt(rate);
               if(i==0){
                    onPath=false;
               }
          }
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     // Verifică dacă entitatea trebuie să porneascp urmărirea jucătorului în funcție de distanță și rate
     public void checkStartChasingOrNot(Entity target, int distance, int rate){
          if(getTileDistance(target)<distance){
               int i = new Random().nextInt(rate);
               if(i==0){
                    onPath=true;
               }
          }
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


     // Inamicul primește o direcție random pentru urmatoarele 120 cadre
     public void getRandomDirection(){
          actionLockCounter ++;
          if(actionLockCounter == 120){
               Random random=new Random();
               int i = random.nextInt(100)+1;
               if(i<=25){
                    direction = "up";
               }
               if(i>25 && i<=50){
                    direction = "down";
               }
               if(i>50 && i<=75){
                    direction = "left";
               }
               if(i>75 && i<=100){
                    direction="right";
               }
               actionLockCounter=0;
          }
     }//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}