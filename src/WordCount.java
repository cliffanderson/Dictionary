import java.io.*;
import java.nio.file.Files;
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
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);

        //First make the table
        int initialSize = getInitialSize(input);
        input.close();

        File textFileDir = new File("resources");

        //For every file, make a hashtable, log metrics, and log output
        for (File testFile : textFileDir.listFiles()) {
            //The file name without ending
            String fileName = testFile.getName().replace(".txt", "");

            //Make metrics and output files
            File metricsFile = new File("hash_table_metrics" + File.separator + fileName + ".metrics");
            File outputFile = new File("hash_table_output" + File.separator + fileName + ".output");

            //Delete files if they already exist
            if(metricsFile.exists()) metricsFile.delete();
            if(outputFile.exists()) outputFile.delete();

            //Create hash table with initial size and metrics file
            HashTable table = new HashTable(initialSize, metricsFile);

            //Read in the test file and add all words (free of punctuation) to the table
            System.out.println("Reading in file: " + fileName);
            fillTableWithFileContents(testFile, table);

            //Print metrics one last time
            table.writeMetricsToFileWithMessage("Final Metrics:");

            generateAndPrintOutput(table, outputFile);
        }
    }

    /**
     * Generate useful output given a hashtable
     * @param table The Hashtable
     */
    private static void generateAndPrintOutput(HashTable table, File outputFile) throws Exception
    {
        //print the words and their frequency to an output file
        PrintWriter out = new PrintWriter(new FileWriter(outputFile));

        for(String s : table.keySet())
        {
            out.println(s + " " + table.get(s).get());
        }

        out.flush();
        out.close();
    }

    /**
     * Helper method to take file contents and add it to the table, incrementing if the word is already in the table
     * @param file The file to fill the table with
     * @param table The HashTable to fill
     * @throws Exception Problem with IO
     */
    private static void fillTableWithFileContents(File file, HashTable table) throws Exception
    {
        //Read in file in bytes
        byte[] bytes = Files.readAllBytes(file.toPath());

        //Convert bytes to string and replace new lines with spaces
        String data = new String(bytes).replace("\n", " ");

        //to all lowercase
        data = data.toLowerCase();

        //remove all characters that are not a-z or spaces
        data = data.replaceAll("[^a-z\\s]", "");

        //split by spaces
        String[] words = data.split("\\s");

        //for each words add it to hashmap or increment count if it exists
        //The incremented count is then added as the new value for the key.
        //This way, for the dictionary, we can know how many times a "key" aka a word appears.
        for(String word : words)
        {
            if(word.isEmpty()) continue;

            if(table.containsKey(word))
            {
                table.get(word).incrementAndGet();
            }
            else
            {
                table.put(word, new AtomicInteger(1));
            }
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
}
