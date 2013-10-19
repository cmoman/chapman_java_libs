package chapman.io;
import java.io.*;

/**
 * Class <code>FileIn</code> contains convenience methods to read formatted
 * <tt>double</tt>, <tt>float</tt>, <tt>int</tt>, or <tt>long</tt> data from 
 * an input file. This class opens the input file when an object is 
 * instantiated, and allows the user to read data until the end of file.  
 * It traps all file exceptions and converts them into status codes stored
 * in instance variable <tt>readStatus</tt>, which can be checked by the user. 
 * The possible status values are:
 * <blockquote><pre>
 *     READ_OK
 *     INVALID_FORMAT
 *     FILE_NOT_FOUND
 *     EOF
 *     IO_EXCEPTION
 * </pre></blockquote>
 * If <tt>readStatus == READ_OK</tt>, then the desired value has been returned
 * by the called method.
 *
 * @author  S. J. Chapman
 * @version 1.00, 09/20/98
 */

public class FileIn {

   /**
    * Reference to a BufferReader.
    */
   private BufferedReader in;

   /**
    * Reference to a StreamTokenizer.
    */
   private StreamTokenizer st;

   /**
    * Indicates successful read.
    */
   public final int READ_OK = 0;

   /**
    * Indicates invalid format.
    */
   public final int INVALID_FORMAT = -996;

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
    * numerical data.
    *
    * @param  fileName  The name of the file to open
    */
   public FileIn ( String fileName ) {
      try {
         in = new BufferedReader( new FileReader(fileName) );
         st = new StreamTokenizer(in);
         st.resetSyntax();
         st.wordChars('!','~');
         st.whitespaceChars(0,' ');
         st.nextToken();
         if (st.ttype != st.TT_EOF )
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
    * This method reads a <code>double</code> value from the
    * input file.
    *
    * @return  a <code>double</code> value
    */
   public double readDouble() {

      double d = 0.0;
      if (st.ttype != st.TT_EOF ) {

         boolean inputOk = false;
         try {
            while ( !inputOk ) {
               if ( st.ttype == st.TT_WORD ) {
                  try { d = Double.parseDouble(st.sval); }
                  catch (NumberFormatException e) { 
                     System.err.println("Invalid double: " + st.sval);
                     d = Double.NaN;
                  }
                  inputOk = true;
               }
               st.nextToken();
               if (st.ttype != st.TT_EOF )
                  readStatus = READ_OK;
               else
                  readStatus = EOF;
               return d;
            }
         }
         catch (IOException e) {
            readStatus = IO_EXCEPTION;
            d = 0;
         }
      }

      else {
         d = 0;
         readStatus = EOF;
      }
      return d;
   }


   /**
    * This method reads a <code>float</code> value from the
    * input file.
    *
    * @return  a <code>float</code> value
    */
   public float readFloat() {

      float f = 0.0f;
      if (st.ttype != st.TT_EOF ) {

         boolean inputOk = false;
         try {
            while ( !inputOk ) {
               if ( st.ttype == st.TT_WORD ) {
                  try { f = Float.parseFloat(st.sval); }
                  catch (NumberFormatException e) { 
                     System.err.println("Invalid float: " + st.sval);
                     f = Float.NaN;
                  }
                  inputOk = true;
               }
               st.nextToken();
               if (st.ttype != st.TT_EOF )
                  readStatus = READ_OK;
               else
                  readStatus = EOF;
               return f;
            }
         }
         catch (IOException e) {
            readStatus = IO_EXCEPTION;
            f = 0;
         }
      }

      else {
         f = 0;
         readStatus = EOF;
      }
      return f;
   }


   /**
    * This method reads an <code>int</code> value from the
    * input file.
    *
    * @return  a <code>int</code> value
    */
   public int readInt() {

      int i = 0;
      if (st.ttype != st.TT_EOF ) {

         boolean inputOk = false;
         try {
            while ( !inputOk ) {
               if ( st.ttype == st.TT_WORD ) {
                  try { i = Integer.parseInt(st.sval); }
                  catch (NumberFormatException e) { 
                     System.err.println("Invalid int: " + st.sval);
                     i = 0;
                  }
                  inputOk = true;
               }
               st.nextToken();
               if (st.ttype != st.TT_EOF )
                  readStatus = READ_OK;
               else
                  readStatus = EOF;
               return i;
            }
         }
         catch (IOException e) {
            readStatus = IO_EXCEPTION;
            i = 0;
         }
      }

      else {
         i = 0;
         readStatus = EOF;
      }
      return i;
   }


   /**
    * This method reads a <code>long</code> value from the
    * input file.
    *
    * @return  a <code>long</code> value
    */
   public long readLong() {

      long l = 0;
      if (st.ttype != st.TT_EOF ) {

         boolean inputOk = false;
         try {
            while ( !inputOk ) {
               if ( st.ttype == st.TT_WORD ) {
                  try { l = Long.parseLong(st.sval); }
                  catch (NumberFormatException e) { 
                     System.err.println("Invalid long: " + st.sval);
                     l = 0;
                  }
                  inputOk = true;
               }
               st.nextToken();
               if (st.ttype != st.TT_EOF )
                  readStatus = READ_OK;
               else
                  readStatus = EOF;
               return l;
            }
         }
         catch (IOException e) {
            readStatus = IO_EXCEPTION;
            l = 0;
         }
      }

      else {
         l = 0;
         readStatus = EOF;
      }
      return l;
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
