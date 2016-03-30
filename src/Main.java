import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Course: COMP 2071
 * Assignment: Lab 5
 * Group #: 11
 * Group members: Tom Plano, Cliff Anderson, Will Lawrence, Artur Janowiec
 * Due date: 4/6/16
 */
public class Main {

    /**
     * the main method
     * reads in every line from a given text file then adds the line to a hashmap
     * if the line already exists in the hashmap then it increments the lines value by 1
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        HashMap<String, Integer> dups = new HashMap<String, Integer>();
        BufferedReader reader = new BufferedReader(new FileReader(new File("resources/testFile.txt")));
        String line = "";
        while((line = reader.readLine())!=null)
        {
            if(!dups.containsKey(line))
            {
                dups.put(line,1);
            }
            else
            {
                int total = dups.get(line);
                total++;
                dups.put(line,total);
            }
        }
        printMap(dups);

    }

    /**
     * iterates through a hashmap passed to it
     * prints out each entry as long as its value does not equal 1
     * @param mp
     */
    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(!pair.getValue().equals(1))
            {
                System.out.println(pair.getKey() + " = " + pair.getValue());
            }


            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}
