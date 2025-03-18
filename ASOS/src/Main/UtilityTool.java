package Main;
import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {
    /*
    Metodă utilizată pentru a redimensiona imaginile, menținând proporțiile inițiale ale imaginii
    Creează o noua imagine BufferedImage cu dimensiunile specificate
    Utilizează obiectul g2 pentru a desena imaginea scalată
    Scalarea este proporțională
    Se eliberează resursele grafice folosind dispose()
    Returnăm imaginea scalată
     */
   public BufferedImage scaleImage(BufferedImage original, int width, int height){
       BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

       Graphics2D g2 = scaledImage.createGraphics();
       g2.drawImage(original, 0, 0, width, height, null); // Scalare proporțională
       g2.dispose();

       return scaledImage;
   }////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
