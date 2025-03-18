package Main;
import javax.swing.*;

/*
Punctul de intrare în aplicație
Se ocupă cu configurarea și inițializarea interfeței grafice
 */
public class Main {

    public static void main(String[] args) {
        JFrame window=new JFrame(); // Creează fereastra principală a jocului
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Închide jocul atunci când fereastra este închisă
        window.setResizable(false); // Dezactivează posibilitatea de a redimensiona fereastra
        window.setTitle("A Storm Of Swords"); //Setează titlul ferestrei

        GamePanel gamePanel=new GamePanel(); // Creează panoul de joc
        window.add(gamePanel); // Adaugă gamePanel in fereastra window
        window.pack(); //seteaza dimensiunile potrivite ferestrei pentru conținutul său

        window.setLocationRelativeTo(null); // Plasează fereastra în centrul ecranului
        window.setVisible(true); // Face fereastra vizibilă

        gamePanel.setupGame(); // Inițializează jocul
        gamePanel.StartGameThread(); // Pornețe firul de execuție pentru joc
    }
}