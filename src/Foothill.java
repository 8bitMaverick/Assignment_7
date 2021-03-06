/* Courtney Hunt JAVA 1B Assignment 7 Foothill College
 * 
 * This program is a pseudo Datamatrix barcode scanner. BarcodeImage class takes
 * in an image as a 1D string array, converts it to a 2D boolean array and
 * stores the image. Datamatrix class, which is implements the BarcodeIO
 * interface, utilizes BarcodeImage as it takes in either a BarcodeImage object
 * that is then converted to a text string or a text string that is then
 * converted to a displayable image. True is represented by '*' and False is
 * represented by ' '. An all black spine border is applied to the left and
 * bottom lines of the image as well as an alternating border is applied to the
 * top and right lines of the image. An encompassing border utilizing '-' and 
 * '|' surround each image when displayed. All white space outside the image's
 * alternating borders is clipped.
 */

import java.lang.Math;

public class Foothill
{
   public static void main(String[] args)
   {
      String[] sImageIn = 
         { 
               "                                      ",
               "                                      ",
               "* * * * * * * * * * * * * *           ",
               "*                          *          ",
               "***** **** **** ***** ****            ",
               "****************************          ",
               "*   *  * *  *   *  *  *  *            ",
               "**       *   **  **        *          ",
               "* **   *   * ** * * * *               ",
               "**     *   ***    **    ** *          ",
               "****  ****   **   ***  * **           ",
               "****************************          "
         };

      String[] sImageIn_2 = 
         { 
               "                                          ",
               "                                          ",
               "* * * * * * * * * * * * * * * * * * *     ",
               "*                                    *    ",
               "**** *** **   ***** ****   *********      ",
               "* ************ ************ **********    ",
               "** *      *    *  * * *         * *       ",
               "***   *  *           * **    *      **    ",
               "* ** * *  *   * * * **  *   ***   ***     ",
               "* *           **    *****  *   **   **    ",
               "****  *  * *  * **  ** *   ** *  * *      ",
               "**************************************    "
         };

      BarcodeImage bc = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bc);

      // First secret message
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // second secret message
      bc = new BarcodeImage(sImageIn_2);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // create your own message
      dm.readText("I'm :( this assignment was late, but I did my best.");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
   }   
}

interface BarcodeIO
{
   public boolean scan(BarcodeImage bc);
   public boolean readText(String text);
   public boolean generateImageFromText();
   public boolean translateImageToText();
   public void displayTextToConsole();
   public void displayImageToConsole();
}

// DataMatrix class-------------------------------------------------------------
class DataMatrix implements BarcodeIO
{

   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   public static final int MIN_TEXT_LENGTH = 0, MAX_TEXT_LENGTH = 100;

   private BarcodeImage image;
   private String text;
   private int actualHeight, actualWidth;

   // default constructor
   DataMatrix()
   {
      image = new BarcodeImage();
      text = "undefined";
      actualHeight = 0;
      actualWidth = 0;
   }

   // 1-parameter constructor, modifies image only
   DataMatrix(BarcodeImage image)
   {
      this();
      scan(image);
   }

   // 1-parameter constructor, modifies text only
   DataMatrix(String text)
   {
      this();
      readText(text);
   }

   // mutators
   public boolean readText(String text)
   {
      if (text.length() < MIN_TEXT_LENGTH || text.length() > MAX_TEXT_LENGTH)
         return false;
      
      this.text = text;
      return true;
   }

   public boolean scan(BarcodeImage image)
   {
      if (image == null)
         return false;
      
      try
      {
         this.image = (BarcodeImage)image.clone();
      } catch (CloneNotSupportedException e)
      {

      }

      actualHeight = computeSignalHeight();
      actualWidth = computeSignalWidth();

      return true;
   }

   private void clearImage()
   {
      int row, col;
      actualHeight = 0;
      actualWidth = 0;

      for ( row = 0; row < BarcodeImage.MAX_HEIGHT; row++ )
         for ( col = 0; col < BarcodeImage.MAX_WIDTH; col++ )
            image.setPixel(row, col, false);
   }

   // accessors
   public int getActualHeight()
   {
      return actualHeight;
   }

   public int getActualWidth()
   {
      return actualWidth;
   }

   private int computeSignalHeight()
   {
      for (int row = 0; row < BarcodeImage.MAX_HEIGHT; row++ )
         if(image.getPixel(row, 0) == true)
            return BarcodeImage.MAX_HEIGHT - row;
      return -1;
   }

   // set to private after debugging
   private int computeSignalWidth()
   {
      for (int col = 0; col < BarcodeImage.MAX_WIDTH; col++ )
         if(image.getPixel(BarcodeImage.MAX_HEIGHT - 1, col) == false)
            return col;
      return -1;
   }

   public boolean generateImageFromText()
   {
      int code = 0;
      clearImage();
      actualWidth = text.length() + 2;
      for(int k = 1; k < text.length() + 1; k++)
      {
         code = text.charAt(k - 1);
         writeCharToCol(k, code);
      }
      actualHeight += 2;
      createImageSpine();


      return true;
   }

   public boolean translateImageToText()
   {
      this.text = "";
      for (int col = 1; col < actualWidth - 1; col++)
      {
         this.text += readCharFromCol(col);
      }

      return true;
   }

   // helper methods
   private char readCharFromCol(int col)
   {
      int exp = 0;
      char fromCol = 0;
      for (int row = BarcodeImage.MAX_HEIGHT - 2;
            row > BarcodeImage.MAX_HEIGHT - actualHeight; row--)
      {
         if(image.getPixel(row, col) == true)
            fromCol += Math.pow(2, exp);
         exp++;
      }
      return fromCol;
   }

   private boolean writeCharToCol(int col, int code)
   {

      String binary = "";
      binary = Integer.toBinaryString(code);
      int exp = 0;

      if (binary.length() > actualHeight)
         actualHeight = binary.length();

      for(int row = BarcodeImage.MAX_HEIGHT - binary.length() - 1;
            row < BarcodeImage.MAX_HEIGHT - 1; row++)
      {
         if(binary.charAt(exp) == '1')
            image.setPixel(row, col, true);
         exp++;
      }

      return true;
   }

   private boolean createImageSpine()
   {
      int col = 0;
      int row = 0;

      // left black spine & right alternating border
      for (row = BarcodeImage.MAX_HEIGHT - actualHeight;
            row < BarcodeImage.MAX_HEIGHT; row++)
      {
         image.setPixel(row, 0, true);
         if ((row % 2) == 1 && row != BarcodeImage.MAX_HEIGHT - actualHeight)
            image.setPixel(row, actualWidth - 1, true);
      } 

      // top alternating border & bottom black border
      row = BarcodeImage.MAX_HEIGHT - actualHeight;
      for(col = 0; col < actualWidth; col++ )
      {
         image.setPixel(BarcodeImage.MAX_HEIGHT - 1, col, true);
         if ((col % 2) == 0)
            image.setPixel(row, col, true);
      }

      return true;
   }

   // display methods
   public void displayTextToConsole()
   {
      System.out.print(text);
   }

   public void displayImageToConsole()
   {
      int row, col;

      // top row border
      System.out.println();
      for ( col = 0; col < actualWidth + 2; col++ )
         System.out.print("-");
      System.out.println();

      // now each row from 0 to MAX_WIDTH, adding border chars
      for ( row = BarcodeImage.MAX_HEIGHT - actualHeight;
            row < BarcodeImage.MAX_HEIGHT; row++ )
      {
         System.out.print("|");
         for ( col = 0; col < actualWidth; col++ )
         {
            if (image.getPixel(row, col) == true)
               System.out.print("*");
            else
               System.out.print(" ");
         }
         System.out.println("|");
      }

      // bottom
      for (col = 0; col < actualWidth + 2; col++)
         System.out.print("-");
      System.out.println();
   }

   public void displayRawImage()
   {
      int row, col;

      // top row border
      System.out.println();
      for ( col = 0; col < BarcodeImage.MAX_WIDTH + 2; col++ )
         System.out.print("-");
      System.out.println();

      // now each row from 0 to MAX_WIDTH, adding border chars
      for ( row = 0; row < BarcodeImage.MAX_HEIGHT; row++ )
      {
         System.out.print("|");
         for ( col = 0; col < BarcodeImage.MAX_WIDTH; col++ )
         {
            if (image.getPixel(row, col) == true)
               System.out.print("*");
            else
               System.out.print(" ");
         }
         System.out.println("|");
      }

      // bottom
      for (col = 0; col < BarcodeImage.MAX_WIDTH + 2; col++)
         System.out.print("-");
      System.out.println();
   }
}

// Barcode class----------------------------------------------------------------
class BarcodeImage implements Cloneable
{
   // exact internal dimensions of 2D data
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;

   private boolean image_data[][];

   // default constructor, instantiates max height/width image filled w/"blanks"
   BarcodeImage()
   {
      int row, col;
      image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for ( row = 0; row < image_data.length; row++ )
         for ( col = 0; col < image_data[row].length; col++ )
            image_data[row][col] = false;
   }

   BarcodeImage(String[] str_data)
   {
      this();
      int row, col = 0;

      if ( !checkSize( str_data ) )
         return;  // silent, but there's an error, for sure.

      for ( row = 0; row < str_data.length; row++ )
      {
         char[] char_data = str_data[row].toCharArray();
         if (char_data[0] == '*')
            for ( col = 0; col < char_data.length; col++)
               if (char_data[col] == '*')
                  image_data[row + (MAX_HEIGHT - str_data.length)][col] = true;
      }
   }

   // validator
   private boolean checkSize(String[] data )
   {
      if (data == null)
         return false;
      if (data.length > MAX_HEIGHT)
         return false;
      if (data[0].length() > MAX_WIDTH)
         return false;
      return true;
   }

   // clone method
   public Object clone() throws CloneNotSupportedException
   {
      int row, col;

      // always do this first - parent will clone its image_data correctly
      BarcodeImage newBc = (BarcodeImage)super.clone();

      // now do the immediate class member objects
      newBc.image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for ( row = 0; row < MAX_HEIGHT; row++ )
         for ( col = 0; col < MAX_WIDTH; col++ )
            newBc.image_data[row][col] = this.image_data[row][col];

      return newBc;
   }

   // mutator
   public boolean setPixel(int row, int col, boolean value)
   {
      if (row < 0 || row >= MAX_HEIGHT || col < 0 || col >= MAX_WIDTH)
         return false;
      image_data[row][col] = value;
      return value;
   }

   // accessors
   public boolean getPixel(int row, int col)
   {
      if (row < 0 || row >= MAX_HEIGHT || col < 0 || col >= MAX_WIDTH)
         return false; // use as an error (lame, but easy)
      return image_data[row][col];
   }

   public int getImageHeight()
   {
      return image_data.length;
   }

   public int getImageWidth(int k)
   {
      return image_data[0].length;
   }

   // display for debugging BarcodeImage only, will not be used otherwise
   public void displayToConsole()
   {
      int row, col;

      // top row border
      System.out.println();
      for ( col = 0; col < BarcodeImage.MAX_WIDTH + 2; col++ )
         System.out.print("-");
      System.out.println();

      // now each row from 0 to MAX_WIDTH, adding border chars
      for ( row = 0; row < BarcodeImage.MAX_HEIGHT; row++ )
      {
         System.out.print("|");
         for ( col = 0; col < BarcodeImage.MAX_WIDTH; col++ )
         {
            if (image_data[row][col] == true)
               System.out.print("*");
            else
               System.out.print(" ");
         }
         System.out.println("|");
      }

      // bottom
      for (col = 0; col < BarcodeImage.MAX_WIDTH + 2; col++)
         System.out.print("-");
      System.out.println();
   }
}

/* Sample run-------------------------------------------------------------------
keep away from those tabs!
------------------------------
|* * * * * * * * * * * * * * |
|*                          *|
|***** **** **** ***** ****  |
|****************************|
|*   *  * *  *   *  *  *  *  |
|**       *   **  **        *|
|* **   *   * ** * * * *     |
|**     *   ***    **    ** *|
|****  ****   **   ***  * ** |
|****************************|
------------------------------
You did it!  Great work.  Celebrate.
----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|**** *** **   ***** ****   *********  |
|* ************ ************ **********|
|** *      *    *  * * *         * *   |
|***   *  *           * **    *      **|
|* ** * *  *   * * * **  *   ***   *** |
|* *           **    *****  *   **   **|
|****  *  * *  * **  ** *   ** *  * *  |
|**************************************|
----------------------------------------
I'm :( this assignment was late, but I did my best.
-------------------------------------------------------
|* * * * * * * * * * * * * * * * * * * * * * * * * * *|
|** *    **** ********** *** ****  *** * *** ** ****  |
|* ************************************ **************|
|*    *  *  *  **      * * *   *    **        *   **  |
|** * **  **     * ** *      *   *     *  *  **     **|
|* **    *        ****** *   * ***  **   * * *   * ** |
|* *  *     *  ** **  *  * *       *            * * **|
|****      ** ***** **   ***  * *   *  *  *  **  **   |
|*****************************************************|
-------------------------------------------------------
------------------------------------------------------------------------------*/