package Main;
import entity.Entity;
import object.OBJ_Heart;
import java.awt.*;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UI {
    GamePanel gp;
    Graphics2D g2;  // Obiect de tipul Graphics2D utilizat pentru desenarea elem grafice
    Font arial_40, arial_80B; //Fonturile utilizate pentru textele din UI
    DecimalFormat dFormat=new DecimalFormat("#0.00"); // Formatarea timpului jucat
    public int commandNum=0;
    public BufferedImage menu; // Imagine pentru meniu
    BufferedImage heart_full,heart_half,heart_blank; // Imagine pentru HP
    int counter=0;

    /*
    Constructorul acestei clase inițializează variabilele clasei UI cu valorile corespunzătoare
     */
    public UI(GamePanel gp){
        this.gp=gp;
        arial_40=new Font("Arial",Font.PLAIN,40);
        arial_80B=new Font("Arial",Font.BOLD,80);

        //HUD OBJECT
        Entity heart=new OBJ_Heart(gp);
        heart_full=heart.image;
        heart_half=heart.image2;
        heart_blank=heart.image3;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metodă responsabilă cu desenarea interfeței jocului și a eranului corespunzător
    stării curente a jocului
    În funcție de starea curentă, va fi apelată funcția corespunzătoare pentru a fi desenat ecranul corect
     */
    public void draw(Graphics2D g2){
        this.g2=g2;
        g2.setFont(arial_40); // Setarea fontului
        g2.setColor(Color.white); // Culoarea textului

        // Se apeleaza metoda corespunzătoare titleState-ului
        if(gp.gameState==gp.titleState){ drawTitleScreen(); }

        if(gp.gameState==gp.playState){
            drawPlayerLife(); // Desenează HP-ul jucătorului
            gp.playTime +=(double) 1/60; // Se desenează timpul de joc în colțul din dreapta sus
            g2.drawString("Time: "+dFormat.format(gp.playTime),gp.tileSize*17,65);
        }
        // Se apeleaza metoda corespunzătoare stării de joc
        if(gp.gameState==gp.pauseState){ drawPauseScreen(); }
        if(gp.gameState == gp.gameOverState){ drawGameOverScreen(); }
        if(gp.gameState == gp.transitionState){ drawTransition(); }
        if(gp.gameState == gp.gameFinished){ drawEndScreen(); }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metodă responsabilă cu desenearea ecranului de Game over și a elementelor din acesta
    Metodă apelată în momentul în care HP-ul jucătorului ajunge la 0
     */
    private void drawGameOverScreen() {
        g2.setColor(new Color(0,0,0,150)); // Culoarea este setată la un negru semi-transparent, pentru ecranul de Game over
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight); // Desenează un dreptunghi negru semi transparent care acoperă întregul ecran
        int x,y; // Coordonatele textului
        String text = "GAME OVER";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110f));

        // SHADOW - desenează o umbra a textului
        g2.setColor(Color.BLACK); // Seteaza culoarea - negru
        x=getXforCenteredText(text);
        y=gp.tileSize*4;
        g2.drawString(text,x,y); //Desenează textul la coordonatele calculate mai sus

        //MAIN - desenează textul principal
        g2.setColor(Color.white); // Setează culoarea - alb
        g2.drawString(text,x-7,y-7); // Desenează textul la aproximativ aceeleași coordonate, cu o mică diferență
        //pentru efectul de umbră

        //RETRY - desenează opțiunea de retry
        g2.setFont(g2.getFont().deriveFont(50f));
        text="RETRY";
        x=getXforCenteredText(text);
        y+=gp.tileSize*4;
        g2.drawString(text,x,y);
        if(commandNum == 0){ // Dacă opțiunea de retry este selectată
            g2.drawString(">",x-40,y); // Desenează o săgeată în stânga opțiunii
        }

        //EXIT - desenează optiunea de exit (va duce jucătorul înapoi în meniu)
        text="EXIT";
        x=getXforCenteredText(text);
        y+=55;
        g2.drawString(text,x,y);
        if(commandNum == 1){
            g2.drawString(">",x-40,y);
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metodă ce desenează HP-ul jucătorului
     */
    public void drawPlayerLife(){
        int x=gp.tileSize/2;
        int y=gp.tileSize/2;
        int i=0;

        // În această buclă while, sunt desenate inimile ce reprezintă viața maximă a jucătorului
        // Daca HP-ul maxim al jucătorului este 10, atunci vor fi desenate 5 inimi
        while(i<gp.player.maxLife/2){
            g2.drawImage(heart_blank,x,y,null);
            i++;
            x+=gp.tileSize;
        }

        //RESET
        x=gp.tileSize/2;
        y=gp.tileSize/2;
        i=0;

        // În această buclă while, este desenată viața curentă a jucătorului
        // La fiecare iterație este desenată o inimă parțial plină
        // Dacă jucătorul are suficient HP, atunci o inimă plină este desenată
        while(i<gp.player.life){
            g2.drawImage(heart_half,x,y,null);
            i++;
            if(i<gp.player.life){
                g2.drawImage(heart_full,x,y,null);
            }
            i++;
            x+=gp.tileSize;
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metodă ce desenează meniul jocului
    Se încarcă imaginea specifică de fundal pentru meniul jocului si este desenată de metoda drawImage
     */
    public void drawTitleScreen(){
        try {
            menu = ImageIO.read(getClass().getResourceAsStream("/res/menu/meniu.jpg"));
        }catch(IOException e){
            e.printStackTrace();
        }
        g2.drawImage(menu, 0, 0, gp.screenWidth, gp.screenHeight, null);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F)); // Setează fontul pentru opțiunile jocului
        String text="NEW GAME";
        int x=getXforCenteredText(text);
        int y=gp.tileSize*9;
        g2.drawString(text,x,y);
        if(commandNum==0){ // Desenează o săgeată în stânga opțiunii, daca aceasta este selectată
            g2.drawString(">",x-gp.tileSize/2,y);
        }

        text="LOAD GAME";
        x=getXforCenteredText(text);
        y+=gp.tileSize; //Schimbă coordonata y pentru a desena următoarea opțiune
        g2.drawString(text,x,y);
        if(commandNum==1){ // Desenează o săgeată în stânga opțiunii, daca aceasta este selectată
            g2.drawString(">",x-gp.tileSize/2,y);
        }

        text="QUIT";
        x=getXforCenteredText(text);
        y+=gp.tileSize; //Schimbă coordonata y pentru a desena următoarea opțiune
        g2.drawString(text,x,y);
        if(commandNum==2){ // Desenează o săgeată în stânga opțiunii, daca aceasta este selectată
            g2.drawString(">",x-gp.tileSize/2,y);
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metodă responsabilă pentru desenarea efectului de tranziție la trecerea dintre nivele
     */
    public void drawTransition(){
        counter++; // Incrementăm counter la fiecare apel al metodei pentru a avansa tranziția
        g2.setColor(new Color(0,0,0,counter*5)); // Culoarea va deveni mai intensă la fiecare apel al metodei
        g2.fillRect(0,0, gp.screenWidth,gp.screenHeight); // Desenarea unei dreptunghi pe tot ecranul ce va avea culoarea setată mai sus

        if(counter == 50) { // Atunci când counter-ul ajunge la valoarea respectivă, tranziția este completă
            counter=0;
            gp.gameState = gp.playState; // Schimbăm starea jocului
            gp.currentMap = gp.eHandler.tempMap; // Actualizăm harta cu cea temporară
            gp.player.worldX = gp.tileSize * gp.eHandler.tempCol; // Actualizăm poziția jucătorului
            gp.player.worldY=gp.tileSize*gp.eHandler.tempRow;
            gp.eHandler.previousEventX=gp.player.worldX; // Actualizăm poziția evenimentului anterior
            gp.eHandler.previousEventY=gp.player.worldY;
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metodă responsabilă pentru desenarea ecranului de pauză
     */
    public void drawPauseScreen(){
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F)); // Setarea fontului
        String text="PAUSED";
        int x=getXforCenteredText(text); // Setarea coordonatelor în centrul ecranului
        int y=gp.screenHeight/2;
        g2.drawString(text,x,y); // Desenarea textului
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metodă responsabilă pentru desenarea ecranului de final, care informează jucătorul că a câștigat și îi
    oferă posibilitatea de a juca din nou sau a reveni la meniul principal
     */
    public void drawEndScreen(){
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        int x,y;
        String text = "YOU WON!";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110f));

        //SHADOW
        g2.setColor(Color.BLACK);
        x=getXforCenteredText(text);
        y=gp.tileSize*4;
        g2.drawString(text,x,y);

        //MAIN
        g2.setColor(Color.white);
        g2.drawString(text,x-7,y-7);

        //DRAW PLAY TIME
        g2.setFont(g2.getFont().deriveFont(40f));
        y+=gp.tileSize;
        g2.drawString("PlayTime: "+gp.playTime,x,y);

        //RETRY
        g2.setFont(g2.getFont().deriveFont(50f));
        text="Play again!";
        x=getXforCenteredText(text);
        y+=gp.tileSize*4;
        g2.drawString(text,x,y);
        if(commandNum == 0){
            g2.drawString(">",x-40,y);
        }

        //BACK TO THE MENU
        text="MENU";
        x=getXforCenteredText(text);
        y+=55;
        g2.drawString(text,x,y);
        if(commandNum == 1){
            g2.drawString(">",x-40,y);
        }
    }


    /*
    Calcularea coordonatei X pentru a centra un text pe ecran
    g2.getFontMetrics().getStringBounds(text,g2) obține dimensiunile textului cand este desenat cu fontul curent
    getWidth returnează lațimea textului
    Conversia la int este necesară deoarece metoda getWidth() returnează un double
     */
    public int getXforCenteredText(String text){
        int length=(int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x=gp.screenWidth/2-length/2;
        // gp.screenWidth/2 calculează coordonata X a centrului ecranului
        // length/2 calculează jumatatea lungimii textului
        return x;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
