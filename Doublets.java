import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

/**
 * Doublets.java
 * Provides an implementation of the WordLadderGame interface. The lexicon
 * is stored as a TreeSet of Strings.
 *
 * @author Your Name (you@auburn.edu)
 * @author Dean Hendrix (dh@auburn.edu)
 * @version 2017-04-28
 */
public class Doublets implements WordLadderGame {

   ////////////////////////////////////////////
   // DON'T CHANGE THE FOLLOWING TWO FIELDS. //
   ////////////////////////////////////////////

   // A word ladder with no words. Used as the return value for the ladder methods
   // below when no ladder exists.
   //List<String> EMPTY_LADDER = new ArrayList<>();

   // The word list used to validate words.
   // Must be instantiated and populated in the constructor.
   TreeSet<String> lexicon;


   /**
    * Instantiates a new instance of Doublets with the lexicon populated with
    * the strings in the provided InputStream. The InputStream can be formatted
    * in different ways as long as the first string on each line is a word to be
    * stored in the lexicon.
    */
   public Doublets(InputStream in) {
      try {
         lexicon = new TreeSet<String>();
         Scanner s =
            new Scanner(new BufferedReader(new InputStreamReader(in)));
            
         while (s.hasNext()) {
            String str = s.next();
            ////////////////////////////////////////////////
            // Add code here to store str in the lexicon. //
            ////////////////////////////////////////////////
            lexicon.add(str.toLowerCase());
            
            s.nextLine();
         }
         in.close();
      }
      catch (java.io.IOException e) {
         System.err.println("Error reading from InputStream.");
         System.exit(1);
      }
   }

   ///////////////////////////////////////////////////////////////////////////////
   // Fill in implementations of all the WordLadderGame interface methods here. //
   ///////////////////////////////////////////////////////////////////////////////


    /**
    * Returns the Hamming distance between two strings, str1 and str2. The
    * Hamming distance between two strings of equal length is defined as the
    * number of positions at which the corresponding symbols are different. The
    * Hamming distance is undefined if the strings have different length, and
    * this method returns -1 in that case. See the following link for
    * reference: https://en.wikipedia.org/wiki/Hamming_distance
    *
    * @param  str1 the first string
    * @param  str2 the second string
    * @return      the Hamming distance between str1 and str2 if they are the
    *                  same length, -1 otherwise
    */

   public int getHammingDistance(String str1, String str2) {
      
      if (str1 == null || str2 == null) {
         throw new IllegalArgumentException();
      }
      
      if (str1.length() != str2.length()) {
         return -1;
      } 
      
      str1 = str1.toLowerCase();
      str2 = str2.toLowerCase();
      
      int result = 0;
      
      for (int i = 0; i < str1.length(); i++) {
         if (str1.charAt(i) != str2.charAt(i)) {
            result ++;
         }
      
      }
      
      return result;
   }


  /**
   * Returns a minimum-length word ladder from start to end. If multiple
   * minimum-length word ladders exist, no guarantee is made regarding which
   * one is returned. If no word ladder exists, this method returns an empty
   * list.
   *
   * Breadth-first search must be used in all implementing classes.
   *
   * @param  start  the starting word
   * @param  end    the ending word
   * @return        a minimum length word ladder from start to end
   */
   public List<String> getMinLadder(String start, String end) { 
   
      start = start.toLowerCase();
      end = end.toLowerCase();
      
      if (start.equals(end)) {
         List<String> list = new ArrayList<String>();
         list.add(start);
         return list;   
      }
         
      List<String> result = new Stack<String>();
      Map<String, String> tree = new HashMap<String, String>();
      Set<String> visited = new TreeSet<String>();
      Deque<String> qq = new ArrayDeque<String>();
      qq.addLast(start);
      
      while (!qq.isEmpty()) {
      
         String current = qq.removeFirst();
         visited.add(current);
         
         for (String neighbor : getNeighbors(current)) {
               
            if (!visited.contains(neighbor)) {
            // instantiated HashMap<String, String> tree, therefore a string points to another string
            // the first string will be the key, the 2nd string will be the desired return value
            // therefore hashmap(key, value)... hashmap(key) == value
            // assume the final result will be [cat, mat, hat]
            // the desired output for a map will point back to the previous 'node'
            // therefore tree(end) == 2nd-to-last, tree(2nd-to-last) == first
            // eg tree(hat) == mat, and tree(mat) == cat. 
               tree.put(neighbor, current);
               visited.add(neighbor);
               qq.add(neighbor);
            
               if (neighbor.equals(end)) {
               // if the end is found make the deque empty to break the original while condition
               // acts like a break statement but were in too deep bby 
                  qq = new ArrayDeque<String>();
                  
                  // the value returned by walkbackward() is a stack
                  result = walkBackward(tree, end);
               }
            }
         }
      
      } 
   
      return result;
   }


   private List<String> walkBackward(Map<String, String> tree, String end) {
      
      Stack<String> bkpath = new Stack<String>();
      Stack<String> path = new Stack<String>();
      
      // a while statement where the condition breaks when the 'node' (not really a node) has no previous node
      // in this case the hashmap key will have no value because it is the start
      while (end != null) {
      // the hashmap will begin at the end, and point back to the beginning
      // therefore the stack produced by this while loop will be in reverse order
         bkpath.push(end);
         end = tree.get(end);
      }
   
      while (!bkpath.isEmpty()) {
      // this is definitely retarded, but it pops the values from the backwards stack into a new stack
      // which will thereafter be in the desired order
         path.push(bkpath.pop());
      }
      
      return path;
   }


   /**
    * Returns all the words that have a Hamming distance of one relative to the
    * given word.
    *
    * @param  word the given word
    * @return      the neighbors of the given word
    */
   public List<String> getNeighbors(String word) {
   
      if (word == null) {
         throw new IllegalArgumentException();
      }
      
      List<String> neighbors = new ArrayList<String>();
      
      word = word.toLowerCase();
      
      for (int i = 0; i < word.length(); i++) {
      
         char[] charArray = word.toCharArray();
      
         for (char ch = 'a'; ch <= 'z'; ch++) {
            
            charArray[i] = ch;
            
            String charString = new String(charArray);
            
            if (!charString.equals(word) && isWord(charString)) {
               neighbors.add(charString);
            }
         
         }
      }
      
      return neighbors;
   }


   /**
    * Returns the total number of words in the current lexicon.
    *
    * @return number of words in the lexicon
    */
   public int getWordCount() {
      return lexicon.size();
   }
   


   /**
    * Checks to see if the given string is a word.
    *
    * @param  str the string to check
    * @return     true if str is a word, false otherwise
    */
   public boolean isWord(String str) {
      return lexicon.contains(str.toLowerCase());
   }


   /**
    * Checks to see if the given sequence of strings is a valid word ladder.
    *
    * @param  sequence the given sequence of strings
    * @return          true if the given sequence is a valid word ladder,
    *                       false otherwise
    */
   public boolean isWordLadder(List<String> sequence) {
            
      if (sequence.isEmpty() || !isWord(sequence.get(0))) {
         return false;
      }
      
      for (int i = 1; i < sequence.size(); i++) { 
      
         if (!isWord(sequence.get(i)) 
            || getHammingDistance(sequence.get(i - 1), sequence.get(i)) != 1) {
            
            return false;
         }
         
      }
      return true;
   }

}

