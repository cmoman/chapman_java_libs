package chapman.io;
import java.io.*;
import chapman.io.Fmt;

/**
 * Class <code>FileOut</code> contains convenience methods to write formatted
 * data to user-specified file.  This class provides <code>printf</code> 
 * methods using the C-language <tt>printf</tt> conventions.  The main limitation
 * of this class is that each method can handle only one output
 * value at a time, so you must make multiple calls to the <tt>printf</tt> methods
 * to write a series of values.
 * <p>
 * This class supports output values of types <tt>double</tt>,
 * <tt>long</tt>, <tt>char</tt>, and <tt>String</tt>.  However, it will
 * work for any integer or floating-point type due to Java's coersion
 * of arguments.
 * <p>
* This class opens the output file when an object is instantiated, and allows 
 * the user to write data to it.  It traps all file exceptions and converts them 
 * into status codes stored in instance variable <tt>writeStatus</tt>, which can 
 * be checked by the user. The possible status values are:
 * <blockquote><pre>
 *     WRITE_OK 
 *     IO_EXCEPTION
 * </pre></blockquote>
 * If <tt>writeStatus == WRITE_OK</tt>, then the desired value has been written
 * successfully.
 *
 * @author  S. J. Chapman
 * @version 1.00, 09/20/98
 */

public class FileOut {

   /**
    * Reference to a PrintWriter.
    */
   private PrintWriter out;

   /**
    * Indicates successful write.
    */
   public final int WRITE_OK = 0;

   /**
    * I/O exception marker.
    */
   public final int IO_EXCEPTION = -999;

   /**
    * Write status.
    */
   public int writeStatus;

   /**
    * The constructor opens the specified file for writing formatted data.
    *
    * @param  fileName  The name of the file to open
    */
   public FileOut ( String fileName ) {

      try {
         out =
            new PrintWriter(
               new BufferedWriter(
                  new FileWriter(fileName,false)));
      }
      catch (IOException e) { 
         System.out.println("IOException on file open: the stack trace is:");
         e.printStackTrace();
         writeStatus = IO_EXCEPTION;
      }
   }


   /**
    * The constructor opens the specified file for writing formatted data, 
    * with the option of appending data to a previously-existing file.
    *
    * @param  fileName  The name of the file to open
    * @param  append    If <tt>true</tt>, append to exisitng output file
    */
   public FileOut ( String fileName, boolean append ) {

      try {
         out =
            new PrintWriter(
               new BufferedWriter(
                  new FileWriter(fileName,append)));
      }
      catch (IOException e) { 
         System.out.println("IOException on file open: the stack trace is:");
         e.printStackTrace();
         writeStatus = IO_EXCEPTION;
      }
   }


   /**
    * Print a formatted number to a formatted file following
    * <code>printf</code> conventions.  This method does not generate
    * a newline character, so you must include a <tt>\n</tt> character
    * at the end of the format string if you want to advance to a new
    * line.
    * <p>
    * Example usage:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>double time = 10.345;</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>out.printf("Time = %8.2f s\n",time);</tt>
    * </dl>
    * <p>
    * Result in output file:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>Time =&nbsp;&nbsp;&nbsp;10.35 s</tt>
    * </dl>
    * <p>
    * @param fmt the format string following <tt>printf</tt> conventions.
    * The string <tt>fmt</tt> has a prefix, a format code and a suffix.
    * The prefix and suffix become part of the formatted output, so it is
    * possible to add spaces and other characters to the output stream. The
    * format code directs the formatting of the parameter to be formatted.
    * The code has the following structure:
    * <ul>
    * <li> a <tt>%</tt> (required)
    * <li> a modifier (optional)
    * <dl>
    * <dt> <tt>+</tt> <dd> forces display of + for positive numbers
    * <dt> <tt>0</tt> <dd> show leading zeroes
    * <dt> <tt>-</tt> <dd> align left in the field
    * <dt> <tt>space</tt> <dd> prepend a space in front of positive numbers
    * <dt> <tt>#</tt> <dd> use "alternate" format. Add 0 or 0x for octal or hexadecimal numbers. Don't suppress trailing zeroes in general floating point format.
    * </dl>
    * <li> an integer denoting field width (optional)
    * <li> a period followed by an integer denoting precision (optional)
    * <li> a format descriptor (required)
    * <dl>
    * <dt><tt>f</tt> <dd> floating point number in fixed format
    * <dt><tt>e</tt>, <tt>E</tt> <dd> floating point number in exponential notation
    *      (scientific format). The <tt>E</tt> format results in an uppercase E for
    *      the exponent (1.14130E+003), the <tt>e</tt> format in a lowercase e.
    * <dt><tt>g</tt>, <tt>G</tt> <dd> floating point number in general format (fixed
    *      format for small numbers, exponential format for large numbers). Trailing
    *      zeroes are suppressed. The <tt>G</tt> format results in an uppercase E for
    *      the exponent (if any), the <tt>g</tt> format in a lowercase e.
    * </dl>
    * </ul>
    * <p>
    * @param x the <code>double</code> to convert
    * <p>
    * @exception IllegalArgumentException if bad format
    */
   public void printf(String fmt, double x) {
      out.print(Fmt.sprintf(fmt,x));
      writeStatus = WRITE_OK;
   }


   /**
    * Print a formatted number to a formatted file following
    * <code>printf</code> conventions.  This method does not generate
    * a newline character, so you must include a <tt>\n</tt> character
    * at the end of the format string if you want to advance to a new
    * line.
    * <p>
    * Example usage:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>long i = 12345678;</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>out.printf("i = %10d\n",i);</tt>
    * </dl>
    * <p>
    * Result in output file:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>i =&nbsp;&nbsp;&nbsp;12345678</tt>
    * </dl>
    * <p>
    * @param fmt the format string following <tt>printf</tt> conventions.
    * The string <tt>fmt</tt> has a prefix, a format code and a suffix.
    * The prefix and suffix become part of the formatted output, so it is
    * possible to add spaces and other characters to the output stream. The
    * format code directs the formatting of the parameter to be formatted.
    * The code has the following structure:
    * <ul>
    * <li> a <tt>%</tt> (required)
    * <li> a modifier (optional)
    * <dl>
    * <dt> <tt>+</tt> <dd> forces display of + for positive numbers
    * <dt> <tt>0</tt> <dd> show leading zeroes
    * <dt> <tt>-</tt> <dd> align left in the field
    * <dt> <tt>space</tt> <dd> prepend a space in front of positive numbers
    * <dt> <tt>#</tt> <dd> use "alternate" format. Add 0 or 0x for octal or hexadecimal numbers. Don't suppress trailing zeroes in general floating point format.
    * </dl>
    * <li> an integer denoting field width (optional)
    * <li> a period followed by an integer denoting precision (optional)
    * <li> a format descriptor (required)
    * <dl>
    * <dt><tt>d</tt>, <tt>i</tt> <dd> integer in decimal
    * <dt><tt>x</tt> <dd> integer in hexadecimal
    * <dt><tt>o</tt> <dd> integer in octal
    * </dl>
    * </ul>
    * <p>
    * @param l the <code>long</code> to convert
    * <p>
    * @exception IllegalArgumentException if bad format
    */
   public void printf(String fmt, long l) {
      out.print(Fmt.sprintf(fmt,l));
      writeStatus = WRITE_OK;
   }


   /**
    * Print a formatted number to a formatted file following
    * <code>printf</code> conventions.  This method does not generate
    * a newline character, so you must include a <tt>\n</tt> character
    * at the end of the format string if you want to advance to a new
    * line.
    * <p>
    * Example usage:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>char c = 'A';</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>out.printf("character = %c\n",c);</tt>
    * </dl>
    * <p>
    * Result in output file:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>character = A</tt>
    * </dl>
    * <p>
    * @param fmt the format string following <tt>printf</tt> conventions.
    * The string <tt>fmt</tt> has a prefix, a format code and a suffix.
    * The prefix and suffix become part of the formatted output, so it is
    * possible to add spaces and other characters to the output stream. The
    * format code directs the formatting of the parameter to be formatted.
    * The code has the following structure:
    * <ul>
    * <li> a <tt>%</tt> (required)
    * <li> a modifier (optional)
    * <dl>
    * <dt> <tt>+</tt> <dd> forces display of + for positive numbers
    * <dt> <tt>0</tt> <dd> show leading zeroes
    * <dt> <tt>-</tt> <dd> align left in the field
    * <dt> <tt>space</tt> <dd> prepend a space in front of positive numbers
    * <dt> <tt>#</tt> <dd> use "alternate" format. Add 0 or 0x for octal or hexadecimal numbers. Don't suppress trailing zeroes in general floating point format.
    * </dl>
    * <li> an integer denoting field width (optional)
    * <li> a period followed by an integer denoting precision (optional)
    * <li> a format descriptor (required)
    * <dl>
    * <dt><tt>c</tt> <dd> character
    * </dl>
    * </ul>
    * <p>
    * @param x the <code>double</code> to convert
    * <p>
    * @exception IllegalArgumentException if bad format
    */
   public void printf(String fmt, char c) {
      out.print(Fmt.sprintf(fmt,c));
      writeStatus = WRITE_OK;
   }


   /**
    * Print a formatted number to a formatted file following
    * <code>printf</code> conventions.  This method does not generate
    * a newline character, so you must include a <tt>\n</tt> character
    * at the end of the format string if you want to advance to a new
    * line.
    * <p>
    * Example usage:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>String s = "Test string!";</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>out.printf("string = %s\n",s);</tt>
    * </dl>
    * <p>
    * Result in output file:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>string = Test string!</tt>
    * </dl>
    * <p>
    * @param fmt the format string following <tt>printf</tt> conventions.
    * The string <tt>fmt</tt> has a prefix, a format code and a suffix.
    * The prefix and suffix become part of the formatted output, so it is
    * possible to add spaces and other characters to the output stream. The
    * format code directs the formatting of the parameter to be formatted.
    * The code has the following structure:
    * <ul>
    * <li> a <tt>%</tt> (required)
    * <li> a modifier (optional)
    * <dl>
    * <dt> <tt>+</tt> <dd> forces display of + for positive numbers
    * <dt> <tt>0</tt> <dd> show leading zeroes
    * <dt> <tt>-</tt> <dd> align left in the field
    * <dt> <tt>space</tt> <dd> prepend a space in front of positive numbers
    * <dt> <tt>#</tt> <dd> use "alternate" format. Add 0 or 0x for octal or hexadecimal numbers. Don't suppress trailing zeroes in general floating point format.
    * </dl>
    * <li> an integer denoting field width (optional)
    * <li> a period followed by an integer denoting precision (optional)
    * <li> a format descriptor (required)
    * <dl>
    * <dt><tt>s</tt> <dd> string
    * </dl>
    * </ul>
    * <p>
    * @param x the <code>double</code> to convert
    * <p>
    * @exception IllegalArgumentException if bad format
    */
   public void printf(String fmt, String s) {
      out.print(Fmt.sprintf(fmt,s));
      writeStatus = WRITE_OK;
   }


   /**
    * This method closes the input file.
    */
   public void close() {
      out.close();
   }


   // Finalizer
   protected void finalize() {
      try {
         out.close();
         super.finalize();
      }
      catch (Throwable e) { 
         System.out.println("Throwable: the stack trace is:");
         e.printStackTrace();
      }
   }
}



