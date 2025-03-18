package tile;

import Main.GamePanel;
import Main.UtilityTool;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile; // Array ce va conține tipurile de tile-uri folosite pentru hartă
    public int mapTileNum[][][]; // Array ce va stoca informațiile despre tile-uri, cum ar fi, numărul hărții, coloanele si liniile din hartă

    /*
    Inițializăm array-ul tile pentru a stoca maxim 30 de tile-uri diferite
    Inițializăm array-ul mapTileNum pentru a putea stoca numărul maxim de hărți, rânduri si coloane
    Apelăm metoda getTileImage și încărcăm tile-urile pentru toate cele 3 hărți
     */
    public TileManager(GamePanel gp){
        this.gp=gp;
        tile=new Tile[30];
        mapTileNum=new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/res/maps/level1.txt",0);
        loadMap("/res/maps/level2.txt",1);
        loadMap("/res/maps/level3.txt",2);
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Încărcăm fiecare imagine pentru toate tipurile de tile-uri existente, și setăm coliziunile pentru
    tile-urile ce necesită acest lucru
     */
    public void getTileImage(){
        for(int i=0; i <= 27; i++){
            if(i==1 || (i >= 9 && i <= 11) || (i>=13 && i<=20))
                setup(i,Integer.toString(i),true);
            else
                setup(i,Integer.toString(i),false);
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Inițializăm fiecare tile și încărcăm imaginile cu ajutorul ImageIO.read(). Calea imaginii este compusă din
    folderul ce conține imaginile + ID-ul tile-ului
    Cu ajutorul metodei scaleImage, imaginea va avea dimensiunea stocată in variabila tileSize, după care va fi setată
    coliziunea respectivă tile-ului
    Toate acestea se află într-un bloc try-catch pentru a preveni posibile erori ce pot apărea în timpul încărcării imaginii
     */
    public void setup(int index,String imageID,boolean collision){
        UtilityTool uTool=new UtilityTool();
        try{
            tile[index]=new Tile();
            tile[index].image=ImageIO.read(getClass().getResourceAsStream("/res/tiles/"+imageID+".png"));
            tile[index].image=uTool.scaleImage(tile[index].image,gp.tileSize,gp.tileSize);
            tile[index].collision=collision;
        }catch(IOException e){
            e.printStackTrace();
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Se încearcă deschiderea si citirea fișierului de la adresa dată de primul parametru
    Metoda deschide și citește un fișier text care conține ID-urile pe care le au tile-urile

     */
    public void loadMap(String filePath, int mapNumber){
        try{
            InputStream is=getClass().getResourceAsStream(filePath);
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            int col=0; int row=0; // Inițializarea variabilelor col si row

            while(col<gp.maxWorldCol && row<gp.maxWorldRow){
                String line=br.readLine(); // Citește o linie din fișier
                while(col<gp.maxWorldCol){
                    String numbers[]=line.split(" "); // Împarte linia în mai multe string-uri
                    int num=Integer.parseInt(numbers[col]); // Convertește strig-ul într-un întreg
                    mapTileNum[mapNumber][col][row]=num; // Stochează numărul tile-ului la pozițiile specificate (mapNumber, col, row)
                    col++; // Incrementează coloana curentă
                }
                //Resetează numărul coloanei și trece pe rândul următor
                if(col==gp.maxWorldCol){
                    col=0;
                    row++;
                }
            }
            br.close(); // închide BufferedReader pentru a elibera resure
        }catch(Exception e){
            e.printStackTrace();
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    Metoda draw() desenează toate dalele vizibile pe ecran, astfel fiind implementat conceptul de cameră
    Se asigură că harta este desentată corect în funcție de poziția curentă a jucătorului
    worldX și worldY - coordonatele tile-ului pe hartă
    player.worldX și player.worldY - coordonatele jucătorului pe hartă
    player.screenX și player.screenY - coordonatele jucătorului pe ecran
    screenX și screenY - coordonatele pe ecran ale tile-ului curentt
     */
    public void draw(Graphics2D g2){
        int worldCol=0;
        int worldRow=0;

        while(worldCol<gp.maxWorldCol && worldRow<gp.maxWorldRow){
            int tileNum=mapTileNum[gp.currentMap][worldCol][worldRow]; // Determinarea tile-ului curent
            int worldX=worldCol*gp.tileSize; // Calculăm coordonatele tile-ului curent
            int worldY=worldRow*gp.tileSize;
            int screenX=worldX-gp.player.worldX + gp.player.screenX; // Calculăm poziția pe ecran în funcție de poziția jucătorului pe hartă
            // cât și poziția acestuia pe ecran
            int screenY=worldY-gp.player.worldY + gp.player.screenY;

            // Verificăm daca tile-ul curent este în interiorul ferestrei, daca da, atunci este desenat
            if(worldX+gp.tileSize>gp.player.worldX-gp.player.screenX
                    && worldX-gp.tileSize<gp.player.worldX+gp.player.screenX
                    && worldY+gp.tileSize > gp.player.worldY-gp.player.screenY
                    && worldY-gp.tileSize<gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++;

            if(worldCol==gp.maxWorldCol){
                worldCol=0;
                worldRow++;
            }
        }

    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
