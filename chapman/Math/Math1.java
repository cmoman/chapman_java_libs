package chapman.math;
import java.util.Random;

/**
 * Class <code>Math1</code> contains methods for performing basic
 * numeric operations that were not included in the <code>java.lang.Math</code>
 * class.  Examples include the hyperbolic functions, logarithms to the base 10,
 * and trhgonometric functions that work in degrees instead of radians.
 * It consists entirely of static methods and final static variables.
 *
 * @author  S. J. Chapman
 * @version 1.00, 05/15/98
 */

public final class Math1 {

   //*************************************************
   // Constants
   //*************************************************

   /**
    * Constant to convert degrees to radians.
    */
   public static final double DEG_2_RAD = Math.PI / 180;


   /**
    * Constant to convert radians to degrees.
    */
   public static final double RAD_2_DEG = 180 / Math.PI;


   /**
    * Natural logarithm of 10.0.
    */
   final static private double LOGE_10 = 2.302585092994046;


   //*************************************************
   // Variables
   //*************************************************

   /**
    * Reference for random number generator
    */
   private static Random randomNumberGenerator;

   //*************************************************
   // Constructors
   //*************************************************

   /**
    * Create an inaccessible constructor, so that this class cannot be
    * instantiated.
    */
   private Math1() {}

   //*************************************************
   // Static Methods
   //*************************************************

   /**
    * Returns the inverse cosine of a value, in the range of 0.0 through
    * 180.0 <i>degrees</i>.  Valid input range is -1.0 <= a <= 1.0.
    * @param   a   input value in the range -1.0 <= a <= 1.0.
    * @return  the angle in <i>degrees</i>.
    */
   public static synchronized double acosd( double a ) {
      return ( Math.acos( a ) * RAD_2_DEG );
   }


   /**
    * Returns the inverse hyperbolic cosine of a value.
    * Valid input range is a >= 1.0.
    * @param   a   input value in the range a >= 1.0.
    * @return  the inverse hyperbolic cosine
    */
   public static synchronized double acosh( double a ) {
      return ( Math.log( a + Math.sqrt(a*a-1)) );
   }


   /**
    * Returns the inverse sine of a value, in the range of -90.0 through
    * 90.0 <i>degrees</i>.  Valid input range is -1.0 <= a <= 1.0.
    * @param   a   input value in the range -1.0 <= a <= 1.0.
    * @return  the angle in <i>degrees</i>.
    */
   public static synchronized double asind( double a ) {
      return ( Math.asin( a ) * RAD_2_DEG );
   }


   /**
    * Returns the inverse hyperbolic sine of a value.
    * @param   a   input value
    * @return  the inverse hyperbolic sine
    */
   public static synchronized double asinh( double a ) {
      return ( Math.log( a + Math.sqrt(1+a*a)) );
   }


   /**
    * Returns the inverse tangent of a value, in the range of -90.0 through
    * 90.0 <i>degrees</i>.  Valid input range is -Inf < a < Inf.
    * @param   a   input value
    * @return  the angle in <i>degrees</i>.
    */
   public static synchronized double atand( double a ) {
      return ( Math.atan( a ) * RAD_2_DEG );
   }


   /**
    * Returns the inverse hyperbolic tangent of a value.
    * @param   a   input value
    * @return  the inverse hyperbolic tangent
    */
   public static synchronized double atanh( double a ) {
      return ( 0.5 * Math.log((1+a)/(1-a)) );
   }


   /**
    * Calculates the angle from the positive horizontal (x-)axis to a point (a,b),
    * in the range of -180.0 through 180.0 <i>degrees</i>.
    * @param   a   x-input value
    * @param   a   y-input value
    * @return  the angle in <i>degrees</i>.
    */
   public static synchronized double atan2d( double a, double b ) {
      return ( Math.atan2( a, b ) * RAD_2_DEG );
   }


   /**
    * Returns the cosine of an angle expressed in <i>degrees</i>.
    * @param   a   an angle, in <i>degrees</i>.
    * @return  the cosine of the argument.
    */
   public static synchronized double cosd( double a ) {
      return Math.cos( a * DEG_2_RAD );
   }


   /**
    * Returns the hyperbolic cosine of an angle.
    * @param   a   an angle, in radians.
    * @return  the hyperbolic cosine of the argument.
    */
   public static synchronized double cosh( double a ) {
      return ( (Math.exp(a) + Math.exp(-a)) / 2 );
   }


   /**
    * Returns the logarithm to the base 10 of a number.
    * @param   a   a <code>double</code> value
    * @return  the logarithm to the base 10 of the argument.
    */
   public static synchronized double log10( double a ) {
      return ( Math.log(a) / LOGE_10 );
   }


   /**
    * Returns a normally-distributed Gaussian random number.
    * @return  a normally-distributed pseudorandom
    *          <code>double</code> value.
    */
   public static synchronized double randomGaussian() {
       if (randomNumberGenerator == null)
           randomNumberGenerator = new Random();
       return randomNumberGenerator.nextGaussian();
   }


   /**
    * Returns a Rayleigh-distributed random number.
    * @return  a Rayleigh-distributed pseudorandom
    *          <code>double</code> value.
    */
   public static synchronized double randomRayleigh() {
       if (randomNumberGenerator == null) {
           randomNumberGenerator = new Random();
       }
       double x = randomNumberGenerator.nextGaussian();
       double y = randomNumberGenerator.nextGaussian();
       return Math.sqrt(x*x + y*y);
   }


   /**
    * Returns the function sinc <i>x</i>, where sinc <i>x</i> is defined
    * as sin <i>x</i> / <i>x</i>.
    *
    * @param   a   input value.
    * @return  the sinc of the argument.
    */
   public static synchronized double sinc( double x ) {
      if ( Math.abs(x) < 1.0e-30 )
         return 1.0;
      else
         return Math.sin(x)/x;
   }


   /**
    * Returns the sine of an angle expressed in <i>degrees</i>.
    *
    * @param   a   an angle, in <i>degrees</i>.
    * @return  the sine of the argument.
    */
   public static synchronized double sind( double a ) {
      return Math.sin( a * DEG_2_RAD );
   }


   /**
    * Returns the hyperbolic sine of an angle.
    * @param   a   an angle, in radians.
    * @return  the hyperbolic sine of the argument.
    */
   public static synchronized double sinh( double a ) {
      return ( (Math.exp(a) - Math.exp(-a)) / 2 );
   }


   /**
    * Returns the tangent of an angle expressed in <i>degrees</i>.
    * @param   a   an angle, in <i>degrees</i>.
    * @return  the tangent of the argument.
    */
   public static synchronized double tand( double a ) {
      return Math.tan( a * DEG_2_RAD );
   }


   /**
    * Returns the hyperbolic tangent of an angle.
    * @param   a   an angle, in radians.
    * @return  the hyperbolic tangent of the argument.
    */
   public static synchronized double tanh( double a ) {
      double exp = Math.exp(a);
      double exm = Math.exp(-a);
      return ( (exp - exm) / (exp + exm) );
   }
}
