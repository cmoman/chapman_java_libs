package chapman.io;
import java.io.*;

/**
 * Class <code>FileInLines</code> contains convenience methods to read 
 * a formatted file one line at a time, returning each line in a
 * character string.  This class opens the input file when an object is 
 * instantiated, and allows the user to read data until the end of file.  
 * It traps all file exceptions and converts them into status codes 
 * stored in instance variable <tt>readStatus</tt>, which can be checked 
 * by the user. The possible status values are:
 * <blockquote><pre>
 *     READ_OK
 *     FILE_NOT_FOUND
 *     EOF
 *     IO_EXCEPTION
 * </pre></blockquote>
 * If <tt>readStatus == READ_OK</tt>, then the desired value has been returned
 * by the called method.  Otherwise, a null string will be returned.
 * <p>
 * The code fragment shown below opens file "infile", and reads the lines
 * from "infile" into array a.
 * <blockquote><pre>
 * 
 *    String a[] = new String[100];    
 *    FileInLines in = new FileInLines("infile");

 *    // Check for valid open
 *    if ( in.readStatus != in.FILE_NOT_FOUND ) {

 *       // Read lines into array
 *       while ( in.readStatus != in.EOF ) {
 *          a[i++] = in.readLine();
 *       }
 *       (add code here)
 *    }
 * </pre></blockquote>
 *
 * @author  S. J. Chapman
 * @version 1.00, 09/20/98
 */

public class FileInLines {

   /**
    * Reference to a BufferReader.
    */
   private BufferedReader in;

   /**
    * Save string.
    */
   private String saveString = "";

   /**
    * Indicates successful read.
    */
   public final int READ_OK = 0;

   /**
    * End-of-file marker.
    */
   public final int EOF = -997;

   /**
    * File-not-found marker.
    */
   public final int FILE_NOT_FOUND = -998;

   /**
    * I/O exception marker.
    */
   public final int IO_EXCEPTION = -999;

   /**
    * Read status.
    */
   public int readStatus = READ_OK;

   /**
    * The constructor opens the specified file for reading
    * string data.
    *
    * @param  fileName  The name of the file to open
    */
   public FileInLines ( String fileName ) {
      try {
         in = new BufferedReader( new FileReader(fileName) );
         saveString = in.readLine();
         if ( saveString != null )
            readStatus = READ_OK;
         else
            readStatus = EOF;
      }
      catch (FileNotFoundException e) {
         readStatus = FILE_NOT_FOUND;
      }
      catch (IOException e) {
         readStatus = IO_EXCEPTION;
      }
   }


   /**
    * This method reads a line from the input file, and returns
    * it as a <code>String</code>.
    *
    * @return  a <code>String</code> value
    */
   public String readLine() {

      String s;
      if (readStatus != EOF ) {

         boolean inputOk = false;
         try {
            s = saveString;
            saveString = in.readLine();
            if ( saveString != null )
               readStatus = READ_OK;
            else
               readStatus = EOF;
            return s;
         }
         catch (IOException e) {
            readStatus = IO_EXCEPTION;
            s = null;
         }
      }

      else {
         s = null;
         readStatus = EOF;
      }
      return s;
   }


   /**
    * This method closes the input file.
    */
   public void close() {
      try {
         in.close();
      }
      catch (IOException e) {
         readStatus = IO_EXCEPTION;
      }
   }


   // Finalizer
   protected void finalize() {

      // Close file if open
      try {
         in.close();
      }
      catch (Throwable e) {
         //System.out.println("Throwable: the stack trace is:");
         //e.printStackTrace();
      }
      
      // Call superclass finalizer
      try { super.finalize(); }
      catch (Throwable e) {}
   }
}
