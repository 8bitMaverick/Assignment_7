public class Foothill
{
   public static void main(String[] args) throws CloneNotSupportedException
   {
      int[][] userArray = {
            {1, 1, 1, 1, 1},
            {2, 2, 2, 2, 2},
            {3, 3, 3, 3, 3},
      };
      
      BarcodeImage imObj1 = new BarcodeImage(userArray);
      BarcodeImage imObj2 = (BarcodeImage)imObj1.clone();
      
      // change ONLY the first object
      imObj1.setElement(2, 2, 9);
      imObj1.setElement(4, 0, 9);
     
      // First secret message
      imObj1.display(); 
      imObj2.display();
   }   
}

class BarcodeImage implements Cloneable
{
   // exact internal dimensions of 2D image_data
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   
   // where image is stored
   private boolean image_data[][];
   
   BarcodeImage()
   {
      int row, col;
      image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      for ( row = 0; row < image_data.length; row++ )
         for ( col = 0; col < image_data[row].length; col++ )
            image_data[row][col] = false;
   }
   
   BarcodeImage(boolean[][] booleanData)
   {
      this();
      int row, col;
      
      if ( !checkSize( booleanData ) )
         return;  // silent, but there's an error, for sure.

      for ( row = 0; row < booleanData.length; row++ )
         for ( col = 0; col < booleanData[row].length; col++ )
            image_data[row][col] = booleanData[row][col];
   }

   
   private boolean checkSize(boolean[][] image_data )
   {
      if (image_data == null)
         return false;
      if (image_data.length > MAX_HEIGHT)
         return false;
      if (image_data[0].length > MAX_WIDTH) // since rectangle, only check row 0
         return false;
      return true;
   }
   
   public Object clone() throws CloneNotSupportedException
   {
      int row, col;
      
      // always do this first - parent will clone its image_data correctly
      BarcodeImage newBc = (BarcodeImage)super.clone();
      
      // now do the immediate class member objects
      newBc.image_data = new int[MAX_HEIGHT][MAX_WIDTH];
      for ( row = 0; row < MAX_HEIGHT; row++ )
         for ( col = 0; col < MAX_WIDTH; col++ )
            newBc.image_data[row][col] = this.image_data[row][col];
      
      return newBc;
   }
   
   public boolean setElement(int row, int col, int val)
   {
      if (row < 0 || row >= MAX_HEIGHT || col < 0 || col >= MAX_WIDTH)
         return false;
      image_data[row][col] = val;
      return true;
   }
   public int getElement(int row, int col)
   {
      if (row < 0 || row >= MAX_HEIGHT || col < 0 || col >= MAX_WIDTH)
         return Integer.MAX_VALUE; // use as an error (lame, but easy)
      return image_data[row][col];
   }
   
   
   public void display()
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
            System.out.print(image_data[row][col]);
         System.out.println("|");
      }
      
      // bottom
      for (col = 0; col < BarcodeImage.MAX_WIDTH + 2; col++)
         System.out.print("-");
      System.out.println();
   }
}

/*-----------------------------OUTPUT---------------------------------------------

-------
|11111|
|22222|
|33933|
|00000|
|90000|
-------

-------
|11111|
|22222|
|33333|
|00000|
|00000|
-------

------------------------------------------------------------------------*/