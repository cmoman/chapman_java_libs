//http://java.sun.com/products/jdk/javadoc/writingdoccomments.html
package chapman.io;
import java.io.*;

/**
 * A class for formatting numbers, characters, and Strings and
 * either sending them to the standard output stream ("<tt>printf</tt>")
 * or to an output string ("<tt>sprintf</tt>").  This class provides
 * <code>printf</code> and <code>sprintf</code> methods using
 * the C-language <tt>printf</tt> conventions.  The main limitation
 * of this class is that each method can handle only one output
 * value at a time, so you must make multiple calls to the <tt>Fmt</tt>
 * static methods <tt>printf</tt> or <tt>sprintf</tt> to print or format
 * a series of values.
 * <p>
 * This class supports output values of types <tt>double</tt>,
 * <tt>long</tt>, <tt>char</tt>, and <tt>String</tt>.  However, it will
 * work for any integer or floating-point type due to Java's coersion
 * of arguments.
 * <p>
 * This class is adapted with permission from Cay Horstmann's
 * <tt>Format.java</tt> class, which can be found in the book
 * <b>Core Java</b> (Book/CD-ROM), by Gary Cornell and Cay S. Horstmann,
 * published by SunSoft Press/Prentice-Hall,
 * copyright (C) 1996 Sun Microsystems Inc.  The original
 * copyright notice is reproduced in this class's source file.
 *
 * @version 1.30 1 August 1998
 * @author S. Chapman
 */

/*
 * Gary Cornell and Cay S. Horstmann, Core Java (Book/CD-ROM)
 * Published By SunSoft Press/Prentice-Hall
 * Copyright (C) 1996 Sun Microsystems Inc.
 * All Rights Reserved. ISBN 0-13-565755-5
 *
 * Permission to use, copy, modify, and distribute this
 * software and its documentation for NON-COMMERCIAL purposes
 * and without fee is hereby granted provided that this
 * copyright notice appears in all copies.
 *
 * THE AUTHORS AND PUBLISHER MAKE NO REPRESENTATIONS OR
 * WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHORS
 * AND PUBLISHER SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED
 * BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
public class Fmt {

   /* Declare private instance variables */
   private int width;
   private int precision;
   private String pre;
   private String post;
   private boolean leading_zeroes;
   private boolean show_plus;
   private boolean alternate;
   private boolean show_space;
   private boolean left_align;
   private char fmt; // one of cdeEfgGiosxXos


   //**************************************************************
   //
   // Constructor
   //
   //**************************************************************

   /*
    * @param s the format string following <tt>printf</tt> conventions.
    * The string has a prefix, a format code and a suffix. The prefix and suffix
    * become part of the formatted output. The format code directs the
    * formatting of the (single) parameter to be formatted. The code has the
    * following structure
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
    * <dt><tt>d</tt>, <tt>i</tt> <dd> integer in decimal
    * <dt><tt>x</tt> <dd> integer in hexadecimal
    * <dt><tt>o</tt> <dd> integer in octal
    * <dt><tt>s</tt> <dd> string
    * <dt><tt>c</tt> <dd> character
    * </dl>
    * </ul>
    * @exception IllegalArgumentException if bad format
    */
   private Fmt(String s) {
      width = 0;
      precision = -1;
      pre = "";
      post = "";
      leading_zeroes = false;
      show_plus = false;
      alternate = false;
      show_space = false;
      left_align = false;
      fmt = ' ';

      int state = 0;
      int length = s.length();
      int parse_state = 0;
      // 0 = prefix, 1 = flags, 2 = width, 3 = precision,
      // 4 = format, 5 = end
      int i = 0;

      while (parse_state == 0)
      {  if (i >= length) parse_state = 5;
         else if (s.charAt(i) == '%')
         {  if (i < length - 1)
            {  if (s.charAt(i + 1) == '%')
               {  pre = pre + '%';
                  i++;
               }
               else
                  parse_state = 1;
            }
            else throw new java.lang.IllegalArgumentException();
         }
         else
            pre = pre + s.charAt(i);
         i++;
      }
      while (parse_state == 1)
      {  if (i >= length) parse_state = 5;
         else if (s.charAt(i) == ' ') show_space = true;
         else if (s.charAt(i) == '-') left_align = true;
         else if (s.charAt(i) == '+') show_plus = true;
         else if (s.charAt(i) == '0') leading_zeroes = true;
         else if (s.charAt(i) == '#') alternate = true;
         else { parse_state = 2; i--; }
         i++;
      }
      while (parse_state == 2)
      {  if (i >= length) parse_state = 5;
         else if ('0' <= s.charAt(i) && s.charAt(i) <= '9')
         {  width = width * 10 + s.charAt(i) - '0';
            i++;
         }
         else if (s.charAt(i) == '.')
         {  parse_state = 3;
            precision = 0;
            i++;
         }
         else
            parse_state = 4;
      }
      while (parse_state == 3)
      {  if (i >= length) parse_state = 5;
         else if ('0' <= s.charAt(i) && s.charAt(i) <= '9')
         {  precision = precision * 10 + s.charAt(i) - '0';
            i++;
         }
         else
            parse_state = 4;
      }
      if (parse_state == 4)
      {  if (i >= length) parse_state = 5;
         else fmt = s.charAt(i);
         i++;
      }
      if (i < length)
         post = s.substring(i, length);
   }

   //**************************************************************
   //
   // Beginning of public static methods
   //
   //**************************************************************

   /**
    * Print a formatted number to the standard output stream following
    * <code>printf</code> conventions.  This method does not generate
    * a newline character, so you must include a <tt>\n</tt> character
    * at the end of the format string if you want to advance to a new
    * line.
    * <p>
    * Example usage:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>double time = 10.345;</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>Fmt.printf("Time = %8.2f s\n",time);</tt>
    * </dl>
    * <p>
    * Result:
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
   public static void printf(String fmt, double x) {
      System.out.print (new Fmt(fmt).form(x));
   }


   /**
    * Print a formatted number to the standard output stream following
    * <code>printf</code> conventions.  This method does not generate
    * a newline character, so you must include a <tt>\n</tt> character
    * at the end of the format string if you want to advance to a new
    * line.
    * <p>
    * Example usage:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>long i = 12345678;</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>Fmt.printf("i = %10d\n",i);</tt>
    * </dl>
    * <p>
    * Result:
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
   public static void printf(String fmt, long l) {
      System.out.print (new Fmt(fmt).form(l));
   }


   /**
    * Print a <tt>char</tt> to the standard output stream following
    * <code>printf</code> conventions.  This method does not generate
    * a newline character, so you must include a <tt>\n</tt> character
    * at the end of the format string if you want to advance to a new
    * line.
    * <p>
    * Example usage:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>char c = 'A';</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>Fmt.printf("character = %c\n",c);</tt>
    * </dl>
    * <p>
    * Result:
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
   public static void printf(String fmt, char c) {
      System.out.print (new Fmt(fmt).form(c));
   }


   /**
    * Print a <tt>String</tt> to the standard output stream following
    * <code>printf</code> conventions.  This method does not generate
    * a newline character, so you must include a <tt>\n</tt> character
    * at the end of the format string if you want to advance to a new
    * line.
    * <p>
    * Example usage:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>String s = "Test string!";</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>Fmt.printf("string = %s\n",s);</tt>
    * </dl>
    * <p>
    * Result:
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
   public static void printf(String fmt, String s) {
      System.out.print (new Fmt(fmt).form(s));
   }


   /**
    * Creates a string containing a formatted number following
    * <code>printf</code> conventions.  This method does not generate
    * a newline character, so you must include a <tt>\n</tt> character
    * at the end of the format string if you want to advance to a new
    * line.
    * <p>
    * Example usage:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>double time = 10.345;</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>String str = Fmt.sprintf("Time = %8.2f s",time);</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>System.out.println(str);</tt>
    * </dl>
    * <p>
    * Result:
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
   public static String sprintf(String fmt, double x) {
      return (new Fmt(fmt).form(x));
   }


   /**
    * Creates a string containing a formatted number following
    * <code>printf</code> conventions.
    * <p>
    * Example usage:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>long i = 12345678;</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>String str = Fmt.sprintf("i = %10d",i);</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>System.out.println(str);</tt>
    * </dl>
    * <p>
    * Result:
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
   public static String sprintf(String fmt, long l) {
      return (new Fmt(fmt).form(l));
   }


   /**
    * Creates a string containing a formatted character following
    * <code>printf</code> conventions.
    * <p>
    * Example usage:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>char c = 'A';</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>String str = Fmt.sprintf("character = %c",c);</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>System.out.println(str);</tt>
    * </dl>
    * <p>
    * Result:
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
   public static String sprintf(String fmt, char c) {
      return (new Fmt(fmt).form(c));
   }


   /**
    * Creates a string containing a formatted <tt>String</tt> following
    * <code>printf</code> conventions.
    * <p>
    * Example usage:
    * <dl>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>String s = "Test string!";</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>String str = Fmt.sprintf("string = %s",s);</tt>
    * <dt>&nbsp;&nbsp;&nbsp;&nbsp;<tt>System.out.println(str);</tt>
    * </dl>
    * <p>
    * Result:
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
   public static String sprintf(String fmt, String s) {
      return (new Fmt(fmt).form(s));
   }


   /**
   * A test stub for the <tt>Fmt</tt> class
   */
   public static void main(String[] a)
   {  double x = 1.23456789012;
      double y = 123;
      double z = 1.2345e30;
      double w = 1.02;
      double u = 1.234e-5;
      int d = 0xCAFE;
      System.out.println("printf methods:");
      Fmt.printf( "x = |%f|\n", x);
      Fmt.printf( "u = |%20f|\n", u);
      Fmt.printf( "x = |% .5f|\n", x);
      Fmt.printf( "w = |%20.5f|\n", w);
      Fmt.printf( "x = |%020.5f|\n", x);
      Fmt.printf( "x = |%+20.5f|\n", x);
      Fmt.printf( "x = |%+020.5f|\n", x);
      Fmt.printf( "x = |% 020.5f|\n", x);
      Fmt.printf( "y = |%#+20.5f|\n", y);
      Fmt.printf( "y = |%-+20.5f|\n", y);
      Fmt.printf( "z = |%20.5f|\n", z);

      Fmt.printf( "x = |%e|\n", x);
      Fmt.printf( "u = |%20e|\n", u);
      Fmt.printf( "x = |% .5e|\n", x);
      Fmt.printf( "w = |%20.5e|\n", w);
      Fmt.printf( "x = |%020.5e|\n", x);
      Fmt.printf( "x = |%+20.5e|\n", x);
      Fmt.printf( "x = |%+020.5e|\n", x);
      Fmt.printf( "x = |% 020.5e|\n", x);
      Fmt.printf( "y = |%#+20.5e|\n", y);
      Fmt.printf( "y = |%-+20.5e|\n", y);

      Fmt.printf( "x = |%g|\n", x);
      Fmt.printf( "z = |%g|\n", z);
      Fmt.printf( "w = |%g|\n", w);
      Fmt.printf( "u = |%g|\n", u);
      Fmt.printf( "y = |%.2g|\n", y);
      Fmt.printf( "y = |%#.2g|\n", y);

      Fmt.printf( "d = |%d|\n", d);
      Fmt.printf( "d = |%20d|\n", d);
      Fmt.printf( "d = |%020d|\n", d);
      Fmt.printf( "d = |%+20d|\n", d);
      Fmt.printf( "d = |% 020d|\n", d);
      Fmt.printf( "d = |%-20d|\n", d);
      Fmt.printf( "d = |%20.8d|\n", d);
      Fmt.printf( "d = |%x|\n", d);
      Fmt.printf( "d = |%20X|\n", d);
      Fmt.printf( "d = |%#20x|\n", d);
      Fmt.printf( "d = |%020X|\n", d);
      Fmt.printf( "d = |%20.8x|\n", d);
      Fmt.printf( "d = |%o|\n", d);
      Fmt.printf( "d = |%020o|\n", d);
      Fmt.printf( "d = |%#20o|\n", d);
      Fmt.printf( "d = |%#020o|\n", d);
      Fmt.printf( "d = |%20.12o|\n", d);

      Fmt.printf( "s = |%-20s|\n", "Hello");
      Fmt.printf( "s = |%-20c|\n", '!');

      System.out.println("\nsprintf methods:");
      System.out.print( Fmt.sprintf( "x = |%f|\n", x));
      System.out.print( Fmt.sprintf( "u = |%20f|\n", u));
      System.out.print( Fmt.sprintf( "x = |% .5f|\n", x));
      System.out.print( Fmt.sprintf( "w = |%20.5f|\n", w));
      System.out.print( Fmt.sprintf( "x = |%020.5f|\n", x));
      System.out.print( Fmt.sprintf( "x = |%+20.5f|\n", x));
      System.out.print( Fmt.sprintf( "x = |%+020.5f|\n", x));
      System.out.print( Fmt.sprintf( "x = |% 020.5f|\n", x));
      System.out.print( Fmt.sprintf( "y = |%#+20.5f|\n", y));
      System.out.print( Fmt.sprintf( "y = |%-+20.5f|\n", y));
      System.out.print( Fmt.sprintf( "z = |%20.5f|\n", z));

      System.out.print( Fmt.sprintf( "x = |%e|\n", x));
      System.out.print( Fmt.sprintf( "u = |%20e|\n", u));
      System.out.print( Fmt.sprintf( "x = |% .5e|\n", x));
      System.out.print( Fmt.sprintf( "w = |%20.5e|\n", w));
      System.out.print( Fmt.sprintf( "x = |%020.5e|\n", x));
      System.out.print( Fmt.sprintf( "x = |%+20.5e|\n", x));
      System.out.print( Fmt.sprintf( "x = |%+020.5e|\n", x));
      System.out.print( Fmt.sprintf( "x = |% 020.5e|\n", x));
      System.out.print( Fmt.sprintf( "y = |%#+20.5e|\n", y));
      System.out.print( Fmt.sprintf( "y = |%-+20.5e|\n", y));

      System.out.print( Fmt.sprintf( "x = |%g|\n", x));
      System.out.print( Fmt.sprintf( "z = |%g|\n", z));
      System.out.print( Fmt.sprintf( "w = |%g|\n", w));
      System.out.print( Fmt.sprintf( "u = |%g|\n", u));
      System.out.print( Fmt.sprintf( "y = |%.2g|\n", y));
      System.out.print( Fmt.sprintf( "y = |%#.2g|\n", y));

      System.out.print( Fmt.sprintf( "d = |%d|\n", d));
      System.out.print( Fmt.sprintf( "d = |%20d|\n", d));
      System.out.print( Fmt.sprintf( "d = |%020d|\n", d));
      System.out.print( Fmt.sprintf( "d = |%+20d|\n", d));
      System.out.print( Fmt.sprintf( "d = |% 020d|\n", d));
      System.out.print( Fmt.sprintf( "d = |%-20d|\n", d));
      System.out.print( Fmt.sprintf( "d = |%20.8d|\n", d));
      System.out.print( Fmt.sprintf( "d = |%x|\n", d));
      System.out.print( Fmt.sprintf( "d = |%20X|\n", d));
      System.out.print( Fmt.sprintf( "d = |%#20x|\n", d));
      System.out.print( Fmt.sprintf( "d = |%020X|\n", d));
      System.out.print( Fmt.sprintf( "d = |%20.8x|\n", d));
      System.out.print( Fmt.sprintf( "d = |%o|\n", d));
      System.out.print( Fmt.sprintf( "d = |%020o|\n", d));
      System.out.print( Fmt.sprintf( "d = |%#20o|\n", d));
      System.out.print( Fmt.sprintf( "d = |%#020o|\n", d));
      System.out.print( Fmt.sprintf( "d = |%20.12o|\n", d));

      System.out.print( Fmt.sprintf( "s = |%-20s|\n", "Hello"));
      System.out.print( Fmt.sprintf( "s = |%-20c|\n", '!'));
   }


   //**************************************************************
   //
   // Beginning of private methods
   //
   //**************************************************************

   /**
   * Formats a <tt>double</tt> into a string
   *
   * @param x the number to format
   * @return the formatted string
   * @exception IllegalArgumentException if bad argument
   */
   private String form(double x)
   {  String r;
      if (precision < 0) precision = 6;
      int s = 1;
      if (x < 0) { x = -x; s = -1; }
      if (fmt == 'f')
         r = fixed_format(x);
      else if (fmt == 'e' || fmt == 'E' || fmt == 'g' || fmt == 'G')
         r = exp_format(x);
      else throw new java.lang.IllegalArgumentException();

      return pad(sign(s, r));
   }


   /**
   * Formats a <tt>long</tt> into a string
   *
   * @param x the number to format
   * @return the formatted string
   */
   private String form(long x)
   {  String r;
      int s = 0;
      if (fmt == 'd' || fmt == 'i')
      {  s = 1;
         if (x < 0) { x = -x; s = -1; }
         r = "" + x;
      }
      else if (fmt == 'o')
         r = convert(x, 3, 7, "01234567");
      else if (fmt == 'x')
         r = convert(x, 4, 15, "0123456789abcdef");
      else if (fmt == 'X')
         r = convert(x, 4, 15, "0123456789ABCDEF");
      else throw new java.lang.IllegalArgumentException();

      return pad(sign(s, r));
   }


   /**
   * Formats a <tt>char</tt> into a string
   *
   * @param x the value to format
   * @return the formatted string
   */
   private String form(char c)
   {  if (fmt != 'c')
         throw new java.lang.IllegalArgumentException();

      String r = "" + c;
      return pad(r);
   }


   /**
   * Formats a <tt>String</tt> into another string
   *
   * @param x the value to format
   * @return the formatted string
   */
   private String form(String s)
   {  if (fmt != 's')
         throw new java.lang.IllegalArgumentException();
      if (precision >= 0) s = s.substring(0, precision);
      return pad(s);
   }


   private static String repeat(char c, int n)
   {  if (n <= 0) return "";
      StringBuffer s = new StringBuffer(n);
      for (int i = 0; i < n; i++) s.append(c);
      return s.toString();
   }


   private static String convert(long x, int n, int m, String d)
   {  if (x == 0) return "0";
      String r = "";
      while (x != 0)
      {  r = d.charAt((int)(x & m)) + r;
         x = x >>> n;
      }
      return r;
   }


   private String pad(String r)
   {  String p = repeat(' ', width - r.length());
      if (left_align) return pre + r + p + post;
      else return pre + p + r + post;
   }


   private String sign(int s, String r)
   {  String p = "";
      if (s < 0) p = "-";
      else if (s > 0)
      {  if (show_plus) p = "+";
         else if (show_space) p = " ";
      }
      else
      {  if (fmt == 'o' && alternate && r.length() > 0 && r.charAt(0) != '0') p = "0";
         else if (fmt == 'x' && alternate) p = "0x";
         else if (fmt == 'X' && alternate) p = "0X";
      }
      int w = 0;
      if (leading_zeroes)
         w = width;
      else if ((fmt == 'd' || fmt == 'i' || fmt == 'x' || fmt == 'X' || fmt == 'o')
         && precision > 0) w = precision;

      return p + repeat('0', w - p.length() - r.length()) + r;
   }


   private String fixed_format(double d) {
      String f = "";

      if (d > 0x7FFFFFFFFFFFFFFFL) return exp_format(d);

      // Get the string without the decimal point
      double factor = Math.pow(10,precision);
      long l = (long) (factor*(d + 0.5 / factor));
      StringBuffer sb = new StringBuffer( f + l );

      // Add leading zeros, if necessary.
      if ( sb.length() - precision < 1 ) {
         int endValue = precision - sb.length() + 1;
         for ( int i = 1; i <= endValue; i++ ) {
            sb.insert(0,'0');
         }
      }

      // Insert the decimal point
      // System.out.println("sb.length = " + sb.length());
      sb.insert( sb.length()-precision, '.');
      f = sb.toString();

      // Remove trailing zeroes and decimal point for 'g' descriptor
      if ( (fmt == 'G' || fmt == 'g') && !alternate ) {
         int t = f.length() - 1;
         while (t >= 0 && f.charAt(t) == '0')
            t--;
         if (t >= 0 && f.charAt(t) == '.')
            t--;
         f = f.substring(0, t + 1);
      }
      return f;
   }


   private String frac_part(double fr)
   // precondition: 0 <= fr < 1
   {  String z = "";
      if (precision > 0)
      {  double factor = 1;
         String leading_zeroes = "";
         for (int i = 1; i <= precision && factor <= 0x7FFFFFFFFFFFFFFFL; i++)
         {  factor *= 10;
            leading_zeroes = leading_zeroes + "0";
         }
         long l = (long) (factor * fr + 0.5);

         z = leading_zeroes + l;
         z = z.substring(z.length() - precision, z.length());
      }

      if (precision > 0 || alternate) z = "." + z;
      if ((fmt == 'G' || fmt == 'g') && !alternate)
      // remove trailing zeroes and decimal point
      {  int t = z.length() - 1;
         while (t >= 0 && z.charAt(t) == '0') t--;
         if (t >= 0 && z.charAt(t) == '.') t--;
         z = z.substring(0, t + 1);
      }
      return z;
   }


   private String exp_format(double d)
   {  String f = "";
      int e = 0;
      double dd = d;
      double factor = 1;
      while (dd > 10) { e++; factor /= 10; dd = dd / 10; }
      while (dd < 1) { e--; factor *= 10; dd = dd * 10; }
      if ((fmt == 'g' || fmt == 'G') && e >= -4 && e < precision)
         return fixed_format(d);

      d = d * factor;
      f = f + fixed_format(d);

      if (fmt == 'e' || fmt == 'g')
         f = f + "e";
      else
         f = f + "E";

      String p = "000";
      if (e >= 0)
      {  f = f + "+";
         p = p + e;
      }
      else
      {  f = f + "-";
         p = p + (-e);
      }

      return f + p.substring(p.length() - 3, p.length());
   }
}
