package Main;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
Clasa GamePanel este componenta principală a jocului responsabilă pentru gestionarea si actualizarea tuturor entităților,
precum și desenarea acestora pe ecran.
Implementează interfața Runnable pentru a permite rularea într-un fir de execuție separat
 */
public class GamePanel extends JPanel implements Runnable {
    //Screen Settings
    final int originalTileSize=16;
    final int scale=5;
    public final int tileSize=originalTileSize*scale;
    public final int maxScreenCol=20;
    public final int maxScreenRow=12;
    public final int screenWidth=tileSize*maxScreenCol;
    public final int screenHeight=tileSize*maxScreenRow;

    //WORLD SETTINGS
    public final int maxWorldCol=50;
    public final int maxWorldRow=50;
    public final int maxMap=5;
    public int currentMap=0;

    //SYSTEM
    int FPS=60;
    public TileManager tileM=new TileManager(this);
    public KeyHandler keyH=new KeyHandler(this);
    public UI ui=new UI(this);
    public CollisionChecker colChecker=new CollisionChecker(this);
    public AssetSetter aSetter=new AssetSetter(this);
    public EventHandler eHandler = new EventHandler(this);
    public PathFinder pFinder=new PathFinder(this);
    public Connection c;
    Thread gameThread;

    //ENTITY
    public Player player=new Player(this,keyH);
    public Entity monster[][]=new Entity[maxMap][20];
    ArrayList<Entity> entityList=new ArrayList<>();
    public ArrayList<Entity> projectileList=new ArrayList<>();

    //GAME STATE
    public int gameState;
    public final int titleState=0;
    public final int playState=1;
    public final int pauseState=2;
    public final int gameOverState=3;
    public final int transitionState=4;
    public final int gameFinished = 5;
    public double playTime;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight)); // Setează dimensiunea panoului
        this.setBackground(Color.WHITE); // Setează fundalul la alb
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.c=null;
        try {
            Class.forName("org.sqlite.JDBC");
            this.c= DriverManager.getConnection("jdbc:sqlite:bazadate_score.db");
            c.setAutoCommit(false);
            System.out.println("Connected with success");
        }catch(Exception e)
        {
            System.err.println("Failed to connect to DB");
            e.printStackTrace();
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void loadDB()
    {
        float timer=(float)playTime;
        int level=currentMap;
        int hp=player.life;
        Statement st;

        try {
            st=c.createStatement();
            String s="INSERT INTO tableDB(Level,Time,Health) VALUES ("+level+","+timer+","+hp+");";
            st.executeUpdate(s);
            st.close();
            c.commit();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void unloadDB()
    {
        float time=0;
        int lvl=0,hp=0;
        Statement st;
        try{
            st=c.createStatement();
            String s="SELECT * FROM tableDB";
            ResultSet rs=st.executeQuery(s);
            while(rs.next())
            {
                time=rs.getFloat("Time");
                lvl=rs.getInt("Level");
                hp=rs.getInt("Health");
                System.out.println("Time: "+time);
                System.out.println("Level: "+lvl);
                System.out.println("Health: "+hp);
            }
            rs.close();
            st.close();
        }catch(SQLException E)
        {
            E.printStackTrace();
        }
    }

    public void setupGame(){
        aSetter.setMonster(); // Setează inamicii pe hartă
        gameState=titleState; // Setează starea jocului la titleState
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void retry(){
        player.setDefaultValues(); // Resetează caracteristicile jucătorului
        aSetter.setMonster(); // Amplasează inamicii pe hartă
        if(currentMap == 1){
            currentMap = 1;
            player.worldX=tileSize*20;
            player.worldY=tileSize*41;
        }
        if(currentMap == 2 ){
            currentMap = 2;
            player.worldX = tileSize * 24;
            player.worldY = tileSize * 38;

        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public void StartGameThread(){ // Creează și pornește un fir de execuție pentru joc
        gameThread=new Thread(this);
        gameThread.start();
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    Metoda principală de rulare a jocului, actualizează starea jocului si re-desenarea
    la fiecare ciclu, respectând un interval de timp specific pentru a menține un FPS constant
     */
    @Override
    public void run() {
        double drawInterval=1_000_000_000/FPS;
        double nextDrawTime=System.nanoTime()+drawInterval;
        while(gameThread!=null){
            update();
            repaint();

            try {
                double remainingTime=nextDrawTime-System.nanoTime();
                remainingTime=remainingTime/1_000_000;
                if(remainingTime<0){ remainingTime=0; }
                Thread.sleep((long)remainingTime);
                nextDrawTime+=drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void update(){ // Actualizează starea entităților din joc dacă acesta se află in playState
        if(gameState==playState) {
            player.update(); // Actualizează starea jucătorului
            // Iterează prin array-ul de inamici din harta curentă
            for(int i=0;i<monster[1].length;i++){ // Folosim a doua dimensiune
                if(monster[currentMap][i]!=null){ // Verifică dacă există un monstru la poziția curentă, Daca există, se actualizează starea acestuia
                    if(monster[currentMap][i].alive == true && monster[currentMap][i].dying==false) { monster[currentMap][i].update();}
                    if(monster[currentMap][i].alive==false){ monster[currentMap][i]=null; } // Daca inamicul este mort, este scos din Array
                }
            }

            // Actualizarea proiectilelor
            // Acelasi principiu ca și la inamici
            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    if(projectileList.get(i).alive == true){
                        projectileList.get(i).update();
                    }
                    if(projectileList.get(i).alive == false){
                        projectileList.remove(i);
                    }
                }
            }
        }


    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void paintComponent(Graphics g){
        super.paintComponent(g); // Curăță zona de desenare și permite desenarea de la 0
        Graphics2D g2=(Graphics2D)g; // Conversie pentru a beneficia de funcționalități noi

        //TITLE SCREEN
        if(gameState==titleState){
            ui.draw(g2); // Desenează meniul jocului daca acesta se află în starea corespunzătoare
        }
        else {
            tileM.draw(g2); // Desenează tile-urile

            entityList.add(player); // Adaugă jucătorul în lista de entități
                // Adaugă monștrii din harta curentă în lista de entități
            for(int i=0;i<monster[1].length;i++){
                if(monster[currentMap][i]!=null){ entityList.add(monster[currentMap][i]); }
            }
                // Adaugă proiectilele în lista de entități
            for(int i = 0; i < projectileList.size(); i++){
                if(projectileList.get(i) != null){
                    entityList.add(projectileList.get(i));
                }
            }

            // Sortarea este facută pe baza coordonatei worldY pentru a asigura o desenare corectă
            // din punct de vedere vizual
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY,e2.worldY);
                    return result;
                }
            });

            for(int i=0;i<entityList.size();i++){
                entityList.get(i).draw(g2); // Fiecare entitate este desenată
            }
                // Lista este golită pentru a fi pregătită pentru următorul ciclu
            entityList.clear();

                // Desenarea elementelor de interfață ale utilizatorului
            ui.draw(g2);
        }
        g2.dispose(); // Eliberarea resurselor grafice utilizate de Graphics2D
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
