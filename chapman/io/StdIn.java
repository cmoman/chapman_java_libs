package chapman.io;
import java.io.*;
import java.util.*;

/**
 * Class <code>StdIn</code> contains methods to read formatted <code>boolean</code>,
 * <code>byte</code>, <code>short</code>, <code>int</code>, <code>float</code>,
 * <code>double</code>, <code>char</code>, and <code>String</code> values from
 * the standard input stream.  It is a convenience class designed to hide the
 * complications of the Java I/O system, allowing students to concentrate on
 * other aspects of the Java language.  This class handles <code>IOException</code>s,
 * <code>NumberFormatException</code>s, and <code>NoSuchElementException</code>s
 * internally, providing useful error messages for students.
 * <p>
 * An example of usage is shown below.  This example reads a <code>double</code>
 * and an <code>int</code> value from the standard input stream.
 * <blockquote><pre>
 *     StdIn in = new StdIn();
 *     double d = in.readDouble();
 *     int i = in.readInt();
 * </pre></blockquote>
 *
 * This class is based on class <code>EasyIn</code>, which was developed by
 * Peter van der Linden.
 *
 * @author  S. J. Chapman
 * @version 1.20, 06/02/99
 */

public class StdIn {

  // Declare static and instance variables.  Variables "is" and "br"
  // are declared static so that only one copy will be created no
  // matter how many times this class is used.
  private static InputStreamReader is = new InputStreamReader( System.in );
  private static BufferedReader br = new BufferedReader( is );
  private StringTokenizer st;
  private boolean inputOk;

   /**
    * This method reads a <code>boolean</code> value from the standard
    * input stream.  It returns a <code>true</code> value if the input is
    * "true", and a false otherwise.  Note that this method is case-insensitive.
    *
    * @return  a <code>boolean</code> value
    */
  public boolean readBoolean() {
     try {
        st = getToken();
        return new Boolean(st.nextToken()).booleanValue();
     } catch ( IOException e ) {
        System.err.println("IO Exception in StdIn.readBoolean");
        return false;
     }
  }

   /**
    * This method reads a <code>byte</code> value from the standard
    * input stream.
    *
    * @return  a <code>byte</code> value
    */
  public byte readByte() {
     inputOk = false;
     byte b = 0;
     while ( !inputOk ) {
        try {
           st = getToken();
           b = Byte.parseByte(st.nextToken());
           inputOk = true;
        }
        catch ( NumberFormatException e ) {
           System.err.println("Invalid format for byte-try again:");
        }
        catch ( NoSuchElementException e ) {
           System.err.println("No value found-try again:");
        }
        catch ( IOException e ) {
           System.err.println("IO Exception in StdIn.readByte");
           b = 0;
           inputOk = true;
        }
     }
     return b;
  }

   /**
    * This method reads a <code>short</code> value from the standard
    * input stream.
    *
    * @return  a <code>short</code> value
    */
  public short readShort() {
     inputOk = false;
     short sh = 0;
     while ( !inputOk ) {
        try {
           st = getToken();
           sh = Short.parseShort(st.nextToken());
           inputOk = true;
        }
        catch ( NumberFormatException e ) {
           System.err.println("Invalid format for short-try again:");
        }
        catch ( NoSuchElementException e ) {
           System.err.println("No value found-try again:");
        }
        catch ( IOException e ) {
           System.err.println("IO Exception in StdIn.readShort");
           sh = 0;
           inputOk = true;
        }
     }
     return sh;
  }

   /**
    * This method reads an <code>int</code> value from the standard
    * input stream.
    *
    * @return  an <code>int</code> value
    */
  public int readInt() {
     inputOk = false;
     int i = 0;
     while ( !inputOk ) {
        try {
           st = getToken();
           i = Integer.parseInt(st.nextToken());
           inputOk = true;
        }
        catch ( NumberFormatException e ) {
           System.err.println("Invalid format for integer-try again:");
        }
        catch ( NoSuchElementException e ) {
           System.err.println("No value found-try again:");
        }
        catch ( IOException e ) {
           System.err.println("IO Exception in StdIn.readInt");
           i = 0;
           inputOk = true;
        }
     }
     return i;
  }

   /**
    * This method reads a <code>long</code> value from the standard
    * input stream.
    *
    * @return  a <code>long</code> value
    */
  public long readLong() {
     inputOk = false;
     long l = 0L;
     while ( !inputOk ) {
        try {
           st = getToken();
           l = Long.parseLong(st.nextToken());
           inputOk = true;
        }
        catch ( NumberFormatException e ) {
           System.err.println("Invalid format for long-try again:");
        }
        catch ( NoSuchElementException e ) {
           System.err.println("No value found-try again:");
        }
        catch ( IOException e ) {
           System.err.println("IO Exception in StdIn.readLong");
           l = 0L;
           inputOk = true;
        }
     }
     return l;
  }

   /**
    * This method reads a <code>float</code> value from the standard
    * input stream.
    *
    * @return  a <code>float</code> value
    */
  public float readFloat() {
     inputOk = false;
     float f = 0.0f;
     while ( !inputOk ) {
        try {
           st = getToken();
           f = new Float(st.nextToken()).floatValue();
           inputOk = true;
        }
        catch ( NumberFormatException e ) {
           System.err.println("Invalid format for float-try again:");
        }
        catch ( NoSuchElementException e ) {
           System.err.println("No value found-try again:");
        }
        catch ( IOException e ) {
           // This should normally never occur
           System.err.println("IO Exception in StdIn.readFloat");
           f = 0.0f;
           inputOk = true;
        }
     }
     return f;
  }

   /**
    * This method reads a <code>double</code> value from the standard
    * input stream.
    *
    * @return  a <code>double</code> value
    */
  public double readDouble() {
     inputOk = false;
     double d = 0.0;
     while ( !inputOk ) {
        try {
           st = getToken();
           d = new Double(st.nextToken()).doubleValue();
           inputOk = true;
        }
        catch ( NumberFormatException e ) {
           System.err.println("Invalid format for double-try again:");
        }
        catch ( NoSuchElementException e ) {
           System.err.println("No value found-try again:");
        }
        catch ( IOException e ) {
           // This should normally never occur
           System.err.println("IO Exception in StdIn.readDouble");
           d = 0.0;
           inputOk = true;
        }
     }
     return d;
  }

   /**
    * This method reads a <code>char</code> value from the standard
    * input stream.  It returns the first character of any string
    * typed at the keyboard.
    *
    * @return  a <code>char</code> value
    */
  public char readChar() {
     try {
       String s = br.readLine();
       return s.charAt(0);
     } catch ( IOException e ) {
        System.err.println("IO Exception in StdIn.readChar");
        return 0;
     }
  }

   /**
    * This method reads a <code>String</code> value from the standard
    * input stream.
    *
    * @return  a <code>String</code> value
    */
  public String readString() {
     try {
       return br.readLine();
     } catch ( IOException e ) {
        System.err.println("IO Exception in StdIn.readString");
        return "";
     }
  }

   /**
    * This method reads a line of data from the standard
    * input stream and returns it as a <code>String</code>.  
    * This method is equivalent to method <code>readString</code>.
    *
    * @return  a <code>String</code> containing the line
    */
  public String readLine() {
     try {
       return br.readLine();
     } catch ( IOException e ) {
        System.err.println("IO Exception in StdIn.readLine");
        return "";
     }
  }

  // This method gets a line from the standard input stream and
  // creates a StringTokenizer to parse it
  private StringTokenizer getToken() throws IOException {
     return new StringTokenizer(br.readLine());
  }

   /**
    * This <code>main</code> method tests class <code>StdIn</code>.
    */
   public static void main (String args[]) {
      StdIn in = new StdIn();

      System.out.print("enter char: "); System.out.flush();
      System.out.println("You entered: " + in.readChar() );

      System.out.print("enter String: "); System.out.flush();
      System.out.println("You entered: " + in.readString() );

      System.out.print("enter Line: "); System.out.flush();
      System.out.println("You entered: " + in.readLine() );

      System.out.print("enter boolean: "); System.out.flush();
      System.out.println("You entered: " + in.readBoolean() );

      System.out.print("enter byte: "); System.out.flush();
      System.out.println("You entered: " + in.readByte() );

      System.out.print("enter short: "); System.out.flush();
      System.out.println("You entered: " + in.readShort() );

      System.out.print("enter int: "); System.out.flush();
      System.out.println("You entered: " + in.readInt() );

      System.out.print("enter long: "); System.out.flush();
      System.out.println("You entered: " + in.readLong() );

      System.out.print("enter float: "); System.out.flush();
      System.out.println("You entered: " + in.readFloat() );

      System.out.print("enter double: "); System.out.flush();
      System.out.println("You entered: " + in.readDouble() );
   }
}
