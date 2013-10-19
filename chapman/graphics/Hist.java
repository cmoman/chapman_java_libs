package chapman.graphics;
import java.io.Serializable;
import chapman.graphics.Plot2D;
import chapman.math.Array;

/**
 * Class <code>Hist</code> calculates and (optionally) displays the histogram
 * of a user-specified data set contained in a <tt>double</tt> array.  A user
 * may optionally specify either the number of bins to include in the histogram 
 * or an array containing the centers of each bin.  If neither one is 
 * specified, a histogram is created using 10 bins.  
 * <p>
 * This is a heavyweight component suitable for use with AWT GUI components.
 * The corresponding lightweight component suitable for use with Swing GUI
 * components is {@link chapman.graphics.JHist chapman.graphics.JHist}.
 * <p>
 * Method <tt>getBinCenter()</tt> may be used to recover the center positions of
 * each bin, and method <tt>getBins()</tt> may be used to recover the count in
 * each bin.
 *  
 * @see Plot2D#Plot2D() 
 * @author  S. J. Chapman
 * @version 1.00, 02/02/99
 */

public class Hist extends Plot2D implements Cloneable, Serializable {

   //*************************************************
   // Instance Variables
   //*************************************************
   
   /**
    * The minimum value in the input data set.  
    */
   private double yMinVal;               
   
   /**
    * The maximum value in the input data set.  
    */
   private double yMaxVal;              
   
   /**
    * The number of values in each bin.  
    */
   private double bins[];            
   
   /**
    * The center of each histogram bin.  
    */
   private double binCenter[];            
  
   /**
    * The histogram bin width.  
    */
   private double binWidth;            
   
   /**
    * The number of bins 
    */
   private int nBins;            
   
   /**
    * The raw data.  
    */
   private double data[];            
   
   //*************************************************
   // Constructors
   //*************************************************
   
   /**
    * This constructor creates a new empty <tt>Hist</tt> object
    * with 10 bins and automatic bin selection. 
    */
   public Hist() {
   
      // Set up a bar plot for the histogram
      setPlotType( BAR );

      // Set default number of bins
      nBins = 10;
   }  

   /**
    * This constructor creates a new empty <tt>Hist</tt> object
    * histogram with a user-specified number of bins and automatic
    * bin selection.
    *
    * @param  nBins  Number of bins
    */
   public Hist( int nBins ) {
   
      // Set up a bar plot for the histogram
      setPlotType( BAR );

      // Set default number of bins
      this.nBins = nBins;
   }  

   /**
    * This constructor creates a new <tt>Hist</tt> object
    * histogram with 10 bins and automatic bin selection. 
    *
    * @param  x  Raw data for histogram
    */
   public Hist( double x[] ) {
   
      // Save data
      data = x;
      
      // Set up a bar plot for the histogram
      setPlotType( BAR );

      // Set default number of bins
      nBins = 10;

      // Calculate the histogram
      calcHist();
   }  

   /**
    * This constructor creates a new <tt>Hist</tt> object, creating a 
    * histogram with a user-specified number of bins and automatic bin 
    * selection. 
    *
    * @param  x      Raw data for histogram
    * @param  nBins  Number of bins
    */
   public Hist( double x[], int nBins ) {
   
      // Save data
      data = x;
      
      // Set up a bar plot for the histogram
      setPlotType( BAR );

      // Set number of bins
      this.nBins = nBins;

      // Calculate the histogram
      calcHist();
   }  

   /**
    * This constructor creates a new <tt>Hist</tt> object, creating a 
    * histogram with user-specified bin centers.
    *
    * @param  x           Raw data for histogram
    * @param  binCenters  Bin centers
    */
   public Hist( double x[], double binCenters[] ) {
   
      // Save data
      data = x;
      binCenter = binCenters;
      
      // Set up a bar plot for the histogram
      setPlotType( BAR );

      // Set number of bins
      this.nBins = binCenters.length;

      // Calculate the histogram
      calcHist2();
   }  

   //*************************************************
   // Methods
   //*************************************************
   
   /**
    * This method calculates the histogram of data set
    * with a user-specified number of bins at even 
    * spacings.
    */
   private void calcHist() {
   
      // Declare data
      int i;                   // Loop index
      int index;               // Bin index
   
      // Declare bins
      bins = new double[nBins];
      
      // Find the maximum and minimum values in the data set
      yMaxVal = Array.maxVal(data);   
      yMinVal = Array.minVal(data);
      
      // Calculate the binwidth
      binWidth = ( yMaxVal - yMinVal ) / nBins;
      
      // Calculate the locations of the bin centers
      binCenter = new double[ nBins ];
      for ( i = 0; i < bins.length; i++ ) {
         binCenter[i] = (i + 0.5) * binWidth + yMinVal;
      }

      // Put data into bins
      for ( i = 0; i < data.length; i++ ) {
         index = (int) ((data[i] - yMinVal) / binWidth);
         if ( index == nBins ) {
            index--;
         }
         bins[index]++;
      } 
      
      // Store bin data in the parent object
      setValues( binCenter, bins );
   }  

   /**
    * This method calculates the histogram of data set
    * with user-specified bin centers.  
    */
   private void calcHist2() {
   
      // Declare data
      int i, j;                // Loop index
   
      // Declare bins
      bins = new double[nBins];
      
      // Find the maximum and minimum values in the data set
      yMaxVal = Array.maxVal(data);   
      yMinVal = Array.minVal(data);
      
      // Calculate the bin boundaries
      double binBounds[] = new double[nBins-1];
      for ( i = 0; i < binBounds.length; i++ ) {
         binBounds[i] = (binCenter[i] + binCenter[i+1]) / 2;
      }
      
      // Put data into bins
      for ( i = 0; i < data.length; i++ ) {
         if ( data[i] <= binBounds[0] ) 
            bins[0]++;
         else if ( data[i] > binBounds[binBounds.length-1] ) 
            bins[bins.length-1]++;
         else {
            for ( j = 0; j < binBounds.length; j++ ) {
               if ( data[i] > binBounds[j] && data[i] <= binBounds[j+1] ) {
                  bins[j+1]++;
                  break;
               }
            }
         }
      }

      // Store bin data in the parent object
      setValues( binCenter, bins );
   }  

   /**
    * This method sets new data into the <tt>Hist</tt>
    * object, and recalculates the histogram.
    *
    * @param  x  Raw data for histogram
    */
   public void setData( double x[] ) {
   
      // Save data
      data = x;
      
      // Calculate the histogram
      calcHist();
   }  

   /**
    * This method sets a new number of bins into the <tt>Hist</tt>
    * object, and recalculates the histogram.
    *
    * @param  nBins  Number of bins
    */
   public void setNBins( int nBins ) {
   
      // Set default number of bins
      this.nBins = nBins;

      // Calculate the histogram
      calcHist();
   }  

   /**
    * This method returns the centers of each bin.
    */
   public double[] getBinCenter() {
       return binCenter;  
   }

   /**
    * This method returns the count in each bin.
    */
   public double[] getBins() {
       return bins;  
   }

   /**
    * This method returns the mode of the histogram distribution.
    */
   public double getMode() {
       int loc = Array.maxLoc( bins );
       return binCenter[loc];
   }
} 

