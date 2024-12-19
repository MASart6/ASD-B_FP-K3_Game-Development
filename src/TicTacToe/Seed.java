/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #3
 * 1 - 5026221134 - Mohammad Affan Shofi
 * 2 - 5026231121 - Rian Chairul Ichsan
 * 3 - 5026231211 - Hafidz Putra Dermawan
 */

package TicTacToe;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
/**
 * This enum is used by:
 * 1. Player: takes value of CROSS or NOUGHT
 * 2. Cell content: takes value of CROSS, NOUGHT, or NO_SEED.
 *
 * We also attach a display image icon (text or image) for the items.
 *   and define the related variable/constructor/getter.
 * To draw the image:
 *   g.drawImage(content.getImage(), x, y, width, height, null);
 *
 * Ideally, we should define two enums with inheritance, which is,
 *  however, not supported.
 */
public enum Seed {   // to save as "Seed.java"
   CROSS("X", "TicTacToe/cat.gif"),   // displayName, imageFilename
   NOUGHT("O", "TicTacToe/dog.gif"),
   NO_SEED(" ", null);

   // Private variables
   private String displayName;
   private Image img = null;

   // Constructor (must be private)
   private Seed(String name, String imageFilename) {
      this.displayName = name;

      if (imageFilename != null) {
         URL imgURL = getClass().getClassLoader().getResource(imageFilename);
         ImageIcon icon = null;
         if (imgURL != null) {
            icon = new ImageIcon(imgURL);
            //System.out.println(icon);  // debugging
         } else {
            System.err.println("Couldn't find file " + imageFilename);
         }
         img = icon.getImage();
      }
   }

   // Public getters
   public String getDisplayName() {
      return displayName;
   }
   public Image getImage() {
      return img;
   }
}