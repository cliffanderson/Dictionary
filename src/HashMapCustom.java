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
public class HashMapCustom
{
    //amount of items in the HashMapCustom. Keep in mind that a single array spot can hold many items.
    private int currentSize = 0;

    //Used array space
    private int usedArraySpace = 0;

    //internal array to hold Nodes
    private Node[] nodes;

    //File to log metrics to
    private PrintWriter metricsFileOut;

    public HashMapCustom(int initialSize, File metricsFile) throws Exception
    {
        if(initialSize <= 0)
        {
            throw new Exception("Initial size must be greater than 0");
        }

        //make initialSize prime
        if(!MathUtils.isPrime(initialSize)) {
            initialSize = MathUtils.getNextPrime(initialSize);
        }

        this.nodes = new Node[initialSize];

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
        int index = this.getStringHashCode(s)%this.nodes.length;
        Node node = this.nodes[index];

        if (node == null){
            //Make a new node
            this.nodes[index]=new Node(s,i);
            this.usedArraySpace++;
            this.currentSize++;
        }
        else
        {
             while (node.getNext() != null){
                 node = node.getNext();
             }

            //when next=null
            Node newNode =new Node(s,i);
            node.setNext(newNode);
            this.currentSize++;
        }

        //Check to see if resizing is needed
        //Used array space must not exceed 75% of the array length
        if(this.getLoadFactor() > 1.0 && allowResize)
        {
            //Must resize array
            //first record metrics to file
            this.writeMetricsToFileWithMessage("\n\n\nPre Resize");

            //Move array to a temp variable
            Node[] temp = this.nodes;

            //Setting the usedArraySpace to 0 because it needs to recalculated since the indexes are going to be different.
            usedArraySpace = 0;

            //Get the new size, the next prime greater than the current length * 2
            int newSize = MathUtils.getNextPrime(this.nodes.length * 2);
            this.nodes = new Node[newSize];

            //Fill new array with old elements
            for(int c = 0; c < temp.length; c++)
            {
                Node n = temp[c];
                while(n != null)
                {
                    //Add every node in chain
                    this.privatePut(n.getString(), n.getInteger(), false);
                    n = n.getNext();
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
        //First check and see if there is a node at index hashcode(s)
        int index = this.getStringHashCode(s) % this.nodes.length;
        if(this.nodes[index] == null)
        {
            return null;
        }

        //Search through this chain of nodes looking for the string s
        Node node = this.nodes[index];
        while(node != null)
        {
            if(node.getString().equals(s))
            {
                //Found it
                return node.getInteger();
            }
            else
            {
                //Go to next node
                node = node.getNext();
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
        return (double) this.size() /  (double) this.nodes.length;
    }

    /**
     * Get the amount of buckets in use
     * @return The number of this.nodes indicies that are not null
     */
    private int getBucketsInUse()
    {
        int amount = 0;
        for(Node n : this.nodes)
        {
            if(n != null) amount++;
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

        for(Node bucket : this.nodes)
        {
            int bucketSize = 0;
            while(bucket != null)
            {
                bucket = bucket.next;
                bucketSize++;
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

        double decimalAverage = (double) total / (double) this.nodes.length;
        average = (int) (decimalAverage * 100);

        //to calculate mode, make an array of size max + 1, and increment where each bucket size occurs
        //then find the max value in the array, and the index of the max value is the mode
        int[] occuranceCount = new int[max + 1];
        for(Node bucket : this.nodes)
        {
            int bucketSize = 0;
            while(bucket != null)
            {
                bucket = bucket.next;
                bucketSize++;
            }

            occuranceCount[bucketSize]++;
        }

        int occuranceCountMax = -1;
        for(int i = 0; i < occuranceCount.length; i++)
        {
            if(occuranceCount[i] > occuranceCountMax)
            {
                occuranceCountMax = occuranceCount[i];
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
     * keySet() returns a HashSet of all the keys in the HashMapCustom.
     * The HashSet can be used to get get an iterator to work with the keys in whatever way is needed.
     * @return a HashSet of all the keys.
     * */
    public HashSet<String> keySet() {
        HashSet<String> hashSet = new HashSet<String>();

        //go through every node in this.nodes and then go through the chain
        for(int i = 0; i < this.nodes.length; i++) {
            Node n = this.nodes[i];

            while (n != null) {
                hashSet.add(n.getString());
                n = n.getNext();
            }
        }

        return hashSet;
    }

    /**
     * values() returns a linked list of all the values in the HashMapCustom.
     * The linked list can be used to get an iterator to work with the values in whatever way is needed.
     * @return a linked list of all the values.
     */
    public LinkedList<AtomicInteger> values() {
        LinkedList<AtomicInteger> linkedList = new LinkedList<AtomicInteger>();

        for (int i = 0; i < nodes.length; i++) {
            Node n = nodes[i];

            while (n != null) {
                linkedList.add(n.getInteger());
                n = n.getNext();
            }
        }

        return linkedList;
    }

    /**
     * Clears the HashMap.
     */
    public void clear() {
        nodes = new Node[nodes.length];
        currentSize = 0;
        usedArraySpace = 0;
    }

    /**
     * Prints out the size of each bucket in the internal array of the HashMap.
     * Each bucket is a linked chain of nodes.
     */
    public void printTableAnalysis() {

        int[] sizeOfBuckets = new int[nodes.length];

        //Getting size of each bucket.
        for (int i = 0; i < nodes.length; i++) {
            Node n = nodes[i];
            int size = 0;

            while (n != null) {
                size++;
                n = n.getNext();
            }

            sizeOfBuckets[i] = size;
        }

        //Printing the sizes of each bucket, including the buckets that are empty.
        for (int i = 0; i < sizeOfBuckets.length; i++) {
            System.out.println("Bucket " + i + ": " + sizeOfBuckets[i]);
        }
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
                "Number of buckets: " + this.nodes.length + "\n" +
                "Table size: " + this.size() + "\n" +
                "Load factor: " + this.getLoadFactor() + "\n" +
                "Buckets in use: " + this.getBucketsInUse() + "\n" +
                "Percentage of buckets in use: " + (((double)this.getBucketsInUse() / (double)this.nodes.length)) * 100 + "\n" +
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

    private class Node
    {
        private String string;
        private AtomicInteger integer;
        private Node next;

        public Node() {
            next = null;
            string = null;
            integer = null;
        }

        public Node(String string, AtomicInteger integer)
        {
            this.string = string;
            this.integer = integer;
        }

        public String getString()
        {
            return this.string;
        }

        public AtomicInteger getInteger()
        {
            return this.integer;
        }

        public Node getNext()
        {
            return this.next;
        }

        public void setNext(Node next)
        {
            this.next = next;
        }
    }
}
