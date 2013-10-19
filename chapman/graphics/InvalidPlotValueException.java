package chapman.graphics;

/**
 * The <code>InvalidPlotValueException</code> is used in graphics methods
 * to flag errors, such as non-monotonic tic mark data, etc. 
 * 
 * @author  S. J. Chapman
 * @version 1.00, 06/15/98
 */
public class InvalidPlotValueException extends RuntimeException {

   // Define constructors for this class
   public InvalidPlotValueException() {
      super();
   }

   // Define constructors for this class
   public InvalidPlotValueException(String s) {
      super(s);
   }
}