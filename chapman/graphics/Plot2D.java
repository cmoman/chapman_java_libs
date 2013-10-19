package chapman.graphics;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import chapman.graphics.*;
import chapman.math.*;
import chapman.io.*;
/**
 * Class <code>Plot2D</code> contains methods to plot <i>(x,y)</i> data values.  
 * The plots can be linear-linear, log-linear, linear-log, log-log, polar, 
 * or bar.  This class implements the <code>Printable</code> interface, 
 * and supports a pop-up menu to enable the plot to be sent to a printer.
 * <p>
 * This is a heavyweight component suitable for use with AWT GUI components.
 * The corresponding lightweight component suitable for use with Swing GUI
 * components is {@link chapman.graphics.JPlot2D chapman.graphics.JPlot2D}.
 * <p>
 * This class supports the input of a single <code>double</code> array, 
 * two <code>double</code> arrays, or single <code>Complex</code> array.
 * If a single <code>double</code> array is supplied, the elements of
 * that array will be plotted as a function of their array indices.  If two
 * single <code>double</code> arrays are supplied, the first array will 
 * provide the <i>x</i> coordinate and the second array will provide the
 * <i>y</i> coordinate.  If a single <code>Complex</code> array is supplied,
 * the real part of the array will be plotted on the <i>x</i> axis and the 
 * imaginary part of the array will be plotted on the <i>y</i> axis.
 * <p>
 * Each <code>Plot2D</code> object can display up to 16 separate curves.
 * The line and marker styles and colors associated with each curve can 
 * be controlled separately, with the methods <tt>setLineState</tt> and
 * <tt>setMarkerState</tt> controlling whether or not lines and markers
 * are drawn for each curve.  By default, lines are drawn between points
 * on the curve, and markers are not drawn at data points, but this 
 * behavior can be changed with the above-mentioned methods.  A user may
 * specify one of four possible line styles: 
 * <tt>LINESTYLE_SOLID</tt>, <tt>LINESTYLE_DOT</tt>,
 * <tt>LINESTYLE_LONGDASH</tt>, and <tt>LINESTYLE_SHORTDASH</tt>.  These
 * styles rotate by default on successive curves within a single plot.  
 * A user may specify one of five possible marker styles: 
 * <tt>MARKER_SQUARE</tt>, <tt>MARKER_CIRCLE</tt>, <tt>MARKER_TRIANGLE</tt>, 
 * <tt>MARKER_DIAMOND</tt>, and <tt>MARKER_DOWNSQUARE</tt>.   These
 * styles rotate by default on successive curves within a single plot.    
 * <p>
 * Features of this class include
 * <p>
 *      1.  Up to 16 curves per plot.
 * <p>
 *      2.  User-settable line colors.
 * <p>
 *      3.  User-settable line widths.
 * <p>
 *      4.  User-settable line styles.
 * <p>
 *      5.  User-settable marker styles.
 * <p>
 *      6.  Optional grid lines.
 * <p>
 *      7.  User-settable tic mark locations.
 * <p>
 *      8.  User-settable title and axis labels.
 * <p>
 *      9.  The ability to add additional annotations (except for polar plots).
 * <p>
 *      10. The ability to print the graph using a pop-up menu.
 * <p>
 * Since this class extends <tt>Canvas</tt>, it can be used with 
 * either applications or applets.
 * <p>
 * <b>Note 1:</b> If the plot type is polar, then the <i>(x,y)</i> values are 
 * interpreted as <i>(r,theta)</i>, where <i>r</i> is the radial distance from
 * the origin of the plot, and <i>theta</i> is the angle in radians, 
 * counterclockwise from the positive x-axis.  Also, in the case of polar 
 * plots, the input angles must be specified in  <i>radians</i>, but the 
 * angle labels on the plot are in <i>degrees</i>.
 *<p>
 * <b>Note 2:</b> If an <i>x</i> or <i>y</i> axis is logarithmic, then zero or
 * negative values on that axis will produce an <tt>InvalidPlotValueException</tt>.  
 *<p>
 * <b>Note 3:</b> If the plot is a bar plot, then the <i>y</i> values 
 * must all be non-negative.  A negative value will produce an 
 * <tt>InvalidPlotValueException</tt>.  
 *
 * @author  S. J. Chapman
 * @version 1.00, 11/21/98
 */

public class Plot2D extends Canvas 
       implements ActionListener, Cloneable, Printable {

   //*************************************************
   // Constants
   //*************************************************

   /**
    * Constant containing the maximum number of curves on a single
    * plot.
    */
   public final static int MAX_CURVES = 16;

   /**
    * Constant containing the maximum number of annotations on a single
    * plot.
    */
   public final static int MAX_ANNOTATIONS = 16;

   /**
    * Constant containing a Line Style descriptor for solid lines.
    */
   public final static float LINESTYLE_SOLID[] = { 6.0f, 0.0f };

   /**
    * Constant a containing Line Style descriptor for dotted lines.
    */
   public final static float LINESTYLE_DOT[]   = { 3.0f, 6.0f };

   /**
    * Constant a containing Line Style descriptor for long dashed lines.
    */
   public final static float LINESTYLE_LONGDASH[] = { 16.0f, 6.0f };

   /**
    * Constant containing a Line Style descriptor for short dashed lines.
    */
   public final static float LINESTYLE_SHORTDASH[] = { 8.0f, 6.0f };

   /**
    * Constant containing the sequence of Line Style descriptors applied to successive curves,
    * unless explicitly overridden.  The default order is LINESTYLE_SOLID, LINESTYLE_DOT,
    * LINESTYLE_LONGDASH, and LINESTYLE_SHORTDASH.
    */
   private final static float LINESTYLE[][] =
      { { 6.0f, 0.0f }, { 3.0f, 6.0f }, { 16.0f, 6.0f }, { 8.0f, 6.0f } };

   /**
    * Constant containing the sequence of Line Color descriptors applied to successive curves,
    * unless explicitly overridden.
    */
   private final static Color LINE_COLOR[] =
      {Color.blue, Color.red, Color.black, Color.orange, Color.green, Color.pink, Color.cyan};

   /**
    * Constant containing the sequence of Fill Color descriptors applied to successive curves,
    * unless explicitly overridden.  Fill colors are only used by bar plots.
    */
   private final static Color FILL_COLOR[] = 
      {Color.cyan, Color.green, Color.red, Color.yellow, Color.blue, Color.pink, Color.orange}; 
      
   /**
    * Constant defining the LINE_OFF state.
    */
   public final static boolean LINE_OFF = false;

   /**
    * Constant defining the LINE_ON state.
    */
   public final static boolean LINE_ON = true;

   /**
    * Constant to specify a square marker style.
    */
   public final static int MARKER_SQUARE = 1;

   /**
    * Constant to specify a circle marker style.
    */
   public final static int MARKER_CIRCLE = 2;

   /**
    * Constant to specify a circle marker style.
    */
   public final static int MARKER_TRIANGLE = 3;

   /**
    * Constant to specify a circle marker style.
    */
   public final static int MARKER_DIAMOND = 4;

   /**
    * Constant to specify a circle marker style.
    */
   public final static int MARKER_DOWNTRIANGLE = 5;

   /**
    * Constant to specify a circle marker style.
    */
   private final static int[] MARKER_STYLE = {MARKER_SQUARE, MARKER_CIRCLE,
                                              MARKER_TRIANGLE, MARKER_DIAMOND, 
                                              MARKER_DOWNTRIANGLE};

   /**
    * Constant containing the sequence of Marker Color descriptors applied 
    * to successive curves, unless explicitly overridden.
    */
   private final static Color MARKER_COLOR[] =
      {Color.blue, Color.red, Color.black, Color.orange, Color.green, Color.pink, Color.cyan};

   /**
    * Constant defining the MARKER_OFF state.
    */
   public final static boolean MARKER_OFF = false;

   /**
    * Constant defining the MARKER_ON state.
    */
   public final static boolean MARKER_ON = true;

   /**
    * Constant defining the GRID_ON state.
    */
   public final static boolean GRID_ON = true;

   /**
    * Constant defining the GRID_OFF state.
    */
   public final static boolean GRID_OFF = false;

   /**
    * Constant defining a linear <i>(x,y)</i> plot.  This is the default
    * plot type.
    */
   public final static int LINEAR = 0;

   /**
    * Constant defining a semilog x plot.
    */
   public final static int SEMILOGX = 1;

   /**
    * Constant defining a semilog y plot.
    */
   public final static int SEMILOGY = 2;

   /**
    * Constant defining a log-log plot.
    */
   public final static int LOGLOG = 3;

   /**
    * Constant defining a polar plot.
    */
   public final static int POLAR = 4;

   /**
    * Constant defining a vertical bar plot.
    */
   public final static int BAR = 5; 
   
   //*************************************************
   // Instance Variables
   //*************************************************

   /**
    * Current plot type.
    */
   private int plotType = 0;

   /**
    * Flag to indicate a polar plot.
    */
   private boolean polarPlot = false;

   /**
    * Flag to indicate a bar plot.
    */
   private boolean barPlot = false;

   /**
    * Flag to indicate a horizontal bar plot.
    */
   private boolean barPlotH = false;

   /**
    * Flag to indicate that the x-axis is logarithmic.
    */
   private boolean logXAxis = false;

   /**
    * Flag to indicate that the y-axis is logarithmic.
    */
   private boolean logYAxis = false;

   /**
    * Array of LineStyles for each curve.
    */
   private float lineStyle[][] = new float[MAX_CURVES][];

   /**
    * Array of LineWidths for each curve.
    */
   private float lineWidth[] = new float[MAX_CURVES];

   /**
    * Array of Marker Styles for each curve.
    */
   private int markerStyle[] = new int[MAX_CURVES];

   /**
    * Index to the "current curve" in this object.  The "current curve"
    * is the curve currently being manipulated and/or modified.
    */
   private int currentCurve = 0;

   /**
    * Count of the total number of curves defined on this plot.
    */
   private int totalCurves = 0;

   /**
    * The background color for this plot.
    */
   private Color backColor = Color.white;

   /**
    * The grid color for this plot.
    */
   private Color gridColor = Color.gray;

   /**
    * The label color for this plot.
    */
   private Color labelColor = Color.black;

   /**
    * The bounding box color for this plot.
    */
   private Color boxColor = Color.black;

   /**
    * The tic-mark color for this plot.
    */
   private Color ticColor = Color.black;

   /**
    * The line color(s) for this plot.
    */
   private Color lineColor[] = new Color[MAX_CURVES];

   /**
    * The fill color(s) for this plot.
    */
   private Color fillColor[] = new Color[MAX_CURVES];  
   
   /**
    * The marker color(s) for this plot.
    */
   private Color markerColor[] = new Color[MAX_CURVES];

   /**
    * The annotation color(s) for this plot.
    */
   private Color annotationColor[] = new Color[MAX_ANNOTATIONS];

   /**
    * The plot title string.
    */
   private String title = "Title";

   /**
    * The plot x-label string.
    */
   private String xLabel = "X label";

   /**
    * The plot y-label string.
    */
   private String yLabel = "Y label";

   /**
    * Image changed flag.  This flag tells the paint program to re-draw
    * an imagge instead of just re-displaying it.
    */
   private boolean changed = true;

   /**
    * Annotation strings.  There can be up to <code>MAX_ANNOTATIONS</code>
    * annotations.
    */
   private String annotations[] = new String[MAX_ANNOTATIONS];

   /**
    * Annotation x-position in user coordinates.  There can be up to
    * <code>MAX_ANNOTATIONS</code> annotations.
    */
   private double xAnnPos[] = new double[MAX_ANNOTATIONS];

   /**
    * Annotation y-position in user coordinates.  There can be up to
    * <code>MAX_ANNOTATIONS</code> annotations.
    */
   private double yAnnPos[] = new double[MAX_ANNOTATIONS];

   /**
    * Index to the "current annotation" in this object.  The "current annotation"
    * is the annotation currently being manipulated and/or modified.
    */
   private int currentAnnotation = 0;

   /**
    * Count of the total number of annotations defined on this plot.
    */
   private int totalAnnotations = 0;

   /**
    * Flag to indicate that a valid title is present.
    */
   private boolean validTitle = false;

   /**
    * Flag to indicate that a valid x-label is present.
    */
   private boolean validXLabel = false;

   /**
    * Flag to indicate that a valid y-label is present.
    */
   private boolean validYLabel = false;

   // String size info.  These variables are used when sizing the font
   // used to display titles and labels.  They don't need to  be externally
   // documented with javadoc.
   private BasicStroke bb;               // Bounding box style
   private BasicStroke bg;               // Grid line style
   private BasicStroke bm;               // Marker style
   private BasicStroke bs[] = new BasicStroke[MAX_CURVES]; // Line style
   private BasicStroke bt;               // Tic mark style
   private PopupMenu popup;              // Popup menu
   private MenuItem printCanvas, printCanvasDialog; // "Print" item
   private BufferedImage offImg;         // Buffer to build up off-screen image
   private BufferedImage offImg1;        // Saved buffer
   private int maxCharHeight;            // Maximum character height (pixels)
   private int maxStringLength;          // Maximum string length (pixels)
   private int minFontSize = 8;          // Minimum font size (points)
   private FontMetrics fontMetrics;      // Font info
   private int fontSize;                 // Font size
   private int fontStyle;                // Font style
   private String fontName;              // Font name
   private int stringWidth;              // Width of string in pixels
   private int stringHeight;             // Height of string in pixels


   /**
    * This variable contains the size of the "user" area in <i>pixels</i>.  The
    * "user" area is the total area of the panel, canvas, etc. in which
    * this plot is being created.  It uses the conventional Java system
    * in which the upper left-hand corner is pixel (0,0).  Note that
    * <tt>plotSize</tt>, the actual plotting area, is less than or equal to
    * the size of "user" area.
    */
   private Dimension size;               // Size of "user" area (pixels)

   /**
    * This variable contains the size of the "plot" area in  <i>pixels</i>pixels.  The
    * "plot" area is the subset of the "user" area actually used to draw
    * curves.  The plot area is usually offset from upper left-hand corner
    * of the "user" area, so variables <code>plotStartX</code> and
    * <code>plotStartY</code> specify the origin of the "plot" area.
    */
   private Dimension plotSize = new Dimension(0,0);
                                         // Size of "plot" area (pixels)

   /**
    * The x-origin of the "plot" area within the "user" area in units of
    * <i>pixels</i>pixels.
    */
   private int plotStartX;               // x-origin of "plot" area

   /**
    * The t-origin of the "plot" area within the "user" area in units of
    * <i>pixels</i>pixels.
    */
   private int plotStartY;               // y-origin of "plot" area

   /**
    * The x-origin of the "bar plot" area within the "user" area in units of 
    * <i>pixels</i>pixels.  
    */
   private int barStartX;                // x-origin of bar chart area 
   
   /**
    * The left-hand margin of the "plot" area as a <i>fraction</i> of the total
    * width of the "user" area.
    */
   private float xLeftMargin = 0.125f;

   /**
    * The right-hand margin of the "plot" area as a <i>fraction</i> of the total
    * width of the "user" area.
    */
   private float xRightMargin = 0.045f;

   /**
    * The top margin of the "plot" area as a <i>fraction</i> of the total
    * height of the "user" area.
    */
   private float yTopMargin = 0.075f;

   /**
    * The bottom margin of the "plot" area as a <i>fraction</i> of the total
    * height of the "user" area.
    */
   private float yBottomMargin = 0.075f;

   /**
    * This <code>boolean</code> specifies whether the range of user-defined
    * x values to be mapped into the "plot" area is to be determined
    * automatically from the supplied data or manually by the user.  If
    * <tt>autoXScale</tt> is <code>true</code>, then the range is calculated
    * automatically.  The user-defined coordinates to plot
    * are specifed by the bounds <tt>xMax</tt>, <tt>xMin</tt>,
    * <tt>yMax</tt>, and <tt>yMin</tt>, plus the scale factors
    * <tt>xScale</tt> and <tt>yScale</tt>.  The <code>boolean</code>s
    * <tt>autoXScale</tt> and <tt>autoYScale</tt> control whether
    * the bounds and scale are calculated automtically (the default)
    * or specified manually by the user.
    */
   private boolean autoXScale = true;    // Autoscale mode flag

   /**
    * This <code>boolean</code> specifies whether the range of user-defined
    * y values to be mapped into the "plot" area is to be determined
    * automatically from the supplied data or manually by the user.  If
    * <tt>autoXScale</tt> is <code>true</code>, then the range is calculated
    * automatically.  The user-defined coordinates to plot
    * are specifed by the bounds <tt>xMax</tt>, <tt>xMin</tt>,
    * <tt>yMax</tt>, and <tt>yMin</tt>, plus the scale factors
    * <tt>xScale</tt> and <tt>yScale</tt>.  The <code>boolean</code>s
    * <tt>autoXScale</tt> and <tt>autoYScale</tt> control whether
    * the bounds and scale are calculated automtically (the default)
    * or specified manually by the user.
    */
   private boolean autoYScale = true;    // Autoscale mode flag

   /**
    * This variable contains the maximum user-defined x value to map into
    * pixels within the "plot" area.  The user-defined coordinates to plot
    * are specifed by the bounds <tt>xMax</tt>, <tt>xMin</tt>,
    * <tt>yMax</tt>, and <tt>yMin</tt>, plus the scale factors
    * <tt>xScale</tt> and <tt>yScale</tt>.  The <code>boolean</code>s
    * <tt>autoXScale</tt> and <tt>autoYScale</tt> control whether
    * the bounds and scale are calculated automtically (the default)
    * or specified manually by the user.
    */
   private double xMax = 0;

   /**
    * This variable contains the logarithm of the maximum user-defined x
    * value to map into pixels within the "plot" area.
    */
   private double xMaxLog = 0;

   /**
    * This variable contains the minimum user-defined x value to map into
    * pixels within the "plot" area.  The user-defined coordinates to plot
    * are specifed by the bounds <tt>xMax</tt>, <tt>xMin</tt>,
    * <tt>yMax</tt>, and <tt>yMin</tt>, plus the scale factors
    * <tt>xScale</tt> and <tt>yScale</tt>.  The <code>boolean</code>s
    * <tt>autoXScale</tt> and <tt>autoYScale</tt> control whether
    * the bounds and scale are calculated automtically (the default)
    * or specified manually by the user.
    */
   private double xMin = 0;

   /**
    * This variable contains the logarithm of the minimum user-defined x
    * value to map into pixels within the "plot" area.
    */
   private double xMinLog = 0;

   /**
    * This variable contains the maximum user-defined y value to map into
    * pixels within the "plot" area.  The user-defined coordinates to plot
    * are specifed by the bounds <tt>xMax</tt>, <tt>xMin</tt>,
    * <tt>yMax</tt>, and <tt>yMin</tt>, plus the scale factors
    * <tt>xScale</tt> and <tt>yScale</tt>.  The <code>boolean</code>s
    * <tt>autoXScale</tt> and <tt>autoYScale</tt> control whether
    * the bounds and scale are calculated automtically (the default)
    * or specified manually by the user.
    */
   private double yMax = 0;

   /**
    * This variable contains the logarithm of the maximum user-defined y
    * value to map into pixels within the "plot" area.
    */
   private double yMaxLog = 0;

   /**
    * This variable contains the minimum user-defined y value to map into
    * pixels within the "plot" area.  The user-defined coordinates to plot
    * are specifed by the bounds <tt>xMax</tt>, <tt>xMin</tt>,
    * <tt>yMax</tt>, and <tt>yMin</tt>, plus the scale factors
    * <tt>xScale</tt> and <tt>yScale</tt>.  The <code>boolean</code>s
    * <tt>autoXScale</tt> and <tt>autoYScale</tt> control whether
    * the bounds and scale are calculated automtically (the default)
    * or specified manually by the user.
    */
   private double yMin = 0;

   /**
    * This variable contains the logarithm of the minimum user-defined y
    * value to map into pixels within the "plot" area.
    */
   private double yMinLog = 0;

   /**
    * This variable contains the scale factor for mapping the
    * user-defined x-coordinates of the supplied curve(s) into pixels
    * within the "plot" area.  The user-defined coordinates to plot
    * are specifed by the bounds <tt>xMax</tt>, <tt>xMin</tt>,
    * <tt>yMax</tt>, and <tt>yMin</tt>, plus the scale factors
    * <tt>xScale</tt> and <tt>yScale</tt>.  The <code>boolean</code>s
    * <tt>autoXScale</tt> and <tt>autoYScale</tt> control whether
    * the bounds and scale are calculated automtically (the default)
    * or specified manually by the user.
    */
   private double xScale;

   /**
    * This variable contains the scale factor for mapping the
    * user-defined y-coordinates of the supplied curve(s) into pixels
    * within the "plot" area.  The user-defined coordinates to plot
    * are specifed by the bounds <tt>xMax</tt>, <tt>xMin</tt>,
    * <tt>yMax</tt>, and <tt>yMin</tt>, plus the scale factors
    * <tt>xScale</tt> and <tt>yScale</tt>.  The <code>boolean</code>s
    * <tt>autoXScale</tt> and <tt>autoYScale</tt> control whether
    * the bounds and scale are calculated automtically (the default)
    * or specified manually by the user.
    */
   private double yScale;

   /**
    * This <code>boolean</code> specifies whether x-axis tic-mark locations
    * are to be calculated automatically or manually.  If
    * <tt>autoXTicMode</tt> is <code>true</code>, then the locations are
    * calculated automatically.
    */
   private boolean autoXTicMode = true;

   /**
    * This <code>boolean</code> specifies whether y-axis tic-mark locations
    * are to be calculated automatically or manually.  If
    * <tt>autoYTicMode</tt> is <code>true</code>, then the locations are
    * calculated automatically.
    */
   private boolean autoYTicMode = true;

   /**
    * This <code>boolean</code> specifies whether a line is to plotted or
    * not for a particular curve.
    */
   private boolean plotLine[] = new boolean[MAX_CURVES];

   /**
    * This <code>boolean</code> specifies whether a marker is to plotted or
    * not for a particular curve.
    */
   private boolean plotMarker[] = new boolean[MAX_CURVES];

   /**
    * This <code>boolean</code> specifies whether x-axis grid lines
    * are to be plotted or not.  If <tt>xGridLines</tt> is <code>true</code>,
    * then x-axis grid lines are plotted at the locations of each x-axis
    * tic mark.
    */
   private boolean xGridLines = false;

   /**
    * This <code>boolean</code> specifies whether y-axis grid lines
    * are to be plotted or not.  If <tt>xGridLines</tt> is <code>true</code>,
    * then y-axis grid lines are plotted at the locations of each y-axis
    * tic mark.
    */
   private boolean yGridLines = false;   // Plot grid lines flag

   /**
    * This <code>boolean</code> specifies whether x-axis tic marks
    * are to be plotted or not.  If <tt>xTicMarks</tt> is <code>true</code>,
    * then x-axis tic marks are plotted at the locations specified by
    * array <tt>xTic</tt>.
    */
   private boolean xTicMarks = true;

   /**
    * This <code>boolean</code> specifies whether y-axis tic marks
    * are to be plotted or not.  If <tt>yTicMarks</tt> is <code>true</code>,
    * then y-axis tic marks are plotted at the locations specified by
    * array <tt>yTic</tt>.
    */
   private boolean yTicMarks = true;

   /**
    * This array specifies the locations of x-axis tic marks in user-defined
    * coordinates.
    */
   private double xTic[];

   /**
    * This array specifies the locations of the log to the base 10 of x-axis
    * tic marks in user-defined coordinates.
    */
   private double xTicLog[];

   /**
    * This array character strings corresponding to the values of x-axis
    * tic marks in user-defined coordinates.
    */
   private String xTicS[];

   /**
    * This array specifies the locations of y-axis tic marks in user-defined
    * coordinates.
    */
   private double yTic[];

   /**
    * This array specifies the locations of the log to the base 10 of y-axis
    * tic marks in user-defined coordinates.
    */
   private double yTicLog[];

   /**
    * This array character strings corresponding to the values of y-axis
    * tic marks in user-defined coordinates.
    */
   private String yTicS[];

   /**
    * This array contains the x-values of the curves to plot, in user-defined
    * coordinates.
    */
   private double xVal[][] = new double[MAX_CURVES][];

   /**
    * This array contains the y-values of the curves to plot, in user-defined
    * coordinates.
    */
   private double yVal[][] = new double[MAX_CURVES][];

   //*************************************************
   // Constructors
   //*************************************************

   /**
    * This constructor creates a new <tt>Plot2D</tt> object without initializing
    * the data to be plotted.
    *
    * @see  Plot2D#Plot2D(double[])
    * @see  Plot2D#Plot2D(double[], int)
    * @see  Plot2D#Plot2D(double[],double[])
    * @see  Plot2D#Plot2D(double[],double[],int)
    * @see  Plot2D#Plot2D(Complex[])
    */
   public Plot2D() {

      // Set data to plot
      xVal[0] = null;
      yVal[0] = null;

      // Set the background color
      setBackground(backColor);

      // Set the current curve and the number of curves
      currentCurve = 0;
      totalCurves = 0;

      // Set default linestyle, color, and width
      lineStyle[currentCurve] = LINESTYLE[0];
      lineColor[currentCurve] = LINE_COLOR[0];
      lineWidth[currentCurve] = 1.0f;
      fillColor[currentCurve] = FILL_COLOR[0];
      markerColor[currentCurve] = MARKER_COLOR[0];
      markerStyle[currentCurve] = MARKER_STYLE[0];

      // Set line and marker status
      plotLine[currentCurve] = true;
      plotMarker[currentCurve] = false;
      
      // Set image changed flag
      changed = true;

      // Create line style
      bs[currentCurve] = new BasicStroke( lineWidth[currentCurve],
                         BasicStroke.CAP_SQUARE,
                         BasicStroke.JOIN_MITER,
                         6.0f, lineStyle[currentCurve], 0 );   

      // Create bounding box style.
      float boxStyle[] = {12.0f, 0.0f};
      bb = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, boxStyle, 0 );

      // Create grid line style.
      float gridStyle[] = {3.0f, 6.0f};
      bg = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            1.0f, gridStyle, 0 );

      // Create tic mark stype
      float ticStyle[] = {12.0f, 0.0f};
      bt = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, ticStyle, 0 );

      // Add popup menu for printing
      popup = new PopupMenu( "Options" );
      printCanvas = new MenuItem("Print");
      printCanvas.addActionListener( this );
      printCanvasDialog = new MenuItem("Print with Dialog");
      printCanvasDialog.addActionListener( this );
      popup.add( printCanvas );
      popup.add( printCanvasDialog );
      add( popup );
      enableEvents( AWTEvent.MOUSE_EVENT_MASK );
    }

   /**
    * This constructor creates a new <tt>Plot2D</tt> object with only one
    * input array.  This array will be plotted on the y-axis, with array
    * index number on the x-axis.
    *
    * @param  y  Array of y-values to plot
    * @see  Plot2D#Plot2D()
    * @see  Plot2D#Plot2D(double[], int)
    * @see  Plot2D#Plot2D(double[],double[])
    * @see  Plot2D#Plot2D(double[],double[],int)
    * @see  Plot2D#Plot2D(Complex[])
    */
   public Plot2D ( double y[] ) {

      // Set up x-axis.
      double x[] = new double[y.length];
      for ( int i=0; i < y.length; i++ )
         x[i] = i;

      // Set data to plot
      xVal[0] = x;
      yVal[0] = y;

      // Set the background color
      setBackground(backColor);

      // Set the current curve and the number of curves
      currentCurve = 0;
      totalCurves = 1;

      // Set default linestyle, color, and width
      lineStyle[currentCurve] = LINESTYLE[0];
      lineColor[currentCurve] = LINE_COLOR[0];
      lineWidth[currentCurve] = 1.0f;
      fillColor[currentCurve] = FILL_COLOR[0];
      markerColor[currentCurve] = MARKER_COLOR[0];
      markerStyle[currentCurve] = MARKER_STYLE[0];

      // Set line and marker status
      plotLine[currentCurve] = true;
      plotMarker[currentCurve] = false;

      // Set image changed flag
      changed = true;

      // Create line style
      bs[currentCurve] = new BasicStroke( lineWidth[currentCurve],
                         BasicStroke.CAP_SQUARE,
                         BasicStroke.JOIN_MITER,
                         6.0f, lineStyle[currentCurve], 0 );   

      // Create bounding box style.
      float boxStyle[] = {12.0f, 0.0f};
      bb = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, boxStyle, 0 );

      // Create grid line style.
      float gridStyle[] = {3.0f, 6.0f};
      bg = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            1.0f, gridStyle, 0 );

      // Create tic mark stype
      float ticStyle[] = {12.0f, 0.0f};
      bt = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, ticStyle, 0 );

      // Add popup menu for printing
      popup = new PopupMenu( "Options" );
      printCanvas = new MenuItem("Print");
      printCanvas.addActionListener( this );
      printCanvasDialog = new MenuItem("Print with Dialog");
      printCanvasDialog.addActionListener( this );
      popup.add( printCanvas );
      popup.add( printCanvasDialog );
      add( popup );
      enableEvents( AWTEvent.MOUSE_EVENT_MASK );
   }


   /**
    * This constructor creates a new <tt>Plot2D</tt> object with only one
    * input array.  This array will be plotted on the y-axis, with array
    * index number on the x-axis.  The parameter <tt>nvals</tt> specifies 
    * the number of values from the array to plot.
    *
    * @param  y      Array of y-values to plot
    * @param  nvals  Number of values to plot
    * @see  Plot2D#Plot2D()
    * @see  Plot2D#Plot2D(double[])
    * @see  Plot2D#Plot2D(double[],double[])
    * @see  Plot2D#Plot2D(double[],double[],int)
    * @see  Plot2D#Plot2D(Complex[])
    */
   public Plot2D ( double y[], int nvals ) {

      // Check for incompatible array sizes
      if ( y.length < nvals ) {
         String s = "Insufficient y data: y.length = " + y.length
                  + "; required = " + nvals;
         throw new InvalidArraySizeException(s);
      }

      // Set up data.
      double x1[] = new double[nvals];
      double y1[] = new double[nvals];
      for ( int i=0; i < nvals; i++ ) {
         x1[i] = i;
         y1[i] = y[i];
      }

      // Set data to plot
      xVal[0] = x1;
      yVal[0] = y1;

      // Set the background color
      setBackground(backColor);

      // Set the current curve and the number of curves
      currentCurve = 0;
      totalCurves = 1;

      // Set default linestyle, color, and width
      lineStyle[currentCurve] = LINESTYLE[0];
      lineColor[currentCurve] = LINE_COLOR[0];
      lineWidth[currentCurve] = 1.0f;
      fillColor[currentCurve] = FILL_COLOR[0];
      markerColor[currentCurve] = MARKER_COLOR[0];
      markerStyle[currentCurve] = MARKER_STYLE[0];

      // Set line and marker status
      plotLine[currentCurve] = true;
      plotMarker[currentCurve] = false;

      // Set image changed flag
      changed = true;

      // Create line style
      bs[currentCurve] = new BasicStroke( lineWidth[currentCurve],
                         BasicStroke.CAP_SQUARE,
                         BasicStroke.JOIN_MITER,
                         6.0f, lineStyle[currentCurve], 0 );   

      // Create bounding box style.
      float boxStyle[] = {12.0f, 0.0f};
      bb = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, boxStyle, 0 );

      // Create grid line style.
      float gridStyle[] = {3.0f, 6.0f};
      bg = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            1.0f, gridStyle, 0 );

      // Create tic mark stype
      float ticStyle[] = {12.0f, 0.0f};
      bt = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, ticStyle, 0 );

      // Add popup menu for printing
      popup = new PopupMenu( "Options" );
      printCanvas = new MenuItem("Print");
      printCanvas.addActionListener( this );
      printCanvasDialog = new MenuItem("Print with Dialog");
      printCanvasDialog.addActionListener( this );
      popup.add( printCanvas );
      popup.add( printCanvasDialog );
      add( popup );
      enableEvents( AWTEvent.MOUSE_EVENT_MASK );
   }


   /**
    * This constructor creates a new <tt>Plot2D</tt> object, initializing
    * the data for the first curve to be plotted.  Note that the lengths of
    * arrays <code>x</code> and <code>y</code> must be the same.
    *
    * @param  x  Array of x-values to plot
    * @param  y  Array of y-values to plot
    * @see  Plot2D#Plot2D()
    * @see  Plot2D#Plot2D(double[])
    * @see  Plot2D#Plot2D(double[], int)
    * @see  Plot2D#Plot2D(double[],double[],int)
    * @see  Plot2D#Plot2D(Complex[])
    */
   public Plot2D ( double x[], double y[] ) {

      // Check for incompatible array sizes
      if ( x.length != y.length ) {
         throw new InvalidArraySizeException ("x and y must be equal in length!");
      }

      // Set data to plot
      xVal[0] = x;
      yVal[0] = y;

      // Set the background color
      setBackground(backColor);

      // Set the current curve and the number of curves
      currentCurve = 0;
      totalCurves = 1;

      // Set default linestyle, color, and width
      lineStyle[currentCurve] = LINESTYLE[0];
      lineColor[currentCurve] = LINE_COLOR[0];
      lineWidth[currentCurve] = 1.0f;
      fillColor[currentCurve] = FILL_COLOR[0];
      markerColor[currentCurve] = MARKER_COLOR[0];
      markerStyle[currentCurve] = MARKER_STYLE[0];

      // Set line and marker status
      plotLine[currentCurve] = true;
      plotMarker[currentCurve] = false;

      // Set image changed flag
      changed = true;

      // Create line style
      bs[currentCurve] = new BasicStroke( lineWidth[currentCurve],
                         BasicStroke.CAP_SQUARE,
                         BasicStroke.JOIN_MITER,
                         6.0f, lineStyle[currentCurve], 0 );   

      // Create bounding box style.
      float boxStyle[] = {12.0f, 0.0f};
      bb = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, boxStyle, 0 );

      // Create grid line style.
      float gridStyle[] = {3.0f, 6.0f};
      bg = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            1.0f, gridStyle, 0 );

      // Create tic mark style
      float ticStyle[] = {12.0f, 0.0f};
      bt = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, ticStyle, 0 );

      // Add popup menu for printing
      popup = new PopupMenu( "Options" );
      printCanvas = new MenuItem("Print");
      printCanvas.addActionListener( this );
      printCanvasDialog = new MenuItem("Print with Dialog");
      printCanvasDialog.addActionListener( this );
      popup.add( printCanvas );
      popup.add( printCanvasDialog );
      add( popup );
      enableEvents( AWTEvent.MOUSE_EVENT_MASK );
   }


   /**
    * This constructor creates a new <tt>Plot2D</tt> object, initializing
    * the data for the first curve to be plotted.  Note that the lengths of
    * arrays <code>x</code> and <code>y</code> must be the same.  The
    * parameter <tt>nvals</tt> specifies the number of values from the
    * arrays to plot.
    *
    * @param  x      Array of x-values to plot
    * @param  y      Array of y-values to plot
    * @param  nvals  Number of values to plot
    * @see  Plot2D#Plot2D()
    * @see  Plot2D#Plot2D(double[])
    * @see  Plot2D#Plot2D(double[], int)
    * @see  Plot2D#Plot2D(double[],double[])
    * @see  Plot2D#Plot2D(Complex[])
    */
   public Plot2D ( double x[], double y[], int nvals ) {

      // Check for incompatible array sizes
      if ( x.length < nvals ) {
         String s = "Insufficient x data: x.length = " + x.length
                  + "; required = " + nvals;
         throw new InvalidArraySizeException(s);
      }
      if ( y.length < nvals ) {
         String s = "Insufficient y data: y.length = " + y.length
                  + "; required = " + nvals;
         throw new InvalidArraySizeException(s);
      }

      // Set up data.
      double x1[] = new double[nvals];
      double y1[] = new double[nvals];
      for ( int i=0; i < nvals; i++ ) {
         x1[i] = x[i];
         y1[i] = y[i];
      }

      // Set data to plot
      xVal[0] = x1;
      yVal[0] = y1;

      // Set the background color
      setBackground(backColor);

      // Set the current curve and the number of curves
      currentCurve = 0;
      totalCurves = 1;

      // Set default linestyle, color, and width
      lineStyle[currentCurve] = LINESTYLE[0];
      lineColor[currentCurve] = LINE_COLOR[0];
      lineWidth[currentCurve] = 1.0f;
      fillColor[currentCurve] = FILL_COLOR[0];
      markerColor[currentCurve] = MARKER_COLOR[0];
      markerStyle[currentCurve] = MARKER_STYLE[0];

      // Set line and marker status
      plotLine[currentCurve] = true;
      plotMarker[currentCurve] = false;

      // Set image changed flag
      changed = true;

      // Create line style
      bs[currentCurve] = new BasicStroke( lineWidth[currentCurve],
                         BasicStroke.CAP_SQUARE,
                         BasicStroke.JOIN_MITER,
                         6.0f, lineStyle[currentCurve], 0 );   

      // Create bounding box style.
      float boxStyle[] = {12.0f, 0.0f};
      bb = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, boxStyle, 0 );

      // Create grid line style.
      float gridStyle[] = {3.0f, 6.0f};
      bg = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            1.0f, gridStyle, 0 );

      // Create tic mark style
      float ticStyle[] = {12.0f, 0.0f};
      bt = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, ticStyle, 0 );

      // Add popup menu for printing
      popup = new PopupMenu( "Options" );
      printCanvas = new MenuItem("Print");
      printCanvas.addActionListener( this );
      printCanvasDialog = new MenuItem("Print with Dialog");
      printCanvasDialog.addActionListener( this );
      popup.add( printCanvas );
      popup.add( printCanvasDialog );
      add( popup );
      enableEvents( AWTEvent.MOUSE_EVENT_MASK );
   }


   /**
    * This constructor creates a new <tt>Plot2D</tt> object, initializing
    * the data for the first curve to be plotted.  This version of the 
    * constructor accepts a <code>Complex</code> array, and plots the
    * real part of the array versus the imaginary part of the array. 
    *
    * @param  c  Array of <code>Complex</code>values to plot
    * @see  Plot2D#Plot2D()
    * @see  Plot2D#Plot2D(double[])
    * @see  Plot2D#Plot2D(double[], int)
    * @see  Plot2D#Plot2D(double[],double[])
    * @see  Plot2D#Plot2D(double[],double[],int)
    */
   public Plot2D ( Complex c[] ) {

      // Set data to plot
      double x[] = new double[c.length];
      double y[] = new double[c.length];
      for ( int i = 0; i < c.length; i++ ) {
         x[i] = c[i].re();
         y[i] = c[i].im();
      }
      xVal[0] = x;
      yVal[0] = y;

      // Set the background color
      setBackground(backColor);

      // Set the current curve and the number of curves
      currentCurve = 0;
      totalCurves = 1;

      // Set default linestyle, color, and width
      lineStyle[currentCurve] = LINESTYLE[0];
      lineColor[currentCurve] = LINE_COLOR[0];
      lineWidth[currentCurve] = 1.0f;
      fillColor[currentCurve] = FILL_COLOR[0];
      markerColor[currentCurve] = MARKER_COLOR[0];
      markerStyle[currentCurve] = MARKER_STYLE[0];

      // Set line and marker status
      plotLine[currentCurve] = true;
      plotMarker[currentCurve] = false;

      // Set image changed flag
      changed = true;

      // Create line style
      bs[currentCurve] = new BasicStroke( lineWidth[currentCurve],
                         BasicStroke.CAP_SQUARE,
                         BasicStroke.JOIN_MITER,
                         6.0f, lineStyle[currentCurve], 0 );   

      // Create bounding box style.
      float boxStyle[] = {12.0f, 0.0f};
      bb = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, boxStyle, 0 );

      // Create grid line style.
      float gridStyle[] = {3.0f, 6.0f};
      bg = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            1.0f, gridStyle, 0 );

      // Create tic mark style
      float ticStyle[] = {12.0f, 0.0f};
      bt = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, ticStyle, 0 );

      // Add popup menu for printing
      popup = new PopupMenu( "Options" );
      printCanvas = new MenuItem("Print");
      printCanvas.addActionListener( this );
      printCanvasDialog = new MenuItem("Print with Dialog");
      printCanvasDialog.addActionListener( this );
      popup.add( printCanvas );
      popup.add( printCanvasDialog );
      add( popup );
      enableEvents( AWTEvent.MOUSE_EVENT_MASK );
   }


   /**
    * This method creates an off-screen buffer to 
    * double-buffer the graphics image.
    *
    * @param  g  A graphics object.
    */
    private Graphics2D createGraphics2D(Graphics g) {
    
       Graphics2D g2 = null;

       // Get current screen size
       size = getSize();

       // Create new off-screen image if necessary.
       if (offImg == null || offImg.getWidth() != size.width ||
                             offImg.getHeight() != size.height) {
          offImg = (BufferedImage) createImage(size.width, size.height);
          changed = true;
       } 

       // Create a graphics context and update graphics only
       // if changes have occurred, as signaled by the "change"
       // flag.  Otherwise, we will just re-display the current
       // off-screen buffer.
       if ( changed ) {
       
          // Create a graphics context for this image
          if (offImg != null) {
              g2 = offImg.createGraphics();
              g2.setBackground(getBackground());
          }

          // Set rendering hints to improve display quality
          g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                              RenderingHints.VALUE_RENDER_QUALITY);
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                              RenderingHints.VALUE_ANTIALIAS_ON);

          // Clear canvas  
          g2.clearRect(0, 0, size.width, size.height);
       }

       return g2;
   }


   //*************************************************
   // Paint method, which actually creates the plot
   //*************************************************

   /**
    * This method creates the 2D plot.
    *
    * @param  g  A graphics object.
    */
   public void paint(Graphics g) {

      // Only paint if any data is available
      if ( xVal[0] != null ) {
      
         // Create an off-screen graphics2D object
         Graphics2D g2 = createGraphics2D(g);

         // Redraw the image before displaying it if
         // anything has changed.  Otherwise, just
         // re-display it.
         if ( changed ) {

            // Select the type of curve to plot, and
            // build up off-screen image
            if ( polarPlot )
               polar(g2);
            else if ( barPlot ) 
               bar(g2);
            else if ( logXAxis && logYAxis )
               loglog(g2);
            else if ( logXAxis && !logYAxis )
               semilogx(g2);
            else if ( !logXAxis && logYAxis )
               semilogy(g2);
            else if ( !logXAxis && !logYAxis )
               linlin(g2);
               
            // Clear changed flag
            changed = false;         

            // Dispose of graphics context now that
            // we have finished with it
            g2.dispose();
         }

         // Show new image, or re-display old image
         if (offImg != null && isShowing())  {
            g.drawImage(offImg, 0, 0, this);
         }
      }
   }

   //*************************************************
   // Print method, which prints the plot
   //*************************************************

   /**
    * This method creates a hardcopy of the plot on a printer.
    *
    * @param  g   A graphics object 
    * @param  pf  The size and orientation of the page being drawn
    * @param  pi  Page number (starting with 0) to print 
    */
   public int print(Graphics g, PageFormat pf, int pi) {
   
      // Only one page available
      if ( pi >= 1 ) 
         return Printable.NO_SUCH_PAGE;
      
      // Cast to Graphics2D
      Graphics2D g2 = (Graphics2D) g;
      
      // Set rendering hints 
      g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                          RenderingHints.VALUE_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                          RenderingHints.VALUE_ANTIALIAS_ON);

      // Translate printer graphics context
      g2.translate( pf.getImageableX(), pf.getImageableY() );
      paint(g2);
      return Printable.PAGE_EXISTS;
   }

   //*************************************************
   // Support methods
   //*************************************************

   /**
    * This method sets the plot type.  The possible plot types are Plot2D.LINEAR,
    * Plot2D.SEMILOGX, Plot2D.SEMILOGY, Plot2D.LOGLOG, and Plot2D.POLAR.  
    * The default plot type is a linear <i>(x,y)</i> plot.
    *
    * @param  type  Plot type
    *
    * @see  Plot2D#getPlotType()
    */
   public void setPlotType ( int type ) {
      if ( ( type < Plot2D.LINEAR ) || ( type > Plot2D.BAR ) ) {
         String s = "Invalid plot type: " + type;
         throw new IndexOutOfBoundsException(s);
      }
      else if ( type == Plot2D.LINEAR ) {
         plotType  = type;
         logXAxis  = false;
         logYAxis  = false;
         polarPlot = false;
         barPlot   = false;
         barPlotH  = false;
         changed   = true;
      }
      else if ( type == Plot2D.SEMILOGX ) {
         plotType  = type;
         logXAxis  = true;
         logYAxis  = false;
         polarPlot = false;
         barPlot   = false;
         barPlotH  = false;
         changed   = true;
      }
      else if ( type == Plot2D.SEMILOGY ) {
         plotType  = type;
         logXAxis  = false;
         logYAxis  = true;
         polarPlot = false;
         barPlot   = false;
         barPlotH  = false;
         changed   = true;
      }
      else if ( type == Plot2D.LOGLOG ) {
         plotType  = type;
         logXAxis  = true;
         logYAxis  = true;
         polarPlot = false;
         barPlot   = false;
         barPlotH  = false;
         changed   = true;
      }
      else if ( type == Plot2D.POLAR ) {
         plotType  = type;
         logXAxis  = false;
         logYAxis  = false;
         polarPlot = true;
         barPlot   = false;
         barPlotH  = false;
         changed   = true;
      }
      else if ( type == Plot2D.BAR ) {
         plotType  = type;
         logXAxis  = false;
         logYAxis  = false;
         polarPlot = false;
         barPlot   = true;
         barPlotH  = false;
         changed   = true;
         
         // Specify solid borders around bars
         for ( int i = 0; i < totalCurves; i++ ) 
           lineStyle[i] = LINESTYLE_SOLID;
      }
   }

   /**
    * This method gets the current plot type.  The possible plot types
    * are Plot2D.LINEAR, Plot2D.SEMILOGX, Plot2D.SEMILOGY, Plot2D.LOGLOG,
    * and Plot2D.POLAR.
    *
    * @return an <tt>int</tt> containing the current plot type.
    *
    * @see  Plot2D#setPlotType(int)
    */
   public int getPlotType ( ) {
      return plotType;
   }


   /**
    * This method updates the <code>x</code> and <code>y</code> 
    * values for the current curve.
    *
    * @param  x  Array of x-values to plot
    * @param  y  Array of y-values to plot
    * @see  Plot2D#setValues(double[],double[],int)
    * @see  Plot2D#setValues(Complex[])
    * @see  Plot2D#setValues(Complex[],int)
    */
   public void setValues ( double x[], double y[] ) {

      // Check for incompatible array sizes
      if ( x.length != y.length ) {
         throw new InvalidArraySizeException ("x and y must be equal in length!");
      }

      else {

         // Add the new values
         totalCurves = Math.max(totalCurves, 1);
         xVal[currentCurve] = x;
         yVal[currentCurve] = y;
         changed            = true;
     }
   }


   /**
    * This method updates the <code>x</code> and <code>y</code> 
    * values for the specified curve.
    *
    * @param  x  Array of x-values to plot
    * @param  y  Array of y-values to plot
    * @see  Plot2D#setValues(double[],double[])
    * @see  Plot2D#setValues(Complex[])
    * @see  Plot2D#setValues(Complex[],int)
    */
   public void setValues ( double x[], double y[], int curve ) {

      // Check for incompatible array sizes
      if ( x.length != y.length ) {
         throw new InvalidArraySizeException ("x and y must be equal in length!");
      }

      else {

         // Add the new values
         totalCurves = Math.max(totalCurves, 1);
         xVal[curve] = x;
         yVal[curve] = y;
         changed     = true;
     }
   }


   /**
    * This method updates the <code>Complex</code> 
    * values for the current curve.
    *
    * @param  c  Array of <code>Complex</code> values to plot
    * @see  Plot2D#setValues(double[],double[])
    * @see  Plot2D#setValues(double[],double[],int)
    * @see  Plot2D#setValues(Complex[],int)
    */
   public void setValues ( Complex c[] ) {

      // Set data to plot
      double x[] = new double[c.length];
      double y[] = new double[c.length];
      for ( int i = 0; i < c.length; i++ ) {
         x[i] = c[i].re();
         y[i] = c[i].im();
      }

      // Add the new values
      totalCurves = Math.max(totalCurves, 1);
      xVal[currentCurve] = x;
      yVal[currentCurve] = y;
      changed            = true;
      
      // Repaint image
      repaint();
   }


   /**
    * This method updates the <code>Complex</code> 
    * values for the specified curve.
    *
    * @param  c  Array of <code>Complex</code> values to plot
    * @see  Plot2D#setValues(double[],double[])
    * @see  Plot2D#setValues(double[],double[],int)
    * @see  Plot2D#setValues(Complex[])
    */
   public void setValues ( Complex c[], int curve ) {

      // Set data to plot
      double x[] = new double[c.length];
      double y[] = new double[c.length];
      for ( int i = 0; i < c.length; i++ ) {
         x[i] = c[i].re();
         y[i] = c[i].im();
      }

      // Add the new values
      totalCurves = Math.max(totalCurves, 1);
      xVal[curve] = x;
      yVal[curve] = y;
      changed     = true;
   }


   /**
    * This method adds an additional curve to a previously-created plot.
    * This method differs from <tt>setValues</tt> in that this
    * method <i>adds a <b>new</b> curve</i>, while <tt>setValues</tt>
    * <i>modifies the data values in the <b>current</b> curve</i>.
    * <p>
    * The new curve is created with a line style and color
    * selected from the default tables, and with a line width of 1
    * pixel.  These values may be modified by the user using methods
    * <tt>setLineStyle</tt>, <tt>setLineColor</tt>, and <tt>setLineWidth</tt>.
    *
    * @param  y  Array of y-values to plot
    * @see  Plot2D#addCurve(double[],double[])
    * @see  Plot2D#addCurve(Complex[])
    * @see  Plot2D#setLineStyle(float[])
    * @see  Plot2D#setLineColor(Color)
    * @see  Plot2D#setLineWidth(float)
    */
   public void addCurve ( double y[] ) {

      // Check for space
      if ( totalCurves >= MAX_CURVES ) {
         throw new InvalidPlotValueException ("Too many curves (> " + MAX_CURVES + ")");
      }
      else {

         // Set up x-axis.
         double x[] = new double[y.length];
         for ( int i=0; i < y.length; i++ )
            x[i] = i;

         // Add the new curve
         currentCurve = ++totalCurves - 1;
         xVal[currentCurve] = x;
         yVal[currentCurve] = y;

         // Set default linestyle, color, and width
         lineStyle[currentCurve] = LINESTYLE[currentCurve%4];
         lineColor[currentCurve] = LINE_COLOR[currentCurve%7];
         lineWidth[currentCurve] = 1.0f;
         markerColor[currentCurve] = MARKER_COLOR[currentCurve%7];
         markerStyle[currentCurve] = MARKER_STYLE[currentCurve%5];

         // Set line and marker status
         plotLine[currentCurve] = true;
         plotMarker[currentCurve] = false;

         // Set changed flag
         changed = true;

         // Specify solid borders around bars in bar plots
         if ( plotType == BAR ) 
           lineStyle[currentCurve] = LINESTYLE_SOLID;

         // Create line style
         bs[currentCurve] = new BasicStroke( lineWidth[currentCurve],
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, lineStyle[currentCurve], 0 );   
     }
   }


   /**
    * This method adds an additional curve to a previously-created plot.
    * This method differs from <tt>setValues</tt> in that this
    * method <i>adds a <b>new</b> curve</i>, while <tt>setValues</tt>
    * <i>modifies the data values in the <b>current</b> curve</i>.
    * Note that arrays <code>x</code> and <code>y</code> must be the
    * same length.
    * <p>
    * The new curve is created with a line style and color
    * selected from the default tables, and with a line width of 1
    * pixel.  These values may be modified by the user using methods
    * <tt>setLineStyle</tt>, <tt>setLineColor</tt>, and <tt>setLineWidth</tt>.
    *
    * @param  x  Array of x-values to plot
    * @param  y  Array of y-values to plot
    * @see  Plot2D#addCurve(double[])
    * @see  Plot2D#addCurve(Complex[])
    * @see  Plot2D#setLineStyle(float[])
    * @see  Plot2D#setLineColor(Color)
    * @see  Plot2D#setLineWidth(float)
    */
   public void addCurve ( double x[], double y[] ) {

      // Check for incompatible array sizes
      if ( x.length != y.length ) {
         throw new InvalidArraySizeException ("x and y must be equal in length!");
      }

      // Check for space
      else if ( totalCurves >= MAX_CURVES ) {
         throw new InvalidPlotValueException ("Too many curves (> " + MAX_CURVES + ")");
      }
      else {

         // Add the new curve
         currentCurve = ++totalCurves - 1;
         xVal[currentCurve] = x;
         yVal[currentCurve] = y;

         // Set default linestyle, color, and width
         lineStyle[currentCurve] = LINESTYLE[currentCurve%4];
         lineColor[currentCurve] = LINE_COLOR[currentCurve%7];
         lineWidth[currentCurve] = 1.0f;
         markerColor[currentCurve] = MARKER_COLOR[currentCurve%7];
         markerStyle[currentCurve] = MARKER_STYLE[currentCurve%5];

         // Set line and marker status
         plotLine[currentCurve] = true;
         plotMarker[currentCurve] = false;

         // Set changed flag
         changed = true;

         // Specify solid borders around bars in bar plots
         if ( plotType == BAR ) 
           lineStyle[currentCurve] = LINESTYLE_SOLID;

         // Create line style
         bs[currentCurve] = new BasicStroke( lineWidth[currentCurve],
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, lineStyle[currentCurve], 0 );   
     }
   }


   /**
    * This method adds an additional curve to a previously-created plot.
    * This method differs from <tt>setValues</tt> in that this
    * method <i>adds a <b>new</b> curve</i>, while <tt>setValues</tt>
    * <i>modifies the data values in the <b>current</b> curve</i>.
    * <p>
    * The new curve is created with a line style and color
    * selected from the default tables, and with a line width of 1
    * pixel.  These values may be modified by the user using methods
    * <tt>setLineStyle</tt>, <tt>setLineColor</tt>, and <tt>setLineWidth</tt>.
    *
    * @param  c  Array of <code>Complex</code>-values to plot
    * @see  Plot2D#addCurve(double[],double[])
    * @see  Plot2D#addCurve(double[])
    * @see  Plot2D#setValues(double[],double[])
    * @see  Plot2D#setLineStyle(float[])
    * @see  Plot2D#setLineColor(Color)
    * @see  Plot2D#setLineWidth(float)
    */
   public void addCurve ( Complex c[] ) {

      // Check for space
      if ( totalCurves >= MAX_CURVES ) {
         throw new InvalidPlotValueException ("Too many curves (> " + MAX_CURVES + ")");
      }
      else {

         // Set data to plot
         double x[] = new double[c.length];
         double y[] = new double[c.length];
         for ( int i = 0; i < c.length; i++ ) {
            x[i] = c[i].re();
            y[i] = c[i].im();
         }

         // Add the new curve
         currentCurve = ++totalCurves - 1;
         xVal[currentCurve] = x;
         yVal[currentCurve] = y;

         // Set default linestyle, color, and width
         lineStyle[currentCurve] = LINESTYLE[currentCurve%4];
         lineColor[currentCurve] = LINE_COLOR[currentCurve%7];
         lineWidth[currentCurve] = 1.0f;
         markerColor[currentCurve] = MARKER_COLOR[currentCurve%7];
         markerStyle[currentCurve] = MARKER_STYLE[currentCurve%5];

         // Set line and marker status
         plotLine[currentCurve] = true;
         plotMarker[currentCurve] = false;

         // Set changed flag
         changed = true;

         // Specify solid borders around bars in bar plots
         if ( plotType == BAR ) 
           lineStyle[currentCurve] = LINESTYLE_SOLID;

         // Create line style
         bs[currentCurve] = new BasicStroke( lineWidth[currentCurve],
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, lineStyle[currentCurve], 0 );   
     }
   }


   /**
    * This method gets the x-data values for the current curve.
    *
    * @return a <tt>double[]</tt> containing the x values
    * @see  Plot2D#getPlotY()
    * @see  Plot2D#setCurrentCurve(int)
    */
   public double[] getPlotX() {
      return xVal[currentCurve];
   }


   /**
    * This method gets the y-data values for the current curve.
    *
    * @return a <tt>double[]</tt> containing the y values
    * @see  Plot2D#getPlotX()
    * @see  Plot2D#setCurrentCurve(int)
    */
   public double[] getPlotY() {
      return yVal[currentCurve];
   }

   /**
    * This method sets the current curve.  It throws an
    * <tt>IndexOutOfBoundsException</tt> if the specified curve
    * is out of range.
    *
    * @param  c  Number of curve to make current.  This number must be in
    *            the range [0, <tt>totalCurves</tt>-1].
    *
    * @see  Plot2D#getCurrentCurve()
    * @see  Plot2D#getTotalCurves()
    */
   public void setCurrentCurve ( int c ) {
      if ( ( c < 0 ) || ( c > totalCurves-1) ) {
         String s = "Invalid current curve: " + c;
         throw new IndexOutOfBoundsException(s);
      }
      else {
         currentCurve = c;
      }
   }

   /**
    * This method sets the on/off state for the current line.  If the value
    * is <tt>LINE_ON</tt>, the line will be drawn.  If the value is
    * <tt>LINE_OFF</tt>, the line will not be drawn.
    *
    * @param  b  Line state
    */
   public void setLineState ( boolean b ) {
      plotLine[currentCurve] = b;
      changed = true;
   }

   /**
    * This method sets the on/off state for the markers on the current 
    * line.  If the value is <tt>MARKER_ON</tt>, the markers will be 
    * drawn.  If the value is <tt>MARKER_OFF</tt>, the markers will 
    * not be drawn.
    *
    * @param  b  Marker state
    */
   public void setMarkerState ( boolean b ) {
      plotMarker[currentCurve] = b;
      changed = true;
   }

   /**
    * This method gets the current curve.
    *
    * @return an <tt>int</tt> containing the number of current curve
    *         in the range [0, <tt>totalCurves</tt>-1]
    *
    * @see  Plot2D#getTotalCurves()
    * @see  Plot2D#setCurrentCurve(int)
    */
   public int getCurrentCurve ( ) {
      return currentCurve;
   }

   /**
    * This method gets the total number of curves defined.
    *
    * @return an <tt>int</tt> containing the total number of curves defined.
    *
    * @see  Plot2D#getCurrentCurve()
    * @see  Plot2D#setCurrentCurve(int)
    */
   public int getTotalCurves ( ) {
      return totalCurves;
   }

   /**
    * This method sets the current annotation.  It throws an
    * <tt>IndexOutOfBoundsException</tt> if the specified annotation
    * is out of range.
    *
    * @param  c  Number of annotation to make current.  This number must be in
    *            the range [0, <tt>totalAnnotations</tt>-1].
    *
    * @see  Plot2D#getCurrentAnnotation()
    * @see  Plot2D#getTotalAnnotations()
    */
   public void setCurrentAnnotation ( int a ) {
      if ( ( a < 0 ) || ( a > totalCurves-1) ) {
         String s = "Invalid current annotation: " + a;
         throw new IndexOutOfBoundsException(s);
      }
      else {
         currentAnnotation = a;
      }
   }

   /**
    * This method gets the current annotation.
    *
    * @return an <tt>int</tt> containing the number of current annotation
    *         in the range [0, <tt>totalAnnotations</tt>-1]
    *
    * @see  Plot2D#getTotalAnnotations()
    * @see  Plot2D#setCurrentAnnotation(int)
    */
   public int getCurrentAnnotation ( ) {
      return currentAnnotation;
   }

   /**
    * This method gets the total number of annotations defined.
    *
    * @return an <tt>int</tt> containing the total number of annotations defined.
    *
    * @see  Plot2D#getCurrentAnnotation()
    * @see  Plot2D#setCurrentAnnotation(int)
    */
   public int getTotalAnnotations ( ) {
      return totalAnnotations;
   }

   /**
    * This method sets the background color for the current curve using
    * an object of the <tt>Color</tt> class.
    *
    * @param  c  A <tt>Color</tt> object
    *
    * @see  Plot2D#getBackgroundColor()
    */
   public void setBackgroundColor ( Color c ) {
      backColor = c;
      setBackground(backColor);
      changed = true;
   }

   /**
    * This method gets the background color for the current curve.
    * It returns an object of the <tt>Color</tt> class.
    *
    * @return a <tt>Color</tt> containing the background color
    * @see  Plot2D#setBackgroundColor(Color)
    */
   public Color getBackgroundColor ( ) {
      return backColor;
   }

   /**
    * This method sets the line style for the current curve using the
    * the pattern specified for class <tt>BorderStyle</tt>.
    * This method also accepts the pre-defined constants <tt>LINESTYLE_SOLID</tt>,
    * <tt>LINESTYLE_DOT</tt>, <tt>LINESTYLE_LONGDASH</tt>, and
    * <tt>LINESTYLE_SHORTDASH</tt>, as well as arbitrary styles defined
    * as described in parameter <tt>dash</tt> of class 
    * <tt>java.awt.BasicStroke</tt>.
    *
    * @param  ls  A <tt>float</tt> array containing the line style
    *
    * @see  Plot2D#getLineStyle()
    * @see  Plot2D#LINESTYLE_DOT
    * @see  Plot2D#LINESTYLE_LONGDASH
    * @see  Plot2D#LINESTYLE_SHORTDASH
    * @see  Plot2D#LINESTYLE_SOLID
    * @see  java.awt.BasicStroke#BasicStroke(float,int,int,float,float[],float) 
    */
   public void setLineStyle ( float ls[] ) {
      lineStyle[currentCurve] = ls;
      changed = true;

      // Create line style
      bs[currentCurve] = new BasicStroke( lineWidth[currentCurve],
                         BasicStroke.CAP_SQUARE,
                         BasicStroke.JOIN_MITER,
                         6.0f, lineStyle[currentCurve], 0 );   
   }

   /**
    * This method gets the line style for the current curve using the
    * the pattern specified for class <tt>BorderStyle</tt>.
    *
    * @return a <tt>float[]</tt> containing the line style
    * @see  Plot2D#setLineStyle(float[])
    */
   public float[] getLineStyle ( ) {
      return lineStyle[currentCurve];
   }

   /**
    * This method sets the line color for the current curve using
    * an object of the <tt>Color</tt> class.
    *
    * @param  c  A <tt>Color</tt> object
    *
    * @see  Plot2D#getLineColor()
    */
   public void setLineColor ( Color c ) {
      lineColor[currentCurve] = c;
      changed = true;
   }

   /**
    * This method gets the line color for the current curve.
    * It returns an object of the <tt>Color</tt> class.
    *
    * @return a <tt>Color</tt> containing the line color
    * @see  Plot2D#setLineColor(Color)
    */
   public Color getLineColor ( ) {
      return lineColor[currentCurve];
   }

   /**
    * This method sets the marker style for the current curve.
    * This method accepts the pre-defined constants <tt>MARKER_SQUARE</tt>,
    * <tt>MARKER_CIRCLE</tt>, <tt>MARKER_TRIANGLE</tt>, 
    * <tt>MARKER_DIAMOND/tt>, and <tt>MARKER_DOWNTRIANGLE</tt>.  
    *
    * @param  ms  A <tt>int</tt> array containing the marker style
    */
   public void setMarkerStyle ( int ms ) {
      markerStyle[currentCurve] = ms;
      changed = true;
   }

   /**
    * This method gets the marker style for the current curve.
    *
    * @return an <tt>int</tt> containing the marker style
    */
   public int getMarkerStyle () {
      return markerStyle[currentCurve];
   }

   /**
    * This method sets the marker color for the current curve using
    * an object of the <tt>Color</tt> class.
    *
    * @param  c  A <tt>Color</tt> object
    */
   public void setMarkerColor ( Color c ) {
      markerColor[currentCurve] = c;
      changed = true;
   }

   /**
    * This method gets the marker color for the current curve.
    * It returns an object of the <tt>Color</tt> class.
    *
    * @return a <tt>Color</tt> containing the line color
    */
   public Color getMarkerColor ( ) {
      return markerColor[currentCurve];
   }

   /**
    * This method sets the fill color for the current curve using
    * an object of the <tt>Color</tt> class.  The fill color is
    * only used for horizontal and vertical bar plots.
    *
    * @param  c  A <tt>Color</tt> object
    *
    * @see  Plot2D#getFillColor()
    */
   public void setFillColor ( Color c ) {
      fillColor[currentCurve] = c;
      changed = true;
   }

   /**
    * This method gets the fill color for the current curve.
    * It returns an object of the <tt>Color</tt> class.  The fill 
    * color is only used for horizontal and vertical bar plots.
    *
    *
    * @return a <tt>Color</tt> containing the line color
    * @see  Plot2D#setFillColor(Color)
    */
   public Color getFillColor ( ) {
      return fillColor[currentCurve];
   }

   /**
    * This method sets the line width in pixels for the current curve.
    *
    * @param  w  Width in pixels
    *
    * @see  Plot2D#getLineWidth()
    */
   public void setLineWidth ( float w ) {
      lineWidth[currentCurve] = w;
      changed = true;

      // Create line style
      bs[currentCurve] = new BasicStroke( lineWidth[currentCurve],
                         BasicStroke.CAP_SQUARE,
                         BasicStroke.JOIN_MITER,
                         6.0f, lineStyle[currentCurve], 0 );   
   }

   /**
    * This method gets the line width for the current curve.
    * It returns an object of the <tt>Color</tt> class.
    *
    * @return the line width in pixels
    * @see  Plot2D#setLineWidth(float)
    */
   public float getLineWidth ( ) {
      return lineWidth[currentCurve];
   }

   /**
    * This method sets the plot title.
    *
    * @param  s  Plot title.
    *
    * @see  Plot2D#getTitle()
    * @see  Plot2D#setXLabel(String)
    * @see  Plot2D#setYLabel(String)
    */
   public void setTitle ( String s ) {
      title = s;
      validTitle = true;
      changed = true;
   }

   /**
    * This method gets the plot title.
    *
    * @return the title string
    * @see  Plot2D#getXLabel()
    * @see  Plot2D#getYLabel()
    * @see  Plot2D#setTitle(String)
    */
   public String getTitle ( ) {
      return title;
   }

   /**
    * This method sets the plot x-axis label.
    *
    * @param  s  x-axis label
    *
    * @see  Plot2D#getXLabel()
    * @see  Plot2D#setTitle(String)
    * @see  Plot2D#setYLabel(String)
    */
   public void setXLabel ( String s ) {
      xLabel = s;
      validXLabel = true;
      changed = true;
   }

   /**
    * This method gets the x-axis label.
    *
    * @return the x-axis label.
    * @see  Plot2D#getTitle()
    * @see  Plot2D#getYLabel()
    * @see  Plot2D#setXLabel(String)
    */
   public String getXLabel ( ) {
      return xLabel;
   }

   /**
    * This method sets the plot y-axis label.
    *
    * @param  s  y-axis label
    *
    * @see  Plot2D#getYLabel()
    * @see  Plot2D#setTitle(String)
    * @see  Plot2D#setXLabel(String)
    */
   public void setYLabel ( String s ) {
      yLabel = s;
      validYLabel = true;
      changed = true;
   }

   /**
    * This method gets the y-axis label.
    *
    * @return the y-axis label.
    * @see  Plot2D#getTitle()
    * @see  Plot2D#getXLabel()
    * @see  Plot2D#setYLabel(String)
    */
   public String getYLabel ( ) {
      return yLabel;
   }

   /**
    * This method adds a new annotation to the plot.
    *
    * @param  s  Annotation text
    * @param  x  Annotation x-position in user-defined coordinates
    * @param  y  Annotation y-position in user-defined coordinates
    *
    * @see  Plot2D#getAnnotation()
    */
   public void addAnnotation ( String s, double x, double y ) {

      // Check for space
      if ( totalAnnotations >= MAX_ANNOTATIONS ) {
         throw new InvalidPlotValueException ("Too many annotations (> " + MAX_ANNOTATIONS + ")");
      }
      else {

         // Add the new annotation
         currentAnnotation = ++totalAnnotations - 1;
         annotations[currentAnnotation] = s;
         xAnnPos[currentAnnotation] = x;
         yAnnPos[currentAnnotation] = y;
         annotationColor[currentAnnotation] = Color.black;
      }
   }

   /**
    * This method gets the text of the current annotation.
    *
    * @see  Plot2D#addAnnotation(String,double,double)
    */
   public String getAnnotation( ) {
      return (annotations[currentAnnotation]);
   }

   /**
    * This method sets the annotation color for the current annotation using
    * an object of the <tt>Color</tt> class.
    *
    * @param  c  A <tt>Color</tt> object
    *
    * @see  Plot2D#getAnnotationColor()
    */
   public void setAnnotationColor ( Color c ) {
      annotationColor[currentAnnotation] = c;
      changed = true;
   }

   /**
    * This method gets the annotation color for the current annotation.
    * It returns an object of the <tt>Color</tt> class.
    *
    * @return a <tt>Color</tt> containing the annotation color
    * @see  Plot2D#setAnnotationColor(Color)
    */
   public Color getAnnotationColor ( ) {
      return annotationColor[currentAnnotation];
   }

   /**
    * This method sets the x-axis scale manually.  It automatically
    * forces the x-axis scale mode to manual.
    *
    * @param  x  A two-element array contain the minimum and maximum x values to plot
    *
    * @see  Plot2D#setAutoXScale()
    * @see  Plot2D#setYScale(double[])
    */
   public void setXScale ( double x[] ) {
      if ( x.length != 2 )
         throw new InvalidPlotValueException ("Array must have two values.");
      else if ( x[0] >= x[1] )
         throw new InvalidPlotValueException ("xMax must be > xMin.");
      else {
         autoXScale = false;
         xMin = x[0];
         xMax = x[1];
         changed = true;
      }
   }

   /**
    * This method sets the y-axis scale manually.  It automatically
    * forces the y-axis scale mode to manual.
    *
    * @param  y  A two-element array contain the minimum and maximum y values to plot
    *
    * @see  Plot2D#setAutoYScale()
    * @see  Plot2D#setXScale(double[])
    */
   public void setYScale ( double y[] ) {
      if ( y.length != 2 )
         throw new InvalidPlotValueException ("Input array must have two values.");
      else if ( y[0] >= y[1] )
         throw new InvalidPlotValueException ("yMax must be > yMin.");
      else {
         autoYScale = false;
         yMin = y[0];
         yMax = y[1];
         changed = true;
      }
   }

   /**
    * This method gets the x-axis display limits.
    *
    * @return a two-element array containing the minimum and maximum x values
    *         to display
    *
    * @see  Plot2D#getYScale()
    * @see  Plot2D#setXScale(double[])
    */
   public double[] getXScale () {
      double xScale[] = new double[2];

      // Check to see if we have calculated this scale yet.
      // If not, and if we are in autoscale mode, then we must
      // calculate it before going any further.
      if ( xMin == 0 && xMax == 0 ) {

         // Set the display limits if in autoscale mode
         if ( autoXScale ) {
            xMax = Array.maxVal ( xVal[0] );
            xMin = Array.minVal ( xVal[0] );
            for ( int i = 1; i < totalCurves; i++ ) {
               xMax = Math.max ( xMax, Array.maxVal(xVal[i]) );
               xMin = Math.min ( xMin, Array.minVal(xVal[i]) );
            }
            if ( logXAxis ) {
               xMax = Math.pow(10, Math.ceil( Math1.log10(xMax)) );
               xMin = Math.pow(10, Math.floor( Math1.log10(xMin)) );

               // Handle the case where xMin == xMax
               if ( xMin == xMax ) {
                  xMax = 1.01 * xMin;
               }

               // Set the logarithmic equivalents
               xMaxLog = Math1.log10(xMax);
               xMinLog = Math1.log10(xMin);
            }
         }
      }

      // Now get values
      xScale[0] = xMin;
      xScale[1] = xMax;
      return (xScale);
   }

   /**
    * This method gets the y-axis display limits.
    *
    * @return a two-element array containing the minimum and maximum y values
    *         to display
    *
    * @see  Plot2D#getXScale()
    * @see  Plot2D#setYScale(double[])
    */
   public double[] getYScale () {
      double yScale[] = new double[2];

      // Check to see if we have calculated this scale yet.
      // If not, and if we are in autoscale mode, then we must
      // calculate it before going any further.
      if ( yMin == 0 && yMax == 0 ) {

         if ( autoYScale ) {
            yMax = Array.maxVal ( yVal[0] );
            yMin = Array.minVal ( yVal[0] );
            for ( int i = 1; i < totalCurves; i++ ) {
               yMax = Math.max ( yMax, Array.maxVal(yVal[i]) );
               yMin = Math.min ( yMin, Array.minVal(yVal[i]) );
            }
            if ( logYAxis ) {
               yMax = Math.pow(10, Math.ceil( Math1.log10(yMax)) );
               yMin = Math.pow(10, Math.floor( Math1.log10(yMin)) );

               // Handle the case where yMin == yMax
               if ( yMin == yMax ) {
                  yMax = 1.01 * yMin;
               }

               // Set the logarithmic equivalents
               yMaxLog = Math1.log10(yMax);
               yMinLog = Math1.log10(yMin);
            }
         }
      }

      // Now get values
      yScale[0] = yMin;
      yScale[1] = yMax;
      return (yScale);
   }

   /**
    * This method restores x-axis scaling to automatic mode.  Note that
    * the display limit information will not be updated until the plot
    * has been repainted.
    *
    * @see  Plot2D#setXScale(double[])
    */
   public void setAutoXScale ( ) {
      autoXScale = true;
      changed = true;
   }

   /**
    * This method restores y-axis scaling to automatic mode.  Note that
    * the display limit information will not be updated until the plot
    * has been repainted.
    *
    * @see  Plot2D#setYScale(double[])
    */
   public void setAutoYScale ( ) {
      autoYScale = true;
      changed = true;
   }

   /**
    * This method sets the grid state.  If <tt>y</tt> is <code>true</code>,
    * a grid is drawn.  Otherwise, the grid is not drawn.  This method also
    * accepts the constants <tt>GRID_ON</tt> and <tt>GRID_OFF</tt>.
    *
    * @param  b  Grid state
    *
    * @see  Plot2D#getGridState()
    */
   public void setGridState ( boolean b ) {
      xGridLines = b;
      yGridLines = b;
      changed = true;
   }

   /**
    * This method gets the grid state.
    *
    * @return Grid state--<code>true</code> means that the grid is on
    *
    * @see  Plot2D#setGridState(boolean)
    */
   public boolean getGridState ( ) {
      return xGridLines;
   }

   /**
    * This method sets the x-axis tic mark locations manually.  It automatically
    * forces the x-axis tic mode to manual.  Note that the values in this array
    * are user-defined coordinates, not pixels.
    *
    * @param  xtic  Array of tic mark locations.
    *
    * @see  Plot2D#getXTics()
    * @see  Plot2D#setAutoXTics()
    */
   public void setXTics ( double xtic[] ) {
      autoXTicMode = false;
      xTic = xtic;
      changed = true;
   }

   /**
    * This method gets the x-axis tic mark locations.
    *
    * @return the tic mark locations
    *
    * @see  Plot2D#setXTics(double[])
    * @see  Plot2D#setAutoXTics()
    */
   public double[] getXTics () {
      return (xTic);
   }

   /**
    * This method sets the y-axis tic mark locations manually.  It automatically
    * forces the y-axis tic mode to manual.  Note that the values in this array
    * are user-defined coordinates, not pixels.
    *
    * @param  ytic  Array of tic mark locations.
    *
    * @see  Plot2D#getXTics()
    * @see  Plot2D#setAutoYTics()
    */
   public void setYTics ( double ytic[] ) {
      autoYTicMode = false;
      yTic = ytic;
      changed = true;
   }

   /**
    * This method gets the y-axis tic mark locations.
    *
    * @return the tic mark locations
    *
    * @see  Plot2D#setYTics(double[])
    * @see  Plot2D#setAutoYTics()
    */
   public double[] getYTics () {
      return (yTic);
   }

   /**
    * This method restores x-axis tic marks to automatic mode.  Note that
    * the tic mark location information will not be updated until the plot
    * has been repainted.
    *
    * @see  Plot2D#getXTics()
    * @see  Plot2D#setXTics(double[])
    */
   public void setAutoXTics( ) {
      autoXTicMode = true;
      changed = true;
   }

   /**
    * This method restores y-axis tic marks to automatic mode.  Note that
    * the tic mark location information will not be updated until the plot
    * has been repainted.
    *
    * @see  Plot2D#getYTics()
    * @see  Plot2D#setYTics(double[])
    */
   public void setAutoYTics( ) {
      autoYTicMode = true;
      changed = true;
   }

   /**
    * This method removes all curves from the plot, and resets
    * the plot object to its initial conditions.
    */
   public void removeAll() {

      // Clear data to plot
      for ( int i = 0; i < xVal.length; i++ ) {
         xVal[i] = null;
         yVal[i] = null;
      }

      // Set the background color
      setBackground(backColor);

      // Set the current curve and the number of curves
      currentCurve = 0;
      totalCurves = 0;

      // Set default linestyle, color, and width
      lineStyle[currentCurve] = LINESTYLE[0];
      lineColor[currentCurve] = LINE_COLOR[0];
      lineWidth[currentCurve] = 1.0f;
      fillColor[currentCurve] = FILL_COLOR[0];
      markerColor[currentCurve] = MARKER_COLOR[0];
      markerStyle[currentCurve] = MARKER_STYLE[0];

      // Set line and marker status
      plotLine[currentCurve] = true;
      plotMarker[currentCurve] = false;
      
      // Set image changed flag
      changed = true;

      // Create line style
      bs[currentCurve] = new BasicStroke( lineWidth[currentCurve],
                         BasicStroke.CAP_SQUARE,
                         BasicStroke.JOIN_MITER,
                         6.0f, lineStyle[currentCurve], 0 );   

      // Create bounding box style.
      float boxStyle[] = {12.0f, 0.0f};
      bb = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, boxStyle, 0 );

      // Create grid line style.
      float gridStyle[] = {3.0f, 6.0f};
      bg = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            1.0f, gridStyle, 0 );

      // Create tic mark stype
      float ticStyle[] = {12.0f, 0.0f};
      bt = new BasicStroke( 1.0f,
                            BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER,
                            6.0f, ticStyle, 0 );

    }

   /**
    * This method selects a font size for titles and labels that guarantees
    * that the entire string will fit on the plot.
    *
    * @param  g           The graphics object containing the font
    * @param  longString  The string to be displayed
    * @param  xSpace      The number of pixels available for the display
    *
    * @return  The selected font size in a <tt>FontMetrics</tt> object
    */
   private FontMetrics pickFont(Graphics g, String longString, int xSpace ) {
      boolean fontFits = false;
      Font font = g.getFont();
      FontMetrics fontMetrics = g.getFontMetrics();
      int size = font.getSize();
      String name = font.getName();
      int style = font.getStyle();

      while (!fontFits) {
         if ( (fontMetrics.getHeight() <= maxCharHeight)
           && (fontMetrics.stringWidth(longString) <= xSpace)) {
            fontFits = true;
         } else {
            if (size <= minFontSize) {
               fontFits = true;
            } else {
               g.setFont(font = new Font(name, style,--size));
               fontMetrics = g.getFontMetrics();
            }
         }
      }
      return fontMetrics;
   }

   /**
    * This method calculates the string label for a given value, modifying the
    * format to match required scale.
    */
   private static String calcNumberString( double x ) {

      double xabs = Math.abs(x);
      if ( xabs > 1e6 ) {
         return Fmt.sprintf("%.2e",x);
      }
      else if ( xabs > 9999.5 ) {
         return Fmt.sprintf("%d",Math.rint(x));
      }
      else if ( xabs > 999.5 ) {
         return Fmt.sprintf("%.4g",x);
      }
      else if ( xabs > 99.95 ) {
         return Fmt.sprintf("%.3g",x);
      }
      else if ( xabs > 9.995 ) {
         return Fmt.sprintf("%.2g",x);
      }
      else if ( xabs > 0.0995 ) {
         return Fmt.sprintf("%.3g",x);
      }
      else if ( xabs > 0.00995 ) {
         return Fmt.sprintf("%.4g",x);
      }
      else if ( xabs == 0.00 ) {
         return "0";
      }
      else {
         return Fmt.sprintf("%.2e",x);
      }
  }

   /**
    * This method calculates the default location of the x tic marks to allow
    * for "nice" tic mark and grid values.  It selects nice round numbers for
    * the tic marks, and restricts the total number of tic marks to 11 or less.
    * The resulting tic mark locations are stored directly into instance variable
    * xTic.  (It is used for linear scales only.)
    *
    * @param  xmin        The minimum x value to display
    * @param  xmax        The maximum x value to display
    */
   private void calcXTics( double xmin, double xmax ) {

      double bestDelta[] = { .1, .2, .5, 1, 2, 5, 10, 20, 50 };
                          // Step sizes that produce "nice" numbers
      double delta;       // Step between tics
      double high;        // Highest value to plot
      double low;         // Lowest value to plot
      int tics;           // Number of tic marks

      // Handle the case of equal values
      if (xmin == xmax) {
         xMin = xmin;
         xMax = xmax + 1;
         tics = 2;
         xTic  = new double[ 2 ];
         xTicS = new String[ 2 ];
         xTic[0] = xMin;
         xTic[1] = xMax;
         xTicS[0] = calcNumberString( xTic[0] );
         xTicS[1] = calcNumberString( xTic[1] );
         return;
      }

      // Otherwise, compute fit
      double xDelta = xmax - xmin;
      double power = Math.pow(10, Math.round( Math1.log10(xDelta) - 1 ) );
      for ( int i = 0; i < bestDelta.length; i++ ) {
         delta = power * bestDelta[i];
         high = delta * Math.ceil( xmax / delta );
         low = delta * Math.floor( xmin / delta );
         tics = (int) (Math.round( (high-low) / delta)) + 1;

         // Pick the first case with <= 11 tics
         if ( tics <= 11 ) {
            xMin = low;
            xMax = high;
            delta = ( high - low ) / ( tics - 1 );
            xTic = new double[ tics ];
            xTicS = new String[ tics ];
            for ( int j = 0; j < xTic.length; j++ ) {
               xTic[j] = low + delta * j;
               xTicS[j] = calcNumberString( xTic[j] );
            }
            return;
         }
      }
   }

   /**
    * This method calculates the default location of the y tic marks to allow
    * for "nice" tic mark and grid values.  It selects nice round numbers for
    * the tic marks, and restricts the total number of tic marks to 11 or less.
    * The resulting tic mark locations are stored directly into instance variable
    * yTic.  (It is used for linear scales only.)
    *
    * @param  ymin        The minimum y value to display
    * @param  ymax        The maximum y value to display
    */
   private void calcYTics( double ymin, double ymax ) {

      double bestDelta[] = { .1, .2, .5, 1, 2, 5, 10, 20, 50 };
                          // Step sizes that produce "nice" numbers
      double delta;       // Step between tics
      double high;        // Highest value to plot
      double low;         // Lowest value to plot
      int tics;           // Number of tic marks

      // Handle the case of equal values
      if (ymin == ymax) {
         yMin = ymin;
         yMax = ymax + 1;
         tics = 2;
         yTic = new double[ 2 ];
         yTicS = new String[ 2 ];
         yTic[0] = yMin;
         yTic[1] = yMax;
         yTicS[0] = calcNumberString( yTic[0] );
         yTicS[1] = calcNumberString( yTic[1] );
         return;
      }

      // Otherwise, compute fit
      double yDelta = ymax - ymin;
      double power = Math.pow(10, Math.round( Math1.log10(yDelta) - 1 ) );
      for ( int i = 0; i < bestDelta.length; i++ ) {
         delta = power * bestDelta[i];
         high = delta * Math.ceil( ymax / delta );
         low = delta * Math.floor( ymin / delta );
         tics = (int) (Math.round( (high-low) / delta)) + 1;

         // Pick the first case with <= 11 tics
         if ( tics <= 11 ) {
            yMin = low;
            yMax = high;
            delta = ( high - low ) / ( tics - 1 );
            yTic = new double[ tics ];
            yTicS = new String[ tics ];
            for ( int j = 0; j < yTic.length; j++ ) {
               yTic[j] = low + delta * j;
               yTicS[j] = calcNumberString( yTic[j] );
            }
            return;
         }
      }
   }

   /**
    * This method calculates the default location of the coincentric circles
    * on a polar plot.  It selects nice round numbers for
    * the tic marks, and restricts the total number of tic marks to 11 or less.
    * Because polar plots get "busy" with too many rings, this method further
    * reduces the number of tics if the number is evenly divisible by 2 or 3.
    * The resulting tic mark locations are stored directly into instance variable
    * yTic. 
    *
    * @param  ymax        The maximum value to display
    */
   private void calcPolarCircles( double ymax ) {

      double bestDelta[] = { .1, .2, .5, 1, 2, 5, 10, 20, 50 };
                          // Step sizes that produce "nice" numbers
      double delta;       // Step between tics
      double high;        // Highest value to plot
      double low;         // Lowest value to plot
      int tics;           // Number of tic marks
      double ymin = 0;    // Minimum value in polar plot

      // Handle the case of equal values
      if (ymin == ymax) {
         yMin = ymin;
         yMax = ymax + 1;
         tics = 2;
         yTic = new double[ 2 ];
         yTicS = new String[ 2 ];
         yTic[0] = yMin;
         yTic[1] = yMax;
         yTicS[0] = calcNumberString( yTic[0] );
         yTicS[1] = calcNumberString( yTic[1] );
         return;
      }

      // Otherwise, compute fit
      double yDelta = ymax - ymin;
      double power = Math.pow(10, Math.round( Math1.log10(yDelta) - 1 ) );
      for ( int i = 0; i < bestDelta.length; i++ ) {
         delta = power * bestDelta[i];
         high = delta * Math.ceil( ymax / delta );
         low = delta * Math.floor( ymin / delta );
         tics = (int) (Math.round( (high-low) / delta)) + 1;

         // Pick the first case with <= 11 tics, and try to
         // reduce the number of coinentric rings created.
         if ( tics <= 11 ) {
            if ( tics == 11 )
               tics = 6;
            else if ( tics == 9 )
               tics = 5;
            else if ( tics == 7 )
               tics = 4;
            else if ( tics == 5 )
               tics = 3;
        /*  else if ( tics%2 == 0 )
               tics = tics / 2;   
            else if ( tics%3 == 0 )
               tics = tics / 3; */

            // Calculate locations of marks         
            yMin = low;
            yMax = high;
            delta = ( high - low ) / ( tics - 1 );
            yTic = new double[ tics ];
            yTicS = new String[ tics ];
            for ( int j = 0; j < yTic.length; j++ ) {
               yTic[j] = low + delta * j;
               yTicS[j] = calcNumberString( yTic[j] );
            }
            return;
         }
      }
   }

   /**
    * This method calculates the default location of the x tic marks to allow
    * for "nice" tic mark and grid values on a LOGARITHMIC scale. The resulting
    * tic mark locations are stored directly into instance variable xTic, and
    * the logarithms of the values are sored into xTicLog.
    *
    * @param  low         The minimum value to display
    * @param  high        The maximum value to display
    */
   private void calcXLogTics( double low, double high ) {

      double decade[] =  { 0, 0.30102999566398, 0.47712125471966, 0.60205999132796,
                              0.69897000433602, 0.77815125038364, 0.84509804001426,
                              0.90308998699194, 0.95424250943932, 1.00000000000000 };
                          // Log10 of (1, 2, ..., 10)
      double loglow;      // Log of low value
      double loghigh;     // Log of high value
      double diff;        // Difference between loghigh and loglow
      int tics;           // Number of tic marks

      // Get boundaries for tic marks
      xMin    = Math.pow(10, Math.floor( Math1.log10(low)) );
      xMax    = Math.pow(10, Math.ceil( Math1.log10(high)) );
      loglow  = Math1.log10(xMin);
      loghigh = Math1.log10(xMax);
      diff    = loghigh - loglow;

      // Now calculate the marks in log space
      xTicLog = new double[ (int) Math.round(10*diff) ];
      xTic    = new double[ xTicLog.length ];
      xTicS   = new String[ xTicLog.length ];
      int k = 0;
      for (int i = 0; i < diff; i++) {
         for (int j = 0; j < 10; j++ ) {
            xTicLog[k] = (loglow + i) + decade[j];
            xTic[k] = Math.pow(10, xTicLog[k]);
            if ( (Math.abs(decade[j])     < 0.001) ||
                 (Math.abs(decade[j]-1.0) < 0.001) )
               xTicS[k] = calcNumberString( xTic[k] );
            else
               xTicS[k] = " ";
            k++;
         }
      }
   }

   /**
    * This method calculates the default location of the y tic marks to allow
    * for "nice" tic mark and grid values on a LOGARITHMIC scale. The resulting
    * tic mark locations are stored directly into instance variable yTic, and
    * the logarithms of the values are sored into yTicLog.
    *
    * @param  low         The minimum value to display
    * @param  high        The maximum value to display
    */
   private void calcYLogTics( double low, double high ) {

      double decade[] =  { 0, 0.30102999566398, 0.47712125471966, 0.60205999132796,
                              0.69897000433602, 0.77815125038364, 0.84509804001426,
                              0.90308998699194, 0.95424250943932, 1.00000000000000 };
                          // Log10 of (1, 2, ..., 10)
      double loglow;      // Log of low value
      double loghigh;     // Log of high value
      double diff;        // Difference between loghigh and loglow
      int tics;           // Number of tic marks

      // Get boundaries for tic marks
      yMin    = Math.pow(10, Math.floor( Math1.log10(low)) );
      yMax    = Math.pow(10, Math.ceil( Math1.log10(high)) );
      loglow  = Math1.log10(yMin);
      loghigh = Math1.log10(yMax);
      diff    = loghigh - loglow;

      // Now calculate the marks in log and linear space
      yTicLog = new double[ (int) Math.round(10*diff) ];
      yTic    = new double[ yTicLog.length ];
      yTicS   = new String[ yTicLog.length ];
      int k = 0;
      for (int i = 0; i < diff; i++) {
         for (int j = 0; j < 10; j++ ) {
            yTicLog[k] = (loglow + i) + decade[j];
            yTic[k] = Math.pow(10, yTicLog[k]);
            if ( (Math.abs(decade[j])     < 0.001) ||
                 (Math.abs(decade[j]-1.0) < 0.001) )
               yTicS[k] = calcNumberString( yTic[k] );
            else
               yTicS[k] = " ";
            k++;
         }
      }
   }


   //*************************************************
   // Paint linear plot
   //*************************************************

   /**
    * This method creates a linear-linear plot.
    *
    * @param  g  A graphics object.
    */
   private void linlin(Graphics2D g2) {

      // Declare local variables
      Ellipse2D ell;                     // 2-D ellipse object
      Font font;                         // Font
      int fudge;                         // Positioning fudge factor
      int i, j;                          // loop index
      Line2D line;                       // 2-D line object
      GeneralPath p;                     // General path
      Rectangle2D rect;                  // 2-D rectangle object
      double x1, x2, y1, y2;             // Points on line
      float xs, ys;                      // Starting point for general path

      // Get the size of the "user" area in pixels, and calculate the size and
      // location of the "plot" area in pixels.
      size = getSize();
      plotSize.width = (int) Math.round(size.width * ( 1 - xLeftMargin - xRightMargin));
      plotSize.height = (int) Math.round(size.height * ( 1 - yTopMargin - yBottomMargin));
      plotStartX = (int) Math.round(size.width * xLeftMargin);
      plotStartY = (int) Math.round(size.height * yTopMargin);

      // Set the display limits if in autoscale mode
      if ( autoXScale ) {
         xMax = Array.maxVal ( xVal[0] );
         xMin = Array.minVal ( xVal[0] );
         for ( i = 1; i < totalCurves; i++ ) {
            xMax = Math.max ( xMax, Array.maxVal(xVal[i]) );
            xMin = Math.min ( xMin, Array.minVal(xVal[i]) );
         }
      }
      if ( autoYScale ) {
         yMax = Array.maxVal ( yVal[0] );
         yMin = Array.minVal ( yVal[0] );
         for ( i = 1; i < totalCurves; i++ ) {
            yMax = Math.max ( yMax, Array.maxVal(yVal[i]) );
            yMin = Math.min ( yMin, Array.minVal(yVal[i]) );
         }
      }

      // Calculate locations of tic marks if in automatic mode
      if ( autoXTicMode ) calcXTics( xMin, xMax );
      if ( autoYTicMode ) calcYTics( yMin, yMax );

      // Recalculate the scale factors every time, since the window
      // could have been resized or the data could have changed.
      if ( Math.abs( xMax - xMin ) > 0 )
         xScale = plotSize.width / ( xMax - xMin );
      else
         xScale = 1;

      if ( Math.abs( yMax - yMin ) > 0 )
         yScale = plotSize.height / ( yMax - yMin );
      else
         yScale = 1;

      //**************************************************************
      //
      // Add the bounding box
      //
      //**************************************************************

      // Set the stroke for the bounding box--a thin solid line.
      g2.setStroke(bb);
      g2.setColor(boxColor);

      // Draw the box
      line = new Line2D.Double( plotStartX,                  plotStartY,
                                plotStartX + plotSize.width, plotStartY);
      g2.draw(line);
      line = new Line2D.Double( plotStartX + plotSize.width, plotStartY,
                                plotStartX + plotSize.width, plotStartY + plotSize.height);
      g2.draw(line);
      line = new Line2D.Double( plotStartX + plotSize.width, plotStartY + plotSize.height,
                                plotStartX,                  plotStartY + plotSize.height);
      g2.draw(line);
      line = new Line2D.Double( plotStartX,                  plotStartY + plotSize.height,
                                plotStartX,                  plotStartY);
      g2.draw(line);

      //**************************************************************
      //
      // Add grid lines.
      //
      //**************************************************************

      // Set the stroke for the grid line--a thin dotted line.
      g2.setStroke(bg);
      g2.setColor(gridColor);

      // Draw the linear x-axis grid lines
      if ( xGridLines ) {
         for ( i = 1; i < xTic.length-1; i++ ) {
            line = new Line2D.Double ( (xTic[i]-xMin) * xScale + plotStartX,
                                       plotStartY,
                                       (xTic[i]-xMin) * xScale + plotStartX,
                                       plotStartY + plotSize.height);
            g2.draw(line);
         }
      }

      // Draw the linear y-axis grid lines
      if ( yGridLines ) {
         for ( i = 1; i < yTic.length-1; i++ ) {
            line = new Line2D.Double ( plotStartX,
                                       -(yTic[i]-yMax) * yScale + plotStartY,
                                       plotStartX + plotSize.width,
                                       -(yTic[i]-yMax) * yScale + plotStartY);
            g2.draw(line);
         }
      }

      //**************************************************************
      //
      // Add tic marks.
      //
      //**************************************************************

      // Set the stroke for the tic marks--a thin solid line.
      g2.setStroke(bt);
      g2.setColor(ticColor);

      // Draw linear x-axis tic marks
      if ( xTicMarks ) {
         for ( i = 1; i < xTic.length-1; i++ ) {
            line = new Line2D.Double (  (xTic[i]-xMin) * xScale + plotStartX,
                                        plotStartY,
                                        (xTic[i]-xMin) * xScale + plotStartX,
                                        plotStartY + 0.025*plotSize.height);
            g2.draw(line);
            line = new Line2D.Double (  (xTic[i]-xMin) * xScale + plotStartX,
                                        plotStartY + plotSize.height,
                                        (xTic[i]-xMin) * xScale + plotStartX,
                                        plotStartY + 0.975*plotSize.height);
            g2.draw(line);
         }
      }

      // Draw linear y-axis tic marks
      if ( yTicMarks ) {
         for ( i = 1; i < yTic.length-1; i++ ) {
            line = new Line2D.Double (  plotStartX,
                                        -(yTic[i]-yMax) * yScale + plotStartY,
                                        plotStartX + 0.025*plotSize.width,
                                        -(yTic[i]-yMax) * yScale + plotStartY);
            g2.draw(line);
            line = new Line2D.Double (  plotStartX + plotSize.width,
                                        -(yTic[i]-yMax) * yScale + plotStartY,
                                        plotStartX + 0.975*plotSize.width,
                                        -(yTic[i]-yMax) * yScale + plotStartY);
            g2.draw(line);
         }
      }

      //**************************************************************
      //
      // Add tic mark labels.
      //
      //**************************************************************

      // X-axis
      maxCharHeight = (size.height - plotSize.height - plotStartY) / 2;
      maxStringLength = (int) (plotSize.width / (1.2*xTic.length));
      minFontSize = 8;
      g2.setColor(labelColor);

      // Get the tic mark label font
      g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
      fontMetrics = pickFont(g2, xTicS[0], maxStringLength);

      // Linear tic mark labels
      for ( i = 0; i < xTicS.length; i++ ) {
      
         stringWidth = fontMetrics.stringWidth( xTicS[i] );
         stringHeight = fontMetrics.getHeight();
         g2.drawString( xTicS[i],
                        (int) ((xTic[i]-xMin) * xScale + plotStartX - stringWidth/2),
                        (int) (plotStartY + plotSize.height + 11));
      }

      // Y-axis
      maxCharHeight = (size.height - plotSize.height - plotStartY) / 2;
      maxStringLength = (int) (0.50 * plotStartX);
      minFontSize = 8;
      g2.setColor(labelColor);

      // Get the tic mark label font
      g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
      fontMetrics = pickFont(g2, xTicS[0], maxStringLength);

      for ( i = 0; i < yTicS.length; i++ ) {

         // Draw tic label.  Include a fudge factor on the lowest label to
         // avoid collisions
         if ( i == 0 )
            fudge = 5;
         else
            fudge = 12;
         stringWidth = fontMetrics.stringWidth( yTicS[i] );
         stringHeight = fontMetrics.getHeight();
         g2.drawString( yTicS[i],
                        (int) (plotStartX - stringWidth - 6),
                        (int) (-(yTic[i]-yMax) * yScale + plotStartY - stringHeight/2 + fudge ));
      }

      //**************************************************************
      //
      // Plot the data set(s)
      //
      //**************************************************************

      // Increment over all curves
      for ( i = 0; i < totalCurves; i++ ) {

         // Plot the line
         if ( plotLine[i] ) {

            // Set up stroke, color, etc.
            g2.setStroke(bs[i]);
            g2.setColor(lineColor[i]);

            for ( j = 0; j < xVal[i].length-1; j++ ) {

               x1 = xVal[i][j] - xMin;
               x2 = xVal[i][j+1] - xMin;
               y1 = yVal[i][j] - yMax;
               y2 = yVal[i][j+1] - yMax;
               line = new Line2D.Double (  x1 * xScale + plotStartX,
                                          -y1 * yScale + plotStartY,
                                           x2 * xScale + plotStartX,
                                          -y2 * yScale + plotStartY);
               g2.draw(line);
            }
         }

         // Plot the marker
         if ( plotMarker[i] ) {

            // Set up stroke, color, etc.
            if ( bm == null ) {
               bm = new BasicStroke( 2.0f,
                                     BasicStroke.CAP_SQUARE,
                                     BasicStroke.JOIN_MITER,
                                     6.0f, LINESTYLE_SOLID, 0 );
            }
            g2.setStroke(bm);
            g2.setColor(markerColor[i]);

            for ( j = 0; j < xVal[i].length; j++ ) {
               if ( markerStyle[i] == MARKER_SQUARE ) {

                  x1 = xVal[i][j] - xMin;
                  y1 = yVal[i][j] - yMax;
                  rect = new Rectangle2D.Double (  x1 * xScale + plotStartX-3,
                                                  -y1 * yScale + plotStartY-3,
                                                   6, 6);
                  g2.draw(rect);
               }
               if ( markerStyle[i] == MARKER_CIRCLE ) {

                  x1 = xVal[i][j] - xMin;
                  y1 = yVal[i][j] - yMax;
                  ell = new Ellipse2D.Double (  x1 * xScale + plotStartX-3,
                                               -y1 * yScale + plotStartY-3,
                                                6, 6);
                  g2.draw(ell);
               }
               if ( markerStyle[i] == MARKER_TRIANGLE ) {

                  x1 = xVal[i][j] - xMin;
                  y1 = yVal[i][j] - yMax;
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX-3);
                  ys = (float)(-y1 * yScale + plotStartY+2);
                  p.moveTo( xs,      ys      );
                  p.lineTo( xs+6.0f, ys      );
                  p.lineTo( xs+3.0f, ys-5.2f );
                  p.closePath();
                  g2.draw(p);
               }
               if ( markerStyle[i] == MARKER_DIAMOND ) {

                  x1 = xVal[i][j] - xMin;
                  y1 = yVal[i][j] - yMax;
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX);
                  ys = (float)(-y1 * yScale + plotStartY);
                  p.moveTo( xs-4.0f, ys      );
                  p.lineTo( xs     , ys+4.0f );
                  p.lineTo( xs+4.0f, ys      );
                  p.lineTo( xs     , ys-4.0f );
                  p.closePath();
                  g2.draw(p);
               }
               if ( markerStyle[i] == MARKER_DOWNTRIANGLE ) {

                  x1 = xVal[i][j] - xMin;
                  y1 = yVal[i][j] - yMax;
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX-3);
                  ys = (float)(-y1 * yScale + plotStartY+3);
                  p.moveTo( xs,      ys-5.2f );
                  p.lineTo( xs+6.0f, ys-5.2f );
                  p.lineTo( xs+3.0f, ys      );
                  p.closePath();
                  g2.draw(p);
               }
            }
         }
      }

      //**************************************************************
      //
      // Add title and labels
      //
      //**************************************************************
      maxCharHeight = plotStartY - 15;
      maxStringLength = 3 * plotSize.width / 4;
      minFontSize = 12;
      g2.setColor(labelColor);

      // Draw title
      if ( validTitle ) {
         // Get title font info.  Try to use Helvetica 18 point BOLD if it fits
         g2.setFont(font = new Font("Helvetica", Font.BOLD, 18));
         fontMetrics = pickFont(g2, title, maxStringLength);
         stringWidth = fontMetrics.stringWidth( title );
         stringHeight = fontMetrics.getHeight();
         g2.drawString(title,plotStartX + plotSize.width/2 - stringWidth/2,
                       plotStartY/2 + stringHeight/2);
      }

      // Draw x label
      if ( validXLabel ) {
         // Get x label font info.
         maxStringLength = plotSize.width;
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 18));
         fontMetrics = pickFont(g2, xLabel, maxStringLength);
         stringWidth = fontMetrics.stringWidth( xLabel );
         stringHeight = fontMetrics.getHeight();
         g2.drawString(xLabel,plotStartX + plotSize.width/2 - stringWidth/2,
                       3*plotStartY/2 + plotSize.height + stringHeight/2 );
      }

      // Draw y label
      if ( validYLabel ) {
         // Get y label font info.
         maxStringLength = plotSize.height;
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 18));
         fontMetrics = pickFont(g2, yLabel, maxStringLength);
         stringWidth = fontMetrics.stringWidth( yLabel );
         stringHeight = fontMetrics.getHeight();

         AffineTransform at = new AffineTransform();
         at.rotate(-Math.PI/2);
         g2.setTransform(at);

         int xPos = plotStartY/2 + plotSize.height/2 + stringWidth/2;
         int yPos = (int) (1.05*stringHeight);
         g2.drawString(yLabel,-xPos, yPos );
      }

      //**************************************************************
      //
      // Add annotations
      //
      //**************************************************************
      AffineTransform at = new AffineTransform();
      g2.setTransform(at);

      for ( i = 0; i < totalAnnotations; i++ ) {

         // Select starting location for the annotation
         x1 = xAnnPos[i] - xMin;
         y1 = yAnnPos[i] - yMax;

         // Select font size
         maxCharHeight = 26;
         if ( logXAxis )
            maxStringLength = (int) Math.floor((xMaxLog - Math1.log10( xAnnPos[i] )) * plotSize.width);
         else
            maxStringLength = (int) Math.floor((xMax - xAnnPos[i]) * plotSize.width);
         minFontSize = 8;
         g2.setColor(annotationColor[i]);

         // Get annotation font info.  Try to use Helvetica 12 point regular if it fits
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
         fontMetrics = pickFont(g2, annotations[i], maxStringLength);
         stringWidth = fontMetrics.stringWidth( annotations[i] );
         stringHeight = fontMetrics.getHeight();

         // Draw annotation
         g2.drawString(annotations[i],(int) (x1 * xScale + plotStartX),
                                      (int) (-y1 * yScale + plotStartY));
      }
   }


   //*************************************************
   // Log-log paint method
   //*************************************************

   /**
    * This method actually creates the log-log plot.
    *
    * @param  g  A graphics object.
    */
   private void loglog(Graphics2D g2) { 

      // Declare local variables
      Ellipse2D ell;                     // 2-D ellipse object
      Font font;                         // Font
      int fudge;                         // Positioning fudge factor
      int i, j;                          // loop index
      Line2D line;                       // 2-D line object
      GeneralPath p;                     // General path
      Rectangle2D rect;                  // 2-D rectangle object
      double x1, x2, y1, y2;             // Points on line
      float xs, ys;                      // Starting point for general path

      // Get the size of the "user" area in pixels, and calculate the size and
      // location of the "plot" area in pixels.
      size = getSize();
      plotSize.width = (int) Math.round(size.width * ( 1 - xLeftMargin - xRightMargin));
      plotSize.height = (int) Math.round(size.height * ( 1 - yTopMargin - yBottomMargin));
      plotStartX = (int) Math.round(size.width * xLeftMargin);
      plotStartY = (int) Math.round(size.height * yTopMargin);

      // Set the display limits if in autoscale mode
      if ( autoXScale ) {
         xMax = Array.maxVal ( xVal[0] );
         xMin = Array.minVal ( xVal[0] );
         for ( i = 1; i < totalCurves; i++ ) {
            xMax = Math.max ( xMax, Array.maxVal(xVal[i]) );
            xMin = Math.min ( xMin, Array.minVal(xVal[i]) );
         }

         // If any points are <= 0 with a logarithmic x axis,
         // throw an exception.
         if ( xMin <= 0 ) {
            String s = "Min x plot value <= 0 in logarithmic plot: " + xMin;
            throw new InvalidPlotValueException(s);
         }
 
         // Set limits at even powers of 10
         xMax = Math.pow(10, Math.ceil( Math1.log10(xMax)) );
         xMin = Math.pow(10, Math.floor( Math1.log10(xMin)) );

         // Handle the case where xMin == xMax
         if ( xMin == xMax ) {
            xMax = 1.01 * xMin;
         }

         // Set the logarithmic equivalents
         xMaxLog = Math1.log10(xMax);
         xMinLog = Math1.log10(xMin);
      }
      else {  
         
         // Not autoscale, but check for illegal plot values anyway
         if ( xMin <= 0 ) {
            String s = "Min x plot value <= 0 in logarithmic plot: " + xMin;
            throw new InvalidPlotValueException(s);
         }

         // Handle the case where xMin == xMax
         if ( xMin == xMax ) {
            xMax = 1.01 * xMin;
         }

         // Set the logarithmic equivalents
         xMaxLog = Math1.log10(xMax);
         xMinLog = Math1.log10(xMin);
      }
      
      if ( autoYScale ) {
         yMax = Array.maxVal ( yVal[0] );
         yMin = Array.minVal ( yVal[0] );
         for ( i = 1; i < totalCurves; i++ ) {
            yMax = Math.max ( yMax, Array.maxVal(yVal[i]) );
            yMin = Math.min ( yMin, Array.minVal(yVal[i]) );
         }
         
         // If any points are <= 0 with a logarithmic y axis,
         // throw an exception.
         if ( yMin <= 0 ) {
            String s = "Min y plot value <= 0 in logarithmic plot: " + yMin;
            throw new InvalidPlotValueException(s);
         }
 
         // Set limits at even powers of 10
         yMax = Math.pow(10, Math.ceil( Math1.log10(yMax)) );
         yMin = Math.pow(10, Math.floor( Math1.log10(yMin)) );

         // Handle the case where yMin == yMax
         if ( yMin == yMax ) {
            yMax = 1.01 * yMin;
         }

         // Set the logarithmic equivalents
         yMaxLog = Math1.log10(yMax);
         yMinLog = Math1.log10(yMin);
      }
      else {  
         
         // Not autoscale, but check for illegal plot values anyway
         if ( yMin <= 0 ) {
            String s = "Min y plot value <= 0 in logarithmic plot: " + yMin;
            throw new InvalidPlotValueException(s);
         }

         // Handle the case where yMin == yMax
         if ( yMin == yMax ) {
            yMax = 1.01 * yMin;
         }

         // Set the logarithmic equivalents
         yMaxLog = Math1.log10(yMax);
         yMinLog = Math1.log10(yMin);
      }

      // Calculate locations of tic marks if in automatic mode
      if ( autoXTicMode ) calcXLogTics( xMin, xMax );
      if ( autoYTicMode ) calcYLogTics( yMin, yMax );

      // Recalculate the scale factors every time, since the window
      // could have been resized or the data could have changed.
      if ( Math.abs( xMaxLog - xMinLog ) > 0 )
         xScale = plotSize.width / ( xMaxLog - xMinLog );
      else
         xScale = 1;

      if ( Math.abs( yMaxLog - yMinLog ) > 0 )
         yScale = plotSize.height / ( yMaxLog - yMinLog );
      else
         yScale = 1;

      //**************************************************************
      //
      // Add the bounding box
      //
      //**************************************************************

      // Set the stroke for the bounding box--a thin solid line.
      g2.setStroke(bb);
      g2.setColor(boxColor);

      // Draw the box
      line = new Line2D.Double( plotStartX,                  plotStartY,
                                plotStartX + plotSize.width, plotStartY);
      g2.draw(line);
      line = new Line2D.Double( plotStartX + plotSize.width, plotStartY,
                                plotStartX + plotSize.width, plotStartY + plotSize.height);
      g2.draw(line);
      line = new Line2D.Double( plotStartX + plotSize.width, plotStartY + plotSize.height,
                                plotStartX,                  plotStartY + plotSize.height);
      g2.draw(line);
      line = new Line2D.Double( plotStartX,                  plotStartY + plotSize.height,
                                plotStartX,                  plotStartY);
      g2.draw(line);

      //**************************************************************
      //
      // Add grid lines.
      //
      //**************************************************************
      // Set the stroke for the bounding box--a thin dotted line.
      g2.setStroke(bg);
      g2.setColor(gridColor);

      // Draw the logarithmic x-axis grid lines
      if ( xGridLines ) {
         for ( i = 1; i < xTic.length-1; i++ ) {
            line = new Line2D.Double ( (xTicLog[i]-xMinLog) * xScale + plotStartX,
                                       plotStartY,
                                       (xTicLog[i]-xMinLog) * xScale + plotStartX,
                                       plotStartY + plotSize.height);
            g2.draw(line);
         }
      }

      // Draw the logarithmic y-axis grid lines
      if ( yGridLines ) {
         for ( i = 1; i < yTic.length-1; i++ ) {
            line = new Line2D.Double ( plotStartX,
                                       -(yTicLog[i]-yMaxLog) * yScale + plotStartY,
                                       plotStartX + plotSize.width,
                                       -(yTicLog[i]-yMaxLog) * yScale + plotStartY);
            g2.draw(line);
         }
      }

      //**************************************************************
      //
      // Add tic marks.
      //
      //**************************************************************

      // Set the stroke for the tic marks--a thin solid line.
      g2.setStroke(bt);
      g2.setColor(ticColor);

      // Draw log x-axis tic marks
      if ( xTicMarks ) {
         for ( i = 1; i < xTic.length-1; i++ ) {
            line = new Line2D.Double (  (xTicLog[i]-xMinLog) * xScale + plotStartX,
                                        plotStartY,
                                        (xTicLog[i]-xMinLog) * xScale + plotStartX,
                                        plotStartY + 0.025*plotSize.height);
            g2.draw(line);
            line = new Line2D.Double (  (xTicLog[i]-xMinLog) * xScale + plotStartX,
                                        plotStartY + plotSize.height,
                                        (xTicLog[i]-xMinLog) * xScale + plotStartX,
                                        plotStartY + 0.975*plotSize.height);
            g2.draw(line);
         }
      }

      // Draw log y-axis tic marks
      if ( yTicMarks ) {
         for ( i = 1; i < yTic.length-1; i++ ) {
            line = new Line2D.Double (  plotStartX,
                                        -(yTicLog[i]-yMaxLog) * yScale + plotStartY,
                                        plotStartX + 0.025*plotSize.width,
                                        -(yTicLog[i]-yMaxLog) * yScale + plotStartY);
            g2.draw(line);
            line = new Line2D.Double (  plotStartX + plotSize.width,
                                        -(yTicLog[i]-yMaxLog) * yScale + plotStartY,
                                        plotStartX + 0.975*plotSize.width,
                                        -(yTicLog[i]-yMaxLog) * yScale + plotStartY);
            g2.draw(line);
         }
      }

      //**************************************************************
      //
      // Add tic mark labels.
      //
      //**************************************************************

      // X-axis
      maxCharHeight = (size.height - plotSize.height - plotStartY) / 2;
      if ( logXAxis )
         maxStringLength = (int) ( xRightMargin * plotSize.width );
      else
         maxStringLength = (int) (plotSize.width / (1.2*xTic.length));
      minFontSize = 8;
      g2.setColor(labelColor);

      // Get the tic mark label font
      g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
      fontMetrics = pickFont(g2, xTicS[0], maxStringLength);

      // Log tic mark labels
      for ( i = 0; i < xTicS.length; i++ ) {

         stringWidth = fontMetrics.stringWidth( xTicS[i] );
         stringHeight = fontMetrics.getHeight();

         // Draw tic label
         g2.drawString( xTicS[i],
                        (int) ((xTicLog[i]-xMinLog) * xScale + plotStartX - stringWidth/2),
                        (int) (plotStartY + plotSize.height + 11));
      }

      // Y-axis
      maxCharHeight = (size.height - plotSize.height - plotStartY) / 2;
      maxStringLength = (int) (0.50 * plotStartX);
      minFontSize = 8;
      g2.setColor(labelColor);

      // Get the tic mark label font
      g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
      fontMetrics = pickFont(g2, xTicS[0], maxStringLength);

      // Log tic mark labels
      for ( i = 0; i < yTicS.length; i++ ) {

         stringWidth = fontMetrics.stringWidth( yTicS[i] );
         stringHeight = fontMetrics.getHeight();

         // Draw tic label.  Include a fudge factor on the lowest label to
         // avoid collisions
         if ( i == 0 )
            fudge = 5;
         else
            fudge = 12;
         g2.drawString( yTicS[i],
                        (int) (plotStartX - stringWidth - 6),
                        (int) (-(yTicLog[i]-yMaxLog) * yScale + plotStartY - stringHeight/2 + fudge ));
      }

      //**************************************************************
      //
      // Plot the data set
      //
      //**************************************************************

      // Increment over all curves
      for ( i = 0; i < totalCurves; i++ ) {

         // Plot the current curve 
         if ( plotLine[i] ) {

            // Set up stroke, color, etc.
            g2.setStroke(bs[i]);
            g2.setColor(lineColor[i]);

            for ( j = 0; j < xVal[i].length-1; j++ ) {

               x1 = Math1.log10( xVal[i][j] )   - xMinLog;
               x2 = Math1.log10( xVal[i][j+1] ) - xMinLog;
               y1 = Math1.log10( yVal[i][j] )   - yMaxLog;
               y2 = Math1.log10( yVal[i][j+1] ) - yMaxLog;
               line = new Line2D.Double (  x1 * xScale + plotStartX,
                                          -y1 * yScale + plotStartY,
                                           x2 * xScale + plotStartX,
                                          -y2 * yScale + plotStartY);
               g2.draw(line);
            }
         }

         if ( plotMarker[i] ) {

            // Set up stroke, color, etc.
            if ( bm == null ) {
               bm = new BasicStroke( 2.0f,
                                     BasicStroke.CAP_SQUARE,
                                     BasicStroke.JOIN_MITER,
                                     6.0f, LINESTYLE_SOLID, 0 );
            }
            g2.setStroke(bm);
            g2.setColor(markerColor[i]);

            for ( j = 0; j < xVal[i].length; j++ ) {
               if ( markerStyle[i] == MARKER_SQUARE ) {

                  x1 = Math1.log10( xVal[i][j] )   - xMinLog;
                  y1 = Math1.log10( yVal[i][j] )   - yMaxLog;
                  rect = new Rectangle2D.Double (  x1 * xScale + plotStartX-3,
                                                  -y1 * yScale + plotStartY-3,
                                                   6, 6);
                  g2.draw(rect);
               }
               if ( markerStyle[i] == MARKER_CIRCLE ) {

                  x1 = Math1.log10( xVal[i][j] )   - xMinLog;
                  y1 = Math1.log10( yVal[i][j] )   - yMaxLog;
                  ell = new Ellipse2D.Double (  x1 * xScale + plotStartX-3,
                                               -y1 * yScale + plotStartY-3,
                                                6, 6);
                  g2.draw(ell);
               }
               if ( markerStyle[i] == MARKER_TRIANGLE ) {

                  x1 = Math1.log10( xVal[i][j] )   - xMinLog;
                  y1 = Math1.log10( yVal[i][j] )   - yMaxLog;
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX-3);
                  ys = (float)(-y1 * yScale + plotStartY+2);
                  p.moveTo( xs,      ys      );
                  p.lineTo( xs+6.0f, ys      );
                  p.lineTo( xs+3.0f, ys-5.2f );
                  p.closePath();
                  g2.draw(p);
               }
               if ( markerStyle[i] == MARKER_DIAMOND ) {

                  x1 = Math1.log10( xVal[i][j] )   - xMinLog;
                  y1 = Math1.log10( yVal[i][j] )   - yMaxLog;
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX);
                  ys = (float)(-y1 * yScale + plotStartY);
                  p.moveTo( xs-4.0f, ys      );
                  p.lineTo( xs     , ys+4.0f );
                  p.lineTo( xs+4.0f, ys      );
                  p.lineTo( xs     , ys-4.0f );
                  p.closePath();
                  g2.draw(p);
               }
               if ( markerStyle[i] == MARKER_DOWNTRIANGLE ) {

                  x1 = Math1.log10( xVal[i][j] )   - xMinLog;
                  y1 = Math1.log10( yVal[i][j] )   - yMaxLog;
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX-3);
                  ys = (float)(-y1 * yScale + plotStartY+3);
                  p.moveTo( xs,      ys-5.2f );
                  p.lineTo( xs+6.0f, ys-5.2f );
                  p.lineTo( xs+3.0f, ys      );
                  p.closePath();
                  g2.draw(p);
               }
            }
         }
      }

      //**************************************************************
      //
      // Add title and labels
      //
      //**************************************************************
      maxCharHeight = plotStartY - 15;
      maxStringLength = 3 * plotSize.width / 4;
      minFontSize = 12;
      g2.setColor(labelColor);

      if ( validTitle ) {
         // Get title font info.  Try to use Helvetica 20 point BOLD if it fits
         g2.setFont(font = new Font("Helvetica", Font.BOLD, 18));
         fontMetrics = pickFont(g2, title, maxStringLength);
         stringWidth = fontMetrics.stringWidth( title );
         stringHeight = fontMetrics.getHeight();

         // Draw title
         g2.drawString(title,plotStartX + plotSize.width/2 - stringWidth/2,
                       plotStartY/2 + stringHeight/2);
      }

      if ( validXLabel ) {
         // Get x label font info.
         maxStringLength = plotSize.width;
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 18));
         fontMetrics = pickFont(g2, xLabel, maxStringLength);
         stringWidth = fontMetrics.stringWidth( xLabel );
         stringHeight = fontMetrics.getHeight();

         // Draw x label
         g2.drawString(xLabel,plotStartX + plotSize.width/2 - stringWidth/2,
                       3*plotStartY/2 + plotSize.height + stringHeight/2 );
      }


      if ( validYLabel ) {
         // Get y label font info.
         maxStringLength = plotSize.height;
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 18));
         fontMetrics = pickFont(g2, yLabel, maxStringLength);
         stringWidth = fontMetrics.stringWidth( yLabel );
         stringHeight = fontMetrics.getHeight();

         // Draw y label
         AffineTransform at = new AffineTransform();
         at.rotate(-Math.PI/2);
         g2.setTransform(at);

         int xPos = plotStartY/2 + plotSize.height/2 + stringWidth/2;
         int yPos = (int) (1.05*stringHeight);
         g2.drawString(yLabel,-xPos, yPos );
      }

      //**************************************************************
      //
      // Add annotations
      //
      //**************************************************************
      AffineTransform at = new AffineTransform();
      g2.setTransform(at);

      for ( i = 0; i < totalAnnotations; i++ ) {

         // Select starting location for the annotation
         x1 = Math1.log10( xAnnPos[i] ) - xMinLog;
         y1 = Math1.log10( yAnnPos[i] ) - yMaxLog;

         maxCharHeight = 26;
         if ( logXAxis )
            maxStringLength = (int) Math.floor((xMaxLog - Math1.log10( xAnnPos[i] )) * plotSize.width);
         else
            maxStringLength = (int) Math.floor((xMax - xAnnPos[i]) * plotSize.width);
         minFontSize = 8;
         g2.setColor(annotationColor[i]);

         // Get annotation font info.  Try to use Helvetica 12 point regular if it fits
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
         fontMetrics = pickFont(g2, annotations[i], maxStringLength);
         stringWidth = fontMetrics.stringWidth( annotations[i] );
         stringHeight = fontMetrics.getHeight();

         // Draw annotation
         g2.drawString(annotations[i],(int) (x1 * xScale + plotStartX),
                                      (int) (-y1 * yScale + plotStartY));
      }
   }


   //*************************************************
   // Semilogx plot
   //*************************************************

   /**
    * This method actually creates the semilogx plot.
    *
    * @param  g  A graphics object.
    */
   private void semilogx(Graphics2D g2) {

      // Declare local variables
      Ellipse2D ell;                     // 2-D ellipse object
      Font font;                         // Font
      int fudge;                         // Positioning fudge factor
      int i, j;                          // loop index
      Line2D line;                       // 2-D line object
      GeneralPath p;                     // General path
      Rectangle2D rect;                  // 2-D rectangle object
      double x1, x2, y1, y2;             // Points on line
      float xs, ys;                      // Starting point for general path

      // Get the size of the "user" area in pixels, and calculate the size and
      // location of the "plot" area in pixels.
      size = getSize();
      plotSize.width = (int) Math.round(size.width * ( 1 - xLeftMargin - xRightMargin));
      plotSize.height = (int) Math.round(size.height * ( 1 - yTopMargin - yBottomMargin));
      plotStartX = (int) Math.round(size.width * xLeftMargin);
      plotStartY = (int) Math.round(size.height * yTopMargin);

      // Set the display limits if in autoscale mode
      if ( autoXScale ) {
         xMax = Array.maxVal ( xVal[0] );
         xMin = Array.minVal ( xVal[0] );
         for ( i = 1; i < totalCurves; i++ ) {
            xMax = Math.max ( xMax, Array.maxVal(xVal[i]) );
            xMin = Math.min ( xMin, Array.minVal(xVal[i]) );
         }

         // If any points are <= 0 with a logarithmic x axis,
         // throw an exception.
         if ( xMin <= 0 ) {
            String s = "Min x plot value <= 0 in logarithmic plot: " + xMin;
            throw new InvalidPlotValueException(s);
         }
 
         // Set limits at even powers of 10
         xMax = Math.pow(10, Math.ceil( Math1.log10(xMax)) );
         xMin = Math.pow(10, Math.floor( Math1.log10(xMin)) );

         // Handle the case where xMin == xMax
         if ( xMin == xMax ) {
            xMax = 1.01 * xMin;
         }

         // Set the logarithmic equivalents
         xMaxLog = Math1.log10(xMax);
         xMinLog = Math1.log10(xMin);
      }
      else {  
         
         // Not autoscale, but check for illegal plot values anyway
         if ( xMin <= 0 ) {
            String s = "Min x plot value <= 0 in logarithmic plot: " + xMin;
            throw new InvalidPlotValueException(s);
         }

         // Handle the case where xMin == xMax
         if ( xMin == xMax ) {
            xMax = 1.01 * xMin;
         }

         // Set the logarithmic equivalents
         xMaxLog = Math1.log10(xMax);
         xMinLog = Math1.log10(xMin);
      }
      
      if ( autoYScale ) {
         yMax = Array.maxVal ( yVal[0] );
         yMin = Array.minVal ( yVal[0] );
         for ( i = 1; i < totalCurves; i++ ) {
            yMax = Math.max ( yMax, Array.maxVal(yVal[i]) );
            yMin = Math.min ( yMin, Array.minVal(yVal[i]) );
         }
      }

      // Calculate locations of tic marks if in automatic mode
      if ( autoXTicMode ) calcXLogTics( xMin, xMax );
      if ( autoYTicMode ) calcYTics( yMin, yMax );

      // Recalculate the scale factors every time, since the window
      // could have been resized or the data could have changed.
      if ( Math.abs( xMaxLog - xMinLog ) > 0 )
         xScale = plotSize.width / ( xMaxLog - xMinLog );
      else
         xScale = 1;

      if ( Math.abs( yMax - yMin ) > 0 )
         yScale = plotSize.height / ( yMax - yMin );
      else
         yScale = 1;

      //**************************************************************
      //
      // Add the bounding box
      //
      //**************************************************************

      // Set the stroke for the bounding box--a thin solid line.
      g2.setStroke(bb);
      g2.setColor(boxColor);

      // Draw the box
      line = new Line2D.Double( plotStartX,                  plotStartY,
                                plotStartX + plotSize.width, plotStartY);
      g2.draw(line);
      line = new Line2D.Double( plotStartX + plotSize.width, plotStartY,
                                plotStartX + plotSize.width, plotStartY + plotSize.height);
      g2.draw(line);
      line = new Line2D.Double( plotStartX + plotSize.width, plotStartY + plotSize.height,
                                plotStartX,                  plotStartY + plotSize.height);
      g2.draw(line);
      line = new Line2D.Double( plotStartX,                  plotStartY + plotSize.height,
                                plotStartX,                  plotStartY);
      g2.draw(line);

      //**************************************************************
      //
      // Add grid lines.
      //
      //**************************************************************
      // Set the stroke for the bounding box--a thin dotted line.
      g2.setStroke(bg);
      g2.setColor(gridColor);

      // Draw the logarithmic x-axis grid lines
      if ( xGridLines ) {
         for ( i = 1; i < xTic.length-1; i++ ) {
            line = new Line2D.Double ( (xTicLog[i]-xMinLog) * xScale + plotStartX,
                                       plotStartY,
                                       (xTicLog[i]-xMinLog) * xScale + plotStartX,
                                       plotStartY + plotSize.height);
            g2.draw(line);
         }
      }

      // Draw the linear y-axis grid lines
      if ( yGridLines ) {
         for ( i = 1; i < yTic.length-1; i++ ) {
            line = new Line2D.Double ( plotStartX,
                                       -(yTic[i]-yMax) * yScale + plotStartY,
                                       plotStartX + plotSize.width,
                                       -(yTic[i]-yMax) * yScale + plotStartY);
            g2.draw(line);
         }
      }

      //**************************************************************
      //
      // Add tic marks.
      //
      //**************************************************************

      // Set the stroke for the tic marks--a thin solid line.
      g2.setStroke(bt);
      g2.setColor(ticColor);

      // Draw log x-axis tic marks
      if ( xTicMarks ) {
         for ( i = 1; i < xTic.length-1; i++ ) {
            line = new Line2D.Double (  (xTicLog[i]-xMinLog) * xScale + plotStartX,
                                        plotStartY,
                                        (xTicLog[i]-xMinLog) * xScale + plotStartX,
                                        plotStartY + 0.025*plotSize.height);
            g2.draw(line);
            line = new Line2D.Double (  (xTicLog[i]-xMinLog) * xScale + plotStartX,
                                        plotStartY + plotSize.height,
                                        (xTicLog[i]-xMinLog) * xScale + plotStartX,
                                        plotStartY + 0.975*plotSize.height);
            g2.draw(line);
         }
      }

      // Draw linear y-axis tic marks
      if ( yTicMarks ) {
         for ( i = 1; i < yTic.length-1; i++ ) {
            line = new Line2D.Double (  plotStartX,
                                        -(yTic[i]-yMax) * yScale + plotStartY,
                                        plotStartX + 0.025*plotSize.width,
                                        -(yTic[i]-yMax) * yScale + plotStartY);
            g2.draw(line);
            line = new Line2D.Double (  plotStartX + plotSize.width,
                                        -(yTic[i]-yMax) * yScale + plotStartY,
                                        plotStartX + 0.975*plotSize.width,
                                        -(yTic[i]-yMax) * yScale + plotStartY);
            g2.draw(line);
         }
      }

      //**************************************************************
      //
      // Add tic mark labels.
      //
      //**************************************************************

      // X-axis
      maxCharHeight = (size.height - plotSize.height - plotStartY) / 2;
      if ( logXAxis )
         maxStringLength = (int) ( xRightMargin * plotSize.width );
      else
         maxStringLength = (int) (plotSize.width / (1.2*xTic.length));
      minFontSize = 8;
      g2.setColor(labelColor);

      // Get the tic mark label font
      g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
      fontMetrics = pickFont(g2, xTicS[0], maxStringLength);

      // Log tic mark labels
      for ( i = 0; i < xTicS.length; i++ ) {

         stringWidth = fontMetrics.stringWidth( xTicS[i] );
         stringHeight = fontMetrics.getHeight();

         // Draw tic label
         g2.drawString( xTicS[i],
                        (int) ((xTicLog[i]-xMinLog) * xScale + plotStartX - stringWidth/2),
                        (int) (plotStartY + plotSize.height + 11));
      }

      // Y-axis
      maxCharHeight = (size.height - plotSize.height - plotStartY) / 2;
      maxStringLength = (int) (0.50 * plotStartX);
      minFontSize = 8;
      g2.setColor(labelColor);

      // Get the tic mark label font
      g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
      fontMetrics = pickFont(g2, xTicS[0], maxStringLength);

      // Linear tic mark labels
      for ( i = 0; i < yTicS.length; i++ ) {

         stringWidth = fontMetrics.stringWidth( yTicS[i] );
         stringHeight = fontMetrics.getHeight();

         // Draw tic label.  Include a fudge factor on the lowest label to
         // avoid collisions
         if ( i == 0 )
            fudge = 5;
         else
            fudge = 12;
         g2.drawString( yTicS[i],
                        (int) (plotStartX - stringWidth - 6),
                        (int) (-(yTic[i]-yMax) * yScale + plotStartY - stringHeight/2 + fudge ));
      }

      //**************************************************************
      //
      // Plot the data set
      //
      //**************************************************************

      // Increment over all curves
      for ( i = 0; i < totalCurves; i++ ) {

         // Plot the current curve 
         if ( plotLine[i] ) {

            // Set up stroke, color, etc.
            g2.setStroke(bs[i]);
            g2.setColor(lineColor[i]);

            for ( j = 0; j < xVal[i].length-1; j++ ) {

               x1 = Math1.log10( xVal[i][j] )   - xMinLog;
               x2 = Math1.log10( xVal[i][j+1] ) - xMinLog;
               y1 = yVal[i][j] - yMax;
               y2 = yVal[i][j+1] - yMax;
               line = new Line2D.Double (  x1 * xScale + plotStartX,
                                          -y1 * yScale + plotStartY,
                                           x2 * xScale + plotStartX,
                                          -y2 * yScale + plotStartY);
               g2.draw(line);
            }
         }

         if ( plotMarker[i] ) {

            // Set up stroke, color, etc.
            if ( bm == null ) {
               bm = new BasicStroke( 2.0f,
                                     BasicStroke.CAP_SQUARE,
                                     BasicStroke.JOIN_MITER,
                                     6.0f, LINESTYLE_SOLID, 0 );
            }
            g2.setStroke(bm);
            g2.setColor(markerColor[i]);

            for ( j = 0; j < xVal[i].length; j++ ) {
               if ( markerStyle[i] == MARKER_SQUARE ) {

                  x1 = Math1.log10( xVal[i][j] ) - xMinLog;
                  y1 = yVal[i][j] - yMax;
                  rect = new Rectangle2D.Double (  x1 * xScale + plotStartX-3,
                                                  -y1 * yScale + plotStartY-3,
                                                   6, 6);
                  g2.draw(rect);
               }
               if ( markerStyle[i] == MARKER_CIRCLE ) {

                  x1 = Math1.log10( xVal[i][j] ) - xMinLog;
                  y1 = yVal[i][j] - yMax;
                  ell = new Ellipse2D.Double (  x1 * xScale + plotStartX-3,
                                               -y1 * yScale + plotStartY-3,
                                                6, 6);
                  g2.draw(ell);
               }
               if ( markerStyle[i] == MARKER_TRIANGLE ) {

                  x1 = Math1.log10( xVal[i][j] ) - xMinLog;
                  y1 = yVal[i][j] - yMax;
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX-3);
                  ys = (float)(-y1 * yScale + plotStartY+2);
                  p.moveTo( xs,      ys      );
                  p.lineTo( xs+6.0f, ys      );
                  p.lineTo( xs+3.0f, ys-5.2f );
                  p.closePath();
                  g2.draw(p);
               }
               if ( markerStyle[i] == MARKER_DIAMOND ) {

                  x1 = Math1.log10( xVal[i][j] ) - xMinLog;
                  y1 = yVal[i][j] - yMax;
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX);
                  ys = (float)(-y1 * yScale + plotStartY);
                  p.moveTo( xs-4.0f, ys      );
                  p.lineTo( xs     , ys+4.0f );
                  p.lineTo( xs+4.0f, ys      );
                  p.lineTo( xs     , ys-4.0f );
                  p.closePath();
                  g2.draw(p);
               }
               if ( markerStyle[i] == MARKER_DOWNTRIANGLE ) {

                  x1 = Math1.log10( xVal[i][j] ) - xMinLog;
                  y1 = yVal[i][j] - yMax;
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX-3);
                  ys = (float)(-y1 * yScale + plotStartY+3);
                  p.moveTo( xs,      ys-5.2f );
                  p.lineTo( xs+6.0f, ys-5.2f );
                  p.lineTo( xs+3.0f, ys      );
                  p.closePath();
                  g2.draw(p);
               }
            }
         }
      }

      //**************************************************************
      //
      // Add title and labels
      //
      //**************************************************************
      maxCharHeight = plotStartY - 15;
      maxStringLength = 3 * plotSize.width / 4;
      minFontSize = 12;
      g2.setColor(labelColor);

      if ( validTitle ) {
         // Get title font info.  Try to use Helvetica 20 point BOLD if it fits
         g2.setFont(font = new Font("Helvetica", Font.BOLD, 18));
         fontMetrics = pickFont(g2, title, maxStringLength);
         stringWidth = fontMetrics.stringWidth( title );
         stringHeight = fontMetrics.getHeight();

         // Draw title
         g2.drawString(title,plotStartX + plotSize.width/2 - stringWidth/2,
                       plotStartY/2 + stringHeight/2);
      }

      if ( validXLabel ) {
         // Get x label font info.
         maxStringLength = plotSize.width;
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 18));
         fontMetrics = pickFont(g2, xLabel, maxStringLength);
         stringWidth = fontMetrics.stringWidth( xLabel );
         stringHeight = fontMetrics.getHeight();

         // Draw x label
         g2.drawString(xLabel,plotStartX + plotSize.width/2 - stringWidth/2,
                       3*plotStartY/2 + plotSize.height + stringHeight/2 );
      }


      if ( validYLabel ) {
         // Get y label font info.
         maxStringLength = plotSize.height;
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 18));
         fontMetrics = pickFont(g2, yLabel, maxStringLength);
         stringWidth = fontMetrics.stringWidth( yLabel );
         stringHeight = fontMetrics.getHeight();

         // Draw y label
         AffineTransform at = new AffineTransform();
         at.rotate(-Math.PI/2);
         g2.setTransform(at);

         int xPos = plotStartY/2 + plotSize.height/2 + stringWidth/2;
         int yPos = (int) (1.05*stringHeight);
         g2.drawString(yLabel,-xPos, yPos );
      }

      //**************************************************************
      //
      // Add annotations
      //
      //**************************************************************
      AffineTransform at = new AffineTransform();
      g2.setTransform(at);

      for ( i = 0; i < totalAnnotations; i++ ) {

         // Select starting location for the annotation
         x1 = Math1.log10( xAnnPos[i] ) - xMinLog;
         y1 = yAnnPos[i] - yMax;

         maxCharHeight = 26;
         if ( logXAxis )
            maxStringLength = (int) Math.floor((xMaxLog - Math1.log10( xAnnPos[i] )) * plotSize.width);
         else
            maxStringLength = (int) Math.floor((xMax - xAnnPos[i]) * plotSize.width);
         minFontSize = 8;
         g2.setColor(annotationColor[i]);

         // Get annotation font info.  Try to use Helvetica 12 point regular if it fits
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
         fontMetrics = pickFont(g2, annotations[i], maxStringLength);
         stringWidth = fontMetrics.stringWidth( annotations[i] );
         stringHeight = fontMetrics.getHeight();

         // Draw annotation
         g2.drawString(annotations[i],(int) (x1 * xScale + plotStartX),
                                      (int) (-y1 * yScale + plotStartY));
      }
   }

   //*************************************************
   // Semilogy plot
   //*************************************************

   /**
    * This method actually creates the semilogy plot.
    *
    * @param  g  A graphics object.
    */
   private void semilogy(Graphics2D g2) {

      // Declare local variables
      Ellipse2D ell;                     // 2-D ellipse object
      Font font;                         // Font
      int fudge;                         // Positioning fudge factor
      int i, j;                          // loop index
      Line2D line;                       // 2-D line object
      GeneralPath p;                     // General path
      Rectangle2D rect;                  // 2-D rectangle object
      double x1, x2, y1, y2;             // Points on line
      float xs, ys;                      // Starting point for general path

      // Get the size of the "user" area in pixels, and calculate the size and
      // location of the "plot" area in pixels.
      size = getSize();
      plotSize.width = (int) Math.round(size.width * ( 1 - xLeftMargin - xRightMargin));
      plotSize.height = (int) Math.round(size.height * ( 1 - yTopMargin - yBottomMargin));
      plotStartX = (int) Math.round(size.width * xLeftMargin);
      plotStartY = (int) Math.round(size.height * yTopMargin);

      // Set the display limits if in autoscale mode
      if ( autoXScale ) {
         xMax = Array.maxVal ( xVal[0] );
         xMin = Array.minVal ( xVal[0] );
         for ( i = 1; i < totalCurves; i++ ) {
            xMax = Math.max ( xMax, Array.maxVal(xVal[i]) );
            xMin = Math.min ( xMin, Array.minVal(xVal[i]) );
         }
      }

      if ( autoYScale ) {
         yMax = Array.maxVal ( yVal[0] );
         yMin = Array.minVal ( yVal[0] );
         for ( i = 1; i < totalCurves; i++ ) {
            yMax = Math.max ( yMax, Array.maxVal(yVal[i]) );
            yMin = Math.min ( yMin, Array.minVal(yVal[i]) );
         }
         
         // If any points are <= 0 with a logarithmic y axis,
         // throw an exception.
         if ( yMin <= 0 ) {
            String s = "Min y plot value <= 0 in logarithmic plot: " + yMin;
            throw new InvalidPlotValueException(s);
         }
 
         // Set limits at even powers of 10
         yMax = Math.pow(10, Math.ceil( Math1.log10(yMax)) );
         yMin = Math.pow(10, Math.floor( Math1.log10(yMin)) );

         // Handle the case where yMin == yMax
         if ( yMin == yMax ) {
            yMax = 1.01 * yMin;
         }

         // Set the logarithmic equivalents
         yMaxLog = Math1.log10(yMax);
         yMinLog = Math1.log10(yMin);
      }
      else {  
         
         // Not autoscale, but check for illegal plot values anyway
         if ( yMin <= 0 ) {
            String s = "Min y plot value <= 0 in logarithmic plot: " + yMin;
            throw new InvalidPlotValueException(s);
         }

         // Handle the case where yMin == yMax
         if ( yMin == yMax ) {
            yMax = 1.01 * yMin;
         }

         // Set the logarithmic equivalents
         yMaxLog = Math1.log10(yMax);
         yMinLog = Math1.log10(yMin);
      }

      // Calculate locations of tic marks if in automatic mode
      if ( autoXTicMode ) calcXTics( xMin, xMax );
      if ( autoYTicMode ) calcYLogTics( yMin, yMax );

      // Recalculate the scale factors every time, since the window
      // could have been resized or the data could have changed.
      if ( Math.abs( xMax - xMin ) > 0 )
         xScale = plotSize.width / ( xMax - xMin );
      else
         xScale = 1;

      if ( Math.abs( yMaxLog - yMinLog ) > 0 )
         yScale = plotSize.height / ( yMaxLog - yMinLog );
      else
         yScale = 1;

      //**************************************************************
      //
      // Add the bounding box
      //
      //**************************************************************

      // Set the stroke for the bounding box--a thin solid line.
      g2.setStroke(bb);
      g2.setColor(boxColor);

      // Draw the box
      line = new Line2D.Double( plotStartX,                  plotStartY,
                                plotStartX + plotSize.width, plotStartY);
      g2.draw(line);
      line = new Line2D.Double( plotStartX + plotSize.width, plotStartY,
                                plotStartX + plotSize.width, plotStartY + plotSize.height);
      g2.draw(line);
      line = new Line2D.Double( plotStartX + plotSize.width, plotStartY + plotSize.height,
                                plotStartX,                  plotStartY + plotSize.height);
      g2.draw(line);
      line = new Line2D.Double( plotStartX,                  plotStartY + plotSize.height,
                                plotStartX,                  plotStartY);
      g2.draw(line);

      //**************************************************************
      //
      // Add grid lines.
      //
      //**************************************************************
      // Set the stroke for the bounding box--a thin dotted line.
      g2.setStroke(bg);
      g2.setColor(gridColor);

      // Draw the linear x-axis grid lines
      if ( xGridLines ) {
         for ( i = 1; i < xTic.length-1; i++ ) {
            line = new Line2D.Double ( (xTic[i]-xMin) * xScale + plotStartX,
                                       plotStartY,
                                       (xTic[i]-xMin) * xScale + plotStartX,
                                       plotStartY + plotSize.height);
            g2.draw(line);
         }
      }

      // Draw the logarithmic y-axis grid lines
      if ( yGridLines ) {
         for ( i = 1; i < yTic.length-1; i++ ) {
            line = new Line2D.Double ( plotStartX,
                                       -(yTicLog[i]-yMaxLog) * yScale + plotStartY,
                                       plotStartX + plotSize.width,
                                       -(yTicLog[i]-yMaxLog) * yScale + plotStartY);
            g2.draw(line);
         }
      }

      //**************************************************************
      //
      // Add tic marks.
      //
      //**************************************************************

      // Set the stroke for the tic marks--a thin solid line.
      if ( bt == null ) {
         float ticStyle[] = {12.0f, 0.0f};
         bt = new BasicStroke( 1.0f,
                               BasicStroke.CAP_SQUARE,
                               BasicStroke.JOIN_MITER,
                               6.0f, ticStyle, 0 );
      }
      g2.setStroke(bt);
      g2.setColor(ticColor);

      // Draw linear x-axis tic marks
      if ( xTicMarks ) {
         for ( i = 1; i < xTic.length-1; i++ ) {
            line = new Line2D.Double (  (xTic[i]-xMin) * xScale + plotStartX,
                                        plotStartY,
                                        (xTic[i]-xMin) * xScale + plotStartX,
                                        plotStartY + 0.025*plotSize.height);
            g2.draw(line);
            line = new Line2D.Double (  (xTic[i]-xMin) * xScale + plotStartX,
                                        plotStartY + plotSize.height,
                                        (xTic[i]-xMin) * xScale + plotStartX,
                                        plotStartY + 0.975*plotSize.height);
            g2.draw(line);
         }
      }

      // Draw log y-axis tic marks
      if ( yTicMarks ) {
         for ( i = 1; i < yTic.length-1; i++ ) {
            line = new Line2D.Double (  plotStartX,
                                        -(yTicLog[i]-yMaxLog) * yScale + plotStartY,
                                        plotStartX + 0.025*plotSize.width,
                                        -(yTicLog[i]-yMaxLog) * yScale + plotStartY);
            g2.draw(line);
            line = new Line2D.Double (  plotStartX + plotSize.width,
                                        -(yTicLog[i]-yMaxLog) * yScale + plotStartY,
                                        plotStartX + 0.975*plotSize.width,
                                        -(yTicLog[i]-yMaxLog) * yScale + plotStartY);
            g2.draw(line);
         }
      }

      //**************************************************************
      //
      // Add tic mark labels.
      //
      //**************************************************************

      // X-axis
      maxCharHeight = (size.height - plotSize.height - plotStartY) / 2;
      if ( logXAxis )
         maxStringLength = (int) ( xRightMargin * plotSize.width );
      else
         maxStringLength = (int) (plotSize.width / (1.2*xTic.length));
      minFontSize = 8;
      g2.setColor(labelColor);

      // Get the tic mark label font
      g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
      fontMetrics = pickFont(g2, xTicS[0], maxStringLength);

      // Linear tic mark labels
      for ( i = 0; i < xTicS.length; i++ ) {

         stringWidth = fontMetrics.stringWidth( xTicS[i] );
         stringHeight = fontMetrics.getHeight();

         // Draw tic label
         g2.drawString( xTicS[i],
                        (int) ((xTic[i]-xMin) * xScale + plotStartX - stringWidth/2),
                        (int) (plotStartY + plotSize.height + 11));
      }

      // Y-axis
      maxCharHeight = (size.height - plotSize.height - plotStartY) / 2;
      maxStringLength = (int) (0.50 * plotStartX);
      minFontSize = 8;
      g2.setColor(labelColor);

      // Get the tic mark label font
      g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
      fontMetrics = pickFont(g2, xTicS[0], maxStringLength);

      // Log tic mark labels
      for ( i = 0; i < yTicS.length; i++ ) {

         stringWidth = fontMetrics.stringWidth( yTicS[i] );
         stringHeight = fontMetrics.getHeight();

         // Draw tic label.  Include a fudge factor on the lowest label to
         // avoid collisions
         if ( i == 0 )
            fudge = 5;
         else
            fudge = 12;
         g2.drawString( yTicS[i],
                        (int) (plotStartX - stringWidth - 6),
                        (int) (-(yTicLog[i]-yMaxLog) * yScale + plotStartY - stringHeight/2 + fudge ));
      }

      //**************************************************************
      //
      // Plot the data set
      //
      //**************************************************************

      // Increment over all curves
      for ( i = 0; i < totalCurves; i++ ) {

         // Plot the current curve
         if ( plotLine[i] ) {

            // Set up stroke, color, etc.
            g2.setStroke(bs[i]);
            g2.setColor(lineColor[i]);

            for ( j = 0; j < xVal[i].length-1; j++ ) {

               x1 = xVal[i][j] - xMin;
               x2 = xVal[i][j+1] - xMin;
               y1 = Math1.log10( yVal[i][j] )   - yMaxLog;
               y2 = Math1.log10( yVal[i][j+1] ) - yMaxLog;
               line = new Line2D.Double (  x1 * xScale + plotStartX,
                                          -y1 * yScale + plotStartY,
                                           x2 * xScale + plotStartX,
                                          -y2 * yScale + plotStartY);
               g2.draw(line);
            }
         }

         if ( plotMarker[i] ) {

            // Set up stroke, color, etc.
            if ( bm == null ) {
               bm = new BasicStroke( 2.0f,
                                     BasicStroke.CAP_SQUARE,
                                     BasicStroke.JOIN_MITER,
                                     6.0f, LINESTYLE_SOLID, 0 );
            }
            g2.setStroke(bm);
            g2.setColor(markerColor[i]);

            for ( j = 0; j < xVal[i].length; j++ ) {
               if ( markerStyle[i] == MARKER_SQUARE ) {

                  x1 = xVal[i][j] - xMin;
                  y1 = Math1.log10( yVal[i][j] ) - yMaxLog;
                  rect = new Rectangle2D.Double (  x1 * xScale + plotStartX-3,
                                                  -y1 * yScale + plotStartY-3,
                                                   6, 6);
                  g2.draw(rect);
               }
               if ( markerStyle[i] == MARKER_CIRCLE ) {

                  x1 = xVal[i][j] - xMin;
                  y1 = Math1.log10( yVal[i][j] ) - yMaxLog;
                  ell = new Ellipse2D.Double (  x1 * xScale + plotStartX-3,
                                               -y1 * yScale + plotStartY-3,
                                                6, 6);
                  g2.draw(ell);
               }
               if ( markerStyle[i] == MARKER_TRIANGLE ) {

                  x1 = xVal[i][j] - xMin;
                  y1 = Math1.log10( yVal[i][j] ) - yMaxLog;
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX-3);
                  ys = (float)(-y1 * yScale + plotStartY+2);
                  p.moveTo( xs,      ys      );
                  p.lineTo( xs+6.0f, ys      );
                  p.lineTo( xs+3.0f, ys-5.2f );
                  p.closePath();
                  g2.draw(p);
               }
               if ( markerStyle[i] == MARKER_DIAMOND ) {

                  x1 = xVal[i][j] - xMin;
                  y1 = Math1.log10( yVal[i][j] ) - yMaxLog;
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX);
                  ys = (float)(-y1 * yScale + plotStartY);
                  p.moveTo( xs-4.0f, ys      );
                  p.lineTo( xs     , ys+4.0f );
                  p.lineTo( xs+4.0f, ys      );
                  p.lineTo( xs     , ys-4.0f );
                  p.closePath();
                  g2.draw(p);
               }
               if ( markerStyle[i] == MARKER_DOWNTRIANGLE ) {

                  x1 = xVal[i][j] - xMin;
                  y1 = Math1.log10( yVal[i][j] ) - yMaxLog;
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX-3);
                  ys = (float)(-y1 * yScale + plotStartY+3);
                  p.moveTo( xs,      ys-5.2f );
                  p.lineTo( xs+6.0f, ys-5.2f );
                  p.lineTo( xs+3.0f, ys      );
                  p.closePath();
                  g2.draw(p);
               }
            }
         }
      }

      //**************************************************************
      //
      // Add title and labels
      //
      //**************************************************************
      maxCharHeight = plotStartY - 15;
      maxStringLength = 3 * plotSize.width / 4;
      minFontSize = 12;
      g2.setColor(labelColor);

      if ( validTitle ) {
         // Get title font info.  Try to use Helvetica 20 point BOLD if it fits
         g2.setFont(font = new Font("Helvetica", Font.BOLD, 18));
         fontMetrics = pickFont(g2, title, maxStringLength);
         stringWidth = fontMetrics.stringWidth( title );
         stringHeight = fontMetrics.getHeight();

         // Draw title
         g2.drawString(title,plotStartX + plotSize.width/2 - stringWidth/2,
                       plotStartY/2 + stringHeight/2);
      }

      if ( validXLabel ) {
         // Get x label font info.
         maxStringLength = plotSize.width;
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 18));
         fontMetrics = pickFont(g2, xLabel, maxStringLength);
         stringWidth = fontMetrics.stringWidth( xLabel );
         stringHeight = fontMetrics.getHeight();

         // Draw x label
         g2.drawString(xLabel,plotStartX + plotSize.width/2 - stringWidth/2,
                       3*plotStartY/2 + plotSize.height + stringHeight/2 );
      }


      if ( validYLabel ) {
         // Get y label font info.
         maxStringLength = plotSize.height;
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 18));
         fontMetrics = pickFont(g2, yLabel, maxStringLength);
         stringWidth = fontMetrics.stringWidth( yLabel );
         stringHeight = fontMetrics.getHeight();

         // Draw y label
         AffineTransform at = new AffineTransform();
         at.rotate(-Math.PI/2);
         g2.setTransform(at);

         int xPos = plotStartY/2 + plotSize.height/2 + stringWidth/2;
         int yPos = (int) (1.05*stringHeight);
         g2.drawString(yLabel,-xPos, yPos );
      }

      //**************************************************************
      //
      // Add annotations
      //
      //**************************************************************
      AffineTransform at = new AffineTransform();
      g2.setTransform(at);

      for ( i = 0; i < totalAnnotations; i++ ) {

         // Select starting location for the annotation
         x1 = xAnnPos[i] - xMin;
         y1 = Math1.log10( yAnnPos[i] ) - yMaxLog;

         maxCharHeight = 26;
         if ( logXAxis )
            maxStringLength = (int) Math.floor((xMaxLog - Math1.log10( xAnnPos[i] )) * plotSize.width);
         else
            maxStringLength = (int) Math.floor((xMax - xAnnPos[i]) * plotSize.width);
         minFontSize = 8;
         g2.setColor(annotationColor[i]);

         // Get annotation font info.  Try to use Helvetica 12 point regular if it fits
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
         fontMetrics = pickFont(g2, annotations[i], maxStringLength);
         stringWidth = fontMetrics.stringWidth( annotations[i] );
         stringHeight = fontMetrics.getHeight();

         // Draw annotation
         g2.drawString(annotations[i],(int) (x1 * xScale + plotStartX),
                                      (int) (-y1 * yScale + plotStartY));
      }
   }


   //*************************************************
   // Paint polar plot
   //*************************************************

   /**
    * This method creates a polar plot.
    *
    * @param  g  A graphics object.
    */
   private void polar(Graphics2D g2) {

      // Declare local variables
      Ellipse2D ell;                     // 2-D ellipse object
      Font font;                         // Font
      int fudge;                         // Positioning fudge factor
      int i, j;                          // loop index
      Line2D line;                       // 2-D line object
      GeneralPath p;                     // General path
      Rectangle2D rect;                  // 2-D rectangle object
      String s;                          // Temp string
      double temp, temp1, temp2;         // Scratch variables
      double x1, x2, y1, y2;             // Points on line
      float xs, ys;                      // Starting point for general path

      // Get the size of the "user" area in pixels, and calculate the size and
      // location of the "plot" area in pixels.
      size = getSize();
      plotSize.width = (int) Math.round(size.width * ( 1 - xLeftMargin - xRightMargin));
      plotSize.height = (int) Math.round(size.height * ( 1 - yTopMargin - yBottomMargin));
      plotStartX = (int) Math.round(size.width * xLeftMargin);
      plotStartY = (int) Math.round(size.height * yTopMargin);

      // Set the display limits if in autoscale mode
      if ( autoXScale || autoYScale ) {
         
         // First, find the data limits in (x,y) space
         xMax = Math.abs(xVal[0][0]);
         for ( i = 0; i < totalCurves; i++ ) {
            for ( j = 0; j < xVal[i].length; j++ ) {
               xMax  = Math.max( xMax, Math.abs(xVal[i][j]) );
            }
         }
      }
         
      // Calculate limits for plot, and set values
      xMin = -xMax;
      yMax =  xMax;
      yMin = -xMax;

      // Calculate the coincentric circles for the polar plot;
      calcPolarCircles( yMax );

      // Recalculate the scale factors every time, since the window
      // could have been resized or the data could have changed.
      if ( Math.abs( xMax - xMin ) > 0 )
         xScale = Math.min(plotSize.width,plotSize.height) 
                / ( xMax - xMin );
      else
         xScale = 1;

      if ( Math.abs( yMax - yMin ) > 0 )
         yScale = Math.min(plotSize.width,plotSize.height) 
                / ( yMax - yMin ) / 2;
      else
         yScale = 1;

      //**************************************************************
      //
      // Add the bounding circle
      //
      //**************************************************************

      // Set the stroke for the bounding circle--a thin solid line.
      g2.setStroke(bb);
      g2.setColor(boxColor);

      // Add bounding circle
      x1 = -xMin;
      y1 = -yMax;
      ell = new Ellipse2D.Double (  plotStartX + plotSize.width/2 - x1 * xScale,
                                    plotStartY + plotSize.height/2 + y1 * yScale,
                                    2*xMax*xScale, 2*yMax*yScale);
      g2.draw(ell);    
      //**************************************************************
      //
      // Add grid lines.
      //
      //**************************************************************

      // Set the stroke for the grid line--a thin dotted line.
      g2.setStroke(bg);
      g2.setColor(gridColor);

      // Draw the radial lines
      if ( xGridLines ) {
         for ( i = 0; i < 8; i++ ) {
            temp1 = xMax * Math.cos( i * Math.PI/4 );
            temp2 = xMax * Math.sin( i * Math.PI/4 );
            line = new Line2D.Double ( plotStartX + plotSize.width/2,
                                       plotStartY + plotSize.height/2,
                                       plotStartX + plotSize.width/2 + temp1 * xScale,
                                       plotStartY + plotSize.height/2 + temp2 * yScale);
            g2.draw(line);
         }
      }    

      // Draw the coincentric circles
      if ( yGridLines ) {
         for ( i = 1; i < yTic.length-1; i++ ) {
            ell = new Ellipse2D.Double (  plotStartX + plotSize.width/2 - yTic[i] * xScale,
                                plotStartY + plotSize.height/2 - yTic[i] * yScale,
                                2*yTic[i]*xScale, 2*yTic[i]*yScale);
            g2.draw(ell);
         }
      }

      //**************************************************************
      //
      // Add tic mark labels.
      //
      //**************************************************************

      maxCharHeight = (size.height - plotSize.height - plotStartY) / 2;
      maxStringLength = (int) (0.50 * plotStartX);
      minFontSize = 8;
      g2.setColor(labelColor);

      // Get the tic mark label font
      g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
      fontMetrics = pickFont(g2, yTicS[0], maxStringLength);

      // Label radials (except the one at 90 degrees)
      for ( i = 0; i < 8; i++ ) {

         // Draw tic label. 
         fudge = 4;
         s = "" + i*30 + '\u00B0';
         stringWidth = fontMetrics.stringWidth( s );
         stringHeight = fontMetrics.getHeight();
         temp1 = (xMax+(4+stringWidth/2)/xScale) * Math.cos( i * Math.PI/4 );
         temp2 = (xMax+(4+stringHeight/2)/yScale) * Math.sin( i * Math.PI/4 );
         if ( i != 2 )
            g2.drawString( s,
                        (int) (plotStartX + plotSize.width/2 + temp1 * xScale - stringWidth/2),
                        (int) (plotStartY + plotSize.height/2 - temp2 * yScale - fudge + stringHeight/2));
      }

      // Label circles
      for ( i = 1; i < yTicS.length; i++ ) {

         // Draw tic label. 
         fudge = 4;
         stringWidth = fontMetrics.stringWidth( yTicS[i] );
         stringHeight = fontMetrics.getHeight();
         temp1 = yTic[i] * Math.cos( 3*Math.PI/8 );
         temp2 = yTic[i] * Math.sin( 3*Math.PI/8 );
         g2.drawString( yTicS[i],
                        (int) (plotStartX + plotSize.width/2 + temp1 * xScale - 6),
                        (int) (plotStartY + plotSize.height/2 - temp2 * yScale - fudge ));
      }

      //**************************************************************
      //
      // Plot the data set(s)
      //
      //**************************************************************

      // Increment over all curves
      for ( i = 0; i < totalCurves; i++ ) {

         // Plot the line
         if ( plotLine[i] ) {

            // Set up stroke, color, etc.
            g2.setStroke(bs[i]);
            g2.setColor(lineColor[i]);

            for ( j = 0; j < xVal[i].length-1; j++ ) {

               x1 = xVal[i][j]   * Math.cos( -yVal[i][j]   );
               y1 = xVal[i][j]   * Math.sin( -yVal[i][j]   );
               x2 = xVal[i][j+1] * Math.cos( -yVal[i][j+1] );
               y2 = xVal[i][j+1] * Math.sin( -yVal[i][j+1] );
               line = new Line2D.Double ( plotStartX + plotSize.width/2 + x1 * xScale,
                                          plotStartY + plotSize.height/2 + y1 * yScale,
                                          plotStartX + plotSize.width/2 + x2 * xScale,
                                          plotStartY + plotSize.height/2 + y2 * yScale);
               g2.draw(line);
            }
         }

         // Plot the marker
         if ( plotMarker[i] ) {

            // Set up stroke, color, etc.
            if ( bm == null ) {
               bm = new BasicStroke( 2.0f,
                                     BasicStroke.CAP_SQUARE,
                                     BasicStroke.JOIN_MITER,
                                     6.0f, LINESTYLE_SOLID, 0 );
            }
            g2.setStroke(bm);
            g2.setColor(markerColor[i]);

            for ( j = 0; j < xVal[i].length; j++ ) {
               if ( markerStyle[i] == MARKER_SQUARE ) {

                  x1 = xVal[i][j] * Math.cos( -yVal[i][j] );
                  y1 = xVal[i][j] * Math.sin( -yVal[i][j] );
                  rect = new Rectangle2D.Double ( plotStartX + plotSize.width/2 + x1 * xScale - 3,
                                                  plotStartY + plotSize.height/2 + y1 * yScale - 3,
                                                  6, 6);
                  g2.draw(rect);
               }
               if ( markerStyle[i] == MARKER_CIRCLE ) {

                  x1 = xVal[i][j] * Math.cos( -yVal[i][j] );
                  y1 = xVal[i][j] * Math.sin( -yVal[i][j] );
                  ell = new Ellipse2D.Double ( plotStartX + plotSize.width/2 + x1 * xScale - 3,
                                               plotStartY + plotSize.height/2 + y1 * yScale - 3,
                                               6, 6);
                  g2.draw(ell);
               }
               if ( markerStyle[i] == MARKER_TRIANGLE ) {

                  x1 = xVal[i][j] * Math.cos( -yVal[i][j] );
                  y1 = xVal[i][j] * Math.sin( -yVal[i][j] );
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX-3);
                  ys = (float)(-y1 * yScale + plotStartY+2);
                  p.moveTo( xs,      ys      );
                  p.lineTo( xs+6.0f, ys      );
                  p.lineTo( xs+3.0f, ys-5.2f );
                  p.closePath();
                  g2.draw(p);
               }
               if ( markerStyle[i] == MARKER_DIAMOND ) {

                  x1 = xVal[i][j] * Math.cos( -yVal[i][j] );
                  y1 = xVal[i][j] * Math.sin( -yVal[i][j] );
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX);
                  ys = (float)(-y1 * yScale + plotStartY);
                  p.moveTo( xs-4.0f, ys      );
                  p.lineTo( xs     , ys+4.0f );
                  p.lineTo( xs+4.0f, ys      );
                  p.lineTo( xs     , ys-4.0f );
                  p.closePath();
                  g2.draw(p);
               }
               if ( markerStyle[i] == MARKER_DOWNTRIANGLE ) {

                  x1 = xVal[i][j] * Math.cos( -yVal[i][j] );
                  y1 = xVal[i][j] * Math.sin( -yVal[i][j] );
                  p = new GeneralPath();
                  xs = (float)(x1 * xScale + plotStartX-3);
                  ys = (float)(-y1 * yScale + plotStartY+3);
                  p.moveTo( xs,      ys-5.2f );
                  p.lineTo( xs+6.0f, ys-5.2f );
                  p.lineTo( xs+3.0f, ys      );
                  p.closePath();
                  g2.draw(p);
               }
            }
         }
      }

      //**************************************************************
      //
      // Add title and labels
      //
      //**************************************************************
      maxCharHeight = plotStartY - 15;
      maxStringLength = 3 * plotSize.width / 4;
      minFontSize = 12;
      g2.setColor(labelColor);

      // Draw title
      if ( validTitle ) {
         // Get title font info.  Try to use Helvetica 18 point BOLD if it fits
         g2.setFont(font = new Font("Helvetica", Font.BOLD, 18));
         fontMetrics = pickFont(g2, title, maxStringLength);
         stringWidth = fontMetrics.stringWidth( title );
         stringHeight = fontMetrics.getHeight();
         g2.drawString(title,plotStartX + plotSize.width/2 - stringWidth/2,
                       plotStartY/2 + stringHeight/2);
      }

      // Draw x label
      if ( validXLabel ) {
         // Get x label font info.
         maxStringLength = plotSize.width;
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 18));
         fontMetrics = pickFont(g2, xLabel, maxStringLength);
         stringWidth = fontMetrics.stringWidth( xLabel );
         stringHeight = fontMetrics.getHeight();
         g2.drawString(xLabel,plotStartX + plotSize.width/2 - stringWidth/2,
                       3*plotStartY/2 + plotSize.height + stringHeight/2 );
      }

      // Draw y label
      if ( validYLabel ) {
         // Get y label font info.
         maxStringLength = plotSize.height;
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 18));
         fontMetrics = pickFont(g2, yLabel, maxStringLength);
         stringWidth = fontMetrics.stringWidth( yLabel );
         stringHeight = fontMetrics.getHeight();

         AffineTransform at = new AffineTransform();
         at.rotate(-Math.PI/2);
         g2.setTransform(at);

         int xPos = plotStartY/2 + plotSize.height/2 + stringWidth/2;
         int yPos = (int) (1.05*stringHeight);
         g2.drawString(yLabel,-xPos, yPos );
      }
   }


   //*************************************************
   // Paint bar plot
   //*************************************************
 
   /**
    * This method creates a bar plot.
    *
    * @param  g  A graphics object.
    */
   private void bar(Graphics2D g2) {

      // Declare local variables
      double dx = 1;                     // Step size between bars
      double dx1, dx2;                   // Scratch variables
      Ellipse2D ell;                     // 2-D ellipse object
      Font font;                         // Font
      int fudge;                         // Positioning fudge factor
      double h1;                         // Height of rectangle
      int i, j;                          // loop index
      Line2D line;                       // 2-D line object
      Rectangle2D rect;                  // 2-D rectangle object
      double spacing;                    // Average step size between bars
      double x1, x2, y1, y2;             // Points on line
      double w1;                         // Width of rectangle

      // Get the size of the "user" area in pixels, and calculate the size and
      // location of the "plot" area in pixels.
      size = getSize();
      plotSize.width = (int) Math.round(size.width * ( 1 - xLeftMargin - xRightMargin));
      plotSize.height = (int) Math.round(size.height 
                      * ( 1 - yTopMargin - yBottomMargin));
      plotStartX = (int) Math.round(size.width * xLeftMargin);
      plotStartY = (int) Math.round(size.height * yTopMargin);
      barStartX = (int) (plotStartX + 0.05 * plotSize.width);

      // Set the display limits in in autoscale mode.  Note
      // that x value of bar plots specify the center of 
      // each bin, so we must pad xMin and xMax by half a 
      // bin each.
      if ( autoXScale ) {
         if ( xVal[0].length > 1 ) {
            dx1 = xVal[0][1] - xVal[0][0];
            dx2 = xVal[0][xVal[0].length-1] - xVal[0][xVal[0].length-2];
         }
         else {
            dx1 = 0.5;
            dx2 = 0.5;
         }
         xMin = Array.minVal ( xVal[0] ) - dx1/2; 
         xMax = Array.maxVal ( xVal[0] ) + dx2/2; 
          
         for ( i = 1; i < totalCurves; i++ ) {
            if ( xVal[i].length > 1 ) {
               dx1 = xVal[i][1] - xVal[i][0];
               dx2 = xVal[i][xVal[i].length-1] - xVal[i][xVal[i].length-2];
            }
            else {
               dx1 = 0.5;
               dx2 = 0.5;
            }
            xMin = Math.min ( xMin, Array.minVal(xVal[i]) - dx1/2 ); 
            xMax = Math.max ( xMax, Array.maxVal(xVal[i]) + dx2/2 ); 
         }
      }
      if ( autoYScale ) {
         yMax = Array.maxVal ( yVal[0] ); 
         yMin = Array.minVal ( yVal[0] ); 
         for ( i = 1; i < totalCurves; i++ ) {
            yMax = Math.max ( yMax, Array.maxVal(yVal[i]) ); 
            yMin = Math.min ( yMin, Array.minVal(yVal[i]) );
         }
      }
      
      // Not autoscale, but check for illegal plot values anyway
      if ( yMin < 0 ) {
         String s = "Min y value < 0 in Bar plot: " + yMin;
         throw new InvalidPlotValueException(s);
      }
      
      // Set yMin = 0 so that the bars will always start at 0
      yMin = 0;

      // Calculate locations of tic marks if in automatic mode
      if ( autoXTicMode ) calcXTics( xMin, xMax );
      if ( autoYTicMode ) calcYTics( yMin, yMax );

      // Recalculate the scale factors every time, since the window 
      // could have been resized or the data could have changed. 
      if ( Math.abs( xTic[xTic.length-1] - xTic[0] ) > 0 ) {
         if ( xVal[0].length > 1 )  
            spacing = ( xTic[xTic.length-1] - xTic[0] ) 
                    / ( xVal[0].length - 1 );
         else
            spacing = 1;
         xScale = plotSize.width 
                / ( xTic[xTic.length-1] - xTic[0] + spacing );
      }
      else {
         xScale = 1;
         spacing = 1;
      }

      if ( Math.abs( yTic[yTic.length-1] - yTic[0] ) > 0 )
         yScale = plotSize.height / ( yTic[yTic.length-1] - yTic[0] ); 
      else
         yScale = 1;

      //**************************************************************
      //
      // Add the bounding box
      //
      //**************************************************************

      // Set the stroke for the bounding box--a thin solid line.
      g2.setStroke(bb);
      g2.setColor(boxColor);

      // Draw the box
      line = new Line2D.Double( plotStartX,                  plotStartY,
                                plotStartX + plotSize.width, plotStartY);
      g2.draw(line);
      line = new Line2D.Double( plotStartX + plotSize.width, plotStartY,
                                plotStartX + plotSize.width, plotStartY + plotSize.height);
      g2.draw(line);
      line = new Line2D.Double( plotStartX + plotSize.width, plotStartY + plotSize.height,
                                plotStartX,                  plotStartY + plotSize.height);
      g2.draw(line);
      line = new Line2D.Double( plotStartX,                  plotStartY + plotSize.height,
                                plotStartX,                  plotStartY);
      g2.draw(line);

      //**************************************************************
      //
      // Add grid lines.
      //
      //**************************************************************

      // Set the stroke for the grid line--a thin dotted line.
      g2.setStroke(bg);
      g2.setColor(gridColor);

      // Draw the linear x-axis grid lines
      if ( xGridLines ) {
         for ( i = 1; i < xTic.length-1; i++ ) {
            line = new Line2D.Double ( (xTic[i]-xMin) * xScale + plotStartX,
                                       plotStartY,
                                       (xTic[i]-xMin) * xScale + plotStartX,
                                       plotStartY + plotSize.height);
            g2.draw(line);
         }
      }

      // Draw the linear y-axis grid lines
      if ( yGridLines ) {
         for ( i = 1; i < yTic.length-1; i++ ) {
            line = new Line2D.Double ( plotStartX,
                                       -(yTic[i]-yMax) * yScale + plotStartY,
                                       plotStartX + plotSize.width,
                                       -(yTic[i]-yMax) * yScale + plotStartY);
            g2.draw(line);
         }
      }

      //**************************************************************
      //
      // Add tic marks.
      //
      //**************************************************************

      // Set the stroke for the tic marks--a thin solid line.
      g2.setStroke(bt);
      g2.setColor(ticColor);

      // Draw linear x-axis tic marks
      if ( xTicMarks ) {
         for ( i = 0; i < xTic.length; i++ ) {
            line = new Line2D.Double (  (xTic[i]-xMin) * xScale + barStartX,
                                        plotStartY,
                                        (xTic[i]-xMin) * xScale + barStartX,
                                        plotStartY + 0.025*plotSize.height);
            g2.draw(line);
            line = new Line2D.Double (  (xTic[i]-xMin) * xScale + barStartX,
                                        plotStartY + plotSize.height,
                                        (xTic[i]-xMin) * xScale + barStartX,
                                        plotStartY + 0.975*plotSize.height);
            g2.draw(line);
         }
      }

      // Draw linear y-axis tic marks
      if ( yTicMarks ) {
         for ( i = 1; i < yTic.length-1; i++ ) {
            line = new Line2D.Double (  plotStartX,
                                        -(yTic[i]-yMax) * yScale + plotStartY,
                                        plotStartX + 0.025*plotSize.width,
                                        -(yTic[i]-yMax) * yScale + plotStartY);
            g2.draw(line);
            line = new Line2D.Double (  plotStartX + plotSize.width,
                                        -(yTic[i]-yMax) * yScale + plotStartY,
                                        plotStartX + 0.975*plotSize.width,
                                        -(yTic[i]-yMax) * yScale + plotStartY);
            g2.draw(line);
         }
      }

      //**************************************************************
      //
      // Add tic mark labels.
      //
      //**************************************************************

      // X-axis
      maxCharHeight = (size.height - plotSize.height - plotStartY) / 2;
      maxStringLength = (int) (plotSize.width / (1.2*xTic.length));
      minFontSize = 8;
      g2.setColor(labelColor);

      // Get the tic mark label font
      g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
      fontMetrics = pickFont(g2, xTicS[0], maxStringLength);

      // Linear tic mark labels
      for ( i = 0; i < xTicS.length; i++ ) {
      
         stringWidth = fontMetrics.stringWidth( xTicS[i] );
         stringHeight = fontMetrics.getHeight();
         g2.drawString( xTicS[i],
                        (int) ((xTic[i]-xMin) * xScale + barStartX - stringWidth/2),
                        (int) (plotStartY + plotSize.height + 11));
      }

      // Y-axis
      maxCharHeight = (size.height - plotSize.height - plotStartY) / 2;
      maxStringLength = (int) (0.50 * plotStartX);
      minFontSize = 8;
      g2.setColor(labelColor);

      // Get the tic mark label font
      g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
      fontMetrics = pickFont(g2, xTicS[0], maxStringLength);

      for ( i = 0; i < yTicS.length; i++ ) {

         // Draw tic label.  Include a fudge factor on the lowest label to
         // avoid collisions
         if ( i == 0 )
            fudge = 5;
         else
            fudge = 12;
         stringWidth = fontMetrics.stringWidth( yTicS[i] );
         stringHeight = fontMetrics.getHeight();
         g2.drawString( yTicS[i],
                        (int) (plotStartX - stringWidth - 6),
                        (int) (-(yTic[i]-yMax) * yScale + plotStartY - stringHeight/2 + fudge ));
      }

      //**************************************************************
      //
      // Plot the data set(s)
      //
      //**************************************************************

      // Increment over all curves
      for ( i = 0; i < totalCurves; i++ ) {
      
         // Set up stroke, color, etc.
         g2.setStroke(bs[i]);
         g2.setColor(lineColor[i]);

         // This is a bar plot.  Plot each individual bar.
         for ( j = 0; j < xVal[i].length; j++ ) {
      
            // Calculate the width and the starting point of this bar.  
            // This elaborate calculation is necessary to allow the 
            // bar plot to function correctly for bars of irregular
            // spacings.
            if ( j == 0 && xVal[i].length == 1 ) {
               dx = xMax - xMin;
               x1 = xVal[i][j] - dx/2 - xMin;
               w1 = dx;
            }
            else if ( j == 0 ) {
               dx = xVal[i][j+1] - xVal[i][j];
               x1 = xVal[i][j] - dx/2 - xMin;
               w1 = dx;
            }
            else if ( j == xVal[i].length-1 ) {
               dx = xVal[i][j] - xVal[i][j-1];
               x1 = xVal[i][j] - dx/2 - xMin;
               w1 = dx;
            }
            else {
               dx = (xVal[i][j+1] - xVal[i][j-1]) /2;
               x1 = (xVal[i][j] + xVal[i][j-1])/2 - xMin;
               w1 = dx;
            }

            // If multiple curves are being displayed,
            // modify the width and starting point to allow
            // for a gap between adjacent x values.
            if ( totalCurves > 1 ) {
               double ds = dx / (totalCurves + 1);
               x1 = xVal[i][j] - xMin + (i-1)*ds - ds/2;
               w1 = ds;
            }
            
            // Calculate the height of this bar.
            y1 = yVal[i][j] - yMax;
            h1 = yMax + y1;

            rect = new Rectangle2D.Double (  x1 * xScale + barStartX, 
                                            -y1 * yScale + plotStartY, 
                                             w1 * xScale, 
                                             h1 * yScale - 1 ); 
            g2.setColor(fillColor[i]);
            g2.fill(rect);
            g2.setColor(Color.black);
            g2.draw(rect);
         }
      }
      
      //**************************************************************
      //
      // Add title and labels
      //
      //**************************************************************
      maxCharHeight = plotStartY - 15;
      maxStringLength = 3 * plotSize.width / 4;
      minFontSize = 12;
      g2.setColor(labelColor);

      // Draw title
      if ( validTitle ) {
         // Get title font info.  Try to use Helvetica 18 point BOLD if it fits
         g2.setFont(font = new Font("Helvetica", Font.BOLD, 18));
         fontMetrics = pickFont(g2, title, maxStringLength);
         stringWidth = fontMetrics.stringWidth( title );
         stringHeight = fontMetrics.getHeight();
         g2.drawString(title,plotStartX + plotSize.width/2 - stringWidth/2,
                       plotStartY/2 + stringHeight/2);
      }

      // Draw x label
      if ( validXLabel ) {
         // Get x label font info.
         maxStringLength = plotSize.width;
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 18));
         fontMetrics = pickFont(g2, xLabel, maxStringLength);
         stringWidth = fontMetrics.stringWidth( xLabel );
         stringHeight = fontMetrics.getHeight();
         g2.drawString(xLabel,plotStartX + plotSize.width/2 - stringWidth/2,
                       3*plotStartY/2 + plotSize.height + stringHeight/2 );
      }

      // Draw y label
      if ( validYLabel ) {
         // Get y label font info.
         maxStringLength = plotSize.height;
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 18));
         fontMetrics = pickFont(g2, yLabel, maxStringLength);
         stringWidth = fontMetrics.stringWidth( yLabel );
         stringHeight = fontMetrics.getHeight();

         AffineTransform at = new AffineTransform();
         at.rotate(-Math.PI/2);
         g2.setTransform(at);

         int xPos = plotStartY/2 + plotSize.height/2 + stringWidth/2;
         int yPos = (int) (1.05*stringHeight);
         g2.drawString(yLabel,-xPos, yPos );
      }

      //**************************************************************
      //
      // Add annotations
      //
      //**************************************************************
      AffineTransform at = new AffineTransform();
      g2.setTransform(at);

      for ( i = 0; i < totalAnnotations; i++ ) {

         // Select starting location for the annotation
         x1 = xAnnPos[i] - xMin;
         y1 = yAnnPos[i] - yMax;

         // Select font size
         maxCharHeight = 26;
         if ( logXAxis )
            maxStringLength = (int) Math.floor((xMaxLog - Math1.log10( xAnnPos[i] )) * plotSize.width);
         else
            maxStringLength = (int) Math.floor((xMax - xAnnPos[i]) * plotSize.width);
         minFontSize = 8;
         g2.setColor(annotationColor[i]);

         // Get annotation font info.  Try to use Helvetica 12 point regular if it fits
         g2.setFont(font = new Font("Helvetica", Font.PLAIN, 12));
         fontMetrics = pickFont(g2, annotations[i], maxStringLength);
         stringWidth = fontMetrics.stringWidth( annotations[i] );
         stringHeight = fontMetrics.getHeight();

         // Draw annotation
         g2.drawString(annotations[i],(int) (x1 * xScale + plotStartX),
                                      (int) (-y1 * yScale + plotStartY));
      }
   }


   //**************************************************************
   // Method to catch ActionEvents from popup menu
   //**************************************************************

   /**
    * This method is implements the <tt>ActionListener</tt> interface 
    * to print this object when a selection is made from the popup
    * menu.
    *
    * @param  e  An <tt>ActionEvent</tt> object.
    */
   public void actionPerformed( ActionEvent e ) {
      PageFormat portrait;
      
      PrinterJob printJob = PrinterJob.getPrinterJob();
      if ( e.getActionCommand().equals("Print") )
         portrait = printJob.defaultPage();
      else 
         portrait = printJob.pageDialog(printJob.defaultPage());
      portrait.setOrientation(PageFormat.PORTRAIT);
      Book bk = new Book();
      bk.append( (Printable) this, portrait );
      printJob.setPageable(bk);
      try { 
         printJob.print();
      }
      catch ( Exception PrintException ) {}
   }
   

   //**************************************************************
   // Method to process mouse events
   //**************************************************************

   /**
    * This method processes mouse events for the "print" popup menu.
    */
   public void processMouseEvent( MouseEvent e ) {
   
      if ( e.isPopupTrigger() )
         popup.show( this, e.getX(), e.getY() );
      super.processMouseEvent(e);
   }
}
