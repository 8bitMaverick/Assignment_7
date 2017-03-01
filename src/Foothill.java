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
      bc.displayToConsole();
      dm.translateImageToText();
      dm.generateImageFromText();
      //System.out.println(dm.computeSignalHeight());
      

      // First secret message
      //dm.translateImageToText();
      //dm.displayTextToConsole();
      //dm.displayImageToConsole();

      // second secret message
      //bc = new BarcodeImage(sImageIn_2);
      //dm.scan(bc);
      //dm.translateImageToText();
      //dm.displayTextToConsole();
      //dm.displayImageToConsole();

      // create your own message
      //dm.readText("CS 1B will lead me to the CASH!");
      //dm.generateImageFromText();
      //dm.displayTextToConsole();
      //dm.displayImageToConsole();

      // BarcodeImage main code
      
      String[] userArray = 
         { 
               "                                      ",
               "                                      ",
               "                                      ",
               "* * * * * * * * * * * * * * * * *     ",
               "*                                *    ",
               "**** * ****** ** ****** *** ****      ",
               "* ********************************    ",
               "*    *   *  * *  *   *  *   *  *      ",
               "* **    *      *   **    *       *    ",
               "****** ** *** **  ***** * * *         ",
               "* ***  ****    * *  **        ** *    ",
               "* * *   * **   *  *** *   *  * **     ",
               "**********************************    "
         };

      //BarcodeImage imObj1 = new BarcodeImage(userArray);
      //BarcodeImage imObj2 = (BarcodeImage)imObj1.clone();
      //BarcodeImage imObj3 = new BarcodeImage();

      /* change ONLY the first object
      imObj1.setPixel(2, 2, true);
      imObj1.setPixel(4, 0, false);*/

      // For debugging BarcodeImage class First secret message
      //imObj1.displayToConsole(); 
      /*imObj2.displayToConsole();
      imObj3.displayToConsole();*/

      // test clearImage later when DataMatrix setup
      // imObj2.clearImage();
      // imObj2.displayToConsole();
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

   // a single internal copy of any image scanned-in
   // OR passed-into the constructor
   // OR created by BarcodeIO's generateImageFromText()
   private BarcodeImage image;

   // a single internal copy of any text read-in
   // OR passed-into the constructor
   // OR created by BarcodeIO's translateImageToText()
   private String text;

   // represent the actual portion of the BarcodeImage that has real signal
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
      
      for(int k = 0; k < text.length(); k++)
      {
         code = Integer.parseInt(String.valueOf(text.charAt(k)), 16);
         System.out.print(code);
         writeCharToCol(k, code);
      }
      return true;
   }

   public boolean translateImageToText()
   {
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
      System.out.print(fromCol);
      return fromCol;
   }

   private boolean writeCharToCol(int col, int code)
   {
      int exp = 0;
      char fromCol = 0;
      String binary = Integer.toBinaryString(code);
      System.out.print(binary);
      /*for (int row = BarcodeImage.MAX_HEIGHT - 2;
            row > BarcodeImage.MAX_HEIGHT - actualHeight; row--)
      {
         if(image.getPixel(row, col) == true)
             fromCol += Math.pow(2, exp);
         exp++;
      }*/
      return true;
   }

   // display methods
   public void displayTextToConsole()
   {

   }

   public void displayImageToConsole()
   {

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

   // where the incoming image is stored
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

   /* 1-parameter constructor
      converts given 1D String array into internal 2D boolean array
      packs smaller than max height/width image into lower left corner*/
   BarcodeImage(String[] str_data)
   {
      this();
      int row, col;

      if ( !checkSize( str_data ) )
         return;  // silent, but there's an error, for sure.

      for ( row = 0; row < str_data.length; row++ )
      {
         char[] char_data = str_data[row].toCharArray();
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