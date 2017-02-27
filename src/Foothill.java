public class Foothill
{
   public static void main(String[] args)
   {
      Card[][] deck = new Card[4][13];
      int row, col;
      Card.Suit st;
      char val;
      
      // instantiate the array elements
      for (row = 0; row < deck.length; row++)
         for (col = 0; col < deck[row].length; col++)
            deck[row][col] = new Card();
    
      // assign values to array elements
      for (row = 0; row < deck.length; row++)
      { 
         // set the suit for this loop pass
         st = Card.Suit.values()[row];
 
         // now set all the values for this suit
         deck[row][0].set('A', st);
         for (val='2', col = 1; val <= '9'; val++, col++)
            deck[row][col].set(val, st);
         deck[row][9].set('T', st);
         deck[row][10].set('J', st);
         deck[row][11].set('Q', st);
         deck[row][12].set('K', st);
      }
      
      // test compareTo with queen of spades
      Card queenOfSpades = new Card('q', Card.Suit.spades);
      String phrase;
      for ( row = 0; row < deck.length; row++)
      {
         for ( col = 0; col < deck[row].length; col ++)
         {
            if ( queenOfSpades.compareTo(deck[row][col]) < 0 )
                  phrase = " is less than ";
            else if ( queenOfSpades.compareTo(deck[row][col]) > 0 )
                  phrase = " is greater than ";
            else
                  phrase = " is equal to ";
                 
            System.out.println(queenOfSpades + phrase
                  + deck[row][col] );
         }   
      }
   }
}
class Card implements Comparable
{   

   // type and constants
   public enum Suit { clubs, diamonds, hearts, spades }

   // for ordering
   public static char[] valueRanks = { '2', '3', '4', '5', '6', '7', '8', '9', 
      'T', 'J', 'Q', 'K', 'A'};
   static Suit[] suitRanks = {Suit.clubs, Suit.diamonds, Suit.hearts, 
      Suit.spades};
   static int numValsInOrderingArray = 13;

   // private data
   private char value;
   private Suit suit;

   // sort member methods
   public int compareTo(Object other)
   {
      if (! (other instanceof Card) )
         return -1;
      
      Card otherCard = (Card)other;
      
      if (this.value == otherCard.value)
         return ( getSuitRank(this.suit) - getSuitRank(otherCard.suit) );

      return ( 
            getValueRank(this.value) 
               - getValueRank(otherCard.value) 
            );
   }

   // helpers for compareTo()
   public static int getSuitRank(Suit st)
   {
      int k;

      for (k = 0; k < 4; k++) 
         if (suitRanks[k] == st)
            return k;

      // should not happen
      return 0;
   }

   public  static int getValueRank(char val)
   {
      int k;

      for (k = 0; k < numValsInOrderingArray; k++) 
         if (valueRanks[k] == val)
            return k;

      // should not happen
      return 0;
   }

   // lots of irrelevant things omitted
}
