package Main;

public class EventHandler {
    GamePanel gp;
    EventRect eventRect[][][]; // Array folosit pentru a defini mai multe zone de pe ecran asociate unor evenimente
    int previousEventX, previousEventY; // Coordonatele evenimentului precedent
    boolean canTouchEvent = true; // Un indicator ce stocheaza dacă un eveniment poate fi activat
    int tempMap, tempCol, tempRow; // Varibile temporare utilizate pentru a stoca informații


    /*
    Inițializăm variabilele
    În bucla while sunt setate dimensiunile dreptunghiului, cat si pozitia acestuia pe harta
     */
    public EventHandler(GamePanel gp){
        this.gp=gp;
        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        int map = 0;
        int col = 0;
        int row = 0;

        while(map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow){
            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if(col == gp.maxWorldCol){
                col=0;
                row++;
                if(row == gp.maxWorldRow){
                    row = 0;
                    map++;
                }
            }

        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Verificăm dacă jucătorul se află pe o anumită poziție și are o anumită direcție pentru a putea
    declanșa anumite evenimente, de exemplu schimbarea hărții, sau schimbarea stării de joc (Ecranul de final)
    HP-ul jucatorului va fi resetat la fiecare trecere de nivel
     */

    public void checkEvent(){
        canTouchEvent = true;

// Verificăm dacă jucătorul se află pe poziția tile-ului specificat și pe direcția specificată
        int playerTileX = gp.player.worldX / gp.tileSize;
        int playerTileY = gp.player.worldY / gp.tileSize;

// Verificăm coordonatele tile-ului și direcția jucătorului pentru teleport
        if (canTouchEvent && gp.currentMap == 0 && (playerTileX == 28 || playerTileX == 27 || playerTileX == 29) && playerTileY == 7 && gp.player.direction.equals("up")) {
            gp.gameState = gp.transitionState;
            gp.loadDB();
            gp.player.life=gp.player.maxLife;
            teleport(1, 20, 41);
        } else if (canTouchEvent && gp.currentMap == 1 && (playerTileX == 33 || playerTileX == 32 || playerTileX==34)  && playerTileY == 14 && gp.player.direction.equals("up")) {
            gp.gameState = gp.transitionState;

            gp.loadDB();
            gp.player.life=gp.player.maxLife;
            teleport(2, 24, 38);
        } else if (canTouchEvent && gp.currentMap == 2 && (playerTileX == 23 || playerTileX == 24 || playerTileX==25)  && playerTileY == 13 && gp.player.direction.equals("up")) {
            gp.loadDB();
            gp.gameState=gp.gameFinished;
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Atunci când metoda este apelată, poziția jucătorului va fi actualizată pentru a schimba harta și poziția de pe noua hartă
     */
    public void teleport(int map,int col, int row ){
        tempMap=map;
        tempCol=col;
        tempRow=row;
        canTouchEvent = false;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
