import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Course: COMP 2071
 * Assignment: Lab 5
 * Group #: 11
 * Group members: Tom Plano, Cliff Anderson, Will Lawrence, Artur Janowiec
 * Due date: 4/6/16
 */
public class WordCount {

    /**
     * the main method
     * reads in words from a text file, formats them, and adds to the hashmap
     * if the word already exists in the hashmap then it increments the value by 1
     * @param args Command line arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        Scanner input = new Scanner(System.in);

        //First make the table
        int size = getInitialSize(input);
        HashMapCustom table = new HashMapCustom(size);


        File file = selectFile(new File("resources"), input);

        //can now close the scanner
        input.close();

        //track runtime after selecting file
        trackRunningTime();


        BufferedReader reader = new BufferedReader(new FileReader(file));
        //All data
        String data = "";
        //The current line we are reading
        String line = "";

        while((line = reader.readLine())!=null)
        {
            data += line + " ";
        }
        reader.close();

        //to all lowercase
        data = data.toLowerCase();

        //remove all characters that are not a-z or spaces
        data = data.replaceAll("[^a-z\\s]", "");

        //split by spaces
        String[] words = data.split("\\s");
        System.out.println(String.valueOf(words.length) + " words");

        //for each words add it to hashmap or increment count if it exists
        //The incremented count is then added as the new value for the key.
        //This way, for the dictionary, we can know how many times a "key" aka a word appears.
        for(String word : words)
        {
            if(table.containsKey(word))
            {
                table.get(word).incrementAndGet();
            }
            else
            {
                table.put(word, new AtomicInteger(1));
            }
        }

        printHashTable(table);

        table.printTableAnalysis();

        System.out.println("Size of table (All items including those in the same array index): " + table.size());
    }

    /**
     * iterates through a hashmap passed to it
     * prints out each entry as long as its value does not equal 1
     * @param table The HashTable
     */
    public static void printHashTable(HashMapCustom table) {
        Iterator ourIterator = table.keySet().iterator();

        while (ourIterator.hasNext()) {
            String key = (String) ourIterator.next();
            System.out.println(key + " " + table.get(key));
        }
    }

    public static int getInitialSize(Scanner input)
    {
        System.out.println("Please enter the initial size of the hashtable.");
        System.out.println("If it is not prime it will be increased to the next prime: ");

        int initialSize = -1;

        while(initialSize < 0)
        {
            initialSize = Integer.parseInt(input.nextLine());
        }

        //if not prime increase
        int primeInitialSize = MathUtils.getNextPrime(initialSize);
        if(primeInitialSize != initialSize)
        {
            System.out.println("Initial size was increased to " + primeInitialSize + " to make it prime");
        }

        return primeInitialSize;
    }

    /**
     * Method for choosing a file from a directory
     * @param dir The directory
     * @return The chosen file
     */
    public static File selectFile(File dir, Scanner input)
    {
        //First null check
        if(dir == null) return null;

        //Create list of files in dir (but not dirs)
        List<File> files = new ArrayList<File>();

        for(File f : dir.listFiles())
        {
            if(f.isFile()) files.add(f);
        }

        //Now print out info
        System.out.println("==============================");
        for(int i = 0; i < files.size(); i++)
        {
            System.out.println(String.valueOf(i) + ". " + files.get(i).getName());
        }
        System.out.println("==============================");

        //Read in choice
        int choice = -1;

        while(choice < 0 || choice >= files.size())
        {
            System.out.println("Please input your choice between 0 and " + (files.size() - 1));
            choice = Integer.parseInt(input.nextLine());
        }

        return files.get(choice);
    }

    /**
     * Find out how long the program took to run
     */
    static void trackRunningTime()
    {
        final long start = System.currentTimeMillis();

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                System.out.println("\n\nTotal run time: " + (System.currentTimeMillis() - start) + " ms");
            }

        });
    }
}
