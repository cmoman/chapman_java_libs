package chapman.math;
import java.util.Arrays;
import chapman.math.Array;

/**
 *
 * Class <tt>Statistics</tt> performs a wide variety of statistical 
 * calculations.  It will be expanced to support the collections 
 * interface in the future.
 *  
 * @author  S. J. Chapman
 * @version 1.00, 08/15/99
 */

public class Statistics implements Cloneable {

   //*************************************************
   // Instance Variables
   //*************************************************
   
   /**
    * The raw data.  
    */
   private double data[];            
   
   //*************************************************
   // Constructors
   //*************************************************
   
   /**
    * This constructor creates a new empty <tt>Statistics</tt> object.
    */
   public Statistics() { }

   /**
    * This constructor creates a new <tt>Statistics</tt> object, and
    * initializes the data array.
    */
   public Statistics( double x[] ) {
   
      // Save data
      data = x;
   }  

   /**
    * This constructor creates a new <tt>Statistics</tt> object, and
    * initializes the data array with an array of Objects that must
    * be convertable to numeric values.
    */
/* public Statistics( double x[] ) {
   
      // Save data
      data = x;
   }  */

   //*************************************************
   // Instance Methods
   //*************************************************
   
   /**
    * This method calculates the average or arithmetic mean
    * of a data set. 
    *
    * @return  the arithmetic mean 
    */
   public double mean() {
      return ( mean(data) );
   }
      
   
   /**
    * This method calculates the median of a data set.
    *
    * @return  the median
    */
   public double median() {
      return ( median(data) );
   }
      
   
   /**
    * This method calculates standard deviation of a
    * data set.  This method normalizes by n-1 where
    * n is the sequence length, so it is the best 
    * unbiased estimate of standartd deviation if 
    * the data set is sampled from a normal distribution.
    *
    * @return  the standard deviation 
    */
   public double stdDev() {
      return ( stdDev(data) );
   }
         
   /**
    * This method calculates the skewness of a
    * data set.  Skewness is the third central moment
    * divided by the third power of the standard
    * deviation.
    *
    * @return  the skewness 
    */
   public double skewness() {
      return ( skewness(data) );
   }
         
   /**
    * This method calculates the kurtosis of a
    * data set.  Kurtosis is the fourth central moment
    * divided by the fourth power of the standard
    * deviation.
    *
    * @return  the kurtosis
    */
   public double kurtosis() {
      return ( kurtosis(data) );
   }
         
   //*************************************************
   // Static Methods
   //*************************************************
   
   /**
    * This method calculates the average or arithmetic mean
    * of a data set. 
    * 
    * @param  data  The input data set
    *
    * @return  the mean of <tt>data</tt>.
    */
   public static double mean( double[] data ) {
   
      if ( data.length < 1 ) {
         return ( 0. );
      }
      else {
         double sum = 0;
         for ( int i = 0; i < data.length; i++ ) 
            sum += data[i];
         return ( sum / data.length );
      }
   }
      
   
   /**
    * This method calculates the median of a data set.
    * 
    * @param  data  The input data set
    *
    * @return  the median of <tt>data</tt>.
    */
   public static double median( double[] data ) {
   
      double median = 0;
      
      if ( data.length < 1 ) {
         return ( 0. );
      }
      else {
         
         // Get local copy of data
         double[] out = new double[data.length];
         for ( int i=0; i < data.length; i++) 
            out[i] = data[i];

         // Sort data            
         java.util.Arrays.sort( out );

         // Get median
         if (out.length%2 == 0)
            median = (out[out.length/2-1] + out[out.length/2]) / 2.;
         else
            median = out[out.length/2];

         return median;
      }
   }
      
   
   /**
    * This method calculates standard deviation of a
    * data set.  This method normalizes by n-1 where
    * n is the sequence length, so it is the best 
    * unbiased estimate of standartd deviation if 
    * the data set is sampled from a normal distribution.
    * 
    * @param  data  The input data set
    *
    * @return  the standard deviation of <tt>data</tt>.
    */
   public static double stdDev( double[] data ) {
      return ( Math.sqrt(var(data)) );
   }
         

   /**
    * This method calculates the skewness of a
    * data set.  Skewness is the third central moment
    * divided by the third power of the standard
    * deviation.
    * 
    * @param  data  The input data set
    *
    * @return  the skewness of <tt>data</tt>.
    */
   public static double skewness( double[] data ) {
   
      if ( data.length < 2 ) {
         return ( 0. );
      }
      else {
         double m3 = moment(data,3);
         double sm2 = Math.sqrt( moment(data,2) );
         return ( m3 / Math.pow(sm2,3) );
      }
   }
         
   /**
    * This method calculates the kurtosis of a
    * data set.  Kurtosis is the fourth central moment
    * divided by the fourth power of the standard
    * deviation.
    * 
    * @param  data  The input data set
    *
    * @return  the kurtosis of <tt>data</tt>.
    */
   public static double kurtosis( double[] data ) {
   
      if ( data.length < 2 ) {
         return ( 0. );
      }
      else {
         double m4 = moment(data,4);
         double sm2 = Math.sqrt( moment(data,2) );
         return ( m4 / Math.pow(sm2,4) );
      }
   }
         

   /**
    * This method calculates variance of a
    * data set.  This method normalizes by n-1 where
    * n is the sequence length, so it is the best 
    * unbiased estimate of variance if 
    * the data set is sampled from a normal distribution.
    * 
    * @param  data  The input data set
    *
    * @return  the variance of <tt>data</tt>.
    */
   public static double var( double[] data ) {
      
      if ( data.length < 2 ) {
         return ( 0. );
      }
      else {
         double sum = 0, sum2 = 0, var = 0;
         for ( int i = 0; i < data.length; i++ ) {
            sum  += data[i];
            sum2 += data[i] * data[i];
         }
         var = (data.length * sum2 - sum*sum )
             / (data.length * (data.length-1));
         return ( var );
      }
   }
         
   /**
    * This method calculates the cumulative probability curve
    * of array <tt>data</tt> at locations <tt>x</tt>.
    * 
    * @param  data  The input data set
    * @param  x     The locations  to calculate cumulative probability
    *
    * @return  the cumulative probabilities
    */
   public static double[] cumProbability( double[] data, double[] x ) {
   
      // Sort into ascending order 
      Arrays.sort( data );
      
      // Get minimum and maximum
      double max = data[data.length-1];
      double min = data[0];

      // Create cumulative probability array
      double[] cumProb = new double[ x.length ];

      // Calculate cumulative counts
      for ( int i = 0; i < x.length; i++ ) {
         for ( int j = 0; j < data.length; j++ ) {
            if ( data[j] <= x[i] ) cumProb[i]++;
         }
      }
            
      // Convert to cumulative probability
      for ( int i = 0; i < x.length; i++ ) { 
         cumProb[i] = cumProb[i] / data.length;
      }

      return ( cumProb );
   }


   /**
    * This method calculates the locations of the specified 
    * percentiles <tt>p</tt> of array <tt>data</tt>.
    * 
    * @param  data  The input data set
    * @param  p  The percentiles to calculate
    *
    * @return  the <tt>p</tt>th percentiles of <tt>data</tt>.
    */
   public static double[] percentile( double[] data, double[] p ) {
   
      // Get percentile locations array
      double[] percLoc = new double[ p.length ];

      // Sort into ascending order 
      Arrays.sort( data );
      
      // Get minimum and maximum
      double max = data[data.length-1];
      double min = data[0];

      // Get independent array for cumProb calculation
      double dx = (max - min) / 99;
      double[] xval = new double[ 100 ];
      for ( int i = 0; i < xval.length; i++ ) { 
         xval[i] = min + i * dx;
      }
      
      // Create cumulative probability array
      double[] cumProb = new double[ xval.length ];

      // Calculate cumulative counts
      for ( int i = 0; i < xval.length; i++ ) {
         for ( int j = 0; j < data.length; j++ ) {
            if ( data[j] <= xval[i] ) cumProb[i]++;
         }
      }
            
      // Convert to cumulative probability
      for ( int i = 0; i < xval.length; i++ ) { 
         cumProb[i] = cumProb[i] / data.length;
      }

      // Calculate desired percentiles
      for ( int i = 0; i < p.length; i++ ) {
         percLoc[i] = interp2( cumProb, xval, p[i] );
      }
        
      return ( percLoc );
   }


   /**
    * Returns the maximum value of any element in an array.
    *
    * @param   data   input array.
    *
    * @return  the maximum value of <tt>data[i]</tt>
    */
   public static double maxVal ( double data[] ) {
      double maxVal = data[0];
      for ( int i = 0; i < data.length; i++ ) {
         if (data[i] > maxVal) {
            maxVal = data[i];
         }
      }
      return maxVal;
   }


   /**
    * Returns the minimum value of any element in an array.
    *
    * @param   data   input array.
    *
    * @return  the minimum value of <tt>data[i]</tt>
    */
   public static double minVal ( double data[] ) {
      double minVal = data[0];
      for ( int i = 0; i < data.length; i++ ) {
         if (data[i] < minVal) {
            minVal = data[i];
         }
      }
      return minVal;
   }


   //*************************************************
   // Private methods
   //*************************************************
   
   private static double moment( double[] x, int order ) {
   
      if ( order == 1 ) {
         return( 0. );
      }
      else {
         double mu = mean(x); 
         double sum = 0;
         for (int i = 0; i < x.length; i++ ) {
            sum += Math.pow( (x[i] - mu), order );
         }
         return ( sum / x.length );
      }
   }


   /**
    * This method linearly interpolates the (<tt>x</tt>,<tt>y</tt>) 
    * data at points <tt>x0</tt>.  In this method, if 
    * x0 < x[0], y0 = y[0], and if x0 > x[x.length-1], 
    * y0 = y[x.length-1].
    * 
    * @param  x  Input <tt>x</tt> values
    * @param  y  Input <tt>y</tt> values
    * @param  x0 Locations to interpolate
    *
    * @return  the interpolated values
    */
   private static double interp1( double[] x, double[] y, double x0 ) {
   
      // We know data will be monotonically increasing, so don't
      // bother to check here.  Get straight to it.
      double y0;
      if ( x0 < x[0] ) {
         y0 = y[0];
      }
      else if ( x0 >= x[ x.length-1 ] ) {
         y0 = y[ x.length-1 ];
      }
      else {
      
         // Find the proper interval
         int i = 0;
         for ( i = 0; i < x.length-1; i++ ) {
            if ( x0 >= x[i] && x0 <= x[i+1] ) {
               break;
            }
         }
         
         // Now interpolate the value
         if ( i < x.length ) {
            double dx = (x0 - x[i])/(x[i+1] - x[i]);
            y0 = y[i] + (y[i+1] - y[i]) * dx;
         }
         else {
            y0 = y[x.length-1];
         }
      }
      return ( y0 );
   }


   /**
    * This method linearly interpolates the (<tt>x</tt>,<tt>y</tt>) 
    * data at points <tt>x0</tt>.  In this method, if 
    * x0 < x[0], y0 = NaN, and if x0 > x[x.length-1], 
    * y0 = NaN.
    * 
    * @param  x  Input <tt>x</tt> values
    * @param  y  Input <tt>y</tt> values
    * @param  x0 Locations to interpolate
    *
    * @return  the interpolated values
    */
   private static double interp2( double[] x, double[] y, double x0 ) {
   
      // We know data will be monotonically increasing, so don't
      // bother to check here.  Get straight to it.
      double y0;
      if ( x0 < x[0] ) {
         y0 = Double.NaN;
      }
      else if ( x0 > x[ x.length-1 ] ) {
         y0 = Double.NaN;
      }
      else {
      
         // Find the proper interval
         int i = 0;
         for ( i = 0; i < x.length-1; i++ ) {
            if ( x0 >= x[i] && x0 <= x[i+1] ) {
               break;
            }
         }
         
         // Now interpolate the value
         if ( i < x.length ) {
            double dx = (x0 - x[i])/(x[i+1] - x[i]);
            y0 = y[i] + (y[i+1] - y[i]) * dx;
         }
         else {
            y0 = y[x.length-1];
         }
      }
      return ( y0 );
   }
}
