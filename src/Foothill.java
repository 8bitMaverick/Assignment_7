public class Foothill
{
   public static void main(String[] args) throws CloneNotSupportedException
   {
      /* BarcodeImage main code
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

      BarcodeImage imObj1 = new BarcodeImage(userArray);
      BarcodeImage imObj2 = (BarcodeImage)imObj1.clone();
      BarcodeImage imObj3 = new BarcodeImage();

      // change ONLY the first object
      imObj1.setPixel(2, 2, true);
      imObj1.setPixel(4, 0, false);

      //First secret message
      imObj1.displayToConsole(); 
      imObj2.displayToConsole();
      imObj3.displayToConsole();*/
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

class BarcodeImage implements Cloneable
{
   // exact internal dimensions of 2D data
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;

   // where the incoming image is stored
   private boolean image_data[][];

   // default constructor
   BarcodeImage()
   {
      int row, col;
      image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for ( row = 0; row < image_data.length; row++ )
         for ( col = 0; col < image_data[row].length; col++ )
            image_data[row][col] = false;
   }

   /* 1-parameter constructor
      converts given 1D String array into internal 2D boolean array */
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
               image_data[row][col] = true;
      }
   }

   private boolean checkSize(String[] data )
   {
      if (data == null)
         return false;
      if (data.length > MAX_HEIGHT)
         return false;
      return true;
   }

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

   // accessor
   public boolean getPixel(int row, int col)
   {
      if (row < 0 || row >= MAX_HEIGHT || col < 0 || col >= MAX_WIDTH)
         return false; // use as an error (lame, but easy)
      return image_data[row][col];
   }

   // for debugging BarcodeImage only, will not be used otherwise
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