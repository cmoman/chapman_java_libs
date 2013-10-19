package chapman.math;

/**
 * The <code>InvalidArraySizeException</code> is used in signal-processing
 * methods that are dependent on certain array sizes, such as fft algorithms
 * that require data to come in lengths that are powers of 2.  It is thrown
 * when an array has a illegal length.
 * 
 * @author  S. J. Chapman
 * @version 1.00, 05/15/98
 */
public class InvalidArraySizeException extends RuntimeException {

   // Define constructors for this class
   public InvalidArraySizeException() {
      super();
   }

   // Define constructors for this class
   public InvalidArraySizeException(String s) {
      super(s);
   }
}