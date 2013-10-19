package chapman.graphics;
import java.awt.*;

/**
 * A <code>JCanvas</code> component is a Swing component consisting 
 * of a blank rectangular area of the screen onto which the 
 * an application can draw or from which the application can 
 * trap input events from the user. 
 * <p>
 * An application must subclass the <code>JCanvas</code> class in 
 * order to get useful functionality such as creating a custom 
 * component. The <code>paintComponent</code> method must be overridden 
 * in order to perform custom graphics on the canvas.
 *
 * @version    1.00 12/15/98
 * @author     Stephen J. Chapman
 */
public class JCanvas extends javax.swing.JComponent {

   public JCanvas() {
      setDoubleBuffered( true );
   }
   
   public void paintComponent( Graphics g ) {

      Dimension size = getSize();
      g.setColor(getBackground());
      g.fillRect(0, 0, size.width, size.height);      
   }
}
