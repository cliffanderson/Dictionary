import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Course: COMP 2071
 * Assignment: Lab 5
 * Group #: 11
 * Group members: Tom Plano, Cliff Anderson, Will Lawrence, Artur Janowiec
 * Due date: 4/6/16
 */
public class HashTable
{
    //amount of items in the HashTable. Keep in mind that a single array spot can hold many items.
    private int currentSize = 0;

    //internal array to hold Nodes
    private TeamLinkedList[] buckets;

    //File to log metrics to
    private PrintWriter metricsFileOut;

    public HashTable(int initialSize, File metricsFile) throws Exception
    {
        if(initialSize <= 0)
        {
            throw new Exception("Initial size must be greater than 0");
        }

        //make initialSize prime
        if(!MathUtils.isPrime(initialSize)) {
            initialSize = MathUtils.getNextPrime(initialSize);
        }

        this.buckets = new TeamLinkedList[initialSize];

        this.metricsFileOut = new PrintWriter(new FileWriter(metricsFile));
    }

    /**
     * Add a new entry to the HashTable. If the entry already exists, replace the value
     * @param s the String
     * @param i the value
     */
    public void put(String s, AtomicInteger i)
    {
        privatePut(s, i, true);
    }

    /**
     * Private put method. When resizing, put is called inside to add elements back into the table. This can lead to
     * the chaining of multiple resizes, and messes up the metrics output. To stop that, we recursively call this private
     * put method and do not allow resizing
     * @param s
     * @param i
     * @param allowResize
     */
    public void privatePut(String s, AtomicInteger i, boolean allowResize)
    {
        /*
        if s exists in any node replace the value
        else add new string/ int pair
         */
        int index = this.getStringHashCode(s) % this.buckets.length;
        TeamLinkedList list = this.buckets[index];

        if (list == null){
            //Make a new list
            this.buckets[index] = new TeamLinkedList();
            this.buckets[index].add(s, i);
            this.currentSize++;
        }
        else
        {
            //add to an existing list
            this.buckets[index].add(s, i);
            this.currentSize++;
        }

        //Check to see if resizing is needed
        //Used array space must not exceed 75% of the array length
        if(allowResize && this.getLoadFactor() > 1.0)
        {
            //Must resize array
            //first record metrics to file
            this.writeMetricsToFileWithMessage("\n\n\nPre Resize");

            //Move array to a temp variable
            TeamLinkedList[] temp = this.buckets;

            //Get the new size, the next prime greater than the current length * 2
            int newSize = MathUtils.getNextPrime(this.buckets.length * 2);
            this.buckets = new TeamLinkedList[newSize];

            //Fill new array with old elements

            //For every current bucket, get everything and add it to the new bucket
            for(TeamLinkedList bucket : temp)
            {
                //Buckets may be empty
                if(bucket == null) continue;

                //loop through every bucket
                for(int c = 0; c < bucket.getSize(); c++)
                {
                    String string = bucket.getString(c);
                    AtomicInteger integer = bucket.getInteger(c);

                    //false because we are currently resizing
                    this.privatePut(string, integer, false);
                }
            }

            //Done
            //log metrics again
            this.writeMetricsToFileWithMessage("Post Resize");
        }
    }

    /**
     * Check whether the table contains a key
     * @param s The key to check for
     * @return True if exists, False is not
     */
    public boolean containsKey(String s)
    {
        try
        {
            return this.get(s) != null;
        }
        catch (NoSuchElementException e)
        {
            return false;
        }
    }

    /**
     * Get the value given a key
     * @param s The key
     * @return The value
     */
    public AtomicInteger get(String s) throws NoSuchElementException
    {
        //First check and see if there is a list at index hashcode(s)
        int index = this.getStringHashCode(s) % this.buckets.length;

        //Get the bucket
        TeamLinkedList bucket = this.buckets[index];

        if(bucket == null)
        {
            return null;
        }

        //Otherwise search list for string and return atomic integer
        for(int i = 0; i < bucket.getSize(); i++)
        {
            if(bucket.getString(i).equals(s))
            {
                return bucket.getInteger(i);
            }
        }

        //We never found it
        throw new NoSuchElementException("Could not find integer for string: " + s);
    }

    /**
     * Get the current (filled) size of the table
     * @return The current filled size
     */
    public int size()
    {
        return this.currentSize;
    }

    /**
     * Get the load factor of the hashtable
     * @return Load factor, number of distinct entries / size of array
     */
    private double getLoadFactor()
    {
        return (double) this.size() /  (double) this.buckets.length;
    }

    /**
     * Get the amount of buckets in use
     * @return The number of this.nodes indicies that are not null
     */
    private int getBucketsInUse()
    {
        int amount = 0;
        for(TeamLinkedList list : this.buckets)
        {
            if(list != null && list.getSize() != 0)
            {
                amount++;
            }
        }

        return amount;
    }

    /**
     * Get info about the buckets
     * @return An array of length 4 containing: min, max, avg, and mode of the size of buckets
     */
    private int[] getBucketStats()
    {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int average = 0, total = 0;
        int mode = 0;

        for(TeamLinkedList bucket : this.buckets)
        {
            int bucketSize;
            //buckets may be null
            if(bucket == null)
            {
                bucketSize = 0;
            }
            else
            {
                bucketSize = bucket.getSize();

            }

            if(bucketSize > max)
            {
                max = bucketSize;
            }

            if(bucketSize < min)
            {
                min = bucketSize;
            }

            total += bucketSize;
        }

        double decimalAverage = (double) total / (double) this.buckets.length;
        average = (int) (decimalAverage * 100);

        //to calculate mode, make an array of size max + 1, and increment where each bucket size occurs
        //then find the max value in the array, and the index of the max value is the mode
        int[] occuranceCount = new int[max + 1];
        for(TeamLinkedList bucket : this.buckets)
        {
            if(bucket == null) continue;

            int bucketSize = bucket.getSize();

            occuranceCount[bucketSize]++;
        }

        int occurrenceCountMax = -1;
        for(int i = 0; i < occuranceCount.length; i++)
        {
            if(occuranceCount[i] > occurrenceCountMax)
            {
                occurrenceCountMax = occuranceCount[i];
                mode = i;
            }
        }

        return new int[]{min, max, average, mode};

    }

    /**
     * Write the hashtable metrics to the metrics file with a message written before
     * @param msg The message to go before
     */
    public void writeMetricsToFileWithMessage(String msg)
    {
        this.metricsFileOut.println(msg);
        this.metricsFileOut.println(this.getMetrics());
        this.metricsFileOut.flush();
    }


    /**
     * keySet() returns a HashSet of all the keys in the HashTable.
     * The HashSet can be used to get get an iterator to work with the keys in whatever way is needed.
     * @return a HashSet of all the keys.
     * */
    public HashSet<String> keySet() {
        HashSet<String> hashSet = new HashSet<String>();

        //go through every list in this.buckets
        for(TeamLinkedList list : this.buckets) {

            if(list == null) continue;

            for(int i = 0; i < list.getSize(); i++)
            {
                hashSet.add(list.getString(i));
            }
        }

        return hashSet;
    }

    /**
     * values() returns a linked list of all the values in the HashTable.
     * The linked list can be used to get an iterator to work with the values in whatever way is needed.
     * @return a linked list of all the values.
     */
    public LinkedList<AtomicInteger> values() {
        LinkedList<AtomicInteger> linkedList = new LinkedList<AtomicInteger>();

        //go through every list in this.buckets
        for(TeamLinkedList list : this.buckets) {

            for(int i = 0; i < list.getSize(); i++)
            {
                linkedList.add(list.getInteger(i));
            }
        }

        return linkedList;
    }

    /**
     * Clears the HashMap.
     */
    public void clear() {
        this.buckets = new TeamLinkedList[this.buckets.length];
        currentSize = 0;
    }

    /**
     * Get the metrics of the hashtable
     * @return the number of entries, load factor, info about number of buckets in use, \
     * min,max,avg,mode of size of buckets (All formatted as a string)
     */
    public String getMetrics()
    {
        int[] bucketMetrics = this.getBucketStats(); //min max avg mode

        return "==============================" + "\n" +
                "Number of buckets: " + this.buckets.length + "\n" +
                "Table size: " + this.size() + "\n" +
                "Load factor: " + this.getLoadFactor() + "\n" +
                "Buckets in use: " + this.getBucketsInUse() + "\n" +
                "Percentage of buckets in use: " + (((double)this.getBucketsInUse() / (double)this.buckets.length)) * 100 + "\n" +
                "Bucket min size: " + bucketMetrics[0] + "\n" +
                "Bucket max size: " + bucketMetrics[1] + "\n" +
                "Bucket average size: " + bucketMetrics[2] + "\n" +
                "Bucket mode size: " + bucketMetrics[3] + '\n' +
                "==============================" + "\n";
    }

    /**
     * Get the hashcode of a string
     * @param s The string
     * @return The hashcode
     */
    private int getStringHashCode(String s)
    {
        int hash = 0;

        for(Character c : s.toCharArray())
        {
            hash += Character.getNumericValue(c);
        }

        return hash;
    }
}
