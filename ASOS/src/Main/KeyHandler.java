package Main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
Clasa KeyHandler implementează KeyListener pentru a gestiona evenimentele legate de taste
 */
public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed,attackPressed,projectilePressed, blockStatus;

    public KeyHandler(GamePanel gp){
        this.gp=gp;
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void keyTyped(KeyEvent e) {}

    /*
    Metoda keyPressed este responsabilă pentru gestionarea acțiunilor declanșate atunci
    când o tastă este apăsată în funcție de starea de joc in care ne aflăm
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int code=e.getKeyCode();
        //TITLE STATE
        if(gp.gameState==gp.titleState){
           titleState(code);
        }
        else if(gp.gameState == gp.playState){
            playState(code);
        }else if (gp.gameState == gp.gameOverState){
            gameOverState(code);
        }else if(gp.gameState == gp.gameFinished){
            gameOverState(code);
        }
        pauseState(code);


    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    Metoda keyPressed este responsabilă pentru gestionarea acțiunilor declanșate atunci
    când o tastă este eliberată
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code=e.getKeyCode();
        if(code==KeyEvent.VK_UP){ upPressed=false; }
        if(code==KeyEvent.VK_DOWN){ downPressed=false; }
        if(code==KeyEvent.VK_RIGHT){ rightPressed=false; }
        if(code==KeyEvent.VK_LEFT){ leftPressed=false; }
        if(code==KeyEvent.VK_SPACE){ attackPressed=false; }
        if(code==KeyEvent.VK_F) { projectilePressed=false; }
        if(code==KeyEvent.VK_B) { blockStatus=false; }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    KeyEvent.VK_UP - se decrementează commandNum, acesta controlează selecția meniului in sus
    Dacă commandNum devine mai mic decât 0, atunci primește valoarea 2, pentru a permite navigarea circulară a meniului

    KeyEvent.VK_DOWN - se incrementeaza commandNUm, ce controlează meniul in jos
    KeyEvent.VK_ENTER - verifică valorea din commandNum pentru a determina ce acțiune va fi executatp
    0 - New Game
    1 - Load Game
    2 - Quit
     */
    public void titleState(int code){
        if(code==KeyEvent.VK_UP){
            gp.ui.commandNum--;
            if(gp.ui.commandNum<0){
                gp.ui.commandNum=2;
            }
        }

        if(code==KeyEvent.VK_DOWN){
            gp.ui.commandNum++;
            if(gp.ui.commandNum>2){
                gp.ui.commandNum=0  ;
            }
        }

        if(code==KeyEvent.VK_ENTER){
            if(gp.ui.commandNum==0){ //NEW GAME
                gp.gameState=gp.playState;
            }
            if(gp.ui.commandNum==1){
                //LOAD GAME
            }
            if(gp.ui.commandNum==2){ //QUIT
                System.exit(0);
            }
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
   Metoda playState permite jucătorului să controleze personajul, cum ar fi deplasarea pe hartă,
   atacul, blocarea atacurilor, aruncarea proiectilelor
     */
    public void playState(int code){
        if(code==KeyEvent.VK_SPACE){ attackPressed=true; }
        if(code==KeyEvent.VK_UP){ upPressed=true; }
        if(code==KeyEvent.VK_DOWN){ downPressed=true; }
        if(code==KeyEvent.VK_RIGHT){ rightPressed=true; }
        if(code==KeyEvent.VK_LEFT){ leftPressed=true; }
        if(code==KeyEvent.VK_F) { projectilePressed=true; }
        if(code==KeyEvent.VK_B) { blockStatus=true; }
        if(code == KeyEvent.VK_Q)
        {
            gp.unloadDB();
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    Metoda pauseState permite să controleze acțiunile din ecranul de pauză
    Dacă ne aflăm în playState, la apăsarea tastei P, jocul va intra in pauseState
    Daca ne aflăm în pauseState, la apăsarea tastei P, jocul se va întoarce în playState
     */
    public void pauseState(int code) {
        if (code == KeyEvent.VK_P) {
            if (gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;
            } else if (gp.gameState == gp.pauseState) {
                gp.gameState = gp.playState;
            }
        }
    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    KeyEvent.VK_UP - se decrementează commandNum, acesta controlează selecția meniului in sus
    Dacă commandNum devine mai mic decât 0, atunci primește valoarea 1, pentru a permite navigarea circulară a meniului

    KeyEvent.VK_DOWN - se incrementeaza commandNum, ce controlează meniul in jos
    KeyEvent.VK_ENTER - verifică valorea din commandNum pentru a determina ce acțiune va fi executată
    0 - Retry
    1 - Exit
     */
    public void gameOverState(int code){
        if(code == KeyEvent.VK_UP){
            gp.ui.commandNum--;
            if(gp.ui.commandNum<0){
                gp.ui.commandNum=1;
            }
        }

        if(code == KeyEvent.VK_DOWN){
            gp.ui.commandNum++;
            if(gp.ui.commandNum>1){
                gp.ui.commandNum=0;
            }
        }

        if(code == KeyEvent.VK_ENTER){
            if(gp.ui.commandNum == 0){
                gp.gameState = gp.playState;
                gp.retry();
            }
            else if (gp.ui.commandNum == 1){
                gp.gameState = gp.titleState;
                gp.retry();
            }
        }

    }///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
